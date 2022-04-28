package za.co.spsi.pjtk.util;

public interface Ret<T> {
    T call() throws Exception;
}
