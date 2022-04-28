package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.StringList;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class FieldMDList extends ArrayList<FieldMD> {

    public void load(Driver driver, ResultSetMetaData metaData) throws SQLException {
        for (int i =0;i < metaData.getColumnCount();i++) {
            add(new FieldMD(driver,metaData,i+1));
        }
    }

    public String toString() {
        StringList sl = new StringList();
        for (FieldMD field : this) {
            sl.add(field.toString());
        }
        return sl.toString("\n");
    }

    public FieldMD getByName(String name) {
        return getByName(name,false);
    }

    public FieldMD getByName(String name,boolean ignoreCase) {
        for (FieldMD field : this) {
            if (ignoreCase?field.getName().equalsIgnoreCase(name):field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }


    /**
     * return all the fields that exist in this collection and not in fields
     * @param fields
     * @return
     */
    public FieldMDList subtract(FieldList fields) {
        FieldMDList mdFields = new FieldMDList();
        for (FieldMD field : this) {
            if (fields.getByName(field.getName())==null) {
                mdFields.add(field);
            }
        }
        return mdFields;
    }

    /**
     * return all the fields that does not exist in this collection
     * @param fields
     * @return
     */
    public FieldList diff(FieldList fields) {
        FieldList rFields = new FieldList();
        for (Field field : fields) {
            if (getByName(field.getName(),true) == null) {
                rFields.add(field);
            }
        }
        return rFields;
    }
}
