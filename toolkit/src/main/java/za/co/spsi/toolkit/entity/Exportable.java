package za.co.spsi.toolkit.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2012/11/27
 * Time: 10:14 AM
 * Annotation if applied to Entity fields, will export its data into json
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.TYPE})

public @interface Exportable {
    String name() default "";
    int size() default 0;
    String dateFormat() default "";
    boolean parent() default false;
    boolean forceExport() default false;
    boolean deleteAllReferences() default false;
    boolean dontExport() default false;
    String otherNames()[] default {};
}
