package za.co.spsi.toolkit.util;

import lombok.extern.slf4j.Slf4j;

import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/10/14.
 * Class for scheduling thread executions
 */
@Slf4j
public class ThreadExecutor extends Thread {

    public static Logger TAG = Logger.getLogger(ThreadExecutor.class.getName());

    private Runnable runnable;
    private long interval;
    private long lastExec = 0;
    private boolean closeOnException = false,close = false,logInfo = true,repeat = true;

    public ThreadExecutor(Runnable runnable,long interval) {
        this.runnable = runnable;
        this.interval = interval;
        this.repeat = interval > 0;
    }

    public ThreadExecutor setCloseOnException() {
        closeOnException = true;
        return this;
    }

    public ThreadExecutor delay() {
        lastExec = System.currentTimeMillis();
        return this;
    }

    public ThreadExecutor startProcessing() {
        start();
        return this;
    }

    @Override
    public void run() {
        while (!close) {
            if (System.currentTimeMillis() - lastExec > interval) {
                lastExec = System.currentTimeMillis();
                try {
                    if (logInfo) {
                        log.info("Executing %s",runnable.getClass().getName());
                    }
                    runnable.run();
                } catch (Throwable ex) {
                    log.warn(ex.getMessage(),ex);
                    close = close || closeOnException;
                }
            }
            if (!repeat) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
    }

    public ThreadExecutor setRepeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public void setClose() {
        this.close = true;
    }

    public ThreadExecutor setLogInfo(boolean logInfo) {
        this.logInfo = logInfo;
        return this;
    }
}

