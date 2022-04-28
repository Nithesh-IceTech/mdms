package za.co.spsi.toolkit.entity;

import java.util.Iterator;

/**
 * Created by jaspervdb on 2/3/16.
 */
public interface DataSource<E extends Entity> extends Iterable<E>,Iterator<E> {
}
