package za.co.spsi.mdms.common.properties.watcher;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.properties.service.PropertiesStoreService;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.ejb.*;
import javax.inject.Inject;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class PropertiesWatcher {

    private static Logger logger = Logger.getLogger(PropertiesWatcher.class.getName());

    @Inject
    @ConfValue(value = "configuration.store.filepath", folder = "server")
    private String propertiesFilePath;

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    private PropertiesStoreService propertiesService;

    private Path path;

    public PropertiesWatcher() {

    }

    public void start() {

        try {

            logger.log(Level.INFO, "MDMS PropertiesWatcher Started.");

            this.path = Paths.get(propertiesFilePath);
            WatchService watchService = this.path.getFileSystem().newWatchService();
            this.path.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);

            logger.log(Level.INFO, "Waiting for property change event...");

            WatchKey key;
            while ((key = watchService.take()) != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    logger.log (Level.INFO, "Event kind:" + event.kind() + ". File affected: " + event.context() + ".");

                    if(event.context().toString().contains("configurationStore")) {
                        logger.log(Level.INFO,"MDMS file based properties store has changed.");
                        logger.log(Level.INFO,"Load new file based properties...");

                        Properties properties = propertiesService.loadPropertiesFromFile();
                        if(properties != null) {
                            propertiesConfig.setMdmsFileBasedPropertiesStore(properties);
                            propertiesConfig.updatePropertyValues();
                        } else {
                            logger.log(Level.WARNING,"MDMS file based properties store is empty !");
                        }

                    }

                }
                key.reset();
            }

        } catch(Exception ex) {
            logger.log(Level.SEVERE,ex.getMessage());
        }

    }

}
