package za.co.spsi.toolkit.db.drivers;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class DriverFactory {

    private static Map<String,Driver> helperMap = new HashMap<>();
    private static DataSource DATA_SOURCE;
    private static String SCHEMA;
    private static Driver DRIVER;

    public static void setDataSource(DataSource ds) {
        DriverFactory.DATA_SOURCE = ds;
        setDriver();
    }

    public static void setDriver() {
        DriverFactory.DRIVER = DriverFactory.getHelper(DriverFactory.DATA_SOURCE);
    }

    public static DataSource getDataSource() {
        return DriverFactory.DATA_SOURCE;
    }

    public static Driver getDriver() {
        DriverFactory.DRIVER = DriverFactory.DRIVER != null ?
                DriverFactory.DRIVER :
                DriverFactory.getHelper(DriverFactory.DATA_SOURCE);
        return DriverFactory.DRIVER;
    }

    public static Driver getHelper(Connection connection) {
        try {
            if (!helperMap.containsKey(connection.getSchema())) {
                DriverFactory.SCHEMA = connection.getSchema();
                helperMap.put(DriverFactory.SCHEMA, getHelper(connection,connection.getMetaData().getDatabaseProductName()));
            }
            DriverFactory.DRIVER = helperMap.get(DriverFactory.SCHEMA);
            return DriverFactory.DRIVER;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Driver getHelper(DataSource dataSource) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return getHelper(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Driver getHelper(Connection connection,String productName) throws SQLException {
        switch (productName) {
            case "Oracle":return new OracleDriver().init(connection);
            case "PostgreSQL":return new PostgresDriver().init(connection);
            case "MySQL":return new MysqlDriver().init(connection);
            case "H2":return new H2Driver().init(connection);
            default:throw new RuntimeException(String.format("DB not supported %s",productName));
        }
    }

}
