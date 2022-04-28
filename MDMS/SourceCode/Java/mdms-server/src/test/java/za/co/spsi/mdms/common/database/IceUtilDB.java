package za.co.spsi.mdms.common.database;

import lombok.Data;
import oracle.jdbc.pool.OracleDataSource;
import org.jboss.logging.Logger;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Data
public class IceUtilDB {

    Logger logger = Logger.getLogger(IceUtilDB.class.getName());

    private DataSource ideUtilDS;
    private Driver driver;
    private DateTimeFormatter dateTimeFormatter;

    public IceUtilDB() throws SQLException {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ideUtilDS = getOracleDataSource();
        logger.info(String.format("\nICE Utilities Database Initialized: %s",
                ideUtilDS.getConnection().getMetaData().getDatabaseProductVersion() ));

        driver = DriverFactory.getHelper(ideUtilDS);
        logger.info(String.format("\nICE Utilities Driver Initialized: %s",
                driver.currentTimestamp() ));
    }

    private DataSource getOracleDataSource() {
        Properties props = new Properties();
        FileInputStream fis = null;
        OracleDataSource oracleDS = null;
        try {
            fis = new FileInputStream("src/test/resources/iceutildb.properties");
            props.load(fis);
            oracleDS = new OracleDataSource();
            oracleDS.setURL(props.getProperty("ICEUTIL_DB_URL"));
            oracleDS.setUser(props.getProperty("ICEUTIL_DB_USERNAME"));
            oracleDS.setPassword(props.getProperty("ICEUTIL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oracleDS;
    }

}
