package za.co.spsi.mdms.kamstrup.processor;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.services.MeterDataServiceTest;
import za.co.spsi.mdms.kamstrup.services.order.domain.Register;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KamstrupProcessorTest {

    Logger logger = Logger.getLogger(KamstrupProcessorTest.class.getName());

    private DateTimeFormatter dateTimeFormatter;

    @BeforeAll
    void initDataSource()  {

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    }

    @Test
    @DisplayName("KamstrupProcessor: Filter Registers Test")
    void getSummaryDataServiceTest() {

        String [] registers =  {"1.1.1.8.0.255","1.1.2.8.0.255","1.1.3.8.0.255","1.1.4.8.0.255", // TOTAL
                                "1.1.1.8.1.255","1.1.2.8.1.255","1.1.3.8.1.255","1.1.4.8.1.255", // T1
                                "1.1.1.8.2.255","1.1.2.8.2.255","1.1.3.8.2.255","1.1.4.8.2.255", // T2
                                "1.1.9.6.0.255","1.1.9.6.1.255","1.1.9.6.2.255"}; // KVA

        List<Register> registerList = new ArrayList<>();

        Arrays.stream(registers).forEach(r -> {
            Register reg = new Register();
            reg.id = r;
            reg.scale = 0;
            reg.value = 1.0;
            reg.unit = "generic";
            registerList.add(reg);
        } );

        List<Register> registersFiltered = registerList.stream()
                .filter( r -> !r.id.matches("1.1.9.6.[0,1,2].255"))
                .collect(Collectors.toList());

    }

}
