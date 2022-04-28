package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.lookup.Constants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

public class CountryCdField extends MLCSLookupField<String> {

    public CountryCdField(Field field, Layout model, String caption) {
        super(field, caption, model, Constants.COUNTRY);
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        return super.buildVaadinField();
    }

    @Override
    public void set(String value) {
        super.set(value);
    }

    @Override
    public void intoControl() {
        super.intoControl();
    }

}
