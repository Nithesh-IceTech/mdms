package za.co.spsi.mdms.openmuc.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.services.AbstractPrepaidService;
import za.co.spsi.openmucrest.client.DriverRestClient;
import za.co.spsi.openmucrest.client.OpenMucRestClient;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OpenMucDriverRestClientTest extends AbstractPrepaidService {

    private Logger logger = Logger.getLogger(OpenMucDriverRestClientTest.class.getName());

    private DateTimeFormatter dateTimeFormatter;

    private DriverRestClient driverRestClient;

    @BeforeAll
    void initData() {
        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        driverRestClient = new DriverRestClient();
        driverRestClient.driversUrl = "drivers";
        driverRestClient.openMucRestClient = new OpenMucRestClient();
    }

    @Test
    @DisplayName("OpenMucRestClient: Get All Driver Id's")
    void getAllDriverIds_Test() {

        logger.info("OpenMucRestClient -> getAllDriverIds Unit Test");

        try {
            JsonNode allDriversJsonNode = driverRestClient.getAllDrivers();
            System.out.println( String.format("Response Data: %s", allDriversJsonNode.toString() ) );
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    @DisplayName("OpenMucRestClient: Get Driver By Id")
    void getDriverById_Test() {

        logger.info("OpenMucRestClient -> getDriverById Unit Test");

        try {
            JsonNode oneDriverJsonNode = driverRestClient.getDriverById("dlms");
            System.out.println( String.format("Response Data: %s", oneDriverJsonNode.toString() ) );
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    @DisplayName("OpenMucRestClient: Post Create New Driver")
    void createNewDriver_Test() {

        logger.info("OpenMucRestClient -> createNewDriver Unit Test");

        DriverConfigEntity dlmsDriver = new DriverConfigEntity();
        dlmsDriver.setProtocolDriver("dlms");
        dlmsDriver.setSamplingTimeout("5000");
        dlmsDriver.setConnectRetryInterval("5000");
        dlmsDriver.setDisabled(false);

        try {
            driverRestClient.createNewDriver(dlmsDriver);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    @DisplayName("OpenMucRestClient: Delete Driver")
    void deleteDriver_Test() {

        logger.info("OpenMucRestClient -> deleteDriver Unit Test");

        DriverConfigEntity dlmsDriver = new DriverConfigEntity();
        dlmsDriver.setProtocolDriver("dlms");
        dlmsDriver.setSamplingTimeout("5000");
        dlmsDriver.setConnectRetryInterval("5000");
        dlmsDriver.setDisabled(false);

        try {
            List<String> driverList = new ArrayList<>();
            JsonNode allDriversJsonNode = driverRestClient.getAllDrivers();
            JsonNode driversJson = allDriversJsonNode.get("drivers");
            if (driversJson.isArray()) {
                for (final JsonNode driverNode : driversJson) {
                    driverList.add(driverNode.textValue());
                }
            }
            if(driverList.stream().anyMatch(drv -> drv.equalsIgnoreCase(dlmsDriver.getProtocolDriver()))) {
                logger.info(String.format("Driver %s exists.", dlmsDriver.getProtocolDriver()));
                driverRestClient.deleteDriver(dlmsDriver);
            } else {
                logger.warning(String.format("Driver %s doesn't exist !", dlmsDriver.getProtocolDriver()));
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

    }

}
