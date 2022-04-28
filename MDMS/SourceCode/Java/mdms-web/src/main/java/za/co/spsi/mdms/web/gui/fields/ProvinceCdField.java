package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.lookup.Constants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

public class ProvinceCdField extends MLCSLookupField<String> {

    public ProvinceCdField(Field field, Layout model,CountryCdField countryCdField) {
        super(field, MdmsLocaleId.PROVINCE, model, Constants.PROVINCE,countryCdField);
        setCombineHierarchyNames(true);
    }


    @Override
    public void set(String value) {
        super.set(value);
    }


}
