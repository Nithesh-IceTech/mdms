package za.co.spsi.toolkit.ano;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

public @interface Permission{
    boolean read() default true;
    boolean write() default false;
    boolean create() default false;
    boolean delete() default false;
    boolean search() default true;
}
