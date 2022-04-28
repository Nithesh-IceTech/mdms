package za.co.spsi.mdms.nes.services;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.database.NesMssqlDB;
import za.co.spsi.mdms.nes.db.NESDevicesMeterEntity;
import za.co.spsi.mdms.nes.util.NESConstants;
import za.co.spsi.pjtk.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NESBrokerCommandServiceTest {

    private Logger logger = Logger.getLogger(NESBrokerCommandServiceTest.class.getName());

    private NesMssqlDB nesMssqlDB;

    public NESBrokerCommandServiceTest() {

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

    @BeforeAll
    void initData() {
        nesMssqlDB = new NesMssqlDB();
    }

    @Test
    @DisplayName("NES Broker Command Service: getMeterControlRelayStatus Test")
    void getMeterControlRelayStatus_Test() {

        List<String> nesMeterList =
                Arrays.asList("ELON087416", "ELON042628", "ELON042632", "ELON073973", "ELON067366",
                                                  "ELON048914", "ELON043324", "ELON083759", "ELON036998", "ELON039930",
                                                  "ELON039530", "ELON125835", "ELON098944", "ELON125990", "ELON125912",
                                                  "ELON041534", "ELON042273", "ELON049505", "ELON040367", "ELON068041",
                                                  "ELON069213", "ELON049134", "ELON042583", "ELON040467");

        List<NESDevicesMeterEntity> nesDevicesMeterList = new ArrayList<>();

        for(String serialN: nesMeterList) {
            NESDevicesMeterEntity nesDevicesMeter = nesMssqlDB.getNesDevicesMeterBySerialNumber(serialN);
            nesDevicesMeterList.add(nesDevicesMeter);
        }

        List<String> logs = new ArrayList<>();

        for(NESDevicesMeterEntity nesDevicesMeter: nesDevicesMeterList) {

            String serialN = nesDevicesMeter.serialNumber.get();
            Boolean isCTMeter = nesMssqlDB.isCTMeter(serialN);

            if(isCTMeter) {

                String controlRelayStatus = getControlRelayStatus( nesDevicesMeter.controlRelayStatusTypeId.get() );

                String log = String.format("(CT) Meter: %s -> Control Relay Status: %s",
                        serialN,
                        controlRelayStatus);

                logs.add(log);

            } else {

                String loadVoltageStatus  = getLoadVoltageStatus( nesDevicesMeter.loadVoltageStatusTypeId.get() );

                String log = String.format("(DO) Meter: %s -> Load Voltage Status: %s",
                        serialN,
                        loadVoltageStatus);

                logs.add(log);

            }

        }

        for(String log: logs) {
            System.out.println( log );
        }

    }

}
