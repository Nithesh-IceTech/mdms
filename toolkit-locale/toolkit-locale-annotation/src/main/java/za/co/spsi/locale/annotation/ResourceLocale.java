package za.co.spsi.locale.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by jaspervdb on 15/06/17.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface ResourceLocale {
    public ResourceSet[] resources() default {};
    public String[] locales() default {};
    public String[] context() default {};
}
