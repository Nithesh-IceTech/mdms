package za.co.spsi.toolkit.ee.properties;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

/**
 * Created by jaspervdb on 2016/05/03.
 */
@Dependent
public class ConfigurationFactory {
    @Inject
    @Any
    Instance<Configuration> configurations;

    public String getEnvironment() {
        return System.getProperty("env");
    }

    @Produces
    public Configuration getConfiguration() {
        String env = getEnvironment()!=null?getEnvironment():"dev";
        Instance<Configuration> found = configurations.select(
                new EnvironmentQualifier(env));
        if (!found.isUnsatisfied() && !found.isAmbiguous()) {
            return found.get();
        }
        throw new RuntimeException("Error ...");
    }

    public static class EnvironmentQualifier
            extends AnnotationLiteral<Environment>
            implements Environment {
        private String value;

        public EnvironmentQualifier(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

}
