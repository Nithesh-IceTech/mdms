package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class MeterOwnerCdField extends MLCSLookupField<Integer> {

    public MeterOwnerCdField(Field field, Layout model) {
        super(field, MdmsLocaleId.METER_OWNER, model, MdmsConstants.METER_OWNER);

    }

}
