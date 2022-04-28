package za.co.spsi.mdms.nes.services.result;

import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.services.ReadingGenericGapIdentifier;
import za.co.spsi.mdms.common.services.ReadingGenericGapProcessor;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterResultEntity;
import za.co.spsi.mdms.nes.db.NESMeterResultView;
import za.co.spsi.mdms.nes.services.order.domain.Interval;
import za.co.spsi.mdms.nes.services.order.domain.LoadProfile;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.mdms.util.XmlHelper;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.mdms.util.MeterFilterService.getMax;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@Startup()
@Singleton()
@Lock
@AccessTimeout(value=600000)
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class NESResultProcessor {

    private static SimpleDateFormat dayFormat = new SimpleDateFormat(MeterReadingEntity.ENTRY_DAY_FORMAT);
    public static final Logger TAG = Logger.getLogger(NESResultProcessor.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource mdmsDs;

    @Inject
    private ReadingGenericGapIdentifier gapIdentifier;

    @Inject
    private PrepaidMeterFilterService prepaidFilter;

    @Inject
    private PropertiesConfig propertiesConfig;

    private int unprocessedNesResultCount = 0;

    private boolean active = true;

    private Integer process(Connection connection, NESMeterResultEntity result, NESMeterEntity meter) throws SQLException, IOException, JAXBException {
        String decompressed = result.resultData.getInflatedString();
        if (decompressed.startsWith("<LOADPROFILE>")) {
            LoadProfile loadProfile = XmlHelper.unmarshall(LoadProfile.class, decompressed);

            for (Interval i : loadProfile.intervals.intervals) {

                //Check for status valid meter readings
                if( i.status.equals("1") ) {

                    MeterReadingEntity meterReading = new MeterReadingEntity();
                    meterReading.nesMeterId.set(meter.meterId.get());
                    meterReading.entryTime.set(i.dateTime);
                    meterReading.entryDay.set(Integer.parseInt(dayFormat.format(i.dateTime)));
                    meterReading = DataSourceDB.getFromSet(connection, meterReading);

                    // process Meter Reading
                    meterReading = meterReading == null ? new MeterReadingEntity() : meterReading;
                    meterReading.nesMeterId.set(meter.meterId.get());
                    meterReading.entryTime.set(i.dateTime);
                    meterReading.entryDay.set(Integer.parseInt(dayFormat.format(i.dateTime)));
                    meterReading.nesMeterResultId.set(result.meterResultId.get());
                    meterReading.update(i);
                    meterReading.prepaidMeter.set(meterReading.prepaidMeter.get() == null ? prepaidFilter.isPrepaid(meter.serialN.get()) : meterReading.prepaidMeter.get());

                    GeneratorTransactionEntity.updateForTx(connection, meterReading);

                    Timestamp nesEntryTime  =  meterReading.adjustTimestamp( 30 );
                    gapIdentifier.identifyMeterReadingGaps(connection,meter,meterReading,nesEntryTime,false);

                    // update max time after checking for gaps
                    meter.lastCommsD.set(i.dateTime);
                    meter.maxEntryTime.set(getMax(meter.maxEntryTime.get(),i.dateTime));

                    DataSourceDB.set(connection, meterReading);
                }
            }

            return NESMeterResultEntity.Status.PROCESSED.code;

        } else {
            return NESMeterResultEntity.Status.IGNORED.code;
        }
    }

    private void process(NESMeterResultView entity) throws SQLException {
        try (Connection connection = mdmsDs.getConnection()) {
            connection.setAutoCommit(false);
            NESMeterEntity meter = (NESMeterEntity) entity.meter.clone();
            NESMeterResultEntity result = (NESMeterResultEntity) entity.result.clone();
            try {
                result.status.set(
                        process(connection, result,meter)
                );
                DataSourceDB.set(connection, result);
                DataSourceDB.set(connection, meter);
                connection.commit();
            } catch (Exception ex) {
                TAG.log(Level.WARNING,ex.getMessage(),ex);
                connection.rollback();
                result.status.set(NESMeterResultEntity.Status.FAILED.code);
                result.error.set(ex);
                result.setInDatabase(true);
                DataSourceDB.set(connection, result);
                connection.commit();
            }
        }
    }

    public void checkUnprocessedNesResultsBacklog() {

        try {
            if(propertiesConfig.getNes_processing_enabled()) {
                try (Connection connection = mdmsDs.getConnection()) {
                    PreparedStatement query = connection.prepareStatement("select count(*) as unprocessedCount from NES_METER_RESULT where STATUS = 0");
                    ResultSet rs = query.executeQuery();
                    unprocessedNesResultCount = rs.next() ? rs.getInt("unprocessedCount") : 0;

                    String logMessage = String.format("Unprocessed NES results backlog: %d", unprocessedNesResultCount);
                    TAG.info(logMessage);
                }
            }
        } catch(SQLException sqle) {
            throw new RuntimeException(sqle);
        }

    }

    public void process() {
        try {
            if(propertiesConfig.getNes_processing_enabled()) {
                try (Connection connection = mdmsDs.getConnection()) {
                    for (NESMeterResultView entity : new NESMeterResultView(propertiesConfig.getNes_processing_batch_size()).getDataSource(connection)) {
                        process(entity);
                        if (!active || !propertiesConfig.getNes_processing_enabled()) {
                            break;
                        }
                    }
                }
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    @PreDestroy
    public void stopProcessing() {
        this.active = false;
    }

}