package za.co.spsi.pjtk.util;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdb on 2016/06/13.
 */
public class ExpiringCacheObject<K> {

    private WeakReference<K> reference = new WeakReference<K>(null);
    private long expiryTime,nextExpiration;

    public ExpiringCacheObject(long expiryTime) {
        this.expiryTime = expiryTime;
    }

    public boolean isPresent() {
        if (nextExpiration < System.currentTimeMillis()) {
            reference.clear();
        }
        return reference.get() != null;
    }

    public boolean peek() {
        return reference.get() != null;
    }

    public K get() {
        if (peek()) {
            return reference.get();
        } else {
            return null;
        }
    }

    public K set(K value) {
        nextExpiration = System.currentTimeMillis() + expiryTime;
        reference = new WeakReference<K>(value);
        return value;
    }

    public void clear() {
        reference.clear();
    }

    public static void main(String args[]) throws Exception {
        ExpiringCacheObject expiringCacheObject = new ExpiringCacheObject(TimeUnit.SECONDS.toMillis(1));
        System.out.println(expiringCacheObject.get());
        expiringCacheObject.set("hello");
        System.out.println(expiringCacheObject.get());
        Thread.sleep(1001);
        System.out.println(expiringCacheObject.get());
    }
}
