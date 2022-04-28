package za.co.spsi.mdms.common.services;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.database.MdmsOracleDB;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.Driver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static za.co.spsi.mdms.common.services.PrepaidMeterReadingPushProcessingService.TAG;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrepaidMeterReadingPushServiceTest {

    Logger logger = Logger.getLogger(PrepaidMeterReadingPushServiceTest.class.getName());

    private DataSource mdmsDS;
    private Driver driver;
    private DateTimeFormatter dateTimeFormatter;

    @BeforeAll
    void initDataSource() throws SQLException {

        MdmsOracleDB mdmsOracleDB = new MdmsOracleDB();
        mdmsDS = mdmsOracleDB.getMdmsDS();
        driver = mdmsOracleDB.getDriver();
        dateTimeFormatter = mdmsOracleDB.getDateTimeFormatter();

    }

    private void printMeterReadings(List<MeterReadingEntity> meterReadings) {
        for(MeterReadingEntity meterReading: meterReadings) {
            logger.info(String.format("EntryTime: %s, CreatedTime: %s, PrepaidMeter: %s, Generated: %s, TotalKwhP: %.3f, T1KwhP: %.3f, T2KwhP:%.3f",
                    dateTimeFormatter.format(meterReading.entryTime.get().toLocalDateTime()),
                    dateTimeFormatter.format(meterReading.createTime.get().toLocalDateTime()),
                    meterReading.prepaidMeter.get(),
                    meterReading.generated.get(),
                    meterReading.totalKwhP.get(),
                    meterReading.t1KwhP.get(),
                    meterReading.t1KwhP.get()
            ));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"NES_METER_ID","KAM_METER_ID","ELS_METER_ID","GENERIC_METER_ID"})
    @DisplayName("MDMS Datasource: Query MeterReadingEntity Test")
    void queryMeterReadingEntity(String meterCol) {

        logger.info(String.format("Querying METER_READING table, meter column: %s", meterCol));

        String query = String.format("select * from meter_reading where prepaid_meter = %s " +
                " and %s is not null and generated is null and prepaid_meter_reading_batch_id is null ", driver.boolToNumber(true), meterCol);
        logger.info(String.format("SQL query: \n%s", query));

        List<MeterReadingEntity> meterReadings = DataSourceDB.getAllAsList(MeterReadingEntity.class, mdmsDS, query);

        logger.info(String.format("Number of meter readings retrieved for %s: %d", meterCol, meterReadings.size()));

        printMeterReadings(meterReadings);

    }

    @Test
    void maxEntryTimeTest() {

        String meterCol = "nes_meter_id";
        String meterId = "26022b1aa3a444e0a8b089161dc3fd75";

//        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
//        String maxEntryTimeQuery = String.format("select max(entry_time) from meter_reading where %s = '%s' and " +
//                " prepaid_meter_reading_batch_id is not null and entry_time >= %s", meterCol, meterId,
//                driver.toDate( last24Hours ) );
//
//        Timestamp maxEntryTime = DataSourceDB.executeQuery(mdmsDS,Timestamp.class,maxEntryTimeQuery);
//        if(maxEntryTime == null) {
//            Integer timezoneHours = 120 / 60;
//            Integer ppProcessingInterval = 2;
//            Integer headEndSystemDelay = false ? 12 : 6;
//            maxEntryTime = Timestamp.valueOf( LocalDateTime.now().minusHours( timezoneHours + headEndSystemDelay + ppProcessingInterval ) );
//        }

        int backlogTimeWindowDays = 30;

        Timestamp maxEntryTime = Timestamp.valueOf( LocalDateTime.now().minusHours( (long) 24 * backlogTimeWindowDays ) );

        logger.info(String.format("Max Entry Time %s", driver.toDate(maxEntryTime.toLocalDateTime())));

        String selectQuery = String.format("select * from meter_reading where %s = '%s' and " +
                        " entry_time > %s and prepaid_meter = %s and generated is null and prepaid_meter_reading_batch_id is null ",
                meterCol,
                meterId,
                driver.toDate( maxEntryTime.toLocalDateTime()),
                driver.boolToNumber(true) );

        String orderByQuery = driver.orderBy(selectQuery,"entry_time", false);

        logger.info(String.format("Get Meter Readings SQL Query:\n%s\n", orderByQuery));

        List<MeterReadingEntity> meterReadings = DataSourceDB.getAllAsList(MeterReadingEntity.class,mdmsDS,orderByQuery);

        if(meterReadings == null) {
            meterReadings = new ArrayList<>();
            TAG.info(String.format("Unable to get new meter readings for prepaid batch id assignment. Meter Column: %s, MeterId: %s, MaxEntryTime: %s",
                    meterCol,
                    meterId,
                    driver.toDate(maxEntryTime.toLocalDateTime())));
        } else if (meterReadings.size() < 1) {
            TAG.info(String.format("No new meter readings available for prepaid batch id assignment. Meter Column: %s, MeterId: %s, MaxEntryTime: %s",
                    meterCol,
                    meterId,
                    driver.toDate(maxEntryTime.toLocalDateTime())));
        }

        printMeterReadings(meterReadings);

    }

}
