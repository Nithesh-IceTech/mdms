package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.StringList;

import java.util.ArrayList;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class TableColumnList extends ArrayList<TableColumn> {


    public TableColumn getByName(String name) {
        for (TableColumn m : this) {
            if (m.COLUMN_NAME.get().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }


    /**
     * return all the fields that exist in this collection and not in fields
     * @param fields
     * @return
     */
    public TableColumnList subtract(FieldList fields) {
        TableColumnList tableColumns = new TableColumnList();
        for (TableColumn column : this) {
            if (EntityDB.getColumnNames(fields).indexInsideOfIgnoreCase(column.COLUMN_NAME.get()) == -1) {
                tableColumns.add(column);
            }
        }
        return tableColumns;
    }

    /**
     * return all the fields that does not exist in this collection
     * @param fields
     * @return
     */
    public FieldList diff(FieldList fields) {
        FieldList rFields = new FieldList();
        for (Field field : fields) {
            if (getByName(EntityDB.getColumnName(field)) == null) {
                rFields.add(field);
            }
        }
        return rFields;
    }

    /**
     * return all the fields that does not match
     * @param fields
     * @return
     */
    public FieldList misMatch(Driver driver, FieldList fields,za.co.spsi.toolkit.db.ano.Table table) {
        FieldList rFields = new FieldList();
        for (Field field : fields) {
            if (getByName(EntityDB.getColumnName(field)) != null && !getByName(EntityDB.getColumnName(field)).match(
                    driver,field,table != null && table.maintainStrict())) {
                rFields.add(field);
            }
        }
        return rFields;
    }

    public TableColumnList getPrimaryKey() {
        TableColumnList list = new TableColumnList();
        for (TableColumn column : this) {
            if (column.isPrimaryKey()) {
                list.add(column);
            }
        }
        return list;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (TableColumn column : this) {
            sb.append(column.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
