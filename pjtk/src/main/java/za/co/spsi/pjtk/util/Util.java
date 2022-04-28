package za.co.spsi.pjtk.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Util {

    public static <T> T getNonNull(T value,T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static boolean getNonNull(Boolean value) {
        return value == null ? false : value;
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
    public static <R> R call(Ret<R> call) {
        return call.call();
    }

    @SneakyThrows
    public static <R> R call(RetEx<R> call) {
        return call.call();
    }

    @SneakyThrows
    public static <R> R callX(RetEx<R> call) {
        return call.call();
    }

    public static <T, R> R callHandle(Call1Ex<T, R> call, Call1<Exception, R> callEx, T v1) {
        try {
            return call.call(v1);
        } catch (Exception ex) {
            return callEx.call(ex);
        }
    }

    public static URL getResource(String value) {
        return Thread.currentThread().getContextClassLoader().getResource(value);
    }

    public static InputStream getResourceAsStream(String value) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(value);
    }

    @SneakyThrows
    public static <T> void handle(Call0Ex<T> call, T v1) {
        call.call(v1);
    }

    @SneakyThrows
    public static void handle(CallEx call) {
        call.call();
    }

    @SneakyThrows
    public static <T> T handleAndRet(Call1Ex<Void,T> call) {
        return call.call(null);
    }

    public static <T> T handleAndRet(Call1Ex<Void,T> call, Call0<Exception> callEx) {
        try {
            return call.call(null);
        } catch (Exception ex) {
            callEx.call(ex);
            return null;
        }
    }

    public static <T> void handle(Call0Ex<T> call, Call0<Exception> callEx, T v1) {
        try {
            call.call(v1);
        } catch (Exception ex) {
            callEx.call(ex);
        }
    }
    
    @SneakyThrows
    public static <T> void handle(Call0Ex<T> call, Call1VEx<Exception, T> call1VEx, T v1) {
        try {
            call.call(v1);
        } catch (Exception ex) {
            call1VEx.call(ex, v1);
        }
    }
    
    @SneakyThrows
    public static <T, R> void handle(Call1VEx<T, R> call, Call1VEx<Exception, T> call1VEx, T v1, R v2) {
        try {
            call.call(v1, v2);
        } catch (Exception ex) {
            call1VEx.call(ex, v1);
        }
    }

    public static boolean compareAnyOrder(List n1, List n2) {
        return !(n1.stream().filter(f -> !n2.contains(f)).findAny().isPresent()
                || n2.stream().filter(f -> !n1.contains(f)).findAny().isPresent());
    }

    /**
     * cal
     *
     * @param call
     */
    @SneakyThrows
    public static void handle(CallEx call, Call0Ex<Exception> exCall) {
        try {
            call.call();
        } catch (Exception ex) {
            exCall.call(ex);
        }
    }

    @SneakyThrows
    public static void executeIf(boolean condition, CallEx call) {
        if (condition) {
            call.call();
        }
    }

    public static String convertStackTraceToString(Throwable e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    public static String convertStackTraceToString(Throwable e, int length) {
        String ex = convertStackTraceToString(e);
        return !StringUtils.isEmpty(ex) && ex.length() > length ? ex.substring(0, length) : ex;
    }

    /**
     * substring in reverse
     *
     * @param value
     * @param start
     * @param negOffset
     * @return
     */
    public static String negSubstring(String value, int start, int negOffset) {
        return value.substring(start, value.length() - negOffset);
    }

    public static boolean containsAll(List<String> s1,List<String> s2) {
        return !s2.stream().filter(s -> !s1.contains(s)).findAny().isPresent();
    }

    public static boolean containsAllIgnoreCase(List<String> s1,List<String> s2) {
        return containsAll(s1.stream().map(s -> s.toUpperCase()).collect(Collectors.toList()),s2.stream()
                .map(s -> s.toUpperCase()).collect(Collectors.toList()));
    }

    public static boolean doesClassExist(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ce) {
            return false;
        }
    }


    public static void main(String args[]) {
        System.out.println(containsAllIgnoreCase(Arrays.asList("One","Two"),Arrays.asList("One","Two2")));
    }

}
