package za.co.spsi.mdms.common.database;

import lombok.Data;
import org.jboss.logging.Logger;
import za.co.spsi.mdms.nes.db.NESBrokerCommandEntity;
import za.co.spsi.mdms.nes.db.NESDevicesMeterEntity;
import za.co.spsi.mdms.nes.util.NESConstants;
import za.co.spsi.pjtk.util.StringUtils;
import za.co.spsi.toolkit.db.DataSourceDB;

import java.io.FileInputStream;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Data
public class NesMssqlDB {

    private Connection connObj;
    private DateTimeFormatter dateTimeFormatter;

    Logger logger = Logger.getLogger(NesMssqlDB.class.getName());

    public NesMssqlDB() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    private Connection getNesDBConnection() {
        Properties props = new Properties();
        FileInputStream fis = null;

        Connection connObj = null;
        try {
            fis = new FileInputStream("src/test/resources/nesmssqldb.properties");
            props.load(fis);

            Class.forName(props.getProperty("MSSQL_DB_DRIVER"));
            connObj = DriverManager.getConnection(props.getProperty("MSSQL_DB_URL"),props.getProperty("MSSQL_DB_USERNAME"),props.getProperty("MSSQL_DB_PASSWORD"));
            if(connObj != null) {
                DatabaseMetaData metaObj = (DatabaseMetaData) connObj.getMetaData();
                logger.info("Driver Name?= " + metaObj.getDriverName() + ", Driver Version?= " + metaObj.getDriverVersion() + ", Product Name?= " + metaObj.getDatabaseProductName() + ", Product Version?= " + metaObj.getDatabaseProductVersion());
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }

        return connObj;
    }

    public ResultSet submitSqlQuery(String query) {

        ResultSet resultSet = null;

        try (Connection connection = getNesDBConnection();
             Statement statement = connection.createStatement();) {

            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public Boolean isCTMeter(String serialN) {

        Boolean isCTMeter = false;

        try( Connection connection = getNesDBConnection(); Statement stmt = connection.createStatement(); ) {

            String SQL = "select meterconf.Bits26thru26 from NES_Core.dbo.Devices device join NES_Core.dbo.Devices_MeterHWConfig meterconf on meterconf.deviceid = device.deviceid where device.serialnumber = '%s'";
            SQL = String.format(SQL, serialN);

            ResultSet rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                isCTMeter = rs.getBoolean("Bits26thru26");
                logger.info(String.format("Meter: %s, -> IsCT: %s", serialN, isCTMeter));
            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL error when trying to retrieve meter info from NES DB", e);
        }

        return isCTMeter;
    }

    public NESDevicesMeterEntity getNesDevicesMeterBySerialNumber(String serialN) {

        NESDevicesMeterEntity nesDevicesMeter = null;

        try (Connection connection = getNesDBConnection()) {
            nesDevicesMeter = DataSourceDB.getFromSet(connection, new NESDevicesMeterEntity().serialNumber.set(serialN));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return nesDevicesMeter;
    }

    public String getLoadVoltageStatus(String serialN) {

        String status = "No Status";
        String loadVoltageStatusTypeId = "";
        String SQL = "select * from NES_Core.dbo.Devices_Meter where SerialNumber = '%s'";
        SQL = String.format(SQL, serialN);

        try (Connection connection = getNesDBConnection(); Statement statement = connection.createStatement();) {

            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                loadVoltageStatusTypeId = resultSet.getString("LoadVoltageStatusTypeID");
            }

            if(!StringUtils.isEmpty(loadVoltageStatusTypeId)) {
                switch(loadVoltageStatusTypeId) {
                    case NESConstants.DeviceLoadVoltageStatusTypes.NOT_PRESENT:
                        status = "DISCONNECTED";
                        break;
                    case NESConstants.DeviceLoadVoltageStatusTypes.PRESENT:
                        status = "CONNECTED";
                        break;
                    case NESConstants.DeviceLoadVoltageStatusTypes.UNKNOWN:
                        status = "UNKNOWN";
                        break;
                }
            } else {
                status = "ERROR";
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }

}
