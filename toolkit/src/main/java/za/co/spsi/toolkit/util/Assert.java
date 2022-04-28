package za.co.spsi.toolkit.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Created by jaspervdb on 2/24/16.
 */
@Slf4j
public class Assert {

    public static void isTrue(Class<? extends RuntimeException > exClass,boolean value,String msg,Object ... params) {
        if (!value) {
            try {
                msg = params != null?String.format(msg,params):msg;
                throw exClass.getConstructor(String.class).newInstance(msg);
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SneakyThrows
    public static void isTrue(boolean value,String msg,Call1Ex<String,RuntimeException> call) {
        if (!value) {
            RuntimeException ex = call.call(msg);
            log.warn(ex.getMessage(),ex);
            throw call.call(msg);
        }
    }

    public static void isTrue(boolean value,String msg) {
        if (!value) {
             throw new RuntimeException(msg);
        }
    }

    public static void isTrue(boolean value,Runnable callback) {
        if (!value) {
            callback.run();
        }
    }

    public static void isTrue(boolean condition,String msg,Object ... params) {
        if (!condition) {
            throw new RuntimeException(String.format(msg,params));
        }
    }

    public static void notNull(Class<? extends RuntimeException > exClass,Object value,String msg) {
        isTrue(exClass,value != null,msg);
    }

    public static void notNull(Object value,String msg) {
        isTrue(value != null,msg);
    }

    public static void notNull(Object value,String msg,Object ... params) {
        isTrue(value != null,msg,params);
    }

    public static void notEmpty(Collection value, String msg,Object ... params) {
        isTrue(value != null && !value.isEmpty(),msg,params);
    }

    public static void notEmpty(Collection value, String msg) {
        notEmpty(value,msg,null);
    }

}
