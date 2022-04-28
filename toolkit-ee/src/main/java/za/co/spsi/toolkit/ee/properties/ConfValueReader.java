package za.co.spsi.toolkit.ee.properties;

import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringUtils;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by jaspervdb on 2016/05/03.
 */
@Dependent
public class ConfValueReader {

    @Inject
    private PropertiesAgency propertiesAgency;

    private static Map<String,Properties> propertiesMap = new HashMap<>();
    private static Map<String,Map<String, Properties>> agencyMap = new HashMap<>();

    public static String getEnvironment() {
        return System.getProperty("env");
    }

    private String getEnvFile(String folder,String fname) {
        String env = getEnvironment() != null ? getEnvironment() : "dev";
        return String.format("%senv/%s/%s", folder!=null?(folder+"/"):"",env,fname);
    }


    private String getEnvConfigFile(String folder) {
        return getEnvFile(folder,"conf.properties");
    }

    private Properties getConfProperties(String folder) {
        return readProperties(getEnvConfigFile(folder));
    }

    /**
     * config may be overrided with system env property. Convention env.agency.name / global for no agency
     * @param properties
     * @param agency
     * @param confName
     * @return
     */
    private String getConfAgencyProperty(Properties properties,String agency,String confName,String defaultValue) {
        String systemPropName = String.format("env.%s.%s",agency!=null?agency:"global",confName);
        return System.getProperties().getProperty(systemPropName)!=null?
                System.getProperties().getProperty(systemPropName):
                properties.get(confName)!=null?properties.get(confName).toString()
                        : StringUtils.isEmpty(defaultValue)?null:defaultValue;
    }

    private String readAgencyProperty(InjectionPoint ip,ConfValue confValue) {
        if (!this.agencyMap.containsKey(confValue.agencyMap())) {
            this.agencyMap.put(confValue.agencyMap(),readPropertiesMap(ip,
                    confValue.folder(),getConfProperties(confValue.folder()).getProperty(confValue.agencyMap())));
        }
        String agency = propertiesAgency.getAgency();
        Assert.notNull(agency, " Child Agency has not been set " + ":" + ip.toString() );
        Assert.isTrue
                (this.agencyMap.get(confValue.agencyMap()).get(agency) != null, "No Agency Mapping defined for %s", agency);

        return getConfAgencyProperty(this.agencyMap.get(confValue.agencyMap()).get(agency),agency,confValue.value(),confValue.defaultValue());
    }

    private String readProperty(InjectionPoint ip) {
        ConfValue confValue = ip.getAnnotated().getAnnotation(ConfValue.class);
        if (confValue.agency()) {
            return readAgencyProperty(ip,confValue);
        } else {
            String agencyProperty = propertiesAgency.isAgencySet() && confValue.override() ? readAgencyProperty(ip,confValue):null;
            return agencyProperty != null?agencyProperty:getConfAgencyProperty(getConfProperties(confValue.folder()),null
                    ,confValue.value(),confValue.defaultValue());
        }
    }

    @Produces
    @ConfValue
    public String readString(InjectionPoint ip) {
        return readProperty(ip);
    }

    @Produces
    @ConfValue
    public Map<String, Properties> readPropertiesMap(InjectionPoint ip) {
        ConfValue confValue = ip.getAnnotated().getAnnotation(ConfValue.class);
        return readPropertiesMap(ip,confValue.folder(),readString(ip));
    }

    private Map<String, Properties> readPropertiesMap(InjectionPoint ip,String folder,String value) {

        Assert.notNull(value, "Null Value at IP " + ip.toString());
        Assert.isTrue(value.startsWith("{") && value.endsWith("}"), "@ConfValue of type Map<String,Properties> must be enclosed with {}");
        String values[] = value.substring(1, value.length() - 1).split(",");
        Map<String, Properties> map = new HashMap<>();
        for (int i = 1; i < values.length; i++) {
            Properties properties = readProperties(getEnvFile(folder,values[i]));
            Assert.isTrue(properties.getProperty(values[0])!=null,"Properties file %s does not contain key %s",values[i],values[0]);
            map.put(properties.getProperty(values[0]), properties);
        }
        return map;
    }

    @Produces
    @ConfValue
    public Boolean readBoolean(InjectionPoint ip) {
        String value = readString(ip);
        return value != null ? Boolean.valueOf(value) : null;
    }

    @Produces
    @ConfValue
    public Integer readInteger(InjectionPoint ip) {
        String value = readString(ip);
        return value != null ? Integer.valueOf(value) : null;
    }

    @Produces
    @ConfValue
    public Double readDouble(InjectionPoint ip) {
        String value = readString(ip);
        return value != null ? Double.valueOf(value) : null;
    }

    @Produces
    @ConfValue
    public Float readFloat(InjectionPoint ip) {
        String value = readString(ip);
        return value != null ? Float.valueOf(value) : null;
    }

    private Properties readProperties(String fileInClasspath) {
        if (!propertiesMap.containsKey(fileInClasspath)) {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileInClasspath);
            Assert.isTrue(is != null, "Could not locate properties from resource %s", fileInClasspath);

            try {
                Properties properties = new Properties();
                properties.load(is);
                propertiesMap.put(fileInClasspath,properties);
            } catch (IOException e) {
                throw new RuntimeException("Could not read properties from file " + fileInClasspath + " in classpath. " + e);
            }
        }
        return propertiesMap.get(fileInClasspath);
    }

}
