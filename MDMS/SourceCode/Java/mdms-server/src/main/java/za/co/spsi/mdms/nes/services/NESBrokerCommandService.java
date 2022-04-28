package za.co.spsi.mdms.nes.services;

import com.echelon.wsdl.panoramix.*;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import za.co.spsi.mdms.nes.db.NESBrokerCommandEntity;
import za.co.spsi.mdms.nes.db.NESDevicesMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.nes.util.NESConstants;
import za.co.spsi.pjtk.util.Assert;
import za.co.spsi.pjtk.util.StringUtils;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.mdms.common.MdmsConstants.Status;
/**
 * Created by johan on 2016/12/15.
 */

@TransactionManagement(value = TransactionManagementType.BEAN)
@Dependent
public class NESBrokerCommandService {

    public static final Logger TAG = Logger.getLogger(NESBrokerCommandService.class.getName());

    @Resource(mappedName = "java:/jdbc/nesdb")
    private javax.sql.DataSource nesDataSource;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    WSDLConfig wsdlConfig;

    public NESBrokerCommandService() {

    }

    private List<NESBrokerCommandEntity> getScheduled() {
        return NESBrokerCommandEntity.getByCommandStatus(dataSource, Status.PROCESSING);

    }

    private List<NESBrokerCommandEntity> getSubmitted() {
        return NESBrokerCommandEntity.getByCommandStatus(dataSource, Status.SUBMITED);

    }

    private List<NESBrokerCommandEntity> getWaiting() {
        return NESBrokerCommandEntity.getByCommandStatus(dataSource, Status.WAITING);

    }


