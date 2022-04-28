package za.co.spsi.pjtk.util;

public interface Call3<T,E,F,R> {
    R call(T value1, E value2,F value3);
}