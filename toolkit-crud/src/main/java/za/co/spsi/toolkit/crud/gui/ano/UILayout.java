package za.co.spsi.toolkit.crud.gui.ano;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/08/30
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RUNTIME)
public @interface UILayout {
    int column() default 0;
    int minWidth() default 0;
}
