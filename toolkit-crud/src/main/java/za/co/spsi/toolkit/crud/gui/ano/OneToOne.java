/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * This class will annotate the entity references, either as the main (there can only be one), or as a reference
 * If the entity is a reference then there should be a Field Refernce defined with it in the layout
 */

package za.co.spsi.toolkit.crud.gui.ano;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface OneToOne {
}
