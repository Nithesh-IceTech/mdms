package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.IntegerField;
import za.co.spsi.toolkit.util.StringUtils;

/**
 * Created by jaspervdb on 2016/10/27.
 */
public class IntField extends IntegerField implements TextFieldRetrievable {

    public IntField() {
        tf.setNullRepresentation("");
    }

    private boolean isNum(String str) {
        try {
            if (!StringUtils.isEmpty(str) && !"null".equals(str)) {
                Integer.parseInt(str);
                return true;
            }
            return false;
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    @Override
    protected void userInputToValue(String str) {
        setValue(isNum(str)?Integer.parseInt(str) : null);
    }

    public IntField(String caption) {
        super(caption);
    }

    public TextField getTextField() {
        return tf;
    }
}
