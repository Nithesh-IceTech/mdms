package za.co.spsi.mdms.common.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import za.co.spsi.toolkit.util.ExpiringCacheMap;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdbijl on 2017/03/07.
 */
public class MeterDataResultExpiringMap extends ExpiringCacheMap<String, Map<MeterDataResultExpiringMap.Key,MeterResultDataArray>> {

    public MeterDataResultExpiringMap() {
        super(TimeUnit.SECONDS.toMillis(30));
    }

    public MeterResultDataArray get(String serialN, java.util.Date from, java.util.Date to,int tmzOffset) {
        Map<MeterDataResultExpiringMap.Key,MeterResultDataArray> data = super.get(serialN);
        if (data != null) {
            // check if it fits inside
            Optional<Key> value = data.keySet().stream()
                    .filter(d -> d.contains(from,to))
                    .findAny();
            return value.isPresent() && data.get(value.get()) != null
                    ?data.get(value.get()).subset(from,to,tmzOffset)
                    :null;
        }
        return null;
    }

    public boolean containsKey(String serialN, java.util.Date from, java.util.Date to,int tmzOffset) {
        return super.containsKey(serialN) && get(serialN,from,to,tmzOffset) != null;
    }

    public MeterResultDataArray put(String serialN, java.util.Date from, java.util.Date to, MeterResultDataArray data) {
        Map<Key,MeterResultDataArray> map = super.containsKey(serialN)?super.get(serialN):new HashMap<>();
        map.put(new Key(from,to),data);
        super.put(serialN,map);
        return data;
    }

    @Data @AllArgsConstructor @EqualsAndHashCode
    public static class Key {
        private Date from,to;

        public boolean contains(Date p_from,Date p_to) {
            return from.compareTo(p_from) <= 0 && to.compareTo(p_to) >= 0;
        }
    }

}