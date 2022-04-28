package za.co.spsi.toolkit.ee.util;

import javax.servlet.ServletContext;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ettiennelr
 * Date: 2013/08/21
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class VersionUtil {


    /**
     * @param is eg /META-INF/maven/za.co.spsi/gdm-webapp/pom.properties
     * @return
     */

    public static String getVersion(InputStream is) {

        Properties prop = new Properties();

        try {
            prop.load(is);
        } catch (Exception e) {
            return "UNKNOWN";

        } finally {
            try {
                is.close();
            } catch (Exception ex) {/*ignore*/}
        }

        return prop.getProperty("version");
    }

    public static String getVersion(ServletContext servletContext, String path) {
        Set<String> paths = servletContext.getResourcePaths(path);
        if (paths != null) {
            for (String s : paths) {
                if (s.endsWith("pom.properties")) {
                    return getVersion(servletContext.getResourceAsStream(s));
                } else {
                    String version = getVersion(servletContext, s);
                    if (version != null) {
                        return version;
                    }
                }
            }
        }
        return null;
    }


}
