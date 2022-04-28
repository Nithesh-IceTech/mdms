package za.co.spsi.toolkit.crud.gui.fields;

import za.co.spsi.toolkit.crud.gui.CapsLockWarning;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdb
 * Date: 2013/10/31
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordField extends LField<String> {

    public PasswordField(Field field, String captionId, Layout layout) {
        super(field, captionId, layout);
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        com.vaadin.ui.PasswordField passwordField = new com.vaadin.ui.PasswordField(getCaption());
        passwordField.setImmediate(true);
        new CapsLockWarning().extend(passwordField);
        return wrapInReviewField(passwordField);
    }
}
