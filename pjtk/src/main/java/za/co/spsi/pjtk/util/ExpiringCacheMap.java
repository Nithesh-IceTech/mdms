package za.co.spsi.pjtk.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaspervdb on 2016/06/13.
 */
public class ExpiringCacheMap<K,V> extends HashMap<K,V> {

    private Map<K,Long> expiryMap =new HashMap<>();
    private long expiryTime;

    public ExpiringCacheMap(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    @Override
    public V get(Object key) {
        V value = super.get(key);
        if (expiryMap.containsKey(key) && expiryMap.get(key) < System.currentTimeMillis()) {
            remove(key);
            expiryMap.remove(key);
        }
        return value;
    }

    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(key);
    }

    @Override
    public V put(K key, V value) {
        expiryMap.put(key, System.currentTimeMillis() + expiryTime);
        return super.put(key, value);
    }
}
