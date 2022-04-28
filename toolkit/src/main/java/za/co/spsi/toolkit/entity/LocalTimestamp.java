package za.co.spsi.toolkit.entity;

import java.sql.Timestamp;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdbijl on 2017/03/20.
 */
public class LocalTimestamp extends Timestamp {

    public LocalTimestamp() {
        this(System.currentTimeMillis());
    }

    public LocalTimestamp(long time) {
        super(time - TimeZone.getDefault().getRawOffset());
    }

    public Timestamp toLocal() {
        return new Timestamp(getTime() + TimeZone.getDefault().getRawOffset());
    }

    public LocalTimestamp addTime(long milli) {
        setTime(getTime() + milli);
        return this;
    }

    public LocalTimestamp plusHours(int hours) {
        return addTime(TimeUnit.HOURS.toMillis(hours));
    }

    public LocalTimestamp plusMinutes(int mins) {
        return addTime(TimeUnit.MINUTES.toMillis(mins));
    }

    public LocalTimestamp plusDays(int days) {
        return addTime(TimeUnit.DAYS.toMillis(days));
    }


}
