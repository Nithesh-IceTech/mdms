package za.co.spsi.mdms.common.properties;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PropertiesConfigTest {

    @Mock
    private PropertiesConfig propertiesConfig;

    @BeforeAll
    void initData() {

    }

    @ParameterizedTest(name = "Meter Serial Number: {0}")
    @CsvSource({
            "20529319",
            "20529328",
            "30829119"
    })
    @DisplayName("PropertiesConfig Test: 30min Meter Reading Property Filter Test")
    void prepaidBatch30minMeterFilterTest(String serialNo) {
        // mock prepaid.batch.30min.meter.filter property
        when(propertiesConfig.getPrepaid_batch_30min_meter_filter()).thenReturn("20529319,20529328,30829119");

        // Expression Under Test
        Boolean latestReadingOnly = Arrays.stream(propertiesConfig.getPrepaid_batch_30min_meter_filter().split(",")).noneMatch(mtr -> mtr.equalsIgnoreCase(serialNo));

        Assertions.assertEquals(false, latestReadingOnly,
                String.format("Meter with serial number: %s, is not present in the filter property prepaid.batch.30min.meter.filter.", serialNo));
    }

}
