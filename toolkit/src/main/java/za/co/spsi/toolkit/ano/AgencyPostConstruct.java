/*
 * FieldId.java
 *
 * Created on 12 November 2006, 11:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package za.co.spsi.toolkit.ano;

import java.lang.annotation.*;

/**
 *
 * @author Jasper van der Bijl
 * Anotation for Id Fields
 */
@Inherited
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD,
        ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgencyPostConstruct {

    int[] agency() default 0;

}
