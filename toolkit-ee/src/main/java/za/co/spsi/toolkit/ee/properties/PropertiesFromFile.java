package za.co.spsi.toolkit.ee.properties;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jaspervdb on 2016/05/03.
 */
@Qualifier
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertiesFromFile {

    /**
     * This value must be a properties file in the classpath.
     */
    @Nonbinding
    String value() default "config.properties";
}