package za.co.spsi.toolkit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FiLoCacheMap<E, F> extends HashMap<E, F> {

    private int size;
    private List<E> orderList = new ArrayList<>();

    public FiLoCacheMap(int size) {
        this.size = size;
    }

    @Override
    public F put(E key, F value) {
        if (size() >= size) {
            remove(orderList.get(0));
            orderList.remove(0);
        }
        orderList.remove(key);
        orderList.add(key);
        return super.put(key,value);
    }


}
