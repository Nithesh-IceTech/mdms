package za.co.spsi.toolkit.crud.util.broadcast;

import lombok.Synchronized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaspervdbijl on 2017/07/05.
 */
public class BroadcastRegister {

    private static List<BroadcastListener> broadcastListenerList = new ArrayList<>();
    private static Map<BroadcastListener, Integer> agencyMap = new HashMap<>();

    @Synchronized()
    public static void register(BroadcastListener listener) {
        if (!broadcastListenerList.contains(listener)) {
            broadcastListenerList.add(listener);
        }
    }

    @Synchronized()
    public static void unregister(BroadcastListener listener) {
        agencyMap.remove(listener);
        broadcastListenerList.remove(listener);
    }

    @Synchronized()
    public static void map(BroadcastListener listener,Integer agency) {
        agencyMap.put(listener,agency);
    }

    @Synchronized()
    public static void broadcast(String message) {
        broadcastListenerList.stream().forEach(l -> l.receiveBroadcast(message));
    }

    @Synchronized()
    public static void broadcast(Integer agency,String message) {
        broadcastListenerList.stream().filter(a -> agencyMap.containsKey(a) && agencyMap.get(a).equals(agency)).
                forEach(l -> l.receiveBroadcast(message));
    }

}
