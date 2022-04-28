package za.co.spsi.toolkit.ano;

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
public @interface UI {

    public static final int VISIBLE = 0x00000000;
    public static final int INVISIBLE = 0x00000004;
    public static final int GONE = 0x00000008;
    public static final int REMOVED = -1;

    int[] agency() default 0;

    String caption() default "";
    // load a resource Id
    String captionId() default "";
    int columns() default 0;

    // build form fields with fields placed from left to right, top to bottom. if false will be placed top to bottom, left to right
    boolean leftToRight() default false;
    int rows() default 1;
    String width() default "100%";
    String height() default "";

    // disable this component
    boolean enabled() default true;
    int visibility() default 0;


}
