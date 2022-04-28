package za.co.spsi.toolkit.ano;

import za.co.spsi.toolkit.util.MaskId;

import java.lang.annotation.*;

/**
 * Created by jaspervdb on 15/06/17.
 */
@Inherited
@Target({ ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.FIELD,
        ElementType.METHOD, ElementType.CONSTRUCTOR })
@Retention(RetentionPolicy.RUNTIME)
public @interface UIField {

    // Override the field's default name
    int[] agency() default 0;
    String captionStr()default "";
    int captionId() default 0;

    String name() default "";

    boolean enabled() default true;

    // The minimum value (only applicable to strings)
    int min() default 0;

    // The maximum value (only applicable to strings)
    int max() default -1;

    // The maximum value (only applicable to strings)
    long maxValue() default -1;

    // only allow single line input
    boolean singeLine() default true;

    // Are null fields allowed. Defaults to false
    boolean mandatory() default false;

    boolean mandatoryExternal() default false;

    // Default value
    String defaultValue() default "";

    // Android input type flag. for example android.text.InputType.TYPE_CLASS_TEXT
    int inputType() default -1;

    // Regular expression mask
    MaskId mask() default MaskId.ANY;

    int rows() default 1;

    // if enabled then the setOnClickListener will be set for the field and an Input Dialog opened on click
    boolean inputAsDialog() default true;

    int visibility() default 0;

    String format() default "";

    int gravity() default 0;

    int sqlId() default 0;

    String setDefault() default "";

    boolean writeOnce() default false;

    boolean uppercase() default true;

    boolean visible() default true;

    boolean immediate() default false;

    String regex() default "";

    // this field value may be used as a caption
    boolean isForCaption() default false;

    float gridWeight() default 1;

    boolean clearInputDialog() default false;

}
