package za.co.spsi.mdms.common.properties.service;

import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class PropertiesStoreService {

    private static Logger logger = Logger.getLogger(PropertiesStoreService.class.getName());

    @Inject
    @ConfValue(value = "configuration.store.filepath", folder = "server")
    private String propertiesFilePath;

    public PropertiesStoreService() {

    }

    @PostConstruct
    private void init() {
        logger.log(Level.INFO, "MDMS PropertiesStoreService Started.");
        loadPropertiesFromFile();
    }

    public Properties loadPropertiesFromFile() {

        Properties configStore = new Properties();

        InputStream stream = null;
        try {
            stream = new FileInputStream(propertiesFilePath + "/configurationStore.properties");
            configStore.load(stream);
            return configStore;
        }  catch ( Exception ex ) {
            logger.log(Level.SEVERE, ex.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioex) {
                    logger.log(Level.SEVERE, ioex.getMessage());
                }
            }
        }

        return null;
    }

    public Map<String,String> convertPropertiesToMap(Properties properties) {
        Map<String, String> propMap = new HashMap<>();
        properties.forEach( (key, val) -> {
            propMap.put(key.toString(), val.toString());
        });
        return propMap;
    }

}
