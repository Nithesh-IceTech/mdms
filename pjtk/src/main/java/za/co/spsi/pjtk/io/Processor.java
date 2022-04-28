package za.co.spsi.pjtk.io;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import za.co.spsi.pjtk.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdbijl on 2017/06/15.
 */
@Slf4j
public class Processor implements Runnable {

    enum Mode {ONCE,REPEAT}

    private Logger TAG;
    private boolean alive = true;
    private long delay = 0,activeTime = 100, passiveTime = TimeUnit.SECONDS.toMillis(30), activeTimeThreshold = TimeUnit.SECONDS.toMillis(15),
            sleepOnError = TimeUnit.MINUTES.toMillis(1);
    private Thread myThread;
    private Runnable runnable;
    private Mode mode = Mode.ONCE;

    private boolean stopOnError = false;

    public Processor(Class sourceClass) {
        TAG = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(sourceClass.getName());
    }

    public Processor setActiveTime(long activeTime) {
        this.activeTime = activeTime;
        return this;
    }

    public Processor setPassiveTime(long passiveTime) {
        this.passiveTime = passiveTime;
        return this;
    }

    public Processor setActiveTimeThreshold(long activeTimeThreshold) {
        this.activeTimeThreshold = activeTimeThreshold;
        return this;
    }

    public Processor start(Runnable runnable) {
        Assert.isTrue(myThread == null || !myThread.isAlive(),"Thread is still running");
        alive = true;
        this.runnable = runnable;
        myThread = new Thread(this);
        myThread.start();
        return this;
    }

    public Processor repeat(Runnable runnable) {
        mode = Mode.REPEAT;
        start(runnable);
        return this;
    }

    public Processor sleepOnErrorMin(int mins) {
        sleepOnError = TimeUnit.MINUTES.toMillis(mins);
        return this;
    }

    public Processor delay(int mins) {
        this.delay = TimeUnit.MINUTES.toMillis(mins);
        return this;
    }

    public Processor passiveMins(int mins) {
        this.passiveTime = TimeUnit.MINUTES.toMillis(mins);
        return this;
    }

    public Processor minutes(int mins) {
        activeTime = TimeUnit.MINUTES.toMillis(mins);
        passiveTime = activeTime;
        sleepOnError = sleepOnError < passiveTime ? passiveTime : sleepOnError;
        return this;
    }

    public Processor seconds(int seconds) {
        activeTime = TimeUnit.SECONDS.toMillis(seconds);
        passiveTime = activeTime;
        return this;
    }

    public Processor milli(int milli) {
        activeTime = milli;
        passiveTime = activeTime;
        return this;
    }

    public boolean isAlive() {
        return myThread != null && myThread.isAlive();
    }

    private void sleepNoError(long sleepTime) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    private void sleepGlobalDelay() {
        try {
            while (ProcessorService.isGlobalDelay())
            Thread.sleep(passiveTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sleepGlobalDelay();
        while (alive) {
            sleep(delay);
            try {
                long time = System.currentTimeMillis();
                runnable.run();
                time = System.currentTimeMillis() - time;
                sleep(time > activeTimeThreshold ? activeTime : passiveTime);
            } catch (Exception ex) {
                log.warn( ex.getMessage(), ex);
                sleep(sleepOnError);
            }
            alive = alive && mode != Mode.ONCE;
        }
    }

    public void stopNonBlocking() {
        alive = false;
    }

    public void stopBlocking() {
        alive = false;
        try {
            while (myThread.isAlive()) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            log.info("Closed ");
        }
    }

}
