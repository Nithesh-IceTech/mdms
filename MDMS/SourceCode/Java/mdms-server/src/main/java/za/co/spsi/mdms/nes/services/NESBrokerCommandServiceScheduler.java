package za.co.spsi.mdms.nes.services;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.nes.db.NESBrokerCommandEntity;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created by jaspervdbijl on 2017/07/24.
 */
@Singleton
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class NESBrokerCommandServiceScheduler extends ProcessorService {

    @Inject
    private BeanManager beanManager;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private PropertiesConfig propertiesConfig;

    private Processor processor = getProcessor();

    private NESBrokerCommandService broker;

    @PostConstruct
    public void startServices() {
        if (propertiesConfig.getNes_broker_processing_enabled()) {
            broker = getBean(beanManager,NESBrokerCommandService.class);
            processor.delay(5).seconds(5).repeat(() -> {
                NESBrokerCommandEntity.scheduleCreated(dataSource); // CREATED -> PROCESSING
                broker.processScheduled(); // PROCESSING -> SUBMITTED
                broker.processSubmitted(); // SUBMITTED -> WAITING
                broker.processWaiting(); // WAITING -> (FAILED_WITH_REASON or FAILED_TIME_OUT or SUCCESSFUL or ERROR)
            });
        }
    }
}
