package za.co.spsi.mdms.common.services;

import za.co.spsi.mdms.common.error.RestException;

import javax.ws.rs.core.Response;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by jaspervdbijl on 2017/01/09.
 */
public class DateTimeParam<E extends java.util.Date> {

    private SimpleDateFormat format;
    private Class<E> typeClass;

    private String dateTime;

    public DateTimeParam(Class<E> typeClass,SimpleDateFormat format,String dateTime) {
        this.typeClass = typeClass;
        this.format = format;
        this.dateTime = dateTime;
    }

    public E get() {
        try {
            return dateTime!= null?typeClass.getConstructor(long.class).newInstance(format.parse(dateTime).getTime()):null;
        } catch (Exception pe) {
            throw new RestException(Response.Status.BAD_REQUEST, "Invalid date time format. yyyyMMddHHmmss");
        }
    }

    public static class DateTime extends DateTimeParam<Timestamp> {

        public DateTime(String dateTime) {
            super(Timestamp.class, new SimpleDateFormat("yyyyMMddHHmmss"), dateTime);
        }
    }

    public static class Date extends DateTimeParam<java.sql.Date> {

        public Date(String dateTime) {
            super(java.sql.Date.class, new SimpleDateFormat("yyyyMMdd"), dateTime);
        }
    }

    public static class Time extends DateTimeParam<java.sql.Time> {

        public Time(String dateTime) {
            super(java.sql.Time.class, new SimpleDateFormat("HHmmss"), dateTime);
        }
    }

}
