package za.co.spsi.mdms.common.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import za.co.spsi.mdms.common.db.utility.ICEMeterPortfolioView;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.properties.TextFile;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Data
class PrepaidMeterInfo {

    private String meterId;
    private String serialN;
    private String meterType;
    private Timestamp lastCommsDate;
    private String prepaidBatchId;
    private String portFolioManagerEmail;
    private String branchManagerEmail;
    private String buildingNumber;
    private String propertyName;

    public PrepaidMeterInfo(String meterId, String serialN, String meterType, Timestamp lastCommsDate) {
        this.meterId = meterId;
        this.serialN = serialN;
        this.meterType = meterType;
        this.lastCommsDate = lastCommsDate;
    }

    public PrepaidMeterInfo(String serialN, String meterType, Timestamp lastCommsDate,
                            String prepaidBatchId, String portFolioManagerEmail, String branchManagerEmail,
                            String buildingNumber, String propertyName) {
        this.serialN = serialN;
        this.meterType = meterType;
        this.lastCommsDate = lastCommsDate;
        this.prepaidBatchId = prepaidBatchId;
        this.portFolioManagerEmail = portFolioManagerEmail;
        this.branchManagerEmail = branchManagerEmail;
        this.buildingNumber = buildingNumber;
        this.propertyName = propertyName;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "serialN=" + serialN
                + ", lastCommsDate=" + lastCommsDate.toLocalDateTime().format(formatter)
                +  ", buildingNumber=" + buildingNumber
                + ", propertyName=" + propertyName;
    }

}

@Data
class PrepaidBatchInfo {

    private String prepaidbatchId;
    private String utilData;
    private String errorDescription;
    private Timestamp updatedDate;

    public PrepaidBatchInfo(String prepaidbatchId, String utilData, String errorDescription, Timestamp updatedDate) {
        this.prepaidbatchId = prepaidbatchId;
        this.utilData = utilData;
        this.errorDescription = errorDescription;
        this.updatedDate = updatedDate;
    }
}

@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn(value = "MDMSUpgradeService")
public class MDMSEmailProcessor {

    @Inject
    @ConfValue(value = "offline.days", folder = "server")
    private int offlineDays = 1;

    @Inject
    @ConfValue(value = "offline.hours", folder = "server")
    private int offlineHours = 6;

    @Inject
    @ConfValue(value = "email.offline.devices", folder = "server")
    private boolean emailOfflineDevices = true;

    @Inject
    @ConfValue(value = "email.meter_sync.to_address", folder = "server", defaultValue = "adele@pecgroup.co.za")
    private String toAddress;

    @Inject
    @TextFile("html/offline_prepaid_meter_intro.html")
    private String INTRO_OFFLINE_METERS;

    @Inject
    @TextFile("html/offline_kamstrup_prepaid_meter.html")
    private String KAMSTRUP_OFFLINE_METERS;

    @Inject
    @TextFile("html/offline_elster_prepaid_meter.html")
    private String ELSTER_OFFLINE_METERS;

    @Inject
    @TextFile("html/offline_nes_prepaid_meter.html")
    private String NES_OFFLINE_METERS;

    @Inject
    @TextFile("html/offline_generic_prepaid_meter.html")
    private String GENERIC_OFFLINE_METERS;

    @Inject
    @TextFile("html/failed_prepaid_batch_intro.html")
    private String INTRO_PREPAID_BATCH;

    @Inject
    @TextFile("html/failed_prepaid_batch_info.html")
    private String FAILED_PREPAID_BATCH_INFO;

    @Inject
    @TextFile("html/failed_kamstrup_prepaid_batch.html")
    private String KAMSTRUP_PREPAID_BATCH;

    @Inject
    @TextFile("html/failed_elster_prepaid_batch.html")
    private String ELSTER_PREPAID_BATCH;

    @Inject
    @TextFile("html/failed_nes_prepaid_batch.html")
    private String NES_PREPAID_BATCH;

    @Inject
    @TextFile("html/failed_generic_prepaid_batch.html")
    private String GENERIC_PREPAID_BATCH;
    @Inject
    MailService mailService;

    @Inject
    PrepaidMeterFilterService filterService;

