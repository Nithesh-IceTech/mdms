package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.upgrade.UpgradeHelper;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2016/12/19.
 */
public class TableForeignKey extends EntityDB {

    public Field<String> CHILD_TABLE = new Field<>(this);
    public Field<String> CHILD_COLUMN = new Field<>(this);
    public Field<String> CONSTRAINT_NAME = new Field<>(this);
    public Field<String> DELETE_RULE = new Field<>(this);
    public Field<String> PARENT_TABLE = new Field<>(this);
    public Field<String> PARENT_COLUMN = new Field<>(this);
    public Field<Integer> DEFERRABLE = new Field<>(this);

    public TableForeignKey() {
        super("");
    }

    public static String TableExportKey(EntityDB parent,Field field,ForeignKey key,Driver driver) {
        za.co.spsi.toolkit.db.ano.Table table = parent.getClass().getAnnotation(za.co.spsi.toolkit.db.ano.Table.class);
        String name = key.name().isEmpty()?String.format("%s_%s_FK",
                (table != null && table.shortName().length() > 0?table.shortName():parent.getName()),EntityDB.getColumnName(field)):key.name();
        return name.length() > driver.getIdentifierLength()? UpgradeHelper.shortenName(name)+"_FKC":name;
    }

    public static String getFKeyName(EntityDB parent,ForeignKey key,Driver driver) {
        String name = key.field().isEmpty()?EntityDB.getColumnName(parent.getSingleId()):key.field();
        return name.length() > driver.getIdentifierLength()?UpgradeHelper.shortenName(name)+"_FK":name;
    }

    public boolean equals(EntityDB parent,Field field,ForeignKey key,Driver driver) {
        return (CHILD_COLUMN.get().equalsIgnoreCase(EntityDB.getColumnName(field)) &&
                PARENT_TABLE.get().equalsIgnoreCase(parent.getName()) &&
                PARENT_COLUMN.get().equalsIgnoreCase(TableExportKey.getFKeyName(parent,key,driver)));
    }

    public boolean equalsStrict(Driver driver, EntityDB parent, Field field, ForeignKey key) {
        return equals(parent,field,key,driver) && driver.getCascadeRule(key.onDeleteAction()).equals(DELETE_RULE.get()) &&
                DEFERRABLE.get() == key.deferrable().getCode();
    }
}
