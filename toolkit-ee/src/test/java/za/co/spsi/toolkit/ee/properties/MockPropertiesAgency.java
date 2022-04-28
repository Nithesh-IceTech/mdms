package za.co.spsi.toolkit.ee.properties;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaspervdb on 2016/10/21.
 */
@Dependent
@Alternative
public class MockPropertiesAgency implements PropertiesAgency {
    //
    private static Map<Thread, String> agencyMap = new HashMap<>();

    public static void setAgency(String agency) {
        agencyMap.put(Thread.currentThread(), agency);
    }

    public String getAgency() {
        String agency = agencyMap.get(Thread.currentThread());
        // clear out old mappings
        List<Thread> remove = new ArrayList<>();
        agencyMap.keySet().stream().forEach(t -> {
            if (!t.isAlive()) {
                remove.add(t);
            }
        });
        remove.stream().forEach(t -> agencyMap.remove(t));
        return agency;
    }


}