    public static final Logger TAG = Logger.getLogger(MDMSEmailProcessor.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Resource(mappedName = "java:/jdbc/IceUtil")
    private DataSource iceDataSource;

    @Lock(LockType.WRITE)
    @Schedule(hour = "3", minute = "0", second = "0", persistent = false)
    public void atLastOneDaySchedule() {

        TAG.info("Schedule job started username = '" + mailService.getUsername() + "' emailOfflineDevices = " + emailOfflineDevices);

        if (emailOfflineDevices && !StringUtils.isBlank(mailService.getUsername())) {
            offlineDays = offlineDays == 0 ? 1 : offlineDays;
            TAG.info("Process job: Last One Day Offline Meters");
            processLastOneDayOfflineDevices();
        }
    }

    @Lock(LockType.WRITE)
    @Schedule(hour = "*/6", minute = "0", second = "0", persistent = false)
    public void atPrepaidMeterOfflineReportSchedule() {

        TAG.info("Schedule job started username = '" + mailService.getUsername() + "' emailOfflineDevices = " + emailOfflineDevices);

        if (emailOfflineDevices && !StringUtils.isBlank(mailService.getUsername()) ) {
            offlineHours = offlineHours == 0 ? 6 : offlineHours;
            TAG.info("Process job: Last 6 Hours Offline Prepaid Meters");
            processSixHourlyPrepaidOfflineDevices();
        }
    }

    @Lock(LockType.WRITE)
    @Schedule(hour = "*/6", minute = "5", second = "0", persistent = false)
    public void atPrepaidBatchFailureReportSchedule() {

        TAG.info("Schedule job started username = '" + mailService.getUsername() + "' emailOfflineDevices = " + emailOfflineDevices);

        if (emailOfflineDevices && !StringUtils.isBlank(mailService.getUsername()) ) {
            offlineHours = offlineHours == 0 ? 6 : offlineHours;
            TAG.info("Process job: Last 6 Hours Prepaid Batch Failures");
            processSixHourlyPrepaidBatchFailures();
        }
    }

    /**
     * Email meters, all types, that were offline yesterday report
     */
    public void processLastOneDayOfflineDevices() {
        DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Set<Callable<FutureResult>> callables = new HashSet<>();

        callables.add(() -> {
            TAG.info("Execute Kamstrup query");
            List kamstrupMeterList = new ArrayList();

            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                String query = String.format("select *" +
                                " from kamstrup_meter " +
                                " where exists " +
                                "  (select * " +
                                "   from meter_reading " +
                                "   where kam_meter_id = kamstrup_meter.meter_id and entry_day >= %s) " +
                                "  and not exists " +
                                "  (select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day >= %s)",
                        LocalDate.now().minusMonths(1).minusDays(3).format(DAY_FORMAT),
                        LocalDate.now().minusDays(offlineDays).format(DAY_FORMAT));

                TAG.info(query);
                for (KamstrupMeterEntity kamstrupMeterEntity :
                        new DataSourceDB<>(KamstrupMeterEntity.class).getAll(connection, query, null)) {

                    if (!StringUtils.isBlank(kamstrupMeterEntity.serialN.get())) {
                        kamstrupMeterList.add(kamstrupMeterEntity.serialN.get());
                    }
                }

                TAG.info("Kamstrup query size : " + kamstrupMeterList.size());
                return new FutureResult(kamstrupMeterList, "_KAMSTRUP_");
            }
        });

        callables.add(() -> {
            TAG.info("Execute Elster query");
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                List elsterMeterEntityList = new ArrayList();
                String query = String.format("select *" +
                                " from elster_meter " +
                                " where exists " +
                                "  (select * " +
                                "   from meter_reading " +
                                "   where els_meter_id = elster_meter.meter_id and entry_day >= ?) " +
                                "  and not exists " +
                                "  (select * from meter_reading where els_meter_id = elster_meter.meter_id and entry_day >= ?)");

                TAG.info(query);
                for (ElsterMeterEntity elsterMeterEntity :
                        new DataSourceDB<>(ElsterMeterEntity.class).getAll(connection, query,
                                LocalDate.now().minusMonths(1).format(DAY_FORMAT),
                                LocalDate.now().minusDays(offlineDays).format(DAY_FORMAT))) {

                    if (!StringUtils.isBlank(elsterMeterEntity.serialN.get())) {
                        elsterMeterEntityList.add(elsterMeterEntity.serialN.get());
                    }
                }

                TAG.info("Elster query size : " + elsterMeterEntityList.size());
                return new FutureResult(elsterMeterEntityList, "_ELSTER_");
            }
        });

