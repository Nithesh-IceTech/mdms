package za.co.spsi.mdms.common.services;

import org.springframework.util.CollectionUtils;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.PrepaidMeterReadingBatch;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.pjtk.util.Assert;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.common.services.PrepaidMeterReadingPushProcessingService.TAG;

/**
 * Created by jaspervdbijl on 2017/01/06.
 */
@Singleton()
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class PrepaidMeterReadingPushService extends AbstractPrepaidService {

    @Resource(mappedName = "java:/jdbc/mdms")
    public javax.sql.DataSource dataSource;

    @Inject
    public PropertiesConfig propertiesConfig;

    @PostConstruct
    public void init() {
        p.delay(5).hours( propertiesConfig.getPrepaid_batch_processing_interval() ).repeat(() -> {
            if(propertiesConfig.getPrepaid_batch_processing_enabled()) {
                process();
            }
        });
    }

    public void process() {

        Driver driver = DriverFactory.getDriver();
        // TODO IED-4584: PP-PROD - Missing Readings & impact on Take on Reading - URGENT
        // Gap filled or generated readings will always be delayed.
        // If a PP batch includes both new readings, and old generated readings, ICE UTIL will reject the batch.
        // Then the new readings will also not be sent, which means there will be missing readings in UTIL where MDMS have the readings available.
        // PP service should not assign batch ID's to gap filled/generated readings.
        // Therefore I included generated is null in the where clause.
        String query = String.format("select count(*) from meter_reading where prepaid_meter = %s and generated is null and prepaid_meter_reading_batch_id is null",
                driver.boolToNumber(true) );
        if (DataSourceDB.executeQuery(dataSource,Integer.class, query) > 0) {
            List<String> meterCols = Arrays.asList("kam_meter_id", "nes_meter_id", "generic_meter_id", "els_meter_id");
            for(String meterCol: meterCols) {
                List<String> meterIds = getPrepaidMeterIds(meterCol);
                updateMeterReadingBatchIds(meterCol.toLowerCase(), meterIds);
            }
        }

    }

    public void updateMeterReadingBatchIds(String meterCol, List<String> meterIds) {
        Driver driver = DriverFactory.getDriver();

        for(String meterId: meterIds) {
             if( meterId != null ) {

                 int backlogTimeWindowDays = propertiesConfig.getPrepaid_batch_processing_backlog_time_window() == null ? 1 :
                         propertiesConfig.getPrepaid_batch_processing_backlog_time_window();

                 Timestamp fromEntryTime = Timestamp.valueOf( LocalDateTime.now().minusHours( (long) 24 * backlogTimeWindowDays ) );

                 int batchSizeLimit = getBatchSizeLimit(meterCol,meterId);
                 if(batchSizeLimit == 0 || batchSizeLimit > 48) batchSizeLimit = 4;

                 List<MeterReadingEntity> newMeterReadings = getNewMeterReadings(meterCol, meterId, fromEntryTime);

                 if(newMeterReadings == null) {
                     newMeterReadings = new ArrayList<>();
                     TAG.info(String.format("Unable to get new meter readings for prepaid batch id assignment. Meter Column: %s, MeterId: %s, MaxEntryTime: %s",
                             meterCol,
                             meterId,
                             driver.toDate(fromEntryTime.toLocalDateTime())));
                 } else if (newMeterReadings.size() < 1) {
                     TAG.info(String.format("No new meter readings available for prepaid batch id assignment. Meter Column: %s, MeterId: %s, MaxEntryTime: %s",
                             meterCol,
                             meterId,
                             driver.toDate(fromEntryTime.toLocalDateTime())));
                 }

                 int batchMultiple = newMeterReadings.size() / batchSizeLimit;

                 for(int i = 0; i < batchMultiple; i++) {

                     if(newMeterReadings.size() >= batchSizeLimit) {

                         List<String> meterReadingIdList = newMeterReadings.stream().map( mtrEntity -> String.format("'%s'",mtrEntity.meterReadingId.get()) ).collect(Collectors.toList());

                         int fromIdx = i * batchSizeLimit;
                         int toIdx = (i+1) * batchSizeLimit;
                         List<String> meterReadingIdSubList = meterReadingIdList.subList(fromIdx, toIdx);
                         if(meterReadingIdSubList.size() < batchSizeLimit) break;

                         String meterReadingIdsStr = String.join(",", meterReadingIdSubList);

                         DataSourceDB.executeInTx(dataSource, connection -> {
                             PrepaidMeterReadingBatch tmpBatch = new PrepaidMeterReadingBatch();
                             tmpBatch.serialN.set( getMeterSerialNumber(meterCol,meterId) );
                             PrepaidMeterReadingBatch batch = DataSourceDB.set(connection,tmpBatch);
                             String updateQuery = String.format("update meter_reading set prepaid_meter_reading_batch_id = ? where meter_reading_id in (%s)",
                                     meterReadingIdsStr);
                             DataSourceDB.execute(connection,updateQuery,batch.prepaidMeterReadingBatchId.get());
                         });

                     }

                 }
            }
        }
    }

    private List<String> getPrepaidMeterIds(String meterCol) {
        Driver driver = DriverFactory.getDriver();
        String query = String.format("select * from meter_reading where prepaid_meter = %s " +
                " and %s is not null and generated is null and prepaid_meter_reading_batch_id is null ",
                driver.boolToNumber(true), meterCol);
        List<MeterReadingEntity> meterReadings = DataSourceDB.getAllAsList(MeterReadingEntity.class, dataSource, query);
        List<String> meterIds = new ArrayList<>();
        if(!CollectionUtils.isEmpty(meterReadings)) {
            meterIds = meterReadings.stream().map(mr -> {
                return meterCol.equalsIgnoreCase("kam_meter_id") ? mr.kamMeterId.get() :
                        meterCol.equalsIgnoreCase("nes_meter_id") ? mr.nesMeterId.get() :
                        meterCol.equalsIgnoreCase("generic_meter_id") ? mr.genericMeterId.get() :
                        meterCol.equalsIgnoreCase("els_meter_id") ? mr.elsterMeterId.get() : null;
            }).distinct().collect(Collectors.toList());
        }
        return meterIds;
    }

    private Integer getBatchSizeLimit(String meterCol, String meterId) {
        boolean isWater = false;
        if(meterCol.contains("kam_meter")) {
            KamstrupMeterEntity kamMeter = DataSourceDB.getFromSet(dataSource, (KamstrupMeterEntity) new KamstrupMeterEntity().meterId.set(meterId));
            isWater = kamMeter.isWater();
        } else if(meterCol.contains("generic_meter")) {
            GenericMeterEntity genericMeter = DataSourceDB.getFromSet(dataSource, (GenericMeterEntity) new GenericMeterEntity().genericMeterId.set(meterId));
            isWater = genericMeter.isWater();
        }
        Integer intervalMultiplier = isWater ? 1 : 2;
        return propertiesConfig.getPrepaid_batch_processing_interval() * intervalMultiplier;
    }

    private List<MeterReadingEntity> getNewMeterReadings(String meterCol, String meterId, Timestamp maxEntryTime) {

        Driver driver = DriverFactory.getDriver();

        String selectQuery = String.format("select * from meter_reading where %s = '%s' and " +
                        " entry_time > %s and prepaid_meter = %s and generated is null and prepaid_meter_reading_batch_id is null ",
                meterCol,
                meterId,
                driver.toDate( maxEntryTime.toLocalDateTime()),
                driver.boolToNumber(true) );

        String orderByQuery = driver.orderBy(selectQuery,"entry_time", false);

        return DataSourceDB.getAllAsList(MeterReadingEntity.class,dataSource,orderByQuery);
    }

    private String getMeterSerialNumber(String meterCol, String meterId) {

        String serialN = "";

        if(meterCol.contains("kam_meter")) {
            KamstrupMeterEntity kamMeter = DataSourceDB.getFromSet(dataSource, (KamstrupMeterEntity) new KamstrupMeterEntity().meterId.set(meterId));
            serialN = kamMeter != null ? kamMeter.serialN.get() : "";
        } else if(meterCol.contains("nes_meter")) {
            NESMeterEntity nesMeter = DataSourceDB.getFromSet(dataSource, (NESMeterEntity) new NESMeterEntity().meterId.set(meterId));
            serialN = nesMeter != null ? nesMeter.serialN.get() : "";
        } else if(meterCol.contains("els_meter")) {
            ElsterMeterEntity elsterMeter = DataSourceDB.getFromSet(dataSource, (ElsterMeterEntity) new ElsterMeterEntity().meterId.set(meterId));
            serialN = elsterMeter != null ? elsterMeter.serialN.get() : "";
        } else if(meterCol.contains("generic_meter")) {
            GenericMeterEntity genericMeter = DataSourceDB.getFromSet(dataSource, (GenericMeterEntity) new GenericMeterEntity().genericMeterId.set(meterId));
            serialN = genericMeter != null ? genericMeter.meterSerialN.get() : "";
        }

        return serialN;
    }

}
