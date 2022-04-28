package za.co.spsi.toolkit.crud.gui.ano;

import za.co.spsi.locale.annotation.ResourceSet;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/08/30
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface UIGroup {
    UILayout[] layout() default {};
    int column() default 0;
}
