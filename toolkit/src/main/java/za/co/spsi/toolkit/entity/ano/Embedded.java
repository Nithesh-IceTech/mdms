package za.co.spsi.toolkit.entity.ano;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by francoism on 2016-02-23.
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface Embedded {
}
