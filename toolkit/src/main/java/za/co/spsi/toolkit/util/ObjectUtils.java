package za.co.spsi.toolkit.util;

import za.co.spsi.toolkit.reflect.RefFields;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 2/16/16.
 */
public class ObjectUtils {

    public static boolean equals(Object object1, Object object2) {
        return object1 == object2?true:object1 != null && object2 != null?
                equalsNonNullObject(object1,object2):false;
    }

    private static boolean equalsNonNullObject(Object value1,Object value2) {
        if (value1 instanceof BigDecimal) {
            return ((BigDecimal)value1).doubleValue()==((BigDecimal)value2).doubleValue();
        } else if (value1 instanceof BigDecimal) {
            return ((BigInteger)value1).longValue()==((BigInteger)value2).longValue();
        } else {
            return value1.equals(value2);
        }
    }

    public static String convertStackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static String convertStackTraceToString(Throwable e,int length) {
        String ex = convertStackTraceToString(e);
        return !StringUtils.isEmpty(ex) && ex.length() > length?ex.substring(0,length):ex;
    }


    public static byte[] serializeObject(Object o) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            try {
                oos.writeObject(o);
            } finally {
                oos.close();
            }
            return bos.toByteArray();
        } finally {
            bos.close();
        }
    }

    public static Object deSerializeObject(InputStream iis) throws IOException, ClassNotFoundException {
        ObjectInput ois = new ObjectInputStream(iis);
        try {
            return ois.readObject();
        } finally {
            ois.close();
        }
    }

    public static Type[] getGenericTypeArgumentsForType(Object value,Class type) {
        for (Type t : value.getClass().getGenericInterfaces()) {
            if (t instanceof ParameterizedType && ((ParameterizedType)t).getRawType().equals(type)) {
                return ((ParameterizedType)t).getActualTypeArguments();
            }
        }
        return null;
    }

    /**
     * @return the declared annotation type of this class
     */
    public static Class getGenericType(Class type) {
        return (Class) ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * @return the declared annotation type of this class
     */
    public static Class getGenericType(Field field) {
        return (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
    }

    public static Class getGenericType(Method method) {
        return (Class) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
    }

    public static boolean hasGenericType(Field field) {
        return field.getGenericType() != null && field.getGenericType() instanceof ParameterizedType &&
                ((ParameterizedType) field.getGenericType()).getActualTypeArguments().length > 0;
    }



    /**
     * convert values between object types
     * @param ref
     * @param dstType
     * @param value
     * @return
     */
    public static Object getConvertedValue(Object ref, Class dstType, Object value) {
        try {
            if (value == null) {
                return null;
            }
            if (Long.class.equals(dstType) && value instanceof Date) {
                return ((Date) value).getTime();
            }
            if (Date.class.equals(dstType) && value instanceof Long) {
                return new Date((Long) value);
            }
            if (Timestamp.class.equals(dstType) && value instanceof Date) {
                return new Timestamp(((Date) value).getTime());
            }
            if (!String.class.equals(dstType)) {
                Method valueOf = dstType.getMethod("valueOf", String.class);
                return valueOf.invoke(ref, value + "");
            }
            return value;
        } catch (Exception  e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Method> findMethodsByAnnotation(Class typeClass, Class<? extends Annotation> anoClass) {
        List<Method> methods = new ArrayList<>();
        for (Method m : typeClass.getMethods()) {
            if (m.getAnnotation(anoClass) != null) {
                methods.add(m);
            }
        }
        return methods;
    }

    public static List<Field> getFieldWithAnnotation(Class objectClass, Class<? extends Annotation> aClass) {
        List<java.lang.reflect.Field> fields = new ArrayList<>();
        for (java.lang.reflect.Field field : objectClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(aClass) != null) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static List<Field> getFieldOfType(Class objectClass, Class type) {
        List<java.lang.reflect.Field> fields = new ArrayList<>();
        for (java.lang.reflect.Field field : objectClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (type.isAssignableFrom(field.getType())) {
                fields.add(field);
            }
        }
        return fields;
    }

    public static List getFieldValues(List<Field> fields,Object source) {
        try {
            List values = new ArrayList();
            for (Field field : fields) {
                values.add(field.get(source));
            }
            return values;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * assert that all fields in source are in the dst
     * @param x
     * @param y
     * @param <T>
     * @return
     */
    public static <T> T assertXinY(T x,Object y) {
        Assert.isTrue(new RefFields(y.getClass()).getNames()
                .containsAll(new RefFields(x.getClass()).getNames()),"Field mismatch");
        return x;
    }

    public static <T> T copy(T dst,Object source) {
        try {
            new RefFields(dst.getClass()).copyValues(dst,source,new RefFields(source.getClass()));
            return dst;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }



}
