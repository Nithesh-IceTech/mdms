package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.lookup.Constants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

public class CityCdField extends MLCSLookupField<String> {

    public CityCdField(Field field, Layout model, MLCSLookupField lookupField) {
        super(field, MdmsLocaleId.CITY, model, Constants.CITY, lookupField);
        setCombineHierarchyNames(true);
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
