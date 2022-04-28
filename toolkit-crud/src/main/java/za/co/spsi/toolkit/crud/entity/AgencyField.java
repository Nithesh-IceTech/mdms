package za.co.spsi.toolkit.crud.entity;

import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2016/07/22.
 */
public class AgencyField extends Field<Integer> {

    public AgencyField(Entity entity) {
        super(entity);
    }

    @Override
    public Integer get() {
        if (super.get() == null) {
            set(Integer.parseInt(ToolkitCrudConstants.getChildAgencyId().toString()));
        }
        return super.get();
    }
}