    public void closeNESDBResources(ResultSet rs, Statement ps, Connection conn) {
        boolean allResourcesClosed = true;
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                TAG.log(Level.WARNING, "Unable to close NESDB ResultSet", e);
                allResourcesClosed = false;
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                TAG.log(Level.WARNING, "Unable to close NESDB Statement", e);
                allResourcesClosed = false;
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                TAG.log(Level.WARNING, "Unable to close NESDB Database Connection", e);
                allResourcesClosed = false;
            }
        }

        if (!allResourcesClosed)
            throw new RuntimeException("Unable to close all resources for query to NESDB");

    }

    public void setDeviceGatewayGUID(NESBrokerCommandEntity entity) {

        try( Connection connection = nesDataSource.getConnection() ) {

            try( Statement stmt = connection.createStatement() ) {

                String SQL = "select device.DeviceID, device.GatewayID, meterconf.Bits26thru26 from NES_Core.dbo.Devices device join NES_Core.dbo.Devices_MeterHWConfig meterconf on meterconf.deviceid = device.deviceid where device.serialnumber = '%s'";
                SQL = String.format(SQL, entity.meterSerialNumber.get());

                ResultSet rs = stmt.executeQuery(SQL);

                while (rs.next()) {
                    entity.meterSerialNumberLong.set(rs.getString("DeviceID"));
                    entity.gatewaySerialNumberLong.set(rs.getString("GatewayID"));
                    entity.isCTMeter.set(rs.getBoolean("Bits26thru26"));
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL error when trying to retrieve meter info from NES DB", e);
        }

    }

    public String getCommandStatusFromNES(NESBrokerCommandEntity entity) {
        String status = "";

        try( Connection connection = nesDataSource.getConnection() ) {

            try( Statement stmt = connection.createStatement() ) {

                String SQL = "select * from NES_Core.dbo.CommandHistory where CommandHistoryID = '%s'";
                SQL = String.format(SQL, entity.deviceCommandTrackingID.get());

                ResultSet rs = stmt.executeQuery(SQL);

                while (rs.next()) {
                    status = rs.getString("StatusTypeID");
                }

            }

        } catch (SQLException e) {
            throw new RuntimeException("SQL error when trying to retrieve meter info from NES DB", e);
        }

        return status;

    }

    public String convertCommandCodeToNES(int commandCode, boolean isCTMeter) {
        if (isCTMeter) {
            if (commandCode == NESBrokerCommandEntity.Command.CONNECT.getCode()) {
                return NESConstants.DeviceCommands.CONNECT_CONTROL_RELAY;
            } else if (commandCode == NESBrokerCommandEntity.Command.DISCONNECT.getCode()) {
                return NESConstants.DeviceCommands.DISCONNECT_CONTROL_RELAY;
            }
        } else {
            if (commandCode == NESBrokerCommandEntity.Command.CONNECT.getCode()) {
                return NESConstants.DeviceCommands.CONNECT_LOAD;
            } else if (commandCode == NESBrokerCommandEntity.Command.DISCONNECT.getCode()) {
                return NESConstants.DeviceCommands.DISCONNECT_LOAD;
            }
        }
        return "";
    }

    public String convertCommandCodeToStringValue(int commandCode, boolean isCTMeter) {
        if (isCTMeter) {
            if (commandCode == NESBrokerCommandEntity.Command.CONNECT.getCode()) {
                return "CONNECT";
            } else if (commandCode == NESBrokerCommandEntity.Command.DISCONNECT.getCode()) {
                return "DISCONNECT";
            }
        } else {
            if (commandCode == NESBrokerCommandEntity.Command.CONNECT.getCode()) {
                return "CONNECT";
            } else if (commandCode == NESBrokerCommandEntity.Command.DISCONNECT.getCode()) {
                return "DISCONNECT";
            }
        }
        return "";
    }

    public NESResults getResultObjectFromXML(String resultXML) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = df.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(resultXML));
        InputStream stream = new ByteArrayInputStream(resultXML.getBytes(StandardCharsets.UTF_8));
        Document doc = builder.parse(stream);
        String trackingID = doc.getDocumentElement().getElementsByTagName("TRACKINGID").item(0).getFirstChild().getNodeValue();
        String commandStatus = doc.getDocumentElement().getElementsByTagName("STATUS").item(0).getFirstChild().getNodeValue();

        NESResults results = new NESResults();
        results.setCommandStatus(commandStatus);
        results.setTrackingID(trackingID);
        return results;
    }

    public void submitGatewayConnect(NESBrokerCommandEntity entity) {
        StringBuffer sb = new StringBuffer();

        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sd.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = sd.format(cal.getTime());

        GatewayManager gm = new GatewayManager( wsdlConfig );
        GatewayManagerSoap gms = gm.getGatewayManagerSoap();
        sb = new StringBuffer();
        sb.append("<PARAMETERS>");
        sb.append("<COMMUNICATIONREQUESTTYPEID>");
        sb.append(NESConstants.GatewayCommunicationRequestTypes.SERVER_INITIATED_NORMAL_PRIORITY); //Server initaited normal priority
        sb.append("</COMMUNICATIONREQUESTTYPEID>");
        sb.append("<TIMEOUTDATETIME>");
        sb.append(formattedDate);
        sb.append("</TIMEOUTDATETIME>");
        sb.append("</PARAMETERS>");

        String output = gms.connect("", entity.gatewaySerialNumberLong.get(), sb.toString());
    }

    public void submitCommand(NESBrokerCommandEntity entity) throws IOException, SAXException, ParserConfigurationException {
        DeviceManager dm = new DeviceManager( wsdlConfig );
        DeviceManagerSoap dms = dm.getDeviceManagerSoap();
        StringBuffer sb = new StringBuffer();
        sb.append("<PARAMETERS>");

        sb.append("<DEVICEID>");
        sb.append(entity.meterSerialNumberLong.get());
        sb.append("</DEVICEID>");

        sb.append("<COMMANDID>");
        sb.append(convertCommandCodeToNES(entity.command.get(), entity.isCTMeter.get()));
        sb.append("</COMMANDID>");

        sb.append("<TIMEOUTDATETIME>");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        sd.setTimeZone(TimeZone.getTimeZone("GMT"));
        String formattedDate = sd.format(cal.getTime());
        sb.append(formattedDate);
        sb.append("</TIMEOUTDATETIME>");

        sb.append("<PRIORITY>");
        sb.append(NESConstants.TaskPriorities.HIGH);
        sb.append("</PRIORITY>");

        if( convertCommandCodeToNES(entity.command.get(), entity.isCTMeter.get()).equals( NESConstants.DeviceCommands.CONNECT_CONTROL_RELAY) ||
                convertCommandCodeToNES(entity.command.get(), entity.isCTMeter.get()).equals( NESConstants.DeviceCommands.CONNECT_LOAD) ) {
            sb.append("<FORCECONNECT>");
            sb.append(NESConstants.StandardAPIOptions.YES);
            sb.append("</FORCECONNECT>");
        } else {
            sb.append("<DISCONNECTPRIORITYLEVEL>");
            sb.append(NESConstants.DeviceCommands.DEVICE_DISCONNECT_PRIORITY_LEVEL_HIGH);
            sb.append("</DISCONNECTPRIORITYLEVEL>");
        }

        sb.append("</PARAMETERS>");

        String output = dms.performCommand("", sb.toString());
        NESResults commandResult = getResultObjectFromXML(output);
        entity.deviceCommandTrackingID.set(commandResult.getTrackingID());
        entity.deviceCommandResultStatus.set(commandResult.getCommandStatus());
        submitGatewayConnect(entity);
    }

    public void saveBrokerCommandStatus(NESBrokerCommandEntity brokerCommandEntity) {
        try (Connection connection = dataSource.getConnection()) {
            DataSourceDB.set(connection, brokerCommandEntity);
        } catch(SQLException ex) {
            TAG.severe(String.format("%s", ex.getMessage()));
        }
    }

    public void saveNesMeter(NESMeterEntity nesMeterEntity) {
        try (Connection connection = dataSource.getConnection()) {
            DataSourceDB.set(connection, nesMeterEntity);
        } catch(SQLException ex) {
            TAG.severe(String.format("%s", ex.getMessage()));
        }
    }

    public NESMeterEntity getNesMeterByRef(NESBrokerCommandEntity brokerCommandEntity) {
        NESMeterEntity nesMeterEntity = null;
        try (Connection connection = dataSource.getConnection()) {
            nesMeterEntity = brokerCommandEntity.meterRef.getOne(connection, null);
        } catch(SQLException ex) {
            TAG.severe(String.format("%s", ex.getMessage()));
        }
        return nesMeterEntity;
    }

    private String getLoadVoltageStatus(String loadVoltageStatusTypeId) {
        String status = "";
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

        return  status;
    }

    private String getControlRelayStatus(String controlRelayStatusTypeId) {
        String status = "";
        if(!StringUtils.isEmpty(controlRelayStatusTypeId)) {
            switch(controlRelayStatusTypeId) {
                case NESConstants.ControlRelayStatus.OPEN:
                    status = "DISCONNECTED";
                    break;
                case NESConstants.ControlRelayStatus.CLOSED:
                    status = "CONNECTED";
                    break;
            }
        } else {
            status = "ERROR";
        }
        return  status;
    }

    private String getMeterStatus(NESBrokerCommandEntity brokerCommandEntity) {

        String meterStatus = "";
        String logMsg = "";

        Assert.notNull(brokerCommandEntity, "getMeterStatus -> BrokerCommandEntity is null");
        String serialN = brokerCommandEntity.meterSerialNumber.get();
        Assert.notNull(serialN, String.format("MeterSerialNumber field is null for meterId: %s", brokerCommandEntity.meterId.get()));
        Boolean isCTMeter = brokerCommandEntity.isCTMeter.get();
        Assert.notNull(isCTMeter, String.format("isCTMeter field is null for meter: %s", serialN));

        NESDevicesMeterEntity devicesMeterEntity = getNesDevicesMeterBySerialNumber(serialN);
        Assert.notNull(devicesMeterEntity, String.format("DevicesMeterEntity is null for meter: %s", serialN));

        if(isCTMeter) {
            String controlRelayStatusTypeId = devicesMeterEntity.controlRelayStatusTypeId.get();
            Assert.notNull(controlRelayStatusTypeId, String.format("The controlRelayStatusTypeId was null for meter: %s", serialN));
            meterStatus = getControlRelayStatus( controlRelayStatusTypeId );
            logMsg = String.format("(CT) Meter: %s -> Control Relay Status: %s", serialN, meterStatus);
        } else {
            String loadVoltageStatusTypeId = devicesMeterEntity.loadVoltageStatusTypeId.get();
            Assert.notNull(loadVoltageStatusTypeId, String.format("The loadVoltageStatusTypeId was null for meter: %s", serialN));
            meterStatus  = getLoadVoltageStatus( loadVoltageStatusTypeId );
            logMsg = String.format("(DO) Meter: %s -> Load Voltage Status: %s", serialN, meterStatus);
        }

        TAG.info(logMsg);

        return meterStatus;
    }

    private Boolean allowBrokerCommandSubmission(NESBrokerCommandEntity brokerCommandEntity) {
        String logMsg = "";
        String meterStatus     = getMeterStatus(brokerCommandEntity);
        String commandAction   = convertCommandCodeToStringValue( brokerCommandEntity.command.get(), brokerCommandEntity.isCTMeter.get() );
        Boolean bothConnect    = commandAction.equalsIgnoreCase("connect") & meterStatus.equalsIgnoreCase("connected");
        Boolean bothDisconnect = commandAction.equalsIgnoreCase("disconnect") & meterStatus.equalsIgnoreCase("disconnected");
        Boolean unknownOrError = meterStatus.equalsIgnoreCase("unknown") | meterStatus.equalsIgnoreCase("error");
        String serialN = brokerCommandEntity.meterSerialNumber.get();
        if(bothConnect) {
            logMsg = String.format("Connect broker command action could not be submitted. Meter status was already connected for meter: %s", serialN);
            brokerCommandEntity.commandStatus.set(Status.SUCCESSFUL.getCode());
            brokerCommandEntity.error.set(logMsg);
            TAG.warning(logMsg);
            return false;
        }else if(bothDisconnect) {
            logMsg = String.format("Disconnect broker command action could not be submitted. Meter status was already disconnected for meter: %s", serialN);
            brokerCommandEntity.commandStatus.set(Status.SUCCESSFUL.getCode());
            brokerCommandEntity.error.set(logMsg);
            TAG.warning(logMsg);
            return false;
        } else if(unknownOrError) {
            logMsg = String.format("Unknown broker command action could not be submitted for meter: %s", serialN);
            brokerCommandEntity.commandStatus.set(Status.FAILED_WITH_REASON.getCode());
            brokerCommandEntity.error.set(logMsg);
            TAG.warning(logMsg);
            return false;
        } else {
            return true;
        }
    }

    public NESDevicesMeterEntity getNesDevicesMeterBySerialNumber(String serialN) {

        NESDevicesMeterEntity nesDevicesMeter = null;

        try (Connection connection = nesDataSource.getConnection()) {
            nesDevicesMeter = DataSourceDB.getFromSet(connection, new NESDevicesMeterEntity().serialNumber.set(serialN));
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return nesDevicesMeter;
    }

    public void processScheduled() {
        List<NESBrokerCommandEntity> brokerCommandsByStatus = getScheduled();
        for (NESBrokerCommandEntity currentEntity : brokerCommandsByStatus) {
            boolean isError = false;
            try {
                setDeviceGatewayGUID(currentEntity);
                // TODO DESK-1512: Check meter status before submitting the command
                // The broker command must only be submitted when the Command Action and Meter Status is different.
                // Example1: Meter Status = DISCONNECTED, Command Action = CONNECT     -> Send command to NES
                // Example2: Meter Status = CONNECTED,    Command Action = DISCONNECT  -> Send command to NES
                // Example3: Meter Status = DISCONNECTED, Command Action = DISCONNECT  -> Don't send command to NES
                // Example4: Meter Status = CONNECTED,    Command Action = CONNECT     -> Don't send command to NES
                if(allowBrokerCommandSubmission(currentEntity)) {
                    submitCommand(currentEntity);
                    currentEntity.commandStatus.set(Status.SUBMITED.getCode());
                } else {
                    currentEntity.submitDate.set( Timestamp.valueOf( LocalDateTime.now() ) );
                }
                saveBrokerCommandStatus(currentEntity);
            } catch(Exception ex) {
                TAG.severe(String.format("%s", ex.getMessage()));
                isError = true;
            } finally {
                if(isError) {
                    currentEntity.commandStatus.set(Status.WAITING.getCode());
                    saveBrokerCommandStatus(currentEntity);
                }
            }
        }
    }

    public void processWaiting() {
        List<NESBrokerCommandEntity> brokerCommandsByStatus = getWaiting();
        //Calendar cal = Calendar.getInstance();
        for (NESBrokerCommandEntity currentEntity : brokerCommandsByStatus) {
            boolean isError = false;
            try {
                Timestamp submittedDate = currentEntity.submitDate.get();

                LocalDateTime submittedLDT =  submittedDate.toLocalDateTime().plusHours(1);

                if ((submittedLDT.toInstant(ZoneOffset.UTC).toEpochMilli()) < System.currentTimeMillis()) {
                    currentEntity.commandStatus.set(Status.FAILED_TIME_OUT.getCode());
                    saveBrokerCommandStatus(currentEntity);
                }

                String NESstatusID = getCommandStatusFromNES(currentEntity);

                if (NESstatusID.equalsIgnoreCase(NESConstants.CommandHistoryStatus.SUCCESS)) {
                    NESMeterEntity nesMeter = getNesMeterByRef(currentEntity);
                    currentEntity.commandStatus.set(Status.SUCCESSFUL.getCode());
                    nesMeter.statusOn.set(currentEntity.command.get().equals(NESBrokerCommandEntity.Command.CONNECT.getCode()));
                    saveNesMeter(nesMeter);
                    saveBrokerCommandStatus(currentEntity);
                } else if (NESstatusID.equalsIgnoreCase(NESConstants.CommandHistoryStatus.WAITING) || NESstatusID.equalsIgnoreCase(NESConstants.CommandHistoryStatus.IN_PROGRESS)) {
                    submitGatewayConnect(currentEntity);
                } else if (NESstatusID.equalsIgnoreCase(NESConstants.CommandHistoryStatus.FAILURE)) {
                    currentEntity.commandStatus.set(Status.FAILED_WITH_REASON.getCode());
                    saveBrokerCommandStatus(currentEntity);
                }

            } catch(Exception ex) {
                TAG.severe(String.format("%s", ex.getMessage()));
                isError = true;
            } finally {
                if(isError) {
                    currentEntity.commandStatus.set(Status.ERROR.getCode());
                    saveBrokerCommandStatus(currentEntity);
                }
            }
        }
    }

    public void processSubmitted() {
        List<NESBrokerCommandEntity> brokerCommandsByStatus = getSubmitted();
        for (NESBrokerCommandEntity currentEntity : brokerCommandsByStatus) {
            currentEntity.commandStatus.set(Status.WAITING.getCode());
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            currentEntity.submitDate.set(currentTime);
            DataSourceDB.set(dataSource, currentEntity);
        }
    }

    private class NESResults {
        private String trackingID;
        private String commandStatus;

        public void setTrackingID(String trackingID) {
            this.trackingID = trackingID;
        }

        public String getTrackingID() {
            return trackingID;
        }

        public void setCommandStatus(String commandStatus) {
            this.commandStatus = commandStatus;
        }

        public String getCommandStatus() {
            return commandStatus;
        }
    }

}