        callables.add(() -> {
            TAG.info("Execute Nes query");
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                List nesMeterEntityList = new ArrayList();
                String query = String.format("select *" +
                                " from nes_meter " +
                                " where exists " +
                                "  (select * " +
                                "   from meter_reading " +
                                "   where nes_meter_id = nes_meter.meter_id and entry_day >= ?) " +
                                "  and not exists " +
                                "  (select * from meter_reading where nes_meter_id = nes_meter.meter_id and entry_day >= ?)");

                TAG.info(query);
                for (NESMeterEntity nesMeterEntity :
                        new DataSourceDB<>(NESMeterEntity.class).getAll(connection, query,
                                LocalDate.now().minusMonths(1).format(DAY_FORMAT),
                                LocalDate.now().minusDays(offlineDays).format(DAY_FORMAT))) {

                    if (!StringUtils.isBlank(nesMeterEntity.serialN.get())) {
                        nesMeterEntityList.add(nesMeterEntity.serialN.get());
                    }
                }

                TAG.info("Nes query size : " + nesMeterEntityList.size());
                return new FutureResult(nesMeterEntityList, "_NES_METER_");
            }
        });

        callables.add(() -> {
            TAG.info("Execute Generic query");
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                List genericMeterEntityList = new ArrayList();

                String qeury = String.format("select *" +
                                " from generic_meter " +
                                " where exists " +
                                "  (select * " +
                                "   from meter_reading " +
                                "   where generic_meter_id = generic_meter.generic_meter_id and entry_day >= ?) " +
                                "  and not exists " +
                                "  (select * from meter_reading where generic_meter_id = generic_meter.generic_meter_id and entry_day >= ?)");

                TAG.info(qeury);
                for (GenericMeterEntity genericMeterEntity :
                        new DataSourceDB<>(GenericMeterEntity.class).getAll(connection, qeury,
                                LocalDate.now().minusMonths(1).format(DAY_FORMAT),
                                LocalDate.now().minusDays(offlineDays).format(DAY_FORMAT))) {

                    if (!StringUtils.isBlank(genericMeterEntity.meterSerialN.get())) {
                        genericMeterEntityList.add(genericMeterEntity.meterSerialN.get());
                    }
                }

                TAG.info("Generic query size : " + genericMeterEntityList.size());
                return new FutureResult(genericMeterEntityList, "_GENERIC_");
            }
        });

        try {

            // Run through results and group email data
            List<Future<FutureResult>> futures = executorService.invokeAll(callables);
            TreeMap<String, TreeMap<String, ArrayList<PortfolioDetail>>> emailDataMap = new TreeMap<>();
            Set<String> metersWithoutEmail = new HashSet<>();

            TAG.info("Process futures");
            for (Future<FutureResult> future : futures) {

                List<String> meters = future.get().getResult();
                String meterType = future.get().getType();

                meters.forEach(serialN -> {
                    // Get email
                    ICEMeterPortfolioView iceMeterPortfolioView =
                            DataSourceDB.get(ICEMeterPortfolioView.class, iceDataSource,
                                    "select * from ICE_Meter_Portfolio_V where upper(trim(ice_meter_number)) = ?",
                                    serialN);

                    if (iceMeterPortfolioView != null && iceMeterPortfolioView.portfolioManagerEmail.get() != null) {
                        setUpEmailDataMap(iceMeterPortfolioView.portfolioManagerEmail.get(),
                                new PortfolioDetail(serialN, iceMeterPortfolioView.buildingNumber.get(),
                                        iceMeterPortfolioView.propertyName.get()), meterType, emailDataMap);
                    } else {
                        TAG.info("No portfolioManagerEmail found for meter serialn = " + serialN + " type " + meterType);
                        metersWithoutEmail.add(serialN + " " + meterType);
                    }

                    if (iceMeterPortfolioView != null && iceMeterPortfolioView.branchManagerEmail.get() != null) {
                        setUpEmailDataMap(iceMeterPortfolioView.branchManagerEmail.get(),
                                new PortfolioDetail(serialN, iceMeterPortfolioView.buildingNumber.get(),
                                        iceMeterPortfolioView.propertyName.get()), meterType, emailDataMap);
                    } else {
                        TAG.info("No branchManagerEmail found for meter serialn = " + serialN + " type " + meterType);
                        metersWithoutEmail.add(serialN + " " + meterType);
                    }
                });
            }

            TAG.info("METERS without email : ");
            metersWithoutEmail.forEach(i -> TAG.info(i + ","));

            TAG.info("Process email");
            // Build email to be send for meter that are off per type
            for (Map.Entry<String, TreeMap<String, ArrayList<PortfolioDetail>>> entryPerEmail : emailDataMap.entrySet()) {

                String email = entryPerEmail.getKey();
                String htmlBodyTemplate = String.format(INTRO_OFFLINE_METERS, offlineDays);

                for (Map.Entry<String, ArrayList<PortfolioDetail>> entryPerType : entryPerEmail.getValue().entrySet()) {
                    String type = entryPerType.getKey();
                    List<PortfolioDetail> portfolioDetailArrayList = entryPerType.getValue();

                    switch (type) {
                        case "_KAMSTRUP_":
                            htmlBodyTemplate += String.format(KAMSTRUP_OFFLINE_METERS, offlineDays);
                            break;
                        case "_ELSTER_":
                            htmlBodyTemplate += String.format(ELSTER_OFFLINE_METERS, offlineDays);
                            break;
                        case "_NES_METER_":
                            htmlBodyTemplate += String.format(NES_OFFLINE_METERS, offlineDays);
                            break;
                        case "_GENERIC_":
                            htmlBodyTemplate += String.format(GENERIC_OFFLINE_METERS, offlineDays);
                            break;
                    }

                    portfolioDetailArrayList.sort(Comparator.comparing(PortfolioDetail::getSerialN));

                    htmlBodyTemplate =
                            htmlBodyTemplate.replace(type,
                                    portfolioDetailArrayList.stream().map(i -> i.toString()).
                                            collect(Collectors.joining("<br>")));
                }

                // Send email
                mailService.sendHtml(email, mailService.getFrom(),
                        String.format("MDMS Production - Devices that were offline the last %d days", offlineDays),
                        htmlBodyTemplate);
            }
        } catch (Exception ex) {
            TAG.severe(ex.getMessage());
        } finally {
            TAG.info("Done");
            executorService.shutdown();
        }

    }

    /**
     * Email prepaid meters that were offline in the last 6 hours report
     */
    public void processSixHourlyPrepaidOfflineDevices() {
        DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<PrepaidMeterInfo> offlinePrepaidMeterList = new ArrayList<>();

        for(String serialN: filterService.getPrepaidMeters()) {

            try (Connection connection = dataSource.getConnection()) {

                connection.setAutoCommit(false);

                try(Statement sqlStatement = connection.createStatement()) {

                    String meterReadingsQuery = String.format("SELECT COUNT(*) AS READING_COUNT " +
                                    "FROM METER_READING " +
                                    "WHERE ENTRY_TIME >= %s " +
                                    "AND _METER_ID_COL_ = '_METER_ID_' ",
                            DriverFactory.getDriver().toDate(LocalDateTime.now().minusHours(offlineHours)));

                    KamstrupMeterEntity kamstrupMeterEntity = null;
                    NESMeterEntity nesMeterEntity = null;
                    ElsterMeterEntity elsterMeterEntity = null;
                    GenericMeterEntity genericMeterEntity = null;
                    PrepaidMeterInfo prepaidMeterInfo = null;

                    kamstrupMeterEntity = this.getKamstrupMeterEntityBySerialN(connection, serialN);

                    if( kamstrupMeterEntity == null ) {
                        nesMeterEntity = this.getNesMeterEntityBySerialN(connection, serialN);
                        if( nesMeterEntity == null ) {
                            elsterMeterEntity = this.getElsterMeterEntityBySerialN(connection, serialN);
                            if( elsterMeterEntity == null ) {
                                genericMeterEntity = this.getGenericMeterEntityBySerialN(connection, serialN);
                                if( genericMeterEntity == null ) {
                                    meterReadingsQuery = "NONE";
                                    prepaidMeterInfo = null;
                                } else {
                                    meterReadingsQuery.replace("_METER_ID_COL_", "GENERIC_METER_ID");
                                    meterReadingsQuery.replace("_METER_ID_", genericMeterEntity.meterId.get());
                                    prepaidMeterInfo = new PrepaidMeterInfo(
                                            genericMeterEntity.meterId.get(),
                                            genericMeterEntity.meterSerialN.get(),
                                            "_GENERIC_",
                                            genericMeterEntity.lastCommsD.get());
                                }
                            } else {
                                meterReadingsQuery = meterReadingsQuery.replace("_METER_ID_COL_", "ELS_METER_ID");
                                meterReadingsQuery = meterReadingsQuery.replace("_METER_ID_", elsterMeterEntity.meterId.get());
                                prepaidMeterInfo = new PrepaidMeterInfo(
                                        elsterMeterEntity.meterId.get(),
                                        elsterMeterEntity.serialN.get(),
                                        "_ELSTER_",
                                        elsterMeterEntity.lastCommsD.get());
                            }
                        } else {
                            meterReadingsQuery = meterReadingsQuery.replace("_METER_ID_COL_", "NES_METER_ID");
                            meterReadingsQuery = meterReadingsQuery.replace("_METER_ID_", nesMeterEntity.meterId.get());
                            prepaidMeterInfo = new PrepaidMeterInfo(
                                    nesMeterEntity.meterId.get(),
                                    nesMeterEntity.serialN.get(),
                                    "_NES_METER_",
                                    nesMeterEntity.lastCommsD.get());
                        }
                    } else {
                        meterReadingsQuery = meterReadingsQuery.replace("_METER_ID_COL_", "KAM_METER_ID");
                        meterReadingsQuery = meterReadingsQuery.replace("_METER_ID_", kamstrupMeterEntity.meterId.get());
                        prepaidMeterInfo = new PrepaidMeterInfo(
                                kamstrupMeterEntity.meterId.get(),
                                kamstrupMeterEntity.serialN.get(),
                                "_KAMSTRUP_",
                                kamstrupMeterEntity.lastCommsD.get());
                    }

                    if( prepaidMeterInfo != null ) {

                        try ( ResultSet meterResultSet = sqlStatement.executeQuery( meterReadingsQuery ) ) {
                            if( meterResultSet.next() ) {
                                int reading_count = meterResultSet.getInt("READING_COUNT");
                                if(reading_count < 1) {
                                    offlinePrepaidMeterList.add(prepaidMeterInfo);
                                }
                            }
                        }
                    }

                }

            } catch (SQLException ex) {
                TAG.severe(ex.getMessage());
            }

        }

        try {

            TreeMap<String, TreeMap<String, ArrayList<PortfolioDetail>>> emailDataMap = new TreeMap<>();
            Set<String> metersWithoutEmail = new HashSet<>();

            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);

                offlinePrepaidMeterList.forEach( prepaidMeter -> {

                    ICEMeterPortfolioView iceMeterPortfolioView =
                            DataSourceDB.get(ICEMeterPortfolioView.class, iceDataSource,
                                    "select * from ICE_Meter_Portfolio_V where upper(trim(ice_meter_number)) = ?",
                                    prepaidMeter.getSerialN());

                    if (iceMeterPortfolioView != null && iceMeterPortfolioView.portfolioManagerEmail.get() != null) {
                        setUpEmailDataMap(iceMeterPortfolioView.portfolioManagerEmail.get(),
                                new PortfolioDetail(prepaidMeter.getSerialN(), prepaidMeter.getLastCommsDate(),
                                        iceMeterPortfolioView.buildingNumber.get(), iceMeterPortfolioView.propertyName.get()),
                                prepaidMeter.getMeterType(), emailDataMap);
                    } else {
                        TAG.info("No portfolioManagerEmail found for meter serialn = " + prepaidMeter.getSerialN() + " type " + prepaidMeter.getMeterType());
                        metersWithoutEmail.add(prepaidMeter.getSerialN() + " " + prepaidMeter.getMeterType());
                    }

                    if (iceMeterPortfolioView != null && iceMeterPortfolioView.branchManagerEmail.get() != null) {
                        setUpEmailDataMap(iceMeterPortfolioView.branchManagerEmail.get(),
                                new PortfolioDetail(prepaidMeter.getSerialN(), prepaidMeter.getLastCommsDate(),
                                        iceMeterPortfolioView.buildingNumber.get(), iceMeterPortfolioView.propertyName.get()),
                                prepaidMeter.getMeterType(), emailDataMap);
                    } else {
                        TAG.info("No branchManagerEmail found for meter serialn = " + prepaidMeter.getSerialN() + " type " + prepaidMeter.getMeterType());
                        metersWithoutEmail.add(prepaidMeter.getSerialN() + " " + prepaidMeter.getMeterType());
                    }
                });

            } catch (SQLException ex) {
                TAG.severe(ex.getMessage());
            }

            TAG.info("METERS without email : ");
            metersWithoutEmail.forEach(i -> TAG.info(i + ","));

            TAG.info("Process offline prepaid email -> ");
            // Build email to be send for meter that are off per type
            for (Map.Entry<String, TreeMap<String, ArrayList<PortfolioDetail>>> entryPerEmail : emailDataMap.entrySet()) {

                String email = entryPerEmail.getKey();
                String htmlBodyTemplate = String.format(INTRO_OFFLINE_METERS, offlineHours);

                for (Map.Entry<String, ArrayList<PortfolioDetail>> entryPerType : entryPerEmail.getValue().entrySet()) {
                    String type = entryPerType.getKey();
                    List<PortfolioDetail> portfolioDetailArrayList = entryPerType.getValue();

                    switch (type) {
                        case "_KAMSTRUP_":
                            htmlBodyTemplate += String.format(KAMSTRUP_OFFLINE_METERS, offlineHours);
                            break;
                        case "_ELSTER_":
                            htmlBodyTemplate += String.format(ELSTER_OFFLINE_METERS, offlineHours);
                            break;
                        case "_NES_METER_":
                            htmlBodyTemplate += String.format(NES_OFFLINE_METERS, offlineHours);
                            break;
                        case "_GENERIC_":
                            htmlBodyTemplate += String.format(GENERIC_OFFLINE_METERS, offlineHours);
                            break;
                    }

                    portfolioDetailArrayList.sort(Comparator.comparing(PortfolioDetail::getSerialN));

                    htmlBodyTemplate =
                            htmlBodyTemplate.replace(type,
                                    portfolioDetailArrayList.stream().map(i -> i.toPrepaidString()).
                                            collect(Collectors.joining("<br>")));
                }

                email = StringUtils.isEmpty(email) ? this.toAddress : email;
                mailService.sendHtml(email, mailService.getFrom(),
                        String.format("MDMS Production - Prepaid meters that were offline the last %d hours", offlineHours),
                        htmlBodyTemplate);
            }

        } catch (Exception ex) {
            TAG.severe(ex.getMessage());
        }

    }

    /**
     * Email prepaid batch failures in the last 6 hours report
     */
    public void processSixHourlyPrepaidBatchFailures() {
        DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<PrepaidBatchInfo> failedPrepaidBatchList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try(Statement sqlStatement = connection.createStatement()) {

                String prepaidBatchQuery = String.format("SELECT * FROM " +
                                "PREPAID_METER_READING_BATCH " +
                                "WHERE UPDATED_DATE >= %s " +
                                "AND UTIL_STATUS_ID = 'Failed' " +
                                "ORDER BY UPDATED_DATE",
                        DriverFactory.getDriver().toDate(LocalDateTime.now().minusHours(offlineHours)));

                try ( ResultSet prepaidBatchResultSet = sqlStatement.executeQuery( prepaidBatchQuery ) ) {
                    while( prepaidBatchResultSet.next() ) {
                        failedPrepaidBatchList.add( new PrepaidBatchInfo(
                                prepaidBatchResultSet.getString("PREPAID_METER_READING_BATCH_ID"),
                                prepaidBatchResultSet.getString("UTIL_DATA"),
                                prepaidBatchResultSet.getString("ERROR"),
                                prepaidBatchResultSet.getTimestamp("UPDATED_DATE") ) );
                    }
                }

            }

        } catch (Exception ex) {
            TAG.severe(ex.getMessage());
        }

        try {

            List<PrepaidMeterInfo> prepaidMeterInfoList = new ArrayList<>();

            failedPrepaidBatchList.forEach( prepaidBatchInfo -> {

                List<String> iceMeterIdList = getPrepaidBatchIceMeterIdList( prepaidBatchInfo.getUtilData() );

                if( !CollectionUtils.isEmpty(iceMeterIdList) ) {

                    String iceMeterIdWhere = getIceMeterIdStringList(iceMeterIdList);

                    String iceMeterPortfolioViewQuery =
                            String.format("select * from ICE_Meter_Portfolio_V where ice_meter_id in (%s)", iceMeterIdWhere);

                    List<ICEMeterPortfolioView> iceMeterPortfolioViewList =
                            DataSourceDB.getAllAsList(ICEMeterPortfolioView.class, iceDataSource, iceMeterPortfolioViewQuery);

                    if( !CollectionUtils.isEmpty(iceMeterPortfolioViewList) ) {

                        for(ICEMeterPortfolioView iceMeterPortfolioView: iceMeterPortfolioViewList) {

                            PrepaidMeterInfo prepaidMeterInfo = getPrepaidMeterInfo(iceMeterPortfolioView, prepaidBatchInfo);

                            if(prepaidBatchInfo != null) {
                                prepaidMeterInfoList.add( prepaidMeterInfo );
                            }

                        }

                    }

                }

            });

            TAG.info("Process prepaid batch failures email -> ");

            List<String> pmEmailList = prepaidMeterInfoList.stream()
                    .map( e -> e.getPortFolioManagerEmail()).distinct().collect(Collectors.toList());

            for( String pmEmail : pmEmailList ) {

                String htmlBodyTemplate = String.format(INTRO_PREPAID_BATCH, offlineHours);

                List<String> prepaidBatchIdList = prepaidMeterInfoList.stream()
                        .map( b -> b.getPrepaidBatchId()).distinct().collect(Collectors.toList());

                for(String prepaidBatchId: prepaidBatchIdList) {

                    PrepaidBatchInfo prepaidBatchInfo = getPrepaidBatchInfoById(prepaidBatchId, failedPrepaidBatchList);

                    String failed_prepaid_batch_info_heading = FAILED_PREPAID_BATCH_INFO
                            .replace("_PREPAID_BATCH_ID_", prepaidBatchInfo.getPrepaidbatchId())
                            .replace("_PROCESSING_DATE_", prepaidBatchInfo.getUpdatedDate().toLocalDateTime().format(formatDatetime()))
                            .replace("_ERROR_DESCRIPTION_", getPrepaidBatchErrorDescription( prepaidBatchInfo.getErrorDescription() ) );

                    htmlBodyTemplate += failed_prepaid_batch_info_heading;

                    List<String> meterTypes = prepaidMeterInfoList.stream()
                            .map( t -> t.getMeterType() ).distinct().collect(Collectors.toList());

                    for(String type: meterTypes) {

                        List<PrepaidMeterInfo> pmPrepaidMeterInfoList = prepaidMeterInfoList.stream()
                                .filter( mtr -> mtr.getPortFolioManagerEmail().equalsIgnoreCase(pmEmail))
                                .filter( mtr -> mtr.getPrepaidBatchId().equals(prepaidBatchId))
                                .filter( mtr -> mtr.getMeterType().equalsIgnoreCase(type))
                                .collect(Collectors.toList());

                        if( !CollectionUtils.isEmpty(pmPrepaidMeterInfoList) ) {

                            pmPrepaidMeterInfoList.sort(Comparator.comparing(PrepaidMeterInfo::getLastCommsDate));

                            switch (type) {
                                case "_KAMSTRUP_":
                                    htmlBodyTemplate += KAMSTRUP_PREPAID_BATCH;
                                    break;
                                case "_ELSTER_":
                                    htmlBodyTemplate += ELSTER_PREPAID_BATCH;
                                    break;
                                case "_NES_METER_":
                                    htmlBodyTemplate += NES_PREPAID_BATCH;
                                    break;
                                case "_GENERIC_METER_":
                                    htmlBodyTemplate += GENERIC_PREPAID_BATCH;
                                    break;
                            }

                            htmlBodyTemplate =
                                    htmlBodyTemplate.replace(type,
                                            pmPrepaidMeterInfoList.stream().map(mtr -> mtr.toString()).
                                                    collect(Collectors.joining("<br>")));

                        }

                    }

                }

                mailService.sendHtml(pmEmail, mailService.getFrom(),
                        String.format("MDMS Production - Prepaid batches failed in the last %d hours", offlineHours),
                        htmlBodyTemplate);

            }

        } catch (Exception ex) {
            TAG.severe(ex.getMessage());
        }

    }

    private KamstrupMeterEntity getKamstrupMeterEntityBySerialN(Connection connection, String serialN) {
        return (KamstrupMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new KamstrupMeterEntity().serialN.set( serialN ));
    }

    private NESMeterEntity getNesMeterEntityBySerialN(Connection connection, String serialN) {
        return (NESMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new NESMeterEntity().serialN.set( serialN ) );
    }

    private ElsterMeterEntity getElsterMeterEntityBySerialN(Connection connection, String serialN) {
        return (ElsterMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new ElsterMeterEntity().serialN.set( serialN ));
    }

    private GenericMeterEntity getGenericMeterEntityBySerialN(Connection connection, String serialN) {
        return (GenericMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new GenericMeterEntity().meterSerialN.set( serialN ));
    }

    private DateTimeFormatter formatDatetime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formatter;
    }

    private String getIceMeterIdStringList(List<String> iceMeterIdList) {

        String iceMeterIdStringList = "";
        List<String> tempIceMeterIdList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(iceMeterIdList)) {
            for(String iceMeterId: iceMeterIdList) {
                tempIceMeterIdList.add( String.format("'%s'",iceMeterId) );
            }

            iceMeterIdStringList = tempIceMeterIdList.stream().collect(Collectors.joining(","));
        }

        return iceMeterIdStringList;
    }

    private String getPrepaidBatchErrorDescription(String fullErrorDescription) {

        List<String> errorLines = new ArrayList<>();
        String shortErrorDescription = "empty";

        if( !StringUtils.isEmpty(fullErrorDescription) ) {

            errorLines = new BufferedReader(new StringReader(fullErrorDescription))
                    .lines().collect(Collectors.toList());

            shortErrorDescription = errorLines.get(0);

        }

        return shortErrorDescription;
    }

    private List<String> getPrepaidBatchIceMeterIdList(String utilData) {
        List<String> iceMeterIdList = new ArrayList<>();

        if( !StringUtils.isEmpty(utilData) ) {

            new BufferedReader(new StringReader(utilData))
                    .lines().forEach( line -> {
                        if( line.contains("ICE_Meter_ID") ) {
                            String[] subStrings = line.split(":");
                            String iceMeterId = subStrings[1];
                            iceMeterId = iceMeterId.replaceAll(" ", "");
                            iceMeterIdList.add( iceMeterId );
                        }
            });

        }

        return iceMeterIdList.stream().distinct().collect(Collectors.toList());
    }

    private PrepaidBatchInfo getPrepaidBatchInfoById(String failedPrepaidBatchId, List<PrepaidBatchInfo> prepaidBatchInfoList) {
        return prepaidBatchInfoList.stream().filter( b -> b.getPrepaidbatchId().equals(failedPrepaidBatchId)).findFirst().get();
    }

    private PrepaidMeterInfo getPrepaidMeterInfo(ICEMeterPortfolioView iceMeterPortfolioView, PrepaidBatchInfo prepaidBatchInfo) {

        String serialN = iceMeterPortfolioView.iceMeterNumber.get();
        String meterType = "_NONE_";
        String buildingNumber = iceMeterPortfolioView.buildingNumber.get();
        String propertyName = iceMeterPortfolioView.propertyName.get();
        Timestamp lastCommsDate = Timestamp.valueOf( LocalDateTime.now() );

        KamstrupMeterEntity kamstrupMeterEntity = null;
        NESMeterEntity nesMeterEntity = null;
        ElsterMeterEntity elsterMeterEntity = null;
        GenericMeterEntity genericMeterEntity = null;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);

            kamstrupMeterEntity = this.getKamstrupMeterEntityBySerialN(connection, serialN);
            if( kamstrupMeterEntity == null ) {
                nesMeterEntity = this.getNesMeterEntityBySerialN(connection, serialN);
                if( nesMeterEntity == null ) {
                    elsterMeterEntity = this.getElsterMeterEntityBySerialN(connection, serialN);
                    if( elsterMeterEntity == null ) {
                        genericMeterEntity = this.getGenericMeterEntityBySerialN(connection, serialN);
                        if( genericMeterEntity == null ) {
                            meterType = "_NONE_";
                        } else {
                            meterType = "_GENERIC_METER_";
                            lastCommsDate = genericMeterEntity.lastCommsD.get() == null ? Timestamp.valueOf( LocalDateTime.now() ) :
                                    genericMeterEntity.lastCommsD.get();
                        }
                    } else {
                        meterType = "_ELSTER_";
                        lastCommsDate = elsterMeterEntity.lastCommsD.get() == null ? Timestamp.valueOf( LocalDateTime.now() ) :
                                elsterMeterEntity.lastCommsD.get();
                    }
                } else {
                    meterType = "_NES_METER_";
                    lastCommsDate = nesMeterEntity.lastCommsD.get() == null ? Timestamp.valueOf( LocalDateTime.now() ) :
                            nesMeterEntity.lastCommsD.get();
                }
            } else {
                meterType = "_KAMSTRUP_";
                lastCommsDate = kamstrupMeterEntity.lastCommsD.get() == null ? Timestamp.valueOf( LocalDateTime.now() ) :
                        kamstrupMeterEntity.lastCommsD.get();
            }

        } catch (SQLException ex) {
            TAG.severe(ex.getMessage());
        }

        String pmEmail = StringUtils.isEmpty(iceMeterPortfolioView.portfolioManagerEmail.get()) ? this.toAddress :
                iceMeterPortfolioView.portfolioManagerEmail.get();

        String bmEmail = StringUtils.isEmpty(iceMeterPortfolioView.branchManagerEmail.get()) ? this.toAddress :
                iceMeterPortfolioView.branchManagerEmail.get();

        PrepaidMeterInfo prepaidMeterInfo = new PrepaidMeterInfo(
                serialN,
                meterType,
                lastCommsDate,
                prepaidBatchInfo.getPrepaidbatchId(),
                pmEmail,
                bmEmail,
                buildingNumber,
                propertyName);

        return prepaidMeterInfo;
    }

    private void setUpEmailDataMap(String email, PortfolioDetail portfolioDetail,
                                   String meterType, TreeMap<String, TreeMap<String,
            ArrayList<PortfolioDetail>>> emailDataMap) {

        TreeMap<String, ArrayList<PortfolioDetail>> typeMeterMap = emailDataMap.get(email);

        if (typeMeterMap == null) {
            emailDataMap.put(email, new TreeMap<>());
            typeMeterMap = emailDataMap.get(email);
        }

        List portFolioDetailList = typeMeterMap.get(meterType);
        if (portFolioDetailList == null) {
            typeMeterMap.put(meterType, new ArrayList<>());
            portFolioDetailList = typeMeterMap.get(meterType);
        }

        if (!portFolioDetailList.contains(portfolioDetail)) {
            portFolioDetailList.add(portfolioDetail);
        }
    }


    @Data
    @AllArgsConstructor
    public static class FutureResult {
        private List result;
        private String type;
    }

    @Data
    public static class PortfolioDetail {
        private String serialN;
        private String buildingNumber;
        private String propertyName;
        private Timestamp lastCommsDate;

        public PortfolioDetail(String serialN, String buildingNumber, String propertyName) {
            this.serialN = serialN;
            this.buildingNumber = buildingNumber;
            this.propertyName = propertyName;
        }

        public PortfolioDetail(String serialN, Timestamp lastCommsDate, String buildingNumber, String propertyName) {
            this.serialN = serialN;
            this.lastCommsDate = lastCommsDate;
            this.buildingNumber = buildingNumber;
            this.propertyName = propertyName;
        }

        public String toPrepaidString() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return "serialN=" + serialN + ", lastCommsDate=" +
                    lastCommsDate.toLocalDateTime().format(formatter) +
                    ", buildingNumber=" + buildingNumber + ", propertyName=" + propertyName;
        }

        @Override
        public String toString() {
            return "serialN=" + serialN + ", lastCommsDate=" + ", buildingNumber=" + buildingNumber
                    + ", propertyName=" + propertyName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PortfolioDetail)) return false;
            PortfolioDetail that = (PortfolioDetail) o;
            return Objects.equals(getSerialN(), that.getSerialN()) &&
                    Objects.equals(getBuildingNumber(), that.getBuildingNumber()) &&
                    Objects.equals(getPropertyName(), that.getPropertyName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getSerialN(), getBuildingNumber(), getPropertyName());
        }
    }
}
