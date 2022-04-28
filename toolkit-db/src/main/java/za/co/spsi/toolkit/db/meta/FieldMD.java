package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.entity.Field;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class FieldMD {

    private String name,className,defaultValue;
    private int precision;
    private boolean isAutoIncrement,nullable;

    public FieldMD(Driver driver, ResultSetMetaData metaData, int index) throws SQLException {
        this.name = metaData.getColumnName(index);
        this.className = driver.translateColumnClassName(metaData.getColumnClassName(index));
        this.precision = metaData.getPrecision(index);
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public int getPrecision() {
        return precision;
    }

    public String toString() {
        return String.format("%s %s %d",name,className,precision);
    }

    private boolean match(Column column) {
        return true;
    }

    public boolean match(Field field) {
        return field.getType().getName().equals(className) && (field.getAnnotation(Column.class) == null ||
                match((Column)field.getAnnotation(Column.class)));
    }

}
