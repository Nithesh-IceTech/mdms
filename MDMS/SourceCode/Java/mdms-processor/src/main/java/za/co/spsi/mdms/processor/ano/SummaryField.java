/*
 * FieldId.java
 *
 * Created on 12 November 2006, 11:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.spsi.mdms.processor.ano;

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
public @interface SummaryField {

    // if this field is calculated from other fields
    String posField() default "";
    String negField() default "";

    boolean exportTotal() default true;
    boolean tou() default false;
}
