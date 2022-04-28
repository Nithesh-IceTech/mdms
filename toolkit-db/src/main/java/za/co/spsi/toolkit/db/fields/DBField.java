package za.co.spsi.toolkit.db.fields;

import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/10/20.
 */
public class DBField<E> extends Field<E> {

    public DBField(Entity entity) {
        super(entity);
    }

    /**
     * notification before execute
     *
     * @return false to cancel the execute event
     */
    public boolean beforeInsertEvent(Connection connection) {
        return true;
    }


    /**
     * notification before execute
     *
     * @return false to cancel the execute event
     */
    public boolean beforeUpdateEvent(Connection connection) {
        return true;
    }


}
