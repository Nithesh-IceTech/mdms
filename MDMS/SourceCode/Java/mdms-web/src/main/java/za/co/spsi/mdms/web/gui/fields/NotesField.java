package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class NotesField extends TextAreaField {

    public NotesField(Field field, Layout model) {
        super(field, MdmsLocaleId.NOTES, model);
        getProperties().setRows(5).setMax(500);

    }
}
