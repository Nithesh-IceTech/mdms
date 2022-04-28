package za.co.spsi.mdms.common.database;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.toolkit.db.drivers.Driver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OracleDBDriverTest {

    Logger logger = Logger.getLogger(OracleDBDriverTest.class.getName());

    private MdmsOracleDB mdmsOracleDB;
    private DataSource mdmsDS;
    private Driver driver;

    @BeforeAll
    void initDataSource() throws SQLException {

        mdmsOracleDB = new MdmsOracleDB();
        mdmsDS = mdmsOracleDB.getMdmsDS();
        driver = mdmsOracleDB.getDriver();

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2020-10-24 00:00:00",
            "2020-10-24 02:00:00",
            "2020-10-24 04:00:00",
            "2020-10-24 06:00:00",
            "2020-10-24 08:00:00"
    })
    @DisplayName("Oracle Driver: toDate() Test")
    void driverToDateTest(String timestampStr) {

        Timestamp to = Timestamp.valueOf(timestampStr);
        Timestamp from = Timestamp.valueOf( to.toLocalDateTime().minusHours(2) );

        String sqlQuery = String.format("select * from meter_reading where entry_time >= %s and entry_time <= %s",
                driver.toDate(from.toLocalDateTime()),
                driver.toDate(to.toLocalDateTime()));

        logger.info(String.format("\nSQL Query: %s", sqlQuery));

    }

}
