package za.co.spsi.toolkit.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaspervdb on 2016/10/25.
 */
public class ReverseHashMap<K,V> extends HashMap<K,V>{

    private Map<V,K> reverse = new HashMap<>();

    @Override
    public V put(K key, V value) {
        Assert.isTrue(reverse.get(value)==null,"Reverse maps may not contain duplicate values %s",value);
        reverse.put(value,key);
        return super.put(key, value);
    }

    @Override
    public V remove(Object key) {
        reverse.remove(get(key));
        return super.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void clear() {
        super.clear();
        reverse.clear();
    }

    /**
     * reverse get
     * @param value
     * @return
     */
    public K rGet(V value) {
        return reverse.get(value);
    }
}
