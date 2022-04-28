package za.co.spsi.toolkit.util;

public interface Call2<T,E,R> {
    R call(T value1,E value2);
}