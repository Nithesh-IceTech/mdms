package za.co.spsi.mdms.nes.services.result;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created by jaspervdb on 2016/10/13.
 * copy the results form NES source to local db
 */
@Singleton()
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn("MDMSUpgradeService")
@Startup()
public class NESMeterResultSyncService extends ProcessorService {

    public static Logger TAG = Logger.getLogger(NESMeterResultSyncService.class.getName());

    @Inject
    private BeanManager beanManager;

    @Inject
    private PropertiesConfig propertiesConfig;


    private Processor processor = getProcessor();

    private NESMeterResultService meterResultService;

    @PostConstruct
    public void startServices() {
        meterResultService = getBean(beanManager,NESMeterResultService.class);
        processor.delay(5).minutes(5).repeat(() -> meterResultService.syncResults());
    }

}
