package za.co.spsi.toolkit.crud.db.fields;

import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.fields.DBField;
import za.co.spsi.toolkit.entity.Entity;

import java.sql.Connection;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/20.
 */
public class UserIdField extends DBField<String> {

    public UserIdField(Entity entity) {
        super(entity);
    }

    @Override
    public boolean beforeInsertEvent(Connection connection) {
        set();
        return super.beforeInsertEvent(connection);
    }

    @Override
    public boolean beforeUpdateEvent(Connection connection) {
        set();
        return super.beforeUpdateEvent(connection);
    }

    public EntityDB set() {
        set(ToolkitUI.getToolkitUI() != null?ToolkitUI.getToolkitUI().getUsername():get());
        return (EntityDB) getEntity();
    }
}
