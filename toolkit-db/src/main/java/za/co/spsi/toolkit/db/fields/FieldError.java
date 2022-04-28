package za.co.spsi.toolkit.db.fields;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.util.ObjectUtils;
import za.co.spsi.toolkit.util.StringUtils;

import java.sql.Connection;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/20.
 */
public class FieldError extends DBField<String> {

    public FieldError(Entity entity) {
        super(entity);
    }

    private void set(String ex, int length) {
        length = length == -1 ? 200 : length-1;
        super.set(!StringUtils.isEmpty(ex) && ex.length() > length ? ex.substring(0, length) : ex);
    }

    @Override
    public Entity set(String value) {
        if (getAnnotation(Column.class) != null) {
            set(value, getAnnotation(Column.class).size());
            return getEntity();
        } else {
            return super.set(value);
        }
    }

    public void set(Exception ex) {
        if (getAnnotation(Column.class) != null) {
            set(ObjectUtils.convertStackTraceToString(ex), getAnnotation(Column.class).size());
        } else {
            super.set(ObjectUtils.convertStackTraceToString(ex));
        }
    }

}
