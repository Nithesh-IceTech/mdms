package za.co.spsi.toolkit.crud.gui.fields;

import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.entity.Field;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateField extends LField<Date> {

    public DateField(Field field, String captionId, String format, Layout model) {
        super(field, captionId, model);
        setDateFormat(format);
    }

    /**
     * @return the value converter to a string
     */
    public String getAsString() {
        Date date = get();
        return date != null?new SimpleDateFormat(getDateFormat()).format(date):"";
    }

    @Override
    public java.util.Date get() {
        Object value = super.get();
        return value != null?
                (value instanceof Long ?new java.util.Date(((Long)value).longValue()):(java.util.Date)value):null;
    }
}

