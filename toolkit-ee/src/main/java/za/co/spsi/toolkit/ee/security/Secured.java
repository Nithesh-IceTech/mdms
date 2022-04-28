package za.co.spsi.toolkit.ee.security;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Secured {
    // roles required to access the resource
    String[] value() default {};
    // any agency
    boolean any() default true;
    // allow anonymous access, ignore roles
    boolean anonymous() default false;
}