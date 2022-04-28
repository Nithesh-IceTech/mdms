package za.co.spsi.toolkit.crud.gui;

import com.vaadin.ui.Button;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Connection;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/08/15.
 * Field that renders a button
 */
public class TimestampField extends LField<java.sql.Timestamp> {

    private Button.ClickListener clickListener;
    private boolean onUpdate = false;

    public TimestampField(Field field, String captionId, Layout layout) {
        super(field, captionId, layout);
        getProperties().setReadOnly(true);
    }

    public TimestampField setOnUpdate() {
        this.onUpdate = true;
        return this;
    }

    @Override
    public void saveEvent(Connection connection) {
        super.saveEvent(connection);
        if (get() == null || onUpdate) {
            set(new Timestamp(System.currentTimeMillis()));
        }
    }

}
