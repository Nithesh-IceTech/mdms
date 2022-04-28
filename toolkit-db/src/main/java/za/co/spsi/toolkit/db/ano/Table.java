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
import java.time.Duration;

/**
 *
 * @author Jasper van der Bijl
 * Anotation for Id Fields
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    // auto maintain table, version ( -1 disable )
    int version() default -1;

    boolean maintainStrict() default true;

    boolean maintainFK() default true;
    boolean allowFkDrop() default false;

    boolean maintainIndex() default true;
    boolean allowIndexDrop() default false;

    // set the table's short notation for constraints
    String shortName() default "";

    // delete old data
    boolean deleteOldRecords() default false;
    String deleteRecordTimeField() default "";

    String maintenanceSql()[] default {};

}
