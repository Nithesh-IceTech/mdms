package za.co.spsi.toolkit.crud.db.fields;

import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.fields.DBField;
import za.co.spsi.toolkit.entity.Entity;

import java.sql.Connection;

/**
 * Created by jaspervdb on 2016/10/20.
 */
public class CreateUserIdField extends DBField<String> {

    public CreateUserIdField(Entity entity) {
        super(entity);
    }

    @Override
    public boolean beforeInsertEvent(Connection connection) {
        set();
        return super.beforeInsertEvent(connection);
    }

    public EntityDB set() {
        set(ToolkitUI.getToolkitUI() != null?ToolkitUI.getToolkitUI().getUsername():get());
        return (EntityDB) getEntity();
    }
}
