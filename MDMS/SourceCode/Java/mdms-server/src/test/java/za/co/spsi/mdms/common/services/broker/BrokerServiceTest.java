package za.co.spsi.mdms.common.services.broker;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.database.IceUtilDB;
import za.co.spsi.mdms.common.database.MdmsOracleDB;
import za.co.spsi.mdms.common.db.utility.IceMeter;
import za.co.spsi.mdms.util.IceMeterCacheService;
import za.co.spsi.toolkit.db.drivers.Driver;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BrokerServiceTest {

    private Logger logger = Logger.getLogger(BrokerServiceTest.class.getName());

    private MdmsOracleDB mdmsOracleDB;
    private DataSource mdmsDS;
    private Driver mdmsDriver;

    private IceUtilDB iceUtilDB;
    private DataSource iceUtilDS;
    private Driver iceUtilDriver;

    private DateTimeFormatter dateTimeFormatter;

    private BrokerService brokerService;

    private IceMeterCacheService iceMeterCacheService;

    @BeforeAll
    void initData() throws SQLException {

        mdmsOracleDB = new MdmsOracleDB();
        mdmsDS = mdmsOracleDB.getMdmsDS();
        mdmsDriver = mdmsOracleDB.getDriver();
        dateTimeFormatter = mdmsOracleDB.getDateTimeFormatter();

        iceUtilDB = new IceUtilDB();
        iceUtilDS = iceUtilDB.getIdeUtilDS();
        iceUtilDriver = iceUtilDB.getDriver();

        dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        iceMeterCacheService = new IceMeterCacheService();
        iceMeterCacheService.iceDataSource = iceUtilDS;

        brokerService = new BrokerService();
        brokerService.mdmsDataSource = mdmsDS;
        brokerService.iceDataSource = iceUtilDS;
        brokerService.iceMeterCacheService = iceMeterCacheService;
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "20529319"
    })
    @DisplayName("Broker Service: getIceMeter Test")
    void getIceMeter_Test(String serialN) {

        logger.info(String.format("Testing meter %s ...", serialN));

        IceMeter iceMeterEntity =
                brokerService.getIceMeter(serialN);

        Assertions.assertNotNull(iceMeterEntity);

        Assertions.assertNotNull(iceMeterEntity.iceMeterID.get());

        logger.info(String.format("ICE Meter ID: %s -> Serial Number: %s",
                iceMeterEntity.iceMeterID.get(),
                serialN
        ));

    }

}
