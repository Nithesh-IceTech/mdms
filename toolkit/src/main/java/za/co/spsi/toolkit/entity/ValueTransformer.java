package za.co.spsi.toolkit.entity;

/**
 * Created by jaspervdbijl on 2017/01/21.
 */
public interface ValueTransformer<E> {
    E transform(E value);
}
