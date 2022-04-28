package za.co.spsi.toolkit.util;

public interface Call1Ex<T,R> {
    R call(T value) throws Exception;
}
