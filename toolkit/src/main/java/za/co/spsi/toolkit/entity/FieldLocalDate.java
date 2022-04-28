package za.co.spsi.toolkit.entity;

import lombok.SneakyThrows;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdb on 2016/10/20.
 */
public class FieldLocalDate<E extends Date> extends Field<E> {

    private static TimeZone ZONE = TimeZone.getDefault();
    private TemporalUnit truncatedTo = null;

    public FieldLocalDate(Entity entity) {
        super(entity);
    }

    public FieldLocalDate<E> truncateTo(TemporalUnit truncatedTo) {
        this.truncatedTo = truncatedTo;
        return this;
    }

    @SneakyThrows
    private E truncate(E value) {
        Class type = value.getClass();
        LocalDateTime time = new Timestamp(value.getTime()).toLocalDateTime().truncatedTo(truncatedTo);
        return (E) type.getConstructor(long.class).newInstance(Timestamp.valueOf(time).getTime());
    }

    @Override
    public Entity set(E value) {
        return super.set(value != null && truncatedTo != null?truncate(value):value);
    }


    public Entity setLocal(E value) {
        if (value != null) {
            E clone = ((E)value.clone());
            clone.setTime(value.getTime() - ZONE.getRawOffset() - ZONE.getDSTSavings());
            return super.set(clone);
        } else {
            return super.set(value);
        }
    }

    public E getLocal() {
        if (super.get() != null) {
            E value = ((E)super.get().clone());
            value.setTime(super.get().getTime() + ZONE.getRawOffset() + ZONE.getDSTSavings());
            return value;
        } else {
            return super.get();
        }
    }

}
