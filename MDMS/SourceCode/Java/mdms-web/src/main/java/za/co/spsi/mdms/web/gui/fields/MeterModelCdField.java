package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class MeterModelCdField extends MLCSLookupField<Integer> {

    public MeterModelCdField(Field field, MeterMakeCdField meterMakeCdField,Layout model) {
        super(field, MdmsLocaleId.METER_MODEL, model, MdmsConstants.METER_MODEL,meterMakeCdField);
        setCombineHierarchyNames(true);
    }

}
