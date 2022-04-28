package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jaspervdbijl on 2016/12/19.
 */
public class TableExportKeyList extends ArrayList<TableExportKey> {

    private EntityDB entity;

    public TableExportKeyList() {
    }

    public TableExportKeyList(Collection<? extends TableExportKey> c) {
        super(c);
    }

    public TableExportKeyList(EntityDB entity) {
        this.entity = entity;
    }


    public TableExportKeyList load(Driver driver, Connection connection) {
        addAll(driver.getForeignKeys(connection, entity.getName()));
        return this;
    }

    public TableExportKeyList reverse() {
        for (TableExportKey key : this) {
            key.reverse();
        }
        return this;
    }

    public int indexOf(Field field, Driver driver, ForeignKey key) {
        try {
            EntityDB parent = (EntityDB) key.table().newInstance();
            for (int i = 0; i < size(); i++) {
                if (get(i).equals(parent, field, key, driver)) {
                    return i;
                }
            }
            return -1;
        } catch (IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean contains(Field field, Driver driver, ForeignKey key) {
        return indexOf(field, driver, key) != -1;
    }

    public FieldList toAdd(Driver driver) {
        FieldList keys = new FieldList();
        for (Field field : entity.getFieldsWithAnnotation(ForeignKey.class)) {
            ForeignKey key = (ForeignKey) field.getAnnotation(ForeignKey.class);
            if (!contains(field, driver, key)) {
                keys.add(field);
            }
        }
        return keys;
    }

    public int indexOf(FieldList fields, Driver driver, TableExportKey key) {
        try {
            for (Field field : fields) {
                ForeignKey fKey = (ForeignKey) field.getAnnotation(ForeignKey.class);
                EntityDB parent = (EntityDB) fKey.table().newInstance();
                if (key.equals(parent, field, fKey, driver)) {
                    return fields.indexOf(field);
                }
            }
            return -1;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean contains(FieldList fields, Driver driver, TableExportKey key) {
        return indexOf(fields, driver, key) > -1;
    }

    /**
     * only drop that matches on name but does not match
     *
     * @return
     */
    public TableExportKeyList toDrop(Driver driver) {
        TableExportKeyList dropFields = new TableExportKeyList();
        FieldList fields = entity.getFieldsWithAnnotation(ForeignKey.class);
        for (TableExportKey key : this) {
            if (!contains(fields, driver, key)) {
                dropFields.add(key);
            }
        }
        return dropFields;
    }

    public TableExportKeyList toModify(Driver driver) {
        try {
            TableExportKeyList modFields = new TableExportKeyList();
            FieldList fields = entity.getFieldsWithAnnotation(ForeignKey.class);
            for (Field field : fields) {
                ForeignKey fKey = (ForeignKey) field.getAnnotation(ForeignKey.class);
                EntityDB parent = (EntityDB) fKey.table().newInstance();
                if (contains(field, driver, fKey) && !get(indexOf(field, driver, fKey)).equalsStrict(driver, parent, field, fKey)) {
                    modFields.add(get(indexOf(field, driver, fKey)));
                    if (contains(field, driver, fKey) && !get(indexOf(field, driver, fKey)).equalsStrict(driver, parent, field, fKey)) {
                        System.out.print("");
                    }
                }
            }
            return modFields;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
