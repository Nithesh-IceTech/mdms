package za.co.spsi.pjtk.io;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdbijl on 2017/07/06.
 */
public class ProcessorService {

    private List<Processor> processors;

    public ProcessorService() {
       processors = new ArrayList<>();
    }

    private static boolean GLOBAL_DELAY = false;

    public static boolean isGlobalDelay() {
        return GLOBAL_DELAY;
    }

    public static void setGlobalDelay(boolean globalDelay) {
        GLOBAL_DELAY = globalDelay;
    }

    public Processor getProcessor() {
        processors.add(new Processor(getClass()));
        return processors.get(processors.size()-1);
    }

    public void stopNonBlocking() {
        for (Processor p : processors) {
            p.stopNonBlocking();
        }
    }
}
