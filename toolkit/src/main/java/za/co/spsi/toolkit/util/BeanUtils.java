package za.co.spsi.toolkit.util;

/**
 * Created by jaspervdb on 2015/10/05.
 */

import za.co.spsi.toolkit.reflect.RefFields;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class BeanUtils {
    private static BeanUtils dashBeanUtils = new BeanUtils();

    private static final Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(
            Arrays.asList(Boolean.class, boolean.class,Character.class, char.class,
                    Byte.class, byte.class,Short.class, short.class, Integer.class, int.class,
                    Long.class, long.class,Float.class, float.class,Double.class,
                    String.class, Date.class, Timestamp.class,java.sql.Date.class));

    public static BeanUtils getInstance() {return dashBeanUtils;}

    public void copyProperties(Object dest, Object source, Set<Class<?>> primitiveTypes) throws NoSuchFieldException, IllegalAccessException {
        RefFields fields = new RefFields(dest.getClass());
        fields.copyValues(dest,source,new RefFields(source.getClass()),primitiveTypes);

    }

    public Object copyProperties(Object dest, Object source) throws NoSuchFieldException, IllegalAccessException {
        copyProperties(dest,source,primitiveTypes);
        return dest;
    }

}