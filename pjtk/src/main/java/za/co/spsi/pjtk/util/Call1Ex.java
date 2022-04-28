package za.co.spsi.pjtk.util;

public interface Call1Ex<T,R> {
    R call(T value) throws Exception;
}
