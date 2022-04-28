package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;

public class QueryEntityField extends LField {

    private Resource toolbarIcon;
    private Button btn = new Button(getCaption());
    private Boolean enabled = true;
    private Callback callback = null;

    public QueryEntityField(String captionId, Resource toolbarIcon, Layout model, Callback callback) {
        super(captionId, "", model);
        this.toolbarIcon = toolbarIcon;
        this.callback = callback;
    }

    @Override
    public Component buildComponent() {
        btn.setIcon(toolbarIcon);
        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                callback.doQuery();
            }
        });
        btn.setCaption(getCaption());
        btn.setDescription(getCaption());
        return btn;
    }

    @Override
    public void intoControl() {
        btn.setEnabled(!getProperties().isReadOnly());
    }

    @Override
    protected com.vaadin.ui.Field intoBindingsWithNoValidation(boolean update) {
        return null;
    }

    @Override
    public void intoBindings() {
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * optional interface that will enable features on display
     */
    public static interface Callback {

        void doQuery();
    }

}

