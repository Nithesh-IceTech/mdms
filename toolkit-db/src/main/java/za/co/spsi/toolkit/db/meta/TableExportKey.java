package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.upgrade.UpgradeHelper;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2016/12/19.
 */
public class TableExportKey extends EntityDB {

    public Field<String> PKTABLE_CAT = new Field<>(this);
    public Field<String> PKTABLE_SCHEM = new Field<>(this);
    public Field<String> PKTABLE_NAME = new Field<>(this);
    public Field<String> PKCOLUMN_NAME = new Field<>(this);
    public Field<String> FKTABLE_CAT = new Field<>(this);
    public Field<String> FKTABLE_SCHEM = new Field<>(this);
    public Field<String> FKTABLE_NAME = new Field<>(this);
    public Field<String> FKCOLUMN_NAME = new Field<>(this);
    public Field<String> KEY_SEQ = new Field<>(this);
    public Field<String> UPDATE_RULE = new Field<>(this);
    public Field<Integer> DELETE_RULE = new Field<>(this);
    public Field<String> FK_NAME = new Field<>(this);
    public Field<String> PK_NAME = new Field<>(this);
    public Field<Integer> DEFERRABILITY = new Field<>(this);



    public TableExportKey() {
        super("");
    }

    public TableExportKey reverse() {
        for (Field field : getFields()) {
            if (field.getName().startsWith("PK")) {
                String tmp = field.getAsString();
                field.set(getFields().getByName("FK"+field.getName().substring(2)).getAsString());
                getFields().getByName("FK"+field.getName().substring(2)).set(tmp);
            }
        }
        return this;
    }


    public static String getFKeyConstraintName(EntityDB parent, Field field, ForeignKey key, Driver driver) {
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
        return (PKCOLUMN_NAME.get().equalsIgnoreCase(EntityDB.getColumnName(field)) &&
                PKTABLE_NAME.get().equalsIgnoreCase(parent.getName()) &&
                FKCOLUMN_NAME.get().equalsIgnoreCase(TableExportKey.getFKeyName(parent,key,driver))) ||
                (FKCOLUMN_NAME.get().equalsIgnoreCase(EntityDB.getColumnName(field)) &&
                        PKTABLE_NAME.get().equalsIgnoreCase(parent.getName()) &&
                        PKCOLUMN_NAME.get().equalsIgnoreCase(TableExportKey.getFKeyName(parent,key,driver)));
    }

    public boolean equalsStrict(Driver driver, EntityDB parent, Field field, ForeignKey key) {
        return equals(parent,field,key,driver) && key.onDeleteAction().getCode() == DELETE_RULE.get() &&
                DEFERRABILITY.get() == key.deferrable().getCode();
    }
}
