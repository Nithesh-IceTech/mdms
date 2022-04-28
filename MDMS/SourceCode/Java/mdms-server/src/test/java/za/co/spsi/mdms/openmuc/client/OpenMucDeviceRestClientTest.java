package za.co.spsi.mdms.openmuc.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.services.AbstractPrepaidService;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;
import za.co.spsi.openmucdoa.entities.dlms.DlmsDeviceConfigEntity;
import za.co.spsi.openmucrest.client.DeviceRestClient;
import za.co.spsi.openmucrest.client.OpenMucRestClient;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenMucDeviceRestClientTest extends AbstractPrepaidService {

    private Logger logger = Logger.getLogger(OpenMucDeviceRestClientTest.class.getName());

    private DateTimeFormatter dateTimeFormatter;

    private DeviceRestClient deviceRestClient;

    @BeforeAll
    void initData() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        deviceRestClient = new DeviceRestClient();
        deviceRestClient.devicesUrl = "devices";
        deviceRestClient.openMucRestClient = new OpenMucRestClient();
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "meterA",
            "meterB",
            "meterC",
    })
    @DisplayName("OpenMucRestClient: Post Create New Device")
    void createNewDevice_Test(String deviceName) {

        logger.info("OpenMucRestClient -> createNewDevice Unit Test");

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

        try {
            deviceRestClient.createNewDevice(meterDlmsConfig, dlmsDriver);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    @DisplayName("OpenMucRestClient: Get All Devices")
    void getAllDevices_Test() {

        logger.info("OpenMucRestClient -> getAllDevices Unit Test");

        try {
            JsonNode responseJsonNode = deviceRestClient.getAllDevices();
            System.out.println( String.format("Response Data: %s", responseJsonNode.toPrettyString() ) );
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "meterA",
            "meterB",
            "meterC",
    })
    @DisplayName("OpenMucRestClient: Get Device By Id")
    void getDeviceById_Test(String deviceName) {

        logger.info("OpenMucRestClient -> getDeviceById Unit Test");

        try {
            JsonNode jsonNodeResponse = deviceRestClient.getDeviceById(deviceName);
            System.out.println( String.format("Device: %s", jsonNodeResponse.toPrettyString() ) );
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "meterA",
            "meterB",
            "meterC",
    })
    @DisplayName("OpenMucRestClient: Get Device State")
    void getDeviceState_Test(String deviceName) {

        logger.info("OpenMucRestClient -> getDeviceState Unit Test");

        try {
            String deviceState = deviceRestClient.getDeviceState(deviceName);
            System.out.println( String.format("Device %s state: %s", deviceName, deviceState ) );
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "meterA",
            "meterB",
            "meterC",
    })
    @DisplayName("OpenMucRestClient: Delete Device")
    void deleteDevice_Test(String deviceName) {

        logger.info("OpenMucRestClient -> deleteDevice Unit Test");

        try {
            List<String> deviceList = new ArrayList<>();
            JsonNode allDevicesJsonNode = deviceRestClient.getAllDevices();
            JsonNode devicesJson = allDevicesJsonNode.get("devices");
            if (devicesJson.isArray()) {
                for (final JsonNode driverNode : devicesJson) {
                    deviceList.add(driverNode.textValue());
                }
            }
            if(deviceList.stream().anyMatch(drv -> drv.equalsIgnoreCase( deviceName ))) {
                logger.info(String.format("Device %s exists.", deviceName ));
                deviceRestClient.deleteDevice(deviceName);
            } else {
                logger.warning(String.format("Device %s doesn't exist !", deviceName ));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

}
