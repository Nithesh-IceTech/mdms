package za.co.spsi.mdms.common.database;

import lombok.Data;
import oracle.jdbc.pool.OracleDataSource;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.postgresql.ds.PGSimpleDataSource;
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
public class MdmsPostgresDB {

    Logger logger = Logger.getLogger(MdmsPostgresDB.class.getName());

    private DataSource mdmsDS;
    private Driver driver;
    private DateTimeFormatter dateTimeFormatter;

    public MdmsPostgresDB() throws SQLException {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        mdmsDS = getPostgresDataSource();
        logger.info(String.format("\nMDMS PostgreSQL Database Initialized: %s",
                mdmsDS.getConnection().getMetaData().getDatabaseProductVersion() ));

        driver = DriverFactory.getHelper(mdmsDS);
        logger.info(String.format("\nMDMS PostgreSQL Driver Initialized: %s",
                driver.currentTimestamp() ));
    }

    private DataSource getPostgresDataSource() {
        Properties props = new Properties();
        FileInputStream fis = null;
        PGSimpleDataSource pgDS = null;
        try {
            fis = new FileInputStream("src/test/resources/mdmspostgresqldb.properties");
            props.load(fis);
            pgDS = new PGSimpleDataSource();
            pgDS.setServerName(props.getProperty("POSTGRESQL_DB_HOSTNAME"));
            pgDS.setPortNumber( Integer.parseInt(props.getProperty("POSTGRESQL_DB_PORT")) );
            pgDS.setDatabaseName(props.getProperty("POSTGRESQL_DB_DATABASE"));
            pgDS.setCurrentSchema(props.getProperty("POSTGRESQL_DB_SCHEMA"));
            pgDS.setUser(props.getProperty("POSTGRESQL_DB_USERNAME"));
            pgDS.setPassword(props.getProperty("POSTGRESQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pgDS;
    }

}
