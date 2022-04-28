package za.co.spsi.mdms.utility;

import org.jboss.logging.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import za.co.spsi.mdms.common.database.IceUtilDB;
import za.co.spsi.mdms.common.database.MdmsOracleDB;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseService;
import za.co.spsi.toolkit.crud.idempiere.AgencyBillingProperties;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.util.Assert;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Disabled
@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BillingDataWrapperTest {

    Logger logger = Logger.getLogger(BillingDataWrapperTest.class.getName());

    private DateTimeFormatter dateTimeFormatter;

    private MdmsOracleDB mdmsOracleDB;
    private DataSource mdmsDS;
    private Driver mdmsDriver;

    private IceUtilDB iceUtilDB;
    private DataSource iceUtilDS;
    private Driver iceUtilDriver;

    private MDMSUtilityHelper utilityHelper;

    private IceTimeOfUseService timeOfUseService;

    @BeforeAll
    void initDataSource() throws SQLException {

        mdmsOracleDB = new MdmsOracleDB();
        mdmsDS = mdmsOracleDB.getMdmsDS();
        mdmsDriver = mdmsOracleDB.getDriver();
        dateTimeFormatter = mdmsOracleDB.getDateTimeFormatter();

        iceUtilDB = new IceUtilDB();
        iceUtilDS = iceUtilDB.getIdeUtilDS();
        iceUtilDriver = iceUtilDB.getDriver();

        utilityHelper = new MDMSUtilityHelper();
        utilityHelper.billingWrapper = new BillingDataWrapper();
        utilityHelper.mdmsDataSource = mdmsDS;
        utilityHelper.iceDataSource = iceUtilDS;
        utilityHelper.billingProperties = getAgencyBillingProperties();

        timeOfUseService = new IceTimeOfUseService();
        timeOfUseService.utilityHelper = utilityHelper;

    }

    private AgencyBillingProperties getAgencyBillingProperties() {
        Properties props = new Properties();
        FileInputStream fis = null;
        AgencyBillingProperties agencyBillingProperties = new AgencyBillingProperties();
        try {
            fis = new FileInputStream("src/test/resources/agencybilling.properties");
            props.load(fis);
            agencyBillingProperties.iceBaseUrl = props.getProperty("iceBaseUrl");
            agencyBillingProperties.iceUser = props.getProperty("iceUser");
            agencyBillingProperties.icePassword = props.getProperty("icePassword");
            agencyBillingProperties.iceClientId = Integer.parseInt( props.getProperty("iceClientId") );
            agencyBillingProperties.iceOrgId = Integer.parseInt( props.getProperty("iceOrgId") );
            agencyBillingProperties.iceRoleId = Integer.parseInt( props.getProperty("iceRoleId") );
            agencyBillingProperties.iceWarehouseId = Integer.parseInt( props.getProperty("iceWarehouseId") );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agencyBillingProperties;
    }

    @Test
    @DisplayName("Billing Wrapper: Get TOU Time Slot Detail Request Test")
    void getTOUTimeSlotDetailsRequestTest() {

        List<List<String>> portalParams = new ArrayList<>();
        List<String> offPeakParams = Arrays.asList("OP","1000032","1000972");
        portalParams.add(offPeakParams);
        List<String> peakParams = Arrays.asList("P","1000029","1000975");
        portalParams.add(peakParams);
        List<String> standardParams = Arrays.asList("STD","1000031","1000978");
        portalParams.add(standardParams);

        LocalDateTime entryDateTime = LocalDateTime.of(2021, 5, 28, 7, 30, 0);
        Timestamp entryTimestamp = Timestamp.valueOf(entryDateTime);

        for(List<String> params : portalParams) {

            String touType = params.get(0);
            Integer priceListId = Integer.parseInt(params.get(1));
            Integer priceListVersionId = Integer.parseInt(params.get(2));

            String touTypeResult = timeOfUseService.getTouTimeSlotType(priceListId, priceListVersionId, entryTimestamp);

            if( touTypeResult != null ) {
                logger.info( String.format("TOU Type: %s, Entry Time: %s",
                        touTypeResult,
                        dateTimeFormatter.format( entryDateTime )
                ) );
                Assert.isTrue( touType.equalsIgnoreCase(touTypeResult), "TOU type incorrect !");
                break;
            }

        }

    }

    @Test
    @DisplayName("Meter Register Update Service")
    void getTOUReadingListToDateTest() {

        LocalDateTime dt = LocalDateTime.of(2021, 5, 31, 23, 59, 59);
        Timestamp date = Timestamp.valueOf(dt);

        Timestamp toDate = Timestamp.valueOf(date.toLocalDateTime().plusSeconds(1));

        logger.info(String.format("%s", DriverFactory.getDriver().toDate(toDate.toLocalDateTime()) ));

    }

}
