package za.co.spsi.toolkit.crud.gui;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Assert;

/**
 * Created by jaspervdb on 2016/08/15.
 * Field that renders a button
 */
public class UidField extends LField<String> {

    private Button.ClickListener clickListener;

    public UidField(Field field, String captionId, Layout layout) {
        super(field, captionId, layout);
        getProperties().setReadOnly(true);
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        if (get() == null) {
            set(((ToolkitUI)ToolkitUI.getCurrent()).getUsername());
        }
    }
}
