package za.co.spsi.toolkit.crud.gui.fields;

import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;
import java.util.TimeZone;

public class LocalTimestampField extends LField<Timestamp> {

    public LocalTimestampField(Field field, String captionId, Layout layout) {
        super(field, captionId, layout);
    }

    public LocalTimestampField(String captionId, String colName, Layout layout) {
        super(captionId, colName, layout);
    }

    @Override
    public void intoControl() {
        // convert to local gmt
        Timestamp date = get();
        if (date != null) {
            try {
                field.set(new Timestamp(get().getTime() + TimeZone.getDefault().getRawOffset()));
                super.intoControl();
            } finally {
                field.set(date);
            }
        } else {
            super.intoControl();
        }
    }

    @Override
    protected com.vaadin.ui.Field intoBindingsWithNoValidation(boolean setOldValue) {
        com.vaadin.ui.Field vaadinField = super.intoBindingsWithNoValidation(setOldValue);
        if (get() != null) {
            field.set(new Timestamp(get().getTime() - TimeZone.getDefault().getRawOffset()));
        }
        return vaadinField;
    }

    @Override
    public String getFullColName() {
        String timezoneOffset = DriverFactory.getDriver().addTimezoneOffset();
        return getColName() + timezoneOffset + " " + getColName();
    }

}

