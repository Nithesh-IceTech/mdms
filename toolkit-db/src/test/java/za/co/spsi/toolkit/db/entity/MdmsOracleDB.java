package za.co.spsi.toolkit.db.entity;

import lombok.Data;
import oracle.jdbc.pool.OracleDataSource;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Data
public class MdmsOracleDB {

    private DataSource mdmsDS;
    private Driver driver;
    private DateTimeFormatter dateTimeFormatter;

    public MdmsOracleDB() throws SQLException {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        mdmsDS = getOracleDataSource();
        System.out.println(String.format("\nMDMS Oracle Database Initialized: %s",
                mdmsDS.getConnection().getMetaData().getDatabaseProductVersion() ));

        driver = DriverFactory.getHelper(mdmsDS);
        System.out.println(String.format("\nMDMS Oracle Driver Initialized: %s",
                driver.currentTimestamp() ));
    }

    private DataSource getOracleDataSource() {
        Properties props = new Properties();
        FileInputStream fis = null;
        OracleDataSource oracleDS = null;
        try {
            fis = new FileInputStream("src/test/resources/mdmsdevoracledb.properties");
            props.load(fis);
            oracleDS = new OracleDataSource();
            oracleDS.setURL(props.getProperty("ORACLE_DB_URL"));
            oracleDS.setUser(props.getProperty("ORACLE_DB_USERNAME"));
            oracleDS.setPassword(props.getProperty("ORACLE_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oracleDS;
    }

}
