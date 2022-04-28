package za.co.spsi.mdms.common.services;

import org.idempiere.webservice.client.base.WebServiceResponse;
import za.co.spsi.mdms.common.db.PrepaidMeterMeterReadingsView;
import za.co.spsi.mdms.common.db.PrepaidMeterReadingBatch;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.utility.MDMSUtilityHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static za.co.spsi.mdms.common.db.PrepaidMeterReadingBatch.Status.*;

/**
 * Created by jaspervdbijl on 2017/01/06.
 */
@Singleton()
@Startup
@DependsOn({"PropertiesConfig"})
@TransactionManagement(value = TransactionManagementType.BEAN)
public class PrepaidMeterReadingPushProcessingService extends AbstractPrepaidService {

    public static Logger TAG = Logger.getLogger(PrepaidMeterReadingPushProcessingService.class.getName());

    @Inject
    protected MDMSUtilityHelper helper;

    @Inject
    private PropertiesConfig propertiesConfig;

    @PostConstruct
    private void init() {
        p.delay(5).minutes(5).repeat(() -> {
            if(propertiesConfig.getUtility_prepaid_push_enabled()) {
                process();
            }
        });
    }

    public void process() {

        Driver driver = DriverFactory.getDriver();
        if(driver != null) {
            String query = "select * from PREPAID_METER_READING_BATCH where STATUS_ID = ?";
            query = driver.orderBy(query, "created_date", false);
            List<PrepaidMeterReadingBatch> prepaidMeterReadingBatchList =
                    DataSourceDB.getAllAsList(PrepaidMeterReadingBatch.class, dataSource, query, MARKED.getCode());
            TAG.info(String.format("Batch List Size: %d", prepaidMeterReadingBatchList.size()));
            prepaidMeterReadingBatchList.stream().forEach(batch -> process(batch));
        }

    }

    public void process(PrepaidMeterReadingBatch batch) {

        StringBuilder sb = new StringBuilder();
        DataSourceDB.executeInTx(dataSource, true, batch, connection -> {
            batch.statusId.set(STARTED.getCode());
            DataSourceDB<PrepaidMeterMeterReadingsView> meterReadingEntityList =
                    new PrepaidMeterMeterReadingsView(batch.prepaidMeterReadingBatchId.get(), false )
                            .getDataSource(connection);
            WebServiceResponse response = helper.sendPrepaidCreateUpdateMeterReadingMultipleRequest(meterReadingEntityList, sb );
            batch.utilStatusId.set(response.getStatus().toString());
            batch.utilData.set(sb.toString());
            batch.statusId.set(COMPLETED.getCode());
        }, entity -> {
            batch.utilStatusId.set("Failed");
            batch.statusId.set(FAILED.getCode());
            batch.utilData.set(sb.toString());
        });

    }

}
