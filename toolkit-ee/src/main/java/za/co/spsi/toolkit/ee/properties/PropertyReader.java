package za.co.spsi.toolkit.ee.properties;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jaspervdb on 2016/05/03.
 */
@Dependent
public class PropertyReader {

//    @Inject
//    Configuration configuration;
    @Produces
    @PropertiesFromFile
    public Properties provideServerProperties(InjectionPoint ip) {
        //get filename from annotation
        String filename = ip.getAnnotated().getAnnotation(PropertiesFromFile.class).value();
        return readProperties(filename);
    }

    private Properties readProperties(String fileInClasspath) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileInClasspath);

        try {
            Properties properties = new Properties();
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Could not read properties from file " + fileInClasspath + " in classpath. " + e);
        }
    }
}
