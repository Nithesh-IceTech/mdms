package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.data.Property;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;

/**
 * Created by jaspervdb on 15/02/03.
 */
public class DoubleEntryField extends TextField implements Property.ValueChangeListener,ClientConnector.AttachListener ,ClientConnector.DetachListener {

    private String value = null;
    private boolean requestReEnter = true;

    public DoubleEntryField(String caption) {
        super(caption);
        this.addAttachListener(this);
        this.addDetachListener(this);
    }

    private void clearText() {
        this.removeValueChangeListener(this);
        setValue("");
        this.addValueChangeListener(this);
    }

    public void valueChange(Property.ValueChangeEvent event) {
        if (requestReEnter) {
            value = getValue();
            clearText();
            VaadinNotification.show("ConfValue must be entered twice", Notification.Type.WARNING_MESSAGE);
            this.focus();
        } else {
            // check if the values matches
            if (!value.equals(getValue())) {
                clearText();
                VaadinNotification.show("Values do not match", Notification.Type.WARNING_MESSAGE);
            }
        }
        requestReEnter = !requestReEnter;
    }

    @Override
    public void attach(AttachEvent event) {
        this.addValueChangeListener(this);
    }

    public void removeValueChangeListener() {
        this.removeValueChangeListener(this);
    }

    public void addValueChangeListener() {
        this.addValueChangeListener(this);
    }

    @Override
    public void detach(DetachEvent event) {
        this.removeValueChangeListener(this);
    }
}
