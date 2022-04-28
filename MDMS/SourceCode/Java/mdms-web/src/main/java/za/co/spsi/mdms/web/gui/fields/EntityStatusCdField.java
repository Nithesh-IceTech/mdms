package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.lookup.Constants;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class EntityStatusCdField extends MLCSLookupField<Integer> {

    public EntityStatusCdField(Field field, Layout model) {
        super(field, MdmsLocaleId.SYNC_STATUS, model, Constants.ENTITYSTAT);
        getProperties().setMandatory(true);
        getProperties().setDefault(MdmsConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING.toString());
        getProperties().setReadOnly(true);
    }


}

