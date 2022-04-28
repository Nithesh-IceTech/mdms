package za.co.spsi.mdms.nes.services.meter;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created by jaspervdb on 2016/10/13.
 * service is responsible for update the meter db with the meter info
 */
@Singleton(name = "NESMeterSyncService")
@TransactionManagement(value = TransactionManagementType.BEAN)
@Startup()
public class NESMeterSyncService extends ProcessorService {

    @Inject
    private BeanManager beanManager;

    private NESMeterService meterService;

    private Processor processor = getProcessor();

    @PostConstruct
    public void startServices() {
        meterService = getBean(beanManager,NESMeterService.class);
        processor.delay(5).minutes(60).repeat(() -> meterService.updateMeters());
    }

}
