package za.co.spsi.toolkit.ano;

import java.lang.annotation.*;

@Inherited
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD,
        ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface AgencyUIQualifier {
    UIField[] uiField() default {};
    UI[] ui() default {};
}
