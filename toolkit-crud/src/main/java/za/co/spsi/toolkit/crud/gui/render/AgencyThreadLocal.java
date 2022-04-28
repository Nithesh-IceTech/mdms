package za.co.spsi.toolkit.crud.gui.render;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/07/01
 * Time: 10:23 AM
 * ToolkitConstants relevant to the web app
 */
public class AgencyThreadLocal {

    private static Map<Thread,String> agencyMap = new HashMap<>();
    private static Map<Thread,Long> agencyTimeMap = new HashMap<>();

    private static void cleanupCache() {
        // clean out all the old requests
        List<Thread> remove = new ArrayList<>();
        for (Thread thread : agencyTimeMap.keySet()) {
            if (System.currentTimeMillis() - agencyTimeMap.get(thread) > DateUtils.MILLIS_PER_MINUTE) {
                remove.add(thread);
            }
        }
        for (Thread thread : remove) {
            removeAgency(thread);
        }
    }

    public static void setAgency(String agency) {
        cleanupCache();
        agencyMap.put(Thread.currentThread(),agency);
        agencyTimeMap.put(Thread.currentThread(),System.currentTimeMillis());
    }

    public static String getAgency() {
        String agency = agencyMap.get(Thread.currentThread());
        return agency;
    }

    public static String removeAgency(Thread thread) {
        agencyTimeMap.remove(thread);
        return agencyMap.remove(thread);
    }

    public static String removeAgency() {
        return agencyMap.remove(Thread.currentThread());
    }

}
