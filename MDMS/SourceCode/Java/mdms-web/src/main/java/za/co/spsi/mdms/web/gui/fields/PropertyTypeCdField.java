package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class PropertyTypeCdField extends MLCSLookupField<Integer> {

    public PropertyTypeCdField(Field field, boolean mandatory,Layout model) {
        super(field, MdmsLocaleId.PROPERTY_TYPE, model, MdmsConstants.PROPERTY_TYPE);
        getProperties().setMandatory(mandatory);
    }

    public PropertyTypeCdField(Field field, Layout model) {
        this(field,true,model);
    }

}
