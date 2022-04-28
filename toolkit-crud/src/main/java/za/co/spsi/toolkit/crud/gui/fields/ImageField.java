package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.custom.CustomImageField;

/**
 * Created by jaspervdbijl on 2017/06/03.
 */
public class ImageField extends LField<ThemeResource> {

    protected Callback callback;
    CustomImageField customImageField;

    public ImageField(String captionId, Layout layout, String collname, Callback callback) {
        super(captionId, collname, layout);
        this.callback = callback;
    }

    @Override
    public Component buildComponent() {
        customImageField = new CustomImageField(new SortableImage(callback.getResource(), callback.getItemId().toString()));
        if (getCaption() != null) {
            customImageField.setCaption(getCaption());
        }

        return customImageField;
    }

    @Override
    public void intoControl() {
        customImageField.setValue(new SortableImage(callback.getResource(), callback.getItemId().toString()));
        customImageField.markAsDirty();
    }

    @Override
    protected Field intoBindingsWithNoValidation() {
        return customImageField;

    }

    @Override
    protected Field intoBindingsWithNoValidation(boolean setOldValue) {
        return customImageField;
    }

    @Override
    public void intoBindings() {

    }

    @Override
    public void set(ThemeResource value) {
        super.set(value);
    }

    /**
     * optional interface that will enable features on display
     */
    public static interface Callback {

        public Resource getResource();

        public Integer getItemId();
    }


}
