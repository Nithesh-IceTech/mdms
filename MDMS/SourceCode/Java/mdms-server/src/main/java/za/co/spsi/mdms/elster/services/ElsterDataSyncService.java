package za.co.spsi.mdms.elster.services;

import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;

@Singleton()
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class ElsterDataSyncService extends ProcessorService {

    @Inject
    ElsterDataSyncProcessor processor;

    private Processor p = getProcessor();

    @PostConstruct
    private void initSchedule() {
        p.delay(5).minutes(5).repeat(() -> processor.process());
    }

}
