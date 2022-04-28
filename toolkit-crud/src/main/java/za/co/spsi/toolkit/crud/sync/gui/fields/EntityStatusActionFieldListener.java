package za.co.spsi.toolkit.crud.sync.gui.fields;

import za.co.spsi.toolkit.dao.ToolkitConstants;

/**
 * Created by jaspervdb on 2016/05/06.
 */
public interface EntityStatusActionFieldListener {
    void action(ToolkitConstants.EntitySyncStatus entitySyncStatus, EntityStatusActionFieldListener listener);
}
