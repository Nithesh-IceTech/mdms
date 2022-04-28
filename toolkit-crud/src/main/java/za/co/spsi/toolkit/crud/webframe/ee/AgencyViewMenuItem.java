package za.co.spsi.toolkit.crud.webframe.ee;

/**
 * Created by jaspervdb on 2016/06/09.
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AgencyViewMenuItem {

    public String[] forAgency();
    public String caption();

   }
