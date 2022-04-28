package za.co.spsi.toolkit.ee.properties;

import javax.decorator.Decorator;
import javax.ejb.Singleton;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaspervdb on 2016/10/21.
 */
public interface PropertiesAgency {
//
//    private static
//    private static Map<Thread,String> agencyMap = new HashMap<>();
//    private static Map<Object,String> alternateAgencyMap = new HashMap<>();
//
//    public static void setAgency(String agency) {
//        agencyMap.put(Thread.currentThread(),agency);
//        alternateAgencyMap.put(alter,agency);
//    }
//
//    public static String getAgency() {
//        String agency = agencyMap.get(Thread.currentThread());
//        // clear out old mappings
//        List<Thread> remove = new ArrayList<>();
//        agencyMap.keySet().stream().forEach(t -> {
//            if (!t.isAlive()) {
//                remove.add(t);
//            }
//        });
//        remove.stream().forEach(t -> agencyMap.remove(t));
//        agency == alternateAgencyMap.get()
//        return agency;
//    }
//
//    public static boolean isAgencySet() {
//        return getAgency() != null;
//    }
//
//    public static interface Call1 {
//
//    }

    public String getAgency();

    public default boolean isAgencySet() {
        return getAgency() != null;
    }
}
