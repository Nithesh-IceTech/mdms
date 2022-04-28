package za.co.spsi.toolkit.crud.util;

import com.vaadin.ui.UI;

/**
 * Created by jaspervdbijl on 2017/05/23.
 */
public class AccessHelper {


    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static class AccessCallback implements Runnable {
        private int delay = 1000;
        private Runnable runnable;

        public AccessCallback(Runnable runnable, int delay) {
            this.runnable = runnable;
            this.delay = delay;
        }

        public int getDelay() {
            return delay;
        }

        @Override
        public void run() {
            UI.getCurrent().access(runnable);
            UI.getCurrent().push();
        }
    }

    public static void accessDelayed(AccessCallback runnable) {
        new Thread(() -> {
            sleep(runnable.getDelay());
            runnable.run();
        }).start();
    }

    public static void access(Runnable mainWork, Runnable onGui) {
        new Thread(() -> {
            mainWork.run();
            UI.getCurrent().access(onGui);
        }).start();
    }

    public static Thread accessThread(Runnable mainWork, Runnable onGui) {
        return new Thread(() -> {
            mainWork.run();
            UI.getCurrent().access(onGui);
        });
    }

    public static void callback(Runnable mainWork, Runnable onGui) {
        new Thread(() -> {
            mainWork.run();
            onGui.run();
        }).start();
    }
}
