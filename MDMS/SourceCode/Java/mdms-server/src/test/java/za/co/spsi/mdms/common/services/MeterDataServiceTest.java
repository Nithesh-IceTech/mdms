package za.co.spsi.mdms.common.services;

import org.idempiere.webservice.client.exceptions.WebServiceException;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.dao.MeterResultData;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.dao.ano.MeterRegister;
import za.co.spsi.mdms.common.database.IceUtilDB;
import za.co.spsi.mdms.common.database.MdmsOracleDB;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity;
import za.co.spsi.mdms.common.db.survey.*;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseService;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseViewSyncService;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.services.utility.MeterRegisterUpdateService;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.mdms.utility.BillingDataWrapper;
import za.co.spsi.mdms.utility.MDMSUtilityHelper;
import za.co.spsi.toolkit.crud.idempiere.AgencyBillingProperties;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.entity.FieldLocalDate;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.mdms.common.dao.*;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.io.FileOutputStream;

import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MeterDataServiceTest {

    Logger logger = Logger.getLogger(MeterDataServiceTest.class.getName());

    private DateTimeFormatter dateTimeFormatter;

    private MdmsOracleDB mdmsOracleDB;
    private DataSource mdmsDS;
    private Driver mdmsDriver;

    private IceUtilDB iceUtilDB;
    private DataSource iceUtilDS;
    private Driver iceUtilDriver;

    private MDMSUtilityHelper utilityHelper;
    private MeterDataService meterDataService;
    private IceTimeOfUseViewSyncService iceTimeOfUseViewSyncService;
    private IceTimeOfUseService iceTimeOfUseService;
    private PrepaidMeterFilterService filterService;

    private LocalDateTime today() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        return date != null?LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault()):null;
    }

    public static LocalDate getLocalDate(Date date) {
        LocalDateTime dateTime = getLocalDateTime(date);
        return dateTime != null?dateTime.toLocalDate():null;
    }

    @BeforeAll
    void initDataSource() throws SQLException {

        mdmsOracleDB = new MdmsOracleDB();
        mdmsDS = mdmsOracleDB.getMdmsDS();
        mdmsDriver = mdmsOracleDB.getDriver();
        dateTimeFormatter = mdmsOracleDB.getDateTimeFormatter();

        iceUtilDB = new IceUtilDB();
        iceUtilDS = iceUtilDB.getIdeUtilDS();
        iceUtilDriver = iceUtilDB.getDriver();

        utilityHelper = new MDMSUtilityHelper();
        utilityHelper.billingWrapper = new BillingDataWrapper();
        utilityHelper.mdmsDataSource = mdmsDS;
        utilityHelper.iceDataSource = iceUtilDS;
        utilityHelper.billingProperties = getAgencyBillingProperties();

        meterDataService = new MeterDataService();
        meterDataService.dataSource = mdmsDS;
        iceTimeOfUseViewSyncService = new IceTimeOfUseViewSyncService();
        meterDataService.touService = iceTimeOfUseViewSyncService;
        iceTimeOfUseService = new IceTimeOfUseService();
        iceTimeOfUseService.utilityHelper = utilityHelper;
        meterDataService.touServiceTest = iceTimeOfUseService;
        iceTimeOfUseViewSyncService.iceDataSource = iceUtilDS;

    }

    private AgencyBillingProperties getAgencyBillingProperties() {
        Properties props = new Properties();
        FileInputStream fis = null;
        AgencyBillingProperties agencyBillingProperties = new AgencyBillingProperties();
        try {
            fis = new FileInputStream("src/test/resources/agencybilling.properties");
            props.load(fis);
            agencyBillingProperties.iceBaseUrl = props.getProperty("iceBaseUrl");
            agencyBillingProperties.iceUser = props.getProperty("iceUser");
            agencyBillingProperties.icePassword = props.getProperty("icePassword");
            agencyBillingProperties.iceClientId = Integer.parseInt( props.getProperty("iceClientId") );
            agencyBillingProperties.iceOrgId = Integer.parseInt( props.getProperty("iceOrgId") );
            agencyBillingProperties.iceRoleId = Integer.parseInt( props.getProperty("iceRoleId") );
            agencyBillingProperties.iceWarehouseId = Integer.parseInt( props.getProperty("iceWarehouseId") );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agencyBillingProperties;
    }

    private void printResultData(MeterResultDataArray resultDataArray) {

        for(MeterResultData resultData: resultDataArray) {
            logger.info(String.format("EntryTime: %s, TotalKwhP: %.2f, TotalKVA: %.2f, T1KwhP: %.2f, T1KVA: %.2f, T2KwhP: %.2f, T2KVA: %.2f, Volume1: %.2f",
                    dateTimeFormatter.format(resultData.getEntryTime().toLocalDateTime()),
                    resultData.getTotalKwhP(),
                    resultData.getTotalKVA(),
                    resultData.getT1KwhP(),
                    resultData.getT1KVA(),
                    resultData.getT2KwhP(),
                    resultData.getT2KVA(),
                    resultData.getVolume1()
            ));
        }

    }

    private void printMeterReadings(List<MeterReadingEntity> meterReadings) {
        for(MeterReadingEntity meterReading: meterReadings) {
            logger.info(String.format("EntryTime: %s, CreatedTime: %s, PrepaidMeter: %s, Generated: %s, TotalKwhP: %.2f, T1KwhP: %.2f, T2KwhP:%.2f, Volume1: %.2f",
                    dateTimeFormatter.format(meterReading.entryTime.get().toLocalDateTime()),
                    dateTimeFormatter.format(meterReading.createTime.get().toLocalDateTime()),
                    meterReading.prepaidMeter.get(),
                    meterReading.generated.get(),
                    meterReading.totalKwhP.get(),
                    meterReading.t1KwhP.get(),
                    meterReading.t2KwhP.get(),
                    meterReading.volume1.get()
            ));
        }
    }

    private Timestamp getToDate(Timestamp date, int sec) {
        Timestamp toDate = Timestamp.valueOf(date.toLocalDateTime().plusSeconds(sec));
        return toDate.toLocalDateTime().compareTo(today()) > 0 ? Timestamp.valueOf(today()) : toDate;
    }

    private static DataSourceDB<PecUtilityMeterReadingListEntity> getList(Connection connection) {
        return new DataSourceDB<PecUtilityMeterReadingListEntity>(PecUtilityMeterReadingListEntity.class).getAll(connection,
                "select * from pec_utility_meter_reading_list where REFERENCE_ID = '1038199'");
    }

    @Test
    void myUpdatePreview() {
        // update smart meter readings preview values
        /*DataSourceDB.executeInTx(mdmsDS, connection -> {
            for (PecUtilityMeterReadingListEntity list : getList(connection)) {
                for (PecMeterRegisterEntity register : list.smartMeterRegisterList.get(connection)) {
                    //preview_update(connection, register.meter.getOne(connection), register, list.cycleStartDate.get(), list.readingDate.get(), 120, true);
                    logger.info(String.format("METER %s REGISTER %s. Start %s End %s", register.meter.getOne(connection).meterN.get(), register.registerId.get()
                            ,  list.cycleStartDate.get().toString(), list.readingDate.get().toString()));
                }
            }
        });*/
        List<PecUtilityMeterReadingListEntity> dbUtilityMeterReadings= DataSourceDB.getAllAsList(PecUtilityMeterReadingListEntity.class, mdmsDS, "select * from pec_utility_meter_reading_list where REFERENCE_ID = '1006497'");
        for(PecUtilityMeterReadingListEntity dbUtilityMeterReading: dbUtilityMeterReadings) {
            for (PecMeterRegisterEntity register : dbUtilityMeterReading.smartMeterRegisterList.getAllAsList(mdmsDS)) {
                //logger.info(String.format("METER %s REGISTER %s. Start %s End %s", register.meter.getOne(mdmsDS).meterN.get(), register.registerId.get()
                //        ,  dbUtilityMeterReading.cycleStartDate.get().toString(), dbUtilityMeterReading.readingDate.get().toString()));
                if (register.meter.getOne(mdmsDS).meterN.get().equals("ELON076566") || register.meter.getOne(mdmsDS).meterN.get().equals("20529328")) {
                    preview_update(register.meter.getOne(mdmsDS), register, dbUtilityMeterReading.cycleStartDate.get(), dbUtilityMeterReading.readingDate.get(), 120, true);
                    //preview_getSummaryTouData(register.meter.getOne(mdmsDS), register, dbUtilityMeterReading.cycleStartDate.get(), dbUtilityMeterReading.readingDate.get(), 120, true);
                    //preview_virtual_meter(register.meter.getOne(mdmsDS), register, dbUtilityMeterReading.cycleStartDate.get(), dbUtilityMeterReading.readingDate.get(), 120, true);
                }

            }
        }
    }
    public void preview_update(PecMeterEntity meter, PecMeterRegisterEntity register, Timestamp lStart, Timestamp date, int tmz
            , boolean persistCalculated) {
        try {
            // assert that the meter is the same

            PecMeterReadingEntity reading = register.meterReading.getOne(mdmsDS);

            if (reading != null) {
                Timestamp to = getToDate(date,2); // Reading Date
                // IED-5241: MDMS - TOU Post-Paid Reading List Investigation
                Timestamp from = lStart != null ? lStart : // Cycle Start Date
                Timestamp.valueOf(to.toLocalDateTime().minusDays(1));

                Assert.isTrue(meterDataService.isDataRequestWithinTimeScope(from, to, MeterDataService.Interval.HALF_HOURLY)
                        , "Meter data request for meter %s and register %s exceeds allowable time frame (90 days)"
                        , meter.meterN.get(), register.registerId.get());

                MeterResultDataArray dataSet = meterDataService.getDetailData(meter.meterN.get(), from, to, tmz
                        , MeterDataService.Interval.HALF_HOURLY, "series1", null, null, false);

                // find the meter
                /*KamstrupMeterEntity kamMeter = DataSourceDB.getFromSet(mdmsDS, (KamstrupMeterEntity) new KamstrupMeterEntity().serialN.set(meter.meterN.get()));
                ElsterMeterEntity elsMeter = kamMeter == null ? DataSourceDB.getFromSet(mdmsDS, (ElsterMeterEntity) new ElsterMeterEntity().serialN.set(meter.meterN.get())) : null;
                NESMeterEntity nesMeter = kamMeter == null && elsMeter == null ? DataSourceDB.getFromSet(mdmsDS, (NESMeterEntity) new NESMeterEntity().serialN.set(meter.meterN.get())) : null;
                String query = String.format("select GENERIC_METER.* from GENERIC_METER where GENERIC_METER.METER_SERIAL_N = ? and GENERIC_METER.LIVE = %s",
                        DriverFactory.getDriver().boolToNumber(true));
                GenericMeterEntity genericMeterEntity = DataSourceDB.get(GenericMeterEntity.class, mdmsDS, query, meter.meterN.get());
*/
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

                // persist the data set
                if (persistCalculated && !dataSet.isPersisted()) {
                    ObjectMapper mapper = new ObjectMapper();
                    FileOutputStream fos = new FileOutputStream(dateFormat.format(from) + "_" + dateFormat.format(to) + "_data.json");
                    mapper.writeValue(fos, dataSet);
                    fos.close();
                    dataSet.setPersisted(true);
                    try (Connection connection = mdmsDS.getConnection()) {
                        dataSet.adjustTime((int) TimeUnit.MINUTES.toMillis(-tmz)).getCalculated()
                                .forEach(d -> MeterReadingEntity.generate(connection, d, meter, register, filterService));
                    }
                }

                dataSet = dataSet.filterTOU( register.getTouFrom() , register.getTouTo() , register.getTouDayOfWeek()
                        , getLocalDate(register.dateRx.get()), tmz);
                String mapped = register.getMappedName();

                if (!dataSet.isEmpty()) {
                    // filter for TOU
                    // either max kva or consumption
                    OptionalDouble maxValue = dataSet.getMaxValue(mapped);
                    reading.actualReading.set(maxValue.isPresent() ? maxValue.getAsDouble() : 0.0);

                    if (register.isMaxReading()) {

                        // could have been populated by the meter readings
                        reading.smartReading.set(maxValue.isPresent() ? maxValue.getAsDouble() : 0.0);

                    } else if (register.isKva()) {

                        reading.smartReading.set(dataSet.getMaxTotalKVA());

                    } else if (register.hasTimeOfUse() || register.isGridRegister()  || register.isGeneratorRegister()) {

                        reading.smartReading.set(dataSet.calculateConsumption(mapped + "Usage"));
                    }
                    else if (register.isGridRegister()) {

                    reading.smartReading.set(dataSet.calculateConsumption(mapped + "Usage"));
                    }

                    if (register.isMaxReading()) {

                        reading.reading.set(reading.smartReading.get());

                    } else if (register.hasTimeOfUse() || register.isKva() || register.isGridRegister() || register.isGeneratorRegister()) {

                        reading.reading.set(reading.prevReading1.getNonNull() + reading.smartReading.get());

                    } else {

                        reading.reading.set(maxValue.isPresent() ? maxValue.getAsDouble() : 0.0);

                    }

                    //Set date with 1 second before save
                    Timestamp to_1 = getToDate(date,0);
                    reading.readingDate.set(to_1);

                    System.out.println("Meter : " + meter.meterN.get());
                    System.out.println("Register : " + register.registerId.get());
                    System.out.println("ActualReading : " + reading.actualReading.getNonNull().toString());
                    System.out.println("SmartReading : " + reading.smartReading.getNonNull().toString());
                    System.out.println("Reading : " + reading.reading.get().toString());
                    System.out.println("PrevReading : " + reading.prevReading1.getNonNull().toString());
/*
                    logger.info("Meter : " + meter.meterN.get());
                    logger.info("Register : " + register.registerId.get());
                    logger.info("ActualReading : " + reading.actualReading.getNonNull().toString());
                    logger.info("SmartReading : " + reading.smartReading.getNonNull().toString());
                    logger.info("Reading : " + reading.reading.get().toString());
                    logger.info("PrevReading : " + reading.prevReading1.getNonNull().toString());
                    //logger.info("TOU Name : " + register.timeOfUseName.get());
                    //logger.info("TOU Day : " + register.timeOfUseDayOfWeekName.get());
                    logger.info("TOU Start : " + register.timeOfUseStartTime.get());
                    logger.info("TOU End : " + register.timeOfUseEndTime.get());*/

                    // INSERT DB connection to ICE_TST
                    String query =" SELECT a.ice_meterreadings_id " +
                    " ,b.ice_meter_registerid" +
                            " ,b.ice_meter_register_id" +
                            " FROM ice_meterreadings a" +
                            " INNER JOIN ice_meter_register b ON a.ICE_METER_REGISTER_ID = b.ICE_METER_REGISTER_ID" +
                            " WHERE a.ICE_METERREADINGS_ID = " + reading.reference_id.get();
                    List<List> set = DataSourceDB.executeQuery(iceUtilDS,query);
                    System.out.println("Ice Meter Register Id : " + set.get(0).get(2));
                    //System.out.println("");
                    //logger.info("METER NR : " + register.meterId.get() + " REGISTER NR: " + register.registerId.get());
                }

            }
        } catch (Exception ex) {

        }
    }

    private double getExportScale(String registerId, int scaleIndex) {
        String fieldName = PecMeterRegisterEntity.getMappedName(registerId);
        Optional<Field> field = Arrays.stream(MeterReadingEntity.class.getFields())
                .filter(f -> f.getName().equals(fieldName))
                .findFirst();
        Assert.isTrue(field.isPresent(),"Could not locate field %s in %s",fieldName,MeterReadingEntity.class.getName());
        return Math.pow(10,field.get().getAnnotation(MeterRegister.class).exportScale()[scaleIndex]);
    }

    @ParameterizedTest(name = "{0},{1},{2}")
    @CsvSource({
            "kamstrup_meter,kam_meter_id,20529319,2021-06-01 00:00:00,2021-06-07 23:59:59"
    })
    @DisplayName("MeterDataService: getDetailData() Test")
    void getDetailDataTest(String meterTable, String meterCol, String serialN, String fromDateStr, String toDateStr) {
        Timestamp from = Timestamp.valueOf(fromDateStr);
        Date fromDate = Date.from( from.toInstant() );
        Timestamp to = Timestamp.valueOf(toDateStr);
        Date toDate = Date.from( to.toInstant() );
        int tmzOffset = 0;
        MeterDataService.Interval interval = MeterDataService.Interval.HALF_HOURLY;
        String default_touc1 = "0";
        String default_touc2 = "0";
        boolean removeTimeZone = false;

        logger.info("STEP1: Get meter readings from DB METER_READING table:");

        String query = String.format("select * from meter_reading where " +
                        " %s in (select meter_id from %s where serial_n = '%s') " +
                        " and entry_time between %s and %s " +
                        " order by entry_time asc",
                meterCol,
                meterTable,
                serialN,
                mdmsDriver.toDate(from.toLocalDateTime()),
                mdmsDriver.toDate(to.toLocalDateTime())
        );

        logger.info(String.format("SQL query: \n%s", query));

        List<MeterReadingEntity> dbMeterReadings = DataSourceDB.getAllAsList(MeterReadingEntity.class, mdmsDS, query);

        Integer numberOfReadings = dbMeterReadings.size();
        Assertions.assertNotEquals(0, numberOfReadings);

        printMeterReadings(dbMeterReadings);

        String registerId = "1.1.1.8.0.255"; // TotalKwhP Register ID
        int exportScaleIdx = 0;
        double scaleFactor = 1.0;

        MeterReadingEntity firstReadingEntity = dbMeterReadings.get(0);
        exportScaleIdx = firstReadingEntity.getExportScaleIndex();
        scaleFactor = getExportScale(registerId, exportScaleIdx);
        Double firstReadingScaled = firstReadingEntity.totalKwhP.get() / scaleFactor;

        MeterReadingEntity lastReadingEntity = dbMeterReadings.get(numberOfReadings - 2);
        exportScaleIdx = lastReadingEntity.getExportScaleIndex();
        scaleFactor = getExportScale(registerId, exportScaleIdx);
        Double lastReadingScaled = lastReadingEntity.totalKwhP.get() / scaleFactor;;

        logger.info("STEP2: Get meter readings from MeterDataService.getDetailData(): ");

        logger.info( String.format("Request data for serialN: %s, FromDate: %s, ToDate %s",
                serialN,
                dateTimeFormatter.format(from.toLocalDateTime()),
                dateTimeFormatter.format(to.toLocalDateTime())
        ));

        MeterResultDataArray resultDataArray =
                meterDataService.getDetailData(serialN, fromDate, toDate,
                        tmzOffset, interval, "series1", default_touc1, default_touc2,removeTimeZone);

//        Double offpeakUsage1 = resultDataArray.stream().filter(res -> res.getTou1().equalsIgnoreCase("OP"))
//                .map(MeterResultData::getTotalKwhPUsage).reduce(0.0, Double::sum);
//
//        Double peakUsage1 = resultDataArray.stream().filter(res -> res.getTou1().equalsIgnoreCase("P"))
//                .map(MeterResultData::getTotalKwhPUsage).reduce(0.0, Double::sum);
//
//        Double standardUsage1 = resultDataArray.stream().filter(res -> res.getTou1().equalsIgnoreCase("STD"))
//                .map(MeterResultData::getTotalKwhPUsage).reduce(0.0, Double::sum);
//
//        Double offpeakUsage2 = resultDataArray.stream().filter(res -> res.getTou2().equalsIgnoreCase("OP"))
//                .map(MeterResultData::getTotalKwhPUsage).reduce(0.0, Double::sum);
//
//        Double peakUsage2 = resultDataArray.stream().filter(res -> res.getTou2().equalsIgnoreCase("P"))
//                .map(MeterResultData::getTotalKwhPUsage).reduce(0.0, Double::sum);
//
//        Double standardUsage2 = resultDataArray.stream().filter(res -> res.getTou2().equalsIgnoreCase("STD"))
//                .map(MeterResultData::getTotalKwhPUsage).reduce(0.0, Double::sum);

//        SummaryEntity.set()

        logger.info(String.format("Number of entries retrieved: %d", resultDataArray.size()));

        printResultData(resultDataArray);

//        logger.info("STEP3: Test Meter Reading Scaling Between METER_READING table and MeterDataService.getDetailData()");
//
//        Integer numberOfResultEntries = resultDataArray.size();
//        Assertions.assertNotEquals(0, numberOfResultEntries);
//
//        MeterResultData firstResult = resultDataArray.get(0);
//        Timestamp expectedStartDate = Timestamp.valueOf( from.toLocalDateTime() );
//        Assertions.assertEquals( expectedStartDate, firstResult.getEntryTime() );
//        Assertions.assertNotNull(firstResult.getTotalKwhP());
//        Assertions.assertNotEquals(0, firstResult.getTotalKwhP());
//
//        logger.info(String.format("First Entry -> Compare scaled meterReadingEntity.totalKwhP: %.2f with meterResultData.totalKwhP: %.2f",
//                firstReadingScaled,
//                firstResult.getTotalKwhP() ));
//        Assertions.assertEquals(firstReadingScaled, firstResult.getTotalKwhP());
//
//        MeterResultData lastResult = resultDataArray.get(numberOfResultEntries - 1);
//        Timestamp expectedEndDate = Timestamp.valueOf( to.toLocalDateTime().minusMinutes(30) );
//        Assertions.assertEquals( expectedEndDate, lastResult.getEntryTime() );
//        Assertions.assertNotNull(lastResult.getTotalKwhP());
//        Assertions.assertNotEquals(0, lastResult.getTotalKwhP());
//
//        logger.info(String.format("Last Entry -> Compare scaled meterReadingEntity.totalKwhP: %.2f with meterResultData.totalKwhP: %.2f",
//                firstReadingScaled,
//                firstResult.getTotalKwhP() ));
//        Assertions.assertEquals(lastReadingScaled, lastResult.getTotalKwhP());

    }

    @ParameterizedTest(name = "{0},{1},{2}")
    @CsvSource({
            "ELON059938,2021-05-28 00:00:00,2021-05-28 23:59:59,Off-Peak,1000972",
            "ELON059938,2021-05-28 00:00:00,2021-05-28 23:59:59,Peak,1000975",
            "ELON059938,2021-05-28 00:00:00,2021-05-28 23:59:59,Standard,1000978"
    })
    @DisplayName("MeterDataService: Time of Use Comparison Profiles Test")
    void touComparisonProfilesTest(String serialN, String fromDateStr, String toDateStr, String touType, String touId1, String touId2) {

        Timestamp from = Timestamp.valueOf(fromDateStr);
        Date fromDate = Date.from( from.toInstant() );
        Timestamp to = Timestamp.valueOf(toDateStr);
        Date toDate = Date.from( to.toInstant() );
        int tmzOffset = 0;
        MeterDataService.Interval interval = MeterDataService.Interval.HALF_HOURLY;
        boolean removeTimeZone = false;

        logger.info( String.format("Request data for serialN: %s, FromDate: %s, ToDate %s",
                serialN,
                dateTimeFormatter.format(from.toLocalDateTime()),
                dateTimeFormatter.format(to.toLocalDateTime())
        ));

        Response response = meterDataService.getMeterData(serialN.split("\\|"), fromDate, toDate, tmzOffset, interval, touId1, touId2, removeTimeZone);

        logger.info("Done");

    }

    //@Test
    /*void TestGapFillingService() throws SQLException {

        Timestamp from = Timestamp.valueOf("2021-05-21 00:00:00");
        Date fromDate = Date.from( from.toInstant() );
        Timestamp to = Timestamp.valueOf("2021-05-21 06:00:00");
        Date toDate = Date.from( to.toInstant() );
        int tmzOffset = 0;
        MeterDataService.Interval interval = MeterDataService.Interval.HALF_HOURLY;
        String default_touc = "0";
        boolean removeTimeZone = false;

        String serialN = "ELON059938";
        String nesMeterId = "26022b1aa3a444e0a8b089161dc3fd75";



        MeterResultDataArray dataSet =
                meterDataService.getDetailData(serialN, fromDate, toDate,
                        tmzOffset, interval, "series1", default_touc1,default_touc2, removeTimeZone);

        for (MeterResultData d : dataSet.getCalculated()) {
            MeterReadingEntity.generate(mdmsDS.getConnection(), d, null, null, nesMeterId, null,
                    "TOTAL", false, 1);
        }

    }*/

    @Test
    void testgetDayOfWeekNumber() throws ParseException {

        Timestamp meterReadingEntryTime = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(2021, 01, 7), LocalTime.of(8, 45, 0)));

        Calendar c = Calendar.getInstance();
        c.setTime(meterReadingEntryTime);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        // logger.info(String.format("Reading date is " + meterReadingEntryTime));
        logger.info("The day of week number is :" + dayOfWeek);

    }

    @Test
    void myTest() {

        Timestamp from = Timestamp.valueOf("2021-09-07 00:00:00");
        Date fromDate = Date.from( from.toInstant() );
        Timestamp to = Timestamp.valueOf("2021-09-15 00:00:00");
        Date toDate = Date.from( to.toInstant() );

        String default_touc = "0";
        boolean removeTimeZone = false;

        String str_serial = "VM-SUMMATION-65004474-BL00030101-22680-KWH";
        //String str_serial = "ELON059938";
        String serialN[] = str_serial.split("\\|");
        MeterDataService.Interval interval = MeterDataService.Interval.MONTHLY;
        int tmzOffset = 120;

        MeterResultDataArray dataArray = new MeterResultDataArray();
        final AtomicInteger cnt = new AtomicInteger(0);
        Arrays.stream(serialN).forEach(s -> {
            MeterResultDataArray data = meterDataService.getDetailData(s, from, to, tmzOffset, interval, "series" + cnt.incrementAndGet(), default_touc, default_touc, removeTimeZone);
            if (data != null) {
                dataArray.addAll(data);
            }
        });
        dataArray.filter(interval).adjustNegative();
    }

    /**
     * IED-3483: MDMS & AMI Portal: TOU Comparison Profile
     * @throws WebServiceException
     * @throws SQLException
     * Test call onl
     */
    @Test
    void testTOUDbCall() throws WebServiceException, SQLException {
       meterDataService.testCallToDb();
    }


    @Test
     void testGetWeekDayName() {
        int dayOfWeekNumber = 5;
        logger.info("The day of week number being tested is :" + dayOfWeekNumber);
        String dayOfWeekNumberName = meterDataService.getWeekDayName(dayOfWeekNumber);
        logger.info("The day name for the day of week number : " + dayOfWeekNumber + " is : " + dayOfWeekNumberName);
    }

    @Test
    public void preview_virtual_meter() {
        try {
            // assert that the meter is the same

            MeterDataResultExpiringMap cacheMap = new MeterDataResultExpiringMap();

            Timestamp from = Timestamp.valueOf("2021-09-07 00:00:00");
            Date fromDate = Date.from( from.toInstant() );
            Timestamp to = Timestamp.valueOf("2021-09-15 00:00:00");
            Date toDate = Date.from( to.toInstant() );
            Timestamp.valueOf(to.toLocalDateTime().minusDays(1));
                VMMeterDataService vmMeterDataService = new VMMeterDataService();
                cacheMap.put("VM-SUMMATION-65004474-BL00030101-22680-KWH", from, to, vmMeterDataService.getDetailDataTmzMilli(meterDataService, "VM-SUMMATION-65004474-BL00030101-22680-KWH",
                        new Date(from.getTime() + 120), new Date(to.getTime() + 120), MeterDataService.Interval.HALF_HOURLY, 120, "series1","1000002", "1000003", false));
            logger.info("PLACEHOLDER");
            MeterResultDataArray temp = cacheMap.get("VM-SUMMATION-65004474-BL00030101-22680-KWH", from, to, 120);

            MeterResultDataArray data = temp == null || temp.isEmpty() ?
                    getZeroPad(new Timestamp(from.getTime()).toLocalDateTime(),
                            new Timestamp(to.getTime()).toLocalDateTime(),
                            MeterDataService.Interval.HALF_HOURLY, "VM-SUMMATION-65004474-BL00030101-22680-KWH", "series1", false) : temp;

            MeterResultDataArray data1 =  false ? data.adjustTime(120) : data;

            logger.info("PLACEHOLDER1");

            } catch(Exception ex){

            }


        }

    @Test
    public void preview_getSummaryTouData(PecMeterEntity meter, PecMeterRegisterEntity register, Timestamp lStart, Timestamp date, int tmz
            , boolean persistCalculated) {
        try {
            // assert that the meter is the same

            PecMeterReadingEntity reading = register.meterReading.getOne(mdmsDS);

            if (reading != null) {
                Timestamp to = getToDate(date,2); // Reading Date
                // IED-5241: MDMS - TOU Post-Paid Reading List Investigation
                Timestamp from = lStart != null ? lStart : // Cycle Start Date
                        Timestamp.valueOf(to.toLocalDateTime().minusDays(1));

                Response resp = meterDataService.getSummaryTouData(meter.meterN.get(), from, to, "1000002", "1000003" ,tmz
                        , MeterDataService.Interval.HALF_HOURLY, false);

                logger.info("PLACEHOLDER");

            }
        } catch (Exception ex) {

        }
    }

    public MeterResultDataArray getZeroPad(LocalDateTime from, LocalDateTime to, MeterDataService.Interval interval, String serialN, String series, Boolean onlyToCurrent) {
        MeterResultDataArray data = new MeterResultDataArray();
        if (onlyToCurrent && to.isAfter(LocalDateTime.now()) && !from.isAfter(LocalDateTime.now())) {
            to = LocalDateTime.now();
        }
        while (from.compareTo(to) < 0) {
            data.add(new MeterResultData(Timestamp.valueOf(from)).zero());
            data.get(data.size() - 1).setSeries(series);
            data.get(data.size() - 1).setSerialN(serialN);
            from = from.plusMinutes(interval.minutes);
        }
        return data;
    }
}
