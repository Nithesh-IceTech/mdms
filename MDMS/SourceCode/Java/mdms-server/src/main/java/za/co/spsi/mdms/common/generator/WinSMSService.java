package za.co.spsi.mdms.common.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.spsi.mdms.common.db.generator.CommunicationLogEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Created by jaspervdbijl on 2017/07/26.
 * receive sms from win sms and insert into comms log for processing
 */
@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class WinSMSService extends ProcessorService {

    public static final Logger TAG = LoggerFactory.getLogger(WinSMSService.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    private WinSmsHelper winSmsHelper;

    private Processor processor = getProcessor();

    private void process() {

        if (propertiesConfig.getWinsms_service_enabled()) {
            TAG.info("Check for new sms's");
            winSmsHelper.getSms().stream().forEach(sms ->
                    DataSourceDB.set(dataSource,new CommunicationLogEntity(sms)));
        } else {
            TAG.info("WinSMS Services Disabled");
        }

    }

    @PostConstruct
    private void schedule() {
        TAG.info(String.format("WinSMS Service %s",
                propertiesConfig.getWinsms_service_enabled() ? "Enabled" : "Disabled" ));
        processor.delay(5).seconds(15).repeat(() -> process());
    }

}
