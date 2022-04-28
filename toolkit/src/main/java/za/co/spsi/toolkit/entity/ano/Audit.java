package za.co.spsi.toolkit.entity.ano;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by francoism on 2016-02-23.
 */
@Retention(RUNTIME)
public @interface Audit {
    // audit for background services
    boolean services() default true;
    boolean exclude() default false;
    boolean reviewable() default false;
    String uidField() default "userId";

    String createUidField() default "createUserId";

}
