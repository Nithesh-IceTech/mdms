package za.co.spsi.mdms.common.services;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.dao.MeterResultData;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.dao.PrepaidBatchData;
import za.co.spsi.mdms.common.dao.PrepaidBatchTOUData;
import za.co.spsi.mdms.common.database.IceUtilDB;
import za.co.spsi.mdms.common.database.MdmsOracleDB;
import za.co.spsi.mdms.common.db.utility.IcePrepaidTimeOfUseViewSyncService;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseViewSyncService;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.db.drivers.Driver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrepaidProcessingHelperTest extends AbstractPrepaidService {

    private Logger logger = Logger.getLogger(PrepaidProcessingHelperTest.class.getName());

    private MdmsOracleDB mdmsOracleDB;
    private DataSource mdmsDS;
    private Driver mdmsDriver;

    private IceUtilDB iceUtilDB;
    private DataSource iceUtilDS;
    private Driver iceUtilDriver;

    private PropertiesConfig propertiesConfig;

    private IcePrepaidTimeOfUseViewSyncService icePrepaidTimeOfUseViewSyncService;

    private PrepaidProcessingHelper prepaidProcessingHelper;

    private PrepaidMeterReadingPushService prepaidMeterReadingPushService;

    private AbstractPrepaidService abstractPrepaidService;

    private MeterDataService meterDataService;

    private IceTimeOfUseViewSyncService iceTimeOfUseViewSyncService;

    private MeterResultDataArray meterResultDataArray;

    private Boolean latestReadingOnlyFlag;

    private DateTimeFormatter dateTimeFormatter;

    private void printResultData(MeterResultDataArray resultDataArray) {
//        String.format("EntryTime: %s, TotalKwhP: %.2f, TotalKVA: %.2f, T1KwhP: %.2f, T1KVA: %.2f, T2KwhP: %.2f, T2KVA: %.2f, Volume1: %.2f",
//        String.format("EntryTime: %s, TotalKwhP: %.4f, TotalKwhPUsage: %.4f",
        for(MeterResultData resultData: resultDataArray) {
            System.out.printf("\nEntryTime: %s, TotalKwhP: %.5f, TotalKwhPUsage: %.5f, TotalKVA: %.5f, T1KwhP: %.5f, T1KVA: %.5f, T2KwhP: %.5f, T2KVA: %.5f, Volume1: %.5f%n",
                    dateTimeFormatter.format(resultData.getEntryTime().toLocalDateTime().plusHours(2)),
                    resultData.getTotalKwhP(),
                    resultData.getTotalKwhPUsage(),
                    resultData.getTotalKVA(),
                    resultData.getT1KwhP(),
                    resultData.getT1KVA(),
                    resultData.getT2KwhP(),
                    resultData.getT2KVA(),
                    resultData.getVolume1()
            );
        }

    }

    private void printPrepaidBatchData(PrepaidBatchData prepaidBatchData) {

        System.out.printf("EntryTime: %s, Reading: %.5f, Consumption: %.5f",
                dateTimeFormatter.format(prepaidBatchData.getEntryTime().toLocalDateTime().plusHours(2)),
                prepaidBatchData.getRegisterReading() == null ? 0.0 : prepaidBatchData.getRegisterReading(),
                prepaidBatchData.getRegisterConsumption() == null ? 0.0 : prepaidBatchData.getRegisterConsumption() );

    }

    @BeforeAll
    void initData() throws SQLException {

        mdmsOracleDB = new MdmsOracleDB();
        mdmsDS = mdmsOracleDB.getMdmsDS();
        mdmsDriver = mdmsOracleDB.getDriver();
        dateTimeFormatter = mdmsOracleDB.getDateTimeFormatter();

        iceUtilDB = new IceUtilDB();
        iceUtilDS = iceUtilDB.getIdeUtilDS();
        iceUtilDriver = iceUtilDB.getDriver();

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        propertiesConfig = new PropertiesConfig();
        propertiesConfig.setMdms_global_timezone_offset(120);
        propertiesConfig.setPrepaid_batch_processing_interval(2);

        icePrepaidTimeOfUseViewSyncService = new IcePrepaidTimeOfUseViewSyncService();
        icePrepaidTimeOfUseViewSyncService.iceDataSource = iceUtilDS;

        meterDataService = new MeterDataService();
        meterDataService.dataSource = mdmsDS;
        iceTimeOfUseViewSyncService = new IceTimeOfUseViewSyncService();
        meterDataService.touService = iceTimeOfUseViewSyncService;
        iceTimeOfUseViewSyncService.iceDataSource = iceUtilDS;

        prepaidProcessingHelper = new PrepaidProcessingHelper();
        prepaidProcessingHelper.mdmsDataSource = mdmsDS;
        prepaidProcessingHelper.meterDataService = meterDataService;
        prepaidProcessingHelper.propertiesConfig = propertiesConfig;
        prepaidProcessingHelper.icePrepaidTimeOfUseViewSyncService = icePrepaidTimeOfUseViewSyncService;

        prepaidMeterReadingPushService = new PrepaidMeterReadingPushService();
        prepaidMeterReadingPushService.dataSource = mdmsDS;
        prepaidMeterReadingPushService.propertiesConfig = propertiesConfig;
        abstractPrepaidService = new AbstractPrepaidService();
        abstractPrepaidService.dataSource = mdmsDS;

        List<MeterResultData> meterResultDataList = new ArrayList<>();
        meterResultDataList.addAll( Arrays.asList( new MeterResultData(), new MeterResultData(), new MeterResultData(), new MeterResultData() ) );

        // 30min interval readings
        Long interval = TimeUnit.MINUTES.toMinutes(30);

        Double startReadingTotalKwhP = 1000.0;
        Double usageTotalKwhP = 15.0;
        List<Double> totalkVAValues = Arrays.asList(1.5, 2.5, 3.5, 0.15);

        Double startReadingT1KwhP = startReadingTotalKwhP;
        Double usageT1KwhP = usageTotalKwhP;
        List<Double> t1kVAValues = totalkVAValues;

        Double startReadingT2KwhP = 1000.0;
        Double usageT2KwhP = 5.0;
        List<Double> t2kVAValues = Arrays.asList(0.15, 0.25, 0.35, 0.015);

        // Start date: 2020-10-22 06:00:00, End date: 2020-10-22 07:30:00
        LocalDateTime startEntryTime = LocalDateTime.of(2020,10,22, 6,0,0);

        logger.info("Initialize MeterResultDataArray Test Readings:");
        Integer loopIdx = 0;
        for(MeterResultData resultData: meterResultDataList) {

            resultData.setEntryTime(Timestamp.valueOf( startEntryTime ));

            resultData.setTotalKwhP(startReadingTotalKwhP);
            resultData.setTotalKVA( totalkVAValues.get(loopIdx) );

            resultData.setT1KwhP(startReadingT1KwhP);
            resultData.setT1KVA( t1kVAValues.get(loopIdx) );

            resultData.setT2KwhP(startReadingT2KwhP);
            resultData.setT2KVA( t2kVAValues.get(loopIdx) );

            logger.finest( String.format("EntryTime: %s -> TotalKwhP: %.3f, TotalKVA: %.3f, T1KwhP: %.3f, T1KVA: %.3f, T2KwhP: %.3f, T2KVA: %.3f",
                    dateTimeFormatter.format(startEntryTime),
                    startReadingTotalKwhP, totalkVAValues.get(loopIdx),
                    startReadingT1KwhP, t1kVAValues.get(loopIdx),
                    startReadingT2KwhP, t2kVAValues.get(loopIdx) )
            );

            startEntryTime        = startEntryTime.plusMinutes(interval);
            startReadingTotalKwhP = startReadingTotalKwhP + usageTotalKwhP;
            startReadingT1KwhP    = startReadingT1KwhP + usageT1KwhP;
            startReadingT2KwhP    = startReadingT2KwhP + usageT2KwhP;

            loopIdx++;
        }

        meterResultDataArray = new MeterResultDataArray(meterResultDataList);

    }

    @Test
    @DisplayName("PP Batch: Get Meter Reading By EntryTime Timestamp Test")
    void getPrepaidBatchMeterReadingByTimestampTest() {

        // 30min interval meter reading push property
        latestReadingOnlyFlag = false; // False -> Return meter reading by PP batch entryTime

        Timestamp expectedEntryTime = Timestamp.valueOf( LocalDateTime.of(2020,10,22, 6,0,0) );
        Double expectedReading = Double.parseDouble("1000.0");

        MeterResultData testResult = prepaidProcessingHelper.getMeterReading(meterResultDataArray, expectedEntryTime, latestReadingOnlyFlag);
        logger.info( String.format("Meter Result Data Result: EntryTime: %s, Reading: %.3f",
                dateTimeFormatter.format(testResult.getEntryTime().toLocalDateTime()),
                testResult.getTotalKwhP() )
        );

        // Assert that the testResult entry time matches the method input entryTime
        Assertions.assertEquals(expectedEntryTime, testResult.getEntryTime());

        // Assert that the testResult totalKwhP reading matches the totalKwhP reading at the expected/given entry time
        Assertions.assertEquals(expectedReading, testResult.getTotalKwhP());

    }

    @Test
    @DisplayName("PP Batch: Get Meter Reading By Latest Reading Only Test")
    void getPrepaidBatchMeterReading_By_LatestReadingOnly_Test() {

        // 2-hourly interval meter reading push property
        latestReadingOnlyFlag = true; // True -> Return latest meter reading only

        // Prepaid batch meter reading entry time
        // In this test, the entryTime as input parameter will be ignored.
        // It is expected that the last meterResultData object in the meterResultDataArray must be returned.
        Timestamp ppBatchEntryTime = Timestamp.valueOf( "2020-10-22 06:00:00" );

        // Method Under Test (MUT)
        MeterResultData testResult = prepaidProcessingHelper.getMeterReading(meterResultDataArray, ppBatchEntryTime, latestReadingOnlyFlag);
        logger.info( String.format("Meter Result Data Result: EntryTime: %s, Reading: %.3f",
                dateTimeFormatter.format(testResult.getEntryTime().toLocalDateTime()),
                testResult.getTotalKwhP() )
        );

        // Assert that the meter reading value matches the last meter reading in the meterResultDataArray
        Double expectedReading = Double.parseDouble("1045.0");
        Assertions.assertEquals(expectedReading, testResult.getTotalKwhP());

        // Assert that the meter reading entry time matches the last meter reading entry time in the meterResultDataArray
        Timestamp expectedEntryTime = Timestamp.valueOf( "2020-10-22 07:30:00" );
        Assertions.assertEquals( expectedEntryTime, testResult.getEntryTime());
    }

    @ParameterizedTest(name = "{0},{1},{2}")
    @CsvSource({
            "1.1.1.8.0.255, 2020-10-22 06:00:00, 1000.0", // TotalKwhP
            "1.1.1.8.1.255, 2020-10-22 06:30:00, 1015.0", // T1KwhP
            "1.1.1.8.2.255, 2020-10-22 07:00:00, 1010.0", // T2KwhP
            "1.1.9.6.0.255, 2020-10-22 06:00:00, 3.5",    // TotalKVA
            "1.1.9.6.1.255, 2020-10-22 06:00:00, 3.5",    // T1KVA
            "1.1.9.6.2.255, 2020-10-22 06:00:00, 0.35"    // T2KVA
    })
    @DisplayName("PP Batch: Process Prepaid Registers Test")
    void processPrepaidRegisters_Test(String meterRegisterId, String ppBatchEntryTimeStr, Double expectedReading) {

        // mock 30min interval meter reading push property
        latestReadingOnlyFlag = false; // False -> Triggers get meter reading by timestamp

        Timestamp expectedEntryTime = Timestamp.valueOf(ppBatchEntryTimeStr);

        // Method Under Test (MUT)
        PrepaidBatchData prepaidBatchData = prepaidProcessingHelper.processPrepaidRegisters(meterResultDataArray, expectedEntryTime, meterRegisterId, latestReadingOnlyFlag);
        logger.info( String.format("Prepaid Batch Data Result: RegisterID: %s, EntryTime: %s, Reading: %.3f",
                meterRegisterId,
                dateTimeFormatter.format(prepaidBatchData.getEntryTime().toLocalDateTime()),
                prepaidBatchData.getRegisterReading() )
        );

        // assert that the PP batch meter readings view totalKwhP register value is set to the expected reading
        Assertions.assertEquals( expectedReading, prepaidBatchData.getRegisterReading() );

        // assert that the PP batch meter readings view entryTime is set to the expected entryTime provided as input to MUT
        Assertions.assertEquals( expectedEntryTime, prepaidBatchData.getEntryTime() );
    }


    @ParameterizedTest(name = "{0}")
    @CsvSource({
            //1.1.1.8.0.255
            "20529319,1.1.1.8.2.255,c7f710d7-a858-4001-afb6-6fa8ad7bce3e",
    })
    @DisplayName("PP Batch: processPrepaidTOURegisters Test")
    void processPrepaidTOURegisters_Test(String serialN, String mdmsMeterRegisterId, String prepaidBatchId) {

        // STEP1: Get PP Batch Meter Reading Data Array

        logger.info(String.format("Get meter readings for ICE Meter Number: %s, Prepaid Batch ID: %s",
                serialN,
                prepaidBatchId));

        MeterResultDataArray resultDataArray = prepaidProcessingHelper.getPrepaidBatchMeterReadings(prepaidBatchId,serialN);

        logger.info(String.format("Number of meter readings retrieved: %d", resultDataArray.size()));
        Assertions.assertNotEquals(0, resultDataArray.size());

        printResultData(resultDataArray);

        Map<String,PrepaidBatchTOUData> ppBatchTouRegisterCacheMap = new HashMap<>();
        latestReadingOnlyFlag = true;

        // Prepaid TOU Registers
        String iceMeterNumber = serialN;
        String peakRegId     = "1118739"; // Peak
        String standardRegId = "1118740"; // Standard
        String offPeakRegId  = "1118741"; // Off-Peak

        for(MeterResultData meterResultData: resultDataArray) {

            System.out.println("\n\n");

            Timestamp ppBatchEntryTime = meterResultData.getEntryTime();

            // STEP2: Process Prepaid Conventional Registers
            PrepaidBatchData prepaidBatchData = prepaidProcessingHelper.processPrepaidRegisters(resultDataArray, ppBatchEntryTime, mdmsMeterRegisterId, latestReadingOnlyFlag);
            Assertions.assertNotNull(prepaidBatchData.getEntryTime());
            Assertions.assertNotNull(prepaidBatchData.getRegisterReading());
//            Assertions.assertNotNull(prepaidBatchData.getRegisterConsumption());

            printPrepaidBatchData(prepaidBatchData);

            // STEP3: Process Prepaid TOU Registers
            // peakRegId,standardRegId,offPeakRegId
            List<String> iceMeterRegisterIdList = Arrays.asList(peakRegId,standardRegId,offPeakRegId);

            for(String iceMeterRegisterId: iceMeterRegisterIdList) {

                String cacheMapKey = iceMeterNumber + ":" + iceMeterRegisterId + ":" + mdmsMeterRegisterId;

                Assertions.assertNotNull(resultDataArray);

                PrepaidBatchTOUData afterTouData =
                        prepaidProcessingHelper.processPrepaidTOURegisters(resultDataArray,
                                ppBatchTouRegisterCacheMap,prepaidBatchData,
                                iceMeterNumber,iceMeterRegisterId,mdmsMeterRegisterId,
                                this.latestReadingOnlyFlag);


                String cacheMapValue = ppBatchTouRegisterCacheMap.get(cacheMapKey) != null ? ppBatchTouRegisterCacheMap.get(cacheMapKey).toString() : "NoValue";

                System.out.println(String.format("\nCacheMapKey: %s -> Result: %s\n", cacheMapKey, cacheMapValue ));

            }

            if(latestReadingOnlyFlag) break;
        }

    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            //1.1.1.8.0.255
            "20529319,1.1.1.8.2.255,c7f710d7-a858-4001-afb6-6fa8ad7bce3e",
    })
    @DisplayName("PP Meter Reading Push Service: updateMeterReadingBatchIds Test")
    void prepaidMeterReadingPushService_Test() {

        List<String> meterIdList = new ArrayList<>();
        meterIdList.add("87ccdc51-4fda-43f7-92c3-1d7a89299dbc"); // serialN = '20529319'

        prepaidMeterReadingPushService.updateMeterReadingBatchIds("kam_meter_id", meterIdList);

    }

}
