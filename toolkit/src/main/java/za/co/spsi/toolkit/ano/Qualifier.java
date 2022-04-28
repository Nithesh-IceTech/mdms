package za.co.spsi.toolkit.ano;

import java.lang.annotation.*;

/**
 * Created by jaspervdb on 15/06/17.
 */
@Inherited
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD,
        ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {
    Role[] roles() default {};
}
