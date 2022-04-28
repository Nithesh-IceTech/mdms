package za.co.spsi.mdms.openmuc.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.services.AbstractPrepaidService;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;
import za.co.spsi.openmucdoa.entities.dlms.DlmsChannelConfigEntity;
import za.co.spsi.openmucdoa.entities.dlms.DlmsDeviceConfigEntity;
import za.co.spsi.openmucrest.client.ChannelRestClient;
import za.co.spsi.openmucrest.client.OpenMucRestClient;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenMucChannelRestClientTest extends AbstractPrepaidService {

    private Logger logger = Logger.getLogger(OpenMucChannelRestClientTest.class.getName());

    private DateTimeFormatter dateTimeFormatter;

    private ChannelRestClient channelRestClient;

    @BeforeAll
    void initData() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        channelRestClient = new ChannelRestClient();
        channelRestClient.devicesUrl = "devices";
        channelRestClient.channelsUrl = "channels";
        channelRestClient.openMucRestClient = new OpenMucRestClient();
    }

    @ParameterizedTest(name = "{0},{1}")
    @CsvSource({
            "meterA,totalkWh,3,1-1:m:8.0,2",
            "meterA,t1kWh,3,1-1:m:8.1,2",
            "meterA,t2kWh,3,1-1:m:8.2,2",

            "meterB,totalkWh,3,1-1:m:8.0,2",
            "meterB,t1kWh,3,1-1:m:8.1,2",
            "meterB,t2kWh,3,1-1:m:8.2,2",

            "meterC,totalkWh,3,1-1:m:8.0,2",
            "meterC,t1kWh,3,1-1:m:8.1,2",
            "meterC,t2kWh,3,1-1:m:8.2,2"
    })
    @DisplayName("OpenMucRestClient: Post Create New Channel")
    void createNewChannel_Test(String deviceName, String channelName, String classId, String obisCode, String attrId) {

        logger.info("OpenMucRestClient -> createNewChannel Unit Test");

        DriverConfigEntity dlmsDriver = new DriverConfigEntity();
        dlmsDriver.setProtocolDriver("dlms");
        dlmsDriver.setSamplingTimeout("5000");
        dlmsDriver.setConnectRetryInterval("5000");
        dlmsDriver.setDisabled(false);

        DlmsDeviceConfigEntity meterDlmsConfig = new DlmsDeviceConfigEntity();
        meterDlmsConfig.setDataCollectorId(1L);
        meterDlmsConfig.setDriverId(1L);
        meterDlmsConfig.setIedName(deviceName);
        meterDlmsConfig.setDescription("DLMS HDLC TCP Meter");
        meterDlmsConfig.setIpAddress("127.0.0.1");
        meterDlmsConfig.setPortNumber("5081");
        meterDlmsConfig.setSerialNumber("73852799");
        meterDlmsConfig.setHdlcAddress("3799");
        meterDlmsConfig.setLogicalDeviceAddress("1");
        meterDlmsConfig.setClientId("16");
        meterDlmsConfig.setDisabled(false);

        DlmsChannelConfigEntity channelDlmsConfig = new DlmsChannelConfigEntity();
        channelDlmsConfig.setChannelName(deviceName + "_" + channelName);
        channelDlmsConfig.setDescription( String.format("Active Energy Register: %s", channelName) );
        channelDlmsConfig.setClassId(classId);
        channelDlmsConfig.setObisCode(obisCode);
        channelDlmsConfig.setAttributeId(attrId);
        channelDlmsConfig.setDataType("DOUBLE");
        channelDlmsConfig.setDataObjectType("DOUBLE_LONG");
        channelDlmsConfig.setChannelType("DOUBLE");
        channelDlmsConfig.setLoggingInterval("5000");
        channelDlmsConfig.setSamplingInterval("5000");

        try {
            channelRestClient.createNewChannel(dlmsDriver.getProtocolDriver(), meterDlmsConfig, channelDlmsConfig);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    @DisplayName("OpenMucRestClient: Get All Channels")
    void getAllChannels_Test() {

        logger.info("OpenMucRestClient -> getAllChannels Unit Test");

        try {
            JsonNode responseJsonNode = channelRestClient.getAllChannels();
            System.out.println( String.format("Response Data: %s", responseJsonNode.toPrettyString() ) );
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "meterA"
    })
    @DisplayName("OpenMucRestClient: Get All Channels By Device Id")
    void getAllChannelRecordsByDeviceId_Test(String deviceName) {

        logger.info("OpenMucRestClient -> getAllChannelsByDeviceId Unit Test");

        try {
            JsonNode jsonNodeResponse = channelRestClient.getAllChannelRecordsByDeviceId(deviceName);
            System.out.println( String.format("Channel Records: %s", jsonNodeResponse.toPrettyString() ) );
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "meterA_totalkWh"
    })
    @DisplayName("OpenMucRestClient: Get Channel Configuration By Channel Name")
    void getConfigByChannel_Test(String channelName) {

        logger.info("OpenMucRestClient -> getConfigByChannel Unit Test");

        try {
            JsonNode jsonNodeResponse = channelRestClient.getConfigByChannel(channelName);
            System.out.println( String.format("Channel %s configuration: %s",
                    channelName,
                    jsonNodeResponse.toPrettyString() ) );
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "meterA,totalkWh",
            "meterA,t1kWh",
            "meterA,t2kWh",

            "meterB,totalkWh",
            "meterB,t1kWh",
            "meterB,t2kWh",

            "meterC,totalkWh",
            "meterC,t1kWh",
            "meterC,t2kWh"
    })
    @DisplayName("OpenMucRestClient: Delete Channel")
    void deleteChannel_Test(String deviceName, String channelName) {

        logger.info("OpenMucRestClient -> deleteDevice Unit Test");

        try {
            List<String> deviceList = new ArrayList<>();
            JsonNode allChannelsJsonNode = channelRestClient.getAllChannels();
            JsonNode channelsJson = allChannelsJsonNode.get("records");
            if (channelsJson.isArray()) {
                for (final JsonNode driverNode : channelsJson) {
                    deviceList.add(driverNode.get("id").textValue());
                }
            }
            if(deviceList.stream().anyMatch(drv -> drv.equalsIgnoreCase( channelName ))) {
                logger.info(String.format("Channel %s exists.", channelName ));
                channelRestClient.deleteChannel(channelName);
            } else {
                logger.warning(String.format("Channel %s doesn't exist !", channelName ));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

}
