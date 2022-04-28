package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.ui.TextArea;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.UIProperties;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdb
 * Date: 2013/11/01
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextAreaField extends LField<String> {

    public TextAreaField(Field field, String captionId, Layout model) {
        super(field, captionId, model);
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        TextArea textArea = new TextArea();
        textArea.setRows(getProperties().getRows());
        return wrapInReviewField(textArea);

    }
}
