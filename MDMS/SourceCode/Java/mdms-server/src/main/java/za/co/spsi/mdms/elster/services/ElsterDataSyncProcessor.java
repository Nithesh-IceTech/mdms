package za.co.spsi.mdms.elster.services;

import lombok.SneakyThrows;
import za.co.spsi.mdms.common.db.interfaces.MeterEntity;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.services.ReadingGenericGapIdentifier;
import za.co.spsi.mdms.common.services.ReadingGenericGapProcessor;
import za.co.spsi.mdms.elster.db.ElsterDataView;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.elster.db.ElsterProperties;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.toolkit.db.DBSettings;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.mdms.util.MeterFilterService.getMax;

@Singleton()
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class ElsterDataSyncProcessor extends ProcessorService {

    private static Logger logger = Logger.getLogger(ElsterDataSyncProcessor.class.getName());

    @Inject
    @ConfValue(value = "elster.gmt", folder = "server")
    private boolean gmt;

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Resource(mappedName = "java:/jdbc/elsterDB")
    private DataSource elsterDB;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.tmz_offset", folder = "server")
    private int tmzOffset;

    @Inject
    private PrepaidMeterFilterService prepaidFilter;

    @Inject
    private ReadingGenericGapIdentifier gapIdentifier;

    @Inject
    private PropertiesConfig propertiesConfig;

    private ElsterMeterEntity meter = null;

    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    private void mergeOrInsert(Connection connection, MeterEntity meterEntity, MeterReadingEntity reading,boolean isWater) {
        // check if the entry exists
        MeterReadingEntity clone = new MeterReadingEntity();
        clone.elsterMeterId.set(reading.elsterMeterId.get());
        clone.entryTime.set(reading.entryTime.get());
        clone = DSDB.getFromSet(connection,clone);

        if (clone != null && clone.generated.getNonNull() ) {

            // update generated reading
            reading.meterReadingId.set(clone.meterReadingId.get());
            DataSourceDB.setUpdate(connection,reading);
            GeneratorTransactionEntity.updateForTx(connection,reading);

        } else if (clone == null) {

            DataSourceDB.setInsert(connection,reading);
            GeneratorTransactionEntity.updateForTx(connection,reading);

            Timestamp elsterEntryTime  = reading.adjustTimestamp( 30 );
            gapIdentifier.identifyMeterReadingGaps(connection,meterEntity,reading,elsterEntryTime,isWater);

        }
        GeneratorTransactionEntity.updateForTx(connection,reading);
    }

    public void process() {

        if(propertiesConfig.getElster_processing_enabled()) {

            logger.log(Level.INFO, "Elster Processor Enabled.");

            DataSourceDB.executeInTx(dataSource,connection -> {

                ElsterProperties properties = DBSettings.get(connection,ElsterProperties.class);
                Long maxId = properties.maxId.get() == null ? (long) -1 : properties.maxId.get();
                Integer batchSize = propertiesConfig.getElster_processing_batch_size();

                DataSourceDB.executeInTx(elsterDB,eCon -> {

                    // Store all ElsterDataView records into a meter reading list
                    for (ElsterDataView v : ElsterDataView.getData(eCon,maxId,batchSize) ) {

                        properties.maxId.set(v.id.get());

                        if (meter != null && !meter.serialN.get().equals(v.SerialNumber.get())) {
                            // update the last comms date
                            DataSourceDB.set(connection,meter);
                        }
                        // rebind meter
                        meter = (meter == null || !meter.serialN.get().equals(v.SerialNumber.get())) ?
                                DataSourceDB.getFromSet(connection, new ElsterMeterEntity().serialN.set(v.SerialNumber.get())) : meter;

                        if (meter == null) {
                            meter = new ElsterMeterEntity();
                            meter.serialN.set(v.SerialNumber.get());
                            DataSourceDB.set(connection,meter);
                        }

                        MeterReadingEntity reading = v.update(new MeterReadingEntity());
                        reading.elsterMeterId.set(meter.meterId.get());
                        reading.prepaidMeter.set(reading.prepaidMeter.get() == null?prepaidFilter.isPrepaid(meter.serialN.get()):reading.prepaidMeter.get());

                        mergeOrInsert(connection,meter,reading,false);

                        // set max after checking for gaps
                        meter.lastCommsD.set(reading.entryTime.get());
                        meter.maxEntryTime.set(getMax(meter.maxEntryTime.get(),reading.entryTime.get()));

                    }
                    DBSettings.set(connection,properties);

                });

            });

        } else {

            logger.log(Level.WARNING, "Elster Processor Disabled.");

        }

    }

}
