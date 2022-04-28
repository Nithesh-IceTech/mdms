package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.LookupField;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2016/05/09.
 *
 */
public class MeterLookupField extends LookupField<String> {

    public MeterLookupField(Field field, Layout model) {
        super(field, MdmsLocaleId.METER_CAPTION,
                String.format("select meter_id,meter_n from meter where ENTITY_STATUS_CD <> %d and AGENCY_ID = %s",
                        ToolkitConstants.ENTITY_STATUS_DELETED,ToolkitCrudConstants.getChildAgencyId()), model);
        getProperties().setMandatory(false);
    }
}
