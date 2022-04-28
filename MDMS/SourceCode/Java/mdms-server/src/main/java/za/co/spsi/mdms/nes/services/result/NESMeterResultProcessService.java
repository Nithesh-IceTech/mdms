package za.co.spsi.mdms.nes.services.result;

import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;

/**
 * Created by jaspervdb on 2016/10/13.
 * copy the results form NES source to local db
 */
@Singleton()
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class NESMeterResultProcessService extends ProcessorService {

    @Inject
    private NESResultProcessor nesResultProcessor;

    private Processor p1 = getProcessor();
    private Processor p2 = getProcessor();

    @PostConstruct
    private void startServices() {
        p1.delay(5).minutes(1).repeat(() -> nesResultProcessor.process());
        p2.hours(1).repeat(() -> nesResultProcessor.checkUnprocessedNesResultsBacklog());
    }

}
