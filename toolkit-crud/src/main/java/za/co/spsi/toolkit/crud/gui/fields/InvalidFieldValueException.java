package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.data.Validator;
import com.vaadin.ui.Field;

/**
 * Created by jaspervdb on 14/10/23.
 */
public class InvalidFieldValueException extends Validator.InvalidValueException {
    private Field invalidField;

    public InvalidFieldValueException(String message, Field invalidField) {
        super(message);
        this.invalidField = invalidField;
    }

    public InvalidFieldValueException(String message, Field invalidField, Validator.InvalidValueException... causes) {
        super(message, causes);
        this.invalidField = invalidField;
    }

    public Field getInvalidField() {
        return invalidField;
    }
}
