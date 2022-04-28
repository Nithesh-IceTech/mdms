/*
 * FieldId.java
 *
 * Created on 12 November 2006, 11:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.spsi.toolkit.db.ano;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Jasper van der Bijl
 * Anotation for Id Fields
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface Column {
    String name() default "";
    String defaultValue() default "";
    boolean notNull() default false;
    // only applied  for field types strings
    int size() default -1;
    int decimalPlaces() default -1;
    boolean clob() default false;
    boolean autoCrop() default false;
    boolean toNumber() default false;

    // alter column for size differences
    boolean maintainStrict() default false;
}
