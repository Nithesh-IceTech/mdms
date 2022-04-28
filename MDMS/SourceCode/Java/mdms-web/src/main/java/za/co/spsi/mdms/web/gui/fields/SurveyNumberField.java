package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.MaskId;

public class SurveyNumberField extends LField<String> {

    public SurveyNumberField(Field field, Layout model, String caption) {
        super(field,caption, model);
        getProperties().setMax(20);
        getProperties().setMaskId(MaskId.ANY);
        getProperties().setMandatory(false);
        getProperties().setReadOnly(true);
    }
}
