package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

public class MeterPlatformTypeCdField extends MLCSLookupField<Integer> {

    public MeterPlatformTypeCdField(Field field, Layout model) {
        super(field, MdmsLocaleId.METER_PLATFORM_TYPE, model, MdmsConstants.METERPLATFRMTYP);
    }
}