package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.VirtualField;

public class ActionField extends LField {

    private Button btn = new Button(getCaption());
    private Resource toolbarIcon;
    private Callback callback;

    public ActionField(String captionId, Resource toolbarIcon, Layout model, Callback callback) {
        super(captionId, "", model);
        this.toolbarIcon = toolbarIcon;
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected com.vaadin.ui.Field intoBindingsWithNoValidation(boolean update) {
        return null;
    }


    @Override
    public Component buildComponent() {
        btn.setIcon(toolbarIcon);
        btn.addClickListener((Button.ClickListener) event -> callback.callback(ActionField.this));
        btn.setCaption(getCaption());
        btn.setDescription(getCaption());
        return btn;
    }

    public interface Callback {
        void callback(ActionField source);
    }

}

