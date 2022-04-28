package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.entity.Field;

public class AgencyField extends LField<Integer> {

    public AgencyField(Field field, String captionId, Layout layout) {
        super(field, captionId, layout);
        getProperties().setDefault(ToolkitCrudConstants.getChildAgencyId().toString());
    }

}
