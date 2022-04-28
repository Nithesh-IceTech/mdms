package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import za.co.spsi.toolkit.crud.gui.fields.SortableImage;

public class CustomImageField extends CustomField<SortableImage> {

    private SortableImage sortableImage;

    public CustomImageField(SortableImage sortableImage) {
        this.sortableImage = sortableImage;
    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout(sortableImage).withExpand(sortableImage, 2f).withSpacing(true).withFullWidth();
    }

    @Override
    public Class<? extends SortableImage> getType() {
        return SortableImage.class;
    }

    @Override
    public void setValue(SortableImage newFieldValue) {
        sortableImage.setValue(newFieldValue.getValue());
    }

    @Override
    public SortableImage getValue() {
        return sortableImage;
    }

}
