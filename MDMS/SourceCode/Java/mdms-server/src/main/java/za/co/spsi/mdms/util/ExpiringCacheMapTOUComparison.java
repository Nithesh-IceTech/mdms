package za.co.spsi.mdms.util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class ExpiringCacheMapTOUComparison<K,V> extends HashMap<K,V> {

    private Map<K,Long> expiryMap =new HashMap<>();
    private long expiryTime;
    public ExpiringCacheMapTOUComparison(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    @Override
    public synchronized V get(Object key) {
        V value = super.get(key);
        return value;
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        purgeExpired();
        return super.containsKey(key);
    }

    @Override
    public V put(K key, V value) {
        expiryMap.put(key, System.currentTimeMillis() + expiryTime);
        return super.put(key, value);
    }

    private void purgeExpired() {
        List expire = expiryMap.keySet().stream()
                .filter(k -> expiryMap.get(k) < System.currentTimeMillis())
                .collect(Collectors.toCollection(ArrayList::new));
        expire.stream().forEach(k -> {
            expiryMap.remove(k);
            remove(k);
        });
    }
}