package za.co.spsi.mdms.util;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import oracle.jdbc.pool.OracleDataSource;
import org.postgresql.ds.PGSimpleDataSource;
import org.postgresql.osgi.PGDataSourceFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class DBUtil {

    public static String returnFirstRows(String driver, String sql, int limit) {

        if (driver.toLowerCase().contains("oracle")) {
            return !sql.toLowerCase().contains("rownum") ? String.format("SELECT * FROM (%s) WHERE rownum <= %d", sql, limit) : sql;
        } else if (driver.toLowerCase().contains("mysql")) {
            return !sql.toLowerCase().contains("limit") ? String.format("%s limit %d", sql, limit) : sql;
        } else if (driver.toLowerCase().contains("sqlserver")) {
            return !sql.toLowerCase().contains("top") ? String.format("select top %d %s", limit, sql.substring("select".length())) : sql;
        } else if (driver.toLowerCase().contains("postgresql")) {
            return !sql.toLowerCase().contains("limit") ? String.format("%s limit %d", sql, limit) : sql;
        }

        throw new RuntimeException("Driver not implemented");

    }

    public static DataSource createDataSource(String driver, String serverAddress,
                                              String serviceName, Integer portNumber,
                                              String dbName, String userName, String password) throws SQLException {

        if (driver.contains("oracle") == true) {
            OracleDataSource oracleDs = new OracleDataSource();
            oracleDs.setServerName(serverAddress);
            oracleDs.setServiceName(serviceName);
            oracleDs.setPortNumber(portNumber);
            oracleDs.setDriverType("thin");
            oracleDs.setUser(userName);
            oracleDs.setPassword(password);
            return oracleDs;

        } else if (driver.contains("mysql") == true) {
            MysqlDataSource mysqlDs = new MysqlDataSource();
            mysqlDs.setServerName(serverAddress);
            mysqlDs.setPort(portNumber);
            mysqlDs.setDatabaseName(dbName);
            mysqlDs.setUser(userName);
            mysqlDs.setPassword(password);
            return mysqlDs;

        } else if (driver.contains("sqlserver") == true) {
            SQLServerDataSource sqlServerDS = new SQLServerDataSource();
            sqlServerDS.setUser(userName);
            sqlServerDS.setPassword(password);
            sqlServerDS.setServerName(serverAddress);
            sqlServerDS.setPortNumber(portNumber);
            sqlServerDS.setDatabaseName(dbName);
            return sqlServerDS;

        } else if (driver.contains("postgresql") == true) {

            PGSimpleDataSource pgDS = new PGSimpleDataSource();
            pgDS.setUser(userName);
            pgDS.setPassword(password);
            pgDS.setServerName(serverAddress);
            pgDS.setPortNumber(portNumber);
            pgDS.setDatabaseName(dbName);
            return pgDS;
        }

        throw new RuntimeException("Driver not implemented : " + driver);
    }

    public static String trimSql(String sql) {
        sql = sql.trim();
        return sql.replaceAll("\n", "").replaceAll(" ", "");
    }
}
