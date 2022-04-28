package za.co.spsi.toolkit.util;

public interface Call2Ex<T,R,F> {
    F call(T v1,R v2) throws Exception;
}
