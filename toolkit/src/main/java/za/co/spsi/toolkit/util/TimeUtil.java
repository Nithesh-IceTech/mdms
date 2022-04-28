package za.co.spsi.toolkit.util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtil {

    private static TimeZone ZONE = TimeZone.getDefault();

    public static <T extends Date> T localToGMT(T date) {
        T adjusted = (T) date.clone();
        adjusted.setTime(date.getTime()- ZONE.getRawOffset() - ZONE.getDSTSavings());
        return adjusted;
    }

    public static <T extends Date> T gmtToLocal(T date) {
        T adjusted = (T) date.clone();
        adjusted.setTime(date.getTime()+ ZONE.getRawOffset() + ZONE.getDSTSavings());
        return adjusted;
    }

    public static void main(String args[]) throws Exception {
        System.out.println(localToGMT(new Timestamp(System.currentTimeMillis())));
    }
}
