package za.co.spsi.mdms.common.services;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import za.co.spsi.mdms.common.dao.MeterResultData;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.MeterReadingGapProcessorJobEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static za.co.spsi.mdms.common.db.MeterReadingGapProcessorJobEntity.JobTypes.WATER;
import static za.co.spsi.mdms.common.db.MeterReadingGapProcessorJobEntity.MeterTypes.*;
import static za.co.spsi.mdms.common.db.MeterReadingGapProcessorJobEntity.Status.*;
import static za.co.spsi.mdms.kamstrup.db.KamstrupGapProcessorJobEntity.Status.CREATED;

@Log
@Startup
@Singleton
@AccessTimeout(value=300000)
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class ReadingGenericGapProcessor {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    private MeterDataService service;

    @Inject
    PrepaidMeterFilterService filterService;

    @Inject
    private PropertiesConfig propertiesConfig;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int getScale(MeterReadingGapProcessorJobEntity entity) {
        entity.meterType.set(entity.meterType.get() == null ? GENERIC.name() : entity.meterType.get());
        return entity.meterType.get().equals(MeterReadingGapProcessorJobEntity.MeterTypes.KAMSTRUP.name()) ? 0 :
                entity.meterType.get().equals(NES.name()) ? 1 :
                        entity.meterType.get().equals(ELSTER.name()) ? 2 :
                                entity.meterType.get().equals(GENERIC.name()) ? 3 : 0;
    }

    public void processGaps(Connection c, MeterReadingGapProcessorJobEntity job) {

        if (!propertiesConfig.getMeter_reading_gap_processor_enabled()) return;

        DSDB.set(c, (EntityDB) job.status.set(STARTED.name()));
        try {

            if(propertiesConfig.getMeter_reading_gap_processor_debug_enabled()) {
                log.info(String.format("Processing gap -> jobId:%s, serialN:%s, meterType:%s, jobType:%s, startTime:'%s', endTime:'%s', runTime:'%s' ",
                        job.meterReadingGapProcessorId.get(),
                        job.meterSerialN.get(),
                        job.meterType.get(),
                        job.jobType.get(),
                        job.startTime.get().toLocalDateTime().format(DATE_TIME_FORMATTER),
                        job.endTime.get().toLocalDateTime().format(DATE_TIME_FORMATTER),
                        job.runTime.get().toLocalDateTime().format(DATE_TIME_FORMATTER)
                ));
            }

            MeterResultDataArray dataSet = service.getDetailData(
                    job.meterSerialN.get(), job.startTime.get(), job.endTime.get()
                    , 0, MeterDataService.Interval.fromMinutes(job.interval.get()), "series1", null, null, false);

            boolean is_Prepaid = filterService.isPrepaid(job.meterSerialN.get());
            boolean is_Water = false;

            int scaleIndex = getScale(job);

            String elsMeterId = job.meterType.get().equals(ELSTER.name()) && !job.jobType.get().equals(WATER.name()) ? job.meterId.get() : null;
            String nesMeterId = job.meterType.get().equals(NES.name()) && !job.jobType.get().equals(WATER.name()) ? job.meterId.get() : null;
            String genMeterId = job.meterType.get().equals(GENERIC.name()) ? job.meterId.get() : null;

            if(genMeterId != null) {
                is_Water = isWater(genMeterId);
            }

            String meterReadingGroupName = is_Water ? "VOLUME" : "TOTAL";

            for (MeterResultData d : dataSet.getCalculated()) {
                MeterReadingEntity.generate(c, d, null, elsMeterId, nesMeterId, genMeterId,
                        meterReadingGroupName, is_Prepaid, scaleIndex);
            }
            DSDB.set(c, (EntityDB) job.status.set(COMPLETED.name()));
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            job.status.set(FAILED.name());
            job.error.set(ex);
            DSDB.set(c, job);
        }

    }

    private GenericMeterEntity getGenericMeterEntityByMeterId(Connection connection, String meterId) {
        return (GenericMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new GenericMeterEntity().meterId.set( meterId ));
    }

    @SneakyThrows
    private boolean isWater(String meterId) {
        boolean is_Water = false;
        try (Connection connection = dataSource.getConnection()) {
            GenericMeterEntity meterEntity = getGenericMeterEntityByMeterId(connection, meterId);
            String meterType = meterEntity.meterType.get();
            if( meterType.equalsIgnoreCase("gas") || meterType.equalsIgnoreCase("water") ) {
                is_Water = true;
            }
        }
        return is_Water;
    }

    @SneakyThrows
    private void processGaps() {
        DSDB.executeInTx(dataSource, c -> {
            try (DataSourceDB<MeterReadingGapProcessorJobEntity> ds = new DataSourceDB<>(MeterReadingGapProcessorJobEntity.class)) {
                Driver driver = DriverFactory.getDriver();
                String query = "select * from meter_reading_gap_processor where status = ?";
                query = driver.limitSqlAndOrderBy(query, propertiesConfig.getMeter_reading_gap_processor_batch_size(), "run_time", false);
                log.info(String.format("Process gap filling jobs: %s", query));
                for (MeterReadingGapProcessorJobEntity gap : ds.getAll(c,false,query,CREATED.name())) {
                    processGaps(c, gap);
                    if (!propertiesConfig.getMeter_reading_gap_processor_enabled()) {
                        break;
                    }
                }
            }
        });
    }

    @Schedule(hour = "*", minute = "*", persistent = false)
    @AccessTimeout(value = 24, unit = TimeUnit.HOURS)
    public void updateMeters() {
        if (propertiesConfig.getMeter_reading_gap_processor_enabled()) {
            processGaps();
        }
    }

}
