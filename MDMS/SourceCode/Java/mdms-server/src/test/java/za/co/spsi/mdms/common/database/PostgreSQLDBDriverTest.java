package za.co.spsi.mdms.common.database;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostgreSQLDBDriverTest {

    Logger logger = Logger.getLogger(PostgreSQLDBDriverTest.class.getName());

    private MdmsPostgresDB mdmsPostgresDB;
    private DataSource mdmsDS;
    private Driver driver;

    @BeforeAll
    void initDataSource() throws SQLException {

        mdmsPostgresDB = new MdmsPostgresDB();
        mdmsDS = mdmsPostgresDB.getMdmsDS();
        driver = DriverFactory.getHelper(mdmsDS);

    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2020-10-24 00:00:00",
            "2020-10-24 02:00:00",
            "2020-10-24 04:00:00",
            "2020-10-24 06:00:00",
            "2020-10-24 08:00:00"
    })
    @DisplayName("PostgreSQL Driver: toDate() Test")
    void driverToDateTest(String timestampStr) {

        Timestamp to = Timestamp.valueOf(timestampStr);
        Timestamp from = Timestamp.valueOf( to.toLocalDateTime().minusHours(2) );

        String sqlQuery = String.format("select * from meter_reading where entry_time >= %s and entry_time <= %s",
                driver.toDate(from.toLocalDateTime()),
                driver.toDate(to.toLocalDateTime()));

        logger.info(String.format("\nSQL Query: %s", sqlQuery));

    }

}
