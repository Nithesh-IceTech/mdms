package za.co.spsi.toolkit.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by jaspervdb on 4/21/16.
 */
@Slf4j
public class Util {

    public static Object getConvertedValue(Object ref, Class dstType, Object value) {
        try {
            if (value == null || value.toString().isEmpty()) {
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
            if (Time.class.equals(dstType) && value instanceof Date) {
                return new Time(((Date) value).getTime());
            }
            if (BigDecimal.class.equals(dstType) && value.getClass().equals(String.class)) {
                return new BigDecimal(((String) value));
            }
            if (BigDecimal.class.equals(dstType) && value.getClass().equals(Double.class)) {
                return new BigDecimal(value + "");
            }
            if (BigInteger.class.equals(dstType) && value.getClass().equals(String.class)) {
                return new BigInteger(value + "");
            }
            if (value.getClass().equals(String.class) && !String.class.equals(dstType)) {
                Method valueOf = dstType.getMethod("valueOf", String.class);
                return valueOf.invoke(ref, value);
            }
            return value;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    public static String getExceptionAsString(Throwable error, int length) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        error.printStackTrace(pw);
        String err = sw.toString();
        return err.length() > length ? err.substring(0, length) : err;
    }


    public static interface Filter {
        boolean filter(Object value);
    }

    /**
     * Retrieve a list of fields who are annotated with annotation and
     * the annotation method methodName will yield the specified result
     * For example persistance.getFieldsWithAnnotationWithMethodSignature(persistance,Column.class,id,true)
     * will return the field who is annotated by Column and who's id() method will return true
     *
     * @param annotation the required annotation
     * @param methodName the annotation's method
     * @return boolean
     * @throws RuntimeException
     */
    public static boolean isAnnotationSetWithMethodSignatureUseFilter(Annotation annotation, String methodName, Filter filter) {

        try {
            if (annotation != null) {
                Method method = annotation.getClass().getMethod(methodName);

                Object methodResult = method.invoke(annotation);
                if (filter.filter(methodResult)) {
                    return true;
                }
            }
            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean isAnnotationSetWithMethodSignature(Annotation annotation, String methodName, final Object result) {
        return isAnnotationSetWithMethodSignatureUseFilter(annotation, methodName, new Filter() {
            @Override
            public boolean filter(Object methodResult) {
                return ObjectUtils.equals(methodResult, result);
            }
        });

    }

    public static boolean isMethodOverloaded(Class currentClass, Object source, String name, Class... params) {
        try {
            return !source.getClass().getMethod(name, params).getDeclaringClass().equals(currentClass);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<Class<? extends T>> getSubTypesOf(String path, Class<T> type) {
        List<Class<? extends T>> values = new ArrayList<>();
        values.addAll(new Reflections(path).getSubTypesOf(type));
        return values;
    }

    public static <T> List<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return getSubTypesOf("", type);
    }

    public static <T extends Annotation> Set<? extends Class> getTypesAnnotatedWith(String path, Class<T> type) {
        return new Reflections(path).getTypesAnnotatedWith(type);
    }

    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static List<String> readLines(String data) {
        try {
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(new ByteArrayInputStream(data.getBytes())));
            List<String> lines = new ArrayList<>();
            for (String line = reader.readLine(); line != null; reader.readLine()) {
                lines.add(line);
            }
            return lines;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    @SneakyThrows
    public static <T, E, F> F call(Call2Ex<T, E, F> call, T v1, E v2) {
        return call.call(v1, v2);
    }

    @SneakyThrows
    public static <T, R> R call(Call1Ex<T, R> call, T v1) {
        return call.call(v1);
    }

    @SneakyThrows
    public static <T> void handle(Call0Ex<T> call, T v1) {
        call.call(v1);
    }

    @SneakyThrows
    public static void handle(CallEx call) {
        call.call();
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        return date != null?LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault()):null;
    }

    public static LocalDate getLocalDate(Date date) {
        LocalDateTime dateTime = getLocalDateTime(date);
        return dateTime != null?dateTime.toLocalDate():null;
    }
}
