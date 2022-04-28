package za.co.spsi.mdms.common.services;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.dao.MeterResultData;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.database.IceUtilDB;
import za.co.spsi.mdms.common.database.MdmsOracleDB;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseViewSyncService;
import za.co.spsi.toolkit.db.drivers.Driver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SummaryDataServiceTest {

    Logger logger = Logger.getLogger(MeterDataServiceTest.class.getName());

    private DateTimeFormatter dateTimeFormatter;

    private MdmsOracleDB mdmsOracleDB;
    private DataSource mdmsDS;
    private Driver mdmsDriver;

    private IceUtilDB iceUtilDB;
    private DataSource iceUtilDS;
    private Driver iceUtilDriver;

    private MeterDataService meterDataService;
    private IceTimeOfUseViewSyncService iceTimeOfUseViewSyncService;

    @BeforeAll
    void initDataSource() throws SQLException {

        mdmsOracleDB = new MdmsOracleDB();
        mdmsDS = mdmsOracleDB.getMdmsDS();
        mdmsDriver = mdmsOracleDB.getDriver();
        dateTimeFormatter = mdmsOracleDB.getDateTimeFormatter();

        iceUtilDB = new IceUtilDB();
        iceUtilDS = iceUtilDB.getIdeUtilDS();
        iceUtilDriver = iceUtilDB.getDriver();

        meterDataService = new MeterDataService();
        meterDataService.dataSource = mdmsDS;
        iceTimeOfUseViewSyncService = new IceTimeOfUseViewSyncService();
        meterDataService.touService = iceTimeOfUseViewSyncService;
        iceTimeOfUseViewSyncService.iceDataSource = iceUtilDS;

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


    @ParameterizedTest(name = "{0},{1},{2}")
    @CsvSource({
            "ELON076566,today",
//            "ELON059938,last7days",
//            "ELON059938,monthtodate",
//            "ELON059938,last30days",
//            "ELON059938,last365days",
    })
    @DisplayName("SummaryDataService: getDetailData() Test")
    void getSummaryDataServiceTest(String serialN, String timeWindow) {

        LocalDateTime toDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        Timestamp to = Timestamp.valueOf( LocalDateTime.now() );

        Timestamp from = to;
        if(timeWindow.equalsIgnoreCase("today")) {
            LocalDateTime fromAdjusted = toDateTime.truncatedTo(ChronoUnit.DAYS);
            from = Timestamp.valueOf( fromAdjusted );
        } else if(timeWindow.equalsIgnoreCase("last7days")) {
            LocalDateTime fromAdjusted = toDateTime.truncatedTo(ChronoUnit.DAYS).minusDays(7);
            from = Timestamp.valueOf( fromAdjusted );
        } else if(timeWindow.equalsIgnoreCase("monthtodate")) {
            LocalDateTime fromAdjusted = toDateTime.truncatedTo(ChronoUnit.DAYS).withDayOfMonth(1);
            from = Timestamp.valueOf( fromAdjusted );
        } else if(timeWindow.equalsIgnoreCase("last30days")) {
            LocalDateTime fromAdjusted = toDateTime.truncatedTo(ChronoUnit.DAYS).minusDays(30);
            from = Timestamp.valueOf( fromAdjusted );
        } else if(timeWindow.equalsIgnoreCase("last365days")) {
            LocalDateTime fromAdjusted = toDateTime.truncatedTo(ChronoUnit.DAYS).minusDays(365);
            from = Timestamp.valueOf( fromAdjusted );
        }

        Date toDate = Date.from( toDateTime.toInstant(ZoneOffset.UTC) );
        Date fromDate = Date.from( from.toInstant() );

        int tmzOffset = 0;
        MeterDataService.Interval interval = MeterDataService.Interval.HALF_HOURLY;
        String default_touc1 = "0";
        String default_touc2 = "0";
        boolean removeTimeZone = false;

        logger.info("STEP1: Get meter readings from MeterDataService.getDetailData(): ");

        logger.info( String.format("Request data for serialN: %s, FromDate: %s, ToDate %s",
                serialN,
                dateTimeFormatter.format(from.toLocalDateTime()),
                dateTimeFormatter.format(to.toLocalDateTime())
        ));

        MeterResultDataArray resultDataArray =
                meterDataService.getDetailData(serialN, fromDate, toDate,
                        tmzOffset, interval, "series1", default_touc1, default_touc2, removeTimeZone);

        logger.info(String.format("Number of entries retrieved: %d", resultDataArray.size()));

        printResultData(resultDataArray);

        logger.info("STEP2: Construct a Summary Data Response");

        Double sumOfDailyUsages = resultDataArray.getMeterRegisterSumOfDailyUsages("total");
        Double averageDailyUsage = resultDataArray.getMeterRegisterAverageDailyUsage("total");

        MeterResultData maxkVAResult = resultDataArray.getMaxTotalKVA_MeterResultData();
        Double maxkVAValue = maxkVAResult.getTotalKVA();
        Timestamp maxkVAEntryTime = maxkVAResult.getEntryTime();

        logger.info( String.format("Summary Data Result for Time_Window: %s\nTotal_kWh_Sum: %.3f, Total_kWh_Avg: %.3f, Total_kVA_Max: %.3f, Total_kVA_Max_DT: %s",
                timeWindow.toUpperCase(),
                sumOfDailyUsages,
                averageDailyUsage,
                maxkVAValue,
                dateTimeFormatter.format(maxkVAEntryTime.toLocalDateTime())) );
    }

}
