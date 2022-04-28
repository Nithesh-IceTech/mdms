package za.co.spsi.toolkit.crud.webframe.ee;

/**
 * Created by jaspervdb on 2016/06/09.
 */
import com.vaadin.server.FontAwesome;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ViewMenuItem {

    public AgencyViewMenuItem[] agencyCaption() default {};

    public static final int END = Integer.MAX_VALUE;
    public static final int BEGINNING = 0;
    public static final int DEFAULT = 1000;

    public boolean enabled() default true;

    public String value();
    public String groupName() default "";

    public int order() default DEFAULT;

    public FontAwesome icon() default FontAwesome.FILE;

}
