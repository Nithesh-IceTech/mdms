package za.co.spsi.mdms.kamstrup.processor;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.util.CollectionUtils;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.services.MeterDataService;
import za.co.spsi.mdms.kamstrup.db.KamstrupGapProcessorJobEntity;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.pjtk.util.StringUtils;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import static za.co.spsi.mdms.kamstrup.db.KamstrupGapProcessorJobEntity.Status.*;

@Log
@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class KamstrupGapProcessor {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    private MeterDataService service;

    @Inject
    PrepaidMeterFilterService filterService;

    @Inject
    private PropertiesConfig propertiesConfig;

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private void processGaps(Connection c, KamstrupGapProcessorJobEntity entity) {

        if (!propertiesConfig.getKamstrup_gap_processing_enabled()) return;

        try {

            DSDB.set(dataSource, (EntityDB) entity.status.set(STARTED.name()));

            String serialN = entity.meter.getOne(dataSource).serialN.get();

            MeterResultDataArray dataSet = service.getDetailData(
                    serialN, entity.startTime.get(), entity.endTime.get()
                    , 0, MeterDataService.Interval.fromMinutes(entity.interval.get()), "series1", null, null, false);

            boolean is_Prepaid = filterService.isPrepaid(entity.meter.get(c).get().meterN.get());

            if( !CollectionUtils.isEmpty(dataSet) ) {

                if(propertiesConfig.getKamstrup_gap_processing_debug_enabled()) {
                    log.info(String.format("Processing gap -> jobId:%s, serialN:%s, jobType:%s, startTime:'%s', endTime:'%s', runTime:'%s' ",
                            entity.kamstrupGapProcessorId.get(),
                            serialN,
                            entity.jobType.get(),
                            entity.startTime.get().toLocalDateTime().format(DATE_TIME_FORMATTER),
                            entity.endTime.get().toLocalDateTime().format(DATE_TIME_FORMATTER),
                            entity.runTime.get().toLocalDateTime().format(DATE_TIME_FORMATTER)
                    ));
                }

                if (entity.jobType.get().equals(KamstrupGapProcessorJobEntity.JobTypes.TOTAL_ENERGY.name())) {

                    MeterResultDataArray dataSetFiltered = dataSet.excludeNullReadings(true, false, false, false, false, false);

                    if(isReadingsStillMissing(dataSetFiltered,entity.startTime.get(),entity.endTime.get(),entity.interval.get())) {
                        dataSetFiltered
                                .normalize(30)
                                .getCalculated().stream()
                                .forEach(d -> MeterReadingEntity.generate(c, d, entity.meterId.get()
                                        , null, null, null, "TOTAL", is_Prepaid, 0));
                    }

                } else if (entity.jobType.get().equals(KamstrupGapProcessorJobEntity.JobTypes.T1_ENERGY.name())) {

                    MeterResultDataArray dataSetFiltered = dataSet.excludeNullReadings(false, true, false, false, false, false);

                    if(isReadingsStillMissing(dataSetFiltered,entity.startTime.get(),entity.endTime.get(),entity.interval.get())) {
                        dataSetFiltered
                                .normalize(30)
                                .getCalculated().stream()
                                .forEach(d -> MeterReadingEntity.generate(c, d, entity.meterId.get()
                                        , null, null, null, "T1", is_Prepaid, 0));
                    }

                } else if (entity.jobType.get().equals(KamstrupGapProcessorJobEntity.JobTypes.T2_ENERGY.name())) {

                    MeterResultDataArray dataSetFiltered = dataSet.excludeNullReadings(false, false, true, false, false, false);

                    if(isReadingsStillMissing(dataSetFiltered,entity.startTime.get(),entity.endTime.get(),entity.interval.get())) {
                        dataSetFiltered
                                .normalize(30)
                                .getCalculated().stream()
                                .forEach(d -> MeterReadingEntity.generate(c, d, entity.meterId.get()
                                        , null, null, null,"T2", is_Prepaid, 0));
                    }

                } else if (entity.jobType.get().equals(KamstrupGapProcessorJobEntity.JobTypes.CURRENT.name())) {

                    MeterResultDataArray dataSetFiltered = dataSet.excludeNullReadings(false, false, false, false, true, false);

                    if(isReadingsStillMissing(dataSetFiltered,entity.startTime.get(),entity.endTime.get(),entity.interval.get())) {
                        dataSetFiltered
                                .normalize(30)
                                .getCalculated().stream()
                                .forEach(d -> MeterReadingEntity.generate(c, d, entity.meterId.get()
                                        , null, null, null,"CURRENT", is_Prepaid, 0));
                    }

                } else if (entity.jobType.get().equals(KamstrupGapProcessorJobEntity.JobTypes.VOLTAGE.name())) {

                    MeterResultDataArray dataSetFiltered = dataSet.excludeNullReadings(false, false, false, true, false, false);

                    if(isReadingsStillMissing(dataSetFiltered,entity.startTime.get(),entity.endTime.get(),entity.interval.get())) {
                        dataSetFiltered
                                .normalize(30)
                                .getCalculated().stream()
                                .forEach(d -> MeterReadingEntity.generate(c, d, entity.meterId.get()
                                        , null, null, null,"VOLTAGE", is_Prepaid, 0));
                    }

                } else if (entity.jobType.get().equals(KamstrupGapProcessorJobEntity.JobTypes.WATER.name()) ||
                        StringUtils.isEmpty(entity.jobType.get())) {

                    MeterResultDataArray dataSetFiltered = dataSet.excludeNullReadings(false, false, false, false, false, true);

                    if(isReadingsStillMissing(dataSetFiltered,entity.startTime.get(),entity.endTime.get(),entity.interval.get())) {
                        dataSetFiltered
                                .normalize(60)
                                .getCalculated().stream()
                                .forEach(d -> MeterReadingEntity.generate(c, d, entity.meterId.get()
                                        , null, null, null, "VOLUME", is_Prepaid, 0));
                    }

                } else if (entity.jobType.get().equals(KamstrupGapProcessorJobEntity.JobTypes.GENERAL.name()) ||
                        StringUtils.isEmpty(entity.jobType.get())) {

                    MeterResultDataArray dataSetFiltered = dataSet.excludeNullReadings(true, false, false, false, false, false);

                    if(isReadingsStillMissing(dataSetFiltered,entity.startTime.get(),entity.endTime.get(),entity.interval.get())) {
                        dataSetFiltered
                                .normalize(30)
                                .getCalculated().stream()
                                .forEach(d -> MeterReadingEntity.generate(c, d, entity.meterId.get()
                                        , null, null, null, "TOTAL", is_Prepaid, 0));
                    }

                }

                DSDB.set(dataSource, (EntityDB) entity.status.set(COMPLETED.name()));

            } else {

                DSDB.set(dataSource, (EntityDB) entity.status.set(DATASET_EMPTY.name()));

            }

        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
            entity.status.set(FAILED.name());
            entity.error.set(ex);
            DSDB.set(dataSource, entity);
        }

    }

    private Boolean isReadingsStillMissing(MeterResultDataArray dataSet, Timestamp startTS, Timestamp endTS, Integer intervalInMinutes) {
        long timeDiffSeconds = ( endTS.getTime() - startTS.getTime() ) / 1000;
        long numberOfReadings = timeDiffSeconds / (intervalInMinutes * 60);
        long datasetSize = dataSet.size();
        return numberOfReadings != datasetSize;
    }

    @SneakyThrows
    private void processGaps() {
        DSDB.executeInTx(dataSource, c -> {
            try (DataSourceDB<KamstrupGapProcessorJobEntity> ds = new DataSourceDB<>(KamstrupGapProcessorJobEntity.class)) {
                Driver driver = DriverFactory.getDriver();
                String query = String.format("select * from kamstrup_gap_processor where status = ? and run_time < %s",
                        driver.toDate(LocalDateTime.now()) ) ;
                query = driver.limitSqlAndOrderBy(query, propertiesConfig.getKamstrup_gap_processing_batch_size(), "run_time", false);
                log.info(String.format("Process gap filling jobs: %s", query));
                for (KamstrupGapProcessorJobEntity gap : ds.getAll(c,false,query,CREATED.name())) {
                    processGaps(c, gap);
                    if (!propertiesConfig.getKamstrup_gap_processing_enabled()) {
                        break;
                    }
                }
            }
        });
    }

    @PostConstruct
    public void resetState() {
        KamstrupGapProcessorJobEntity.resetStarted(dataSource);
    }

    @Schedule(hour = "*", minute = "*", persistent = false)
    @AccessTimeout(value = 24, unit = TimeUnit.HOURS)
    public void updateMeters() {
        if (propertiesConfig.getKamstrup_gap_processing_enabled()) {
            processGaps();
        }
    }

}
