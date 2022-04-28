package za.co.spsi.mdms.common.properties.watcher;

import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn("PropertiesConfig")
public class PropertiesWatcherProcess extends ProcessorService {

    @Inject
    private PropertiesWatcher propertiesWatcher;

    private Processor p = getProcessor();

    @PostConstruct
    void init() {
        p.start(() -> propertiesWatcher.start());
    }

}
