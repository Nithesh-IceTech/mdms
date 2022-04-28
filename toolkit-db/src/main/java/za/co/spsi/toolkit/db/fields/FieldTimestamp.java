package za.co.spsi.toolkit.db.fields;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Entity;

import java.sql.Connection;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/20.
 */
public class FieldTimestamp extends DBField<Timestamp> {

    private boolean setUpdate = false;

    public FieldTimestamp(Entity entity) {
        super(entity);
    }

    public FieldTimestamp onUpdate() {
        setUpdate = true;
        return this;
    }

    public boolean isSetUpdate() {
        return setUpdate;
    }

    @Override
    public boolean beforeInsertEvent(Connection connection) {
        set(new Timestamp(System.currentTimeMillis()));
        return super.beforeInsertEvent(connection);
    }

    @Override
    public boolean beforeUpdateEvent(Connection connection) {
        if (setUpdate) {
            set(new Timestamp(System.currentTimeMillis()));
        }
        return super.beforeUpdateEvent(connection);
    }

    public EntityDB set() {
        set(new Timestamp(System.currentTimeMillis()));
        return (EntityDB) getEntity();
    }
}
