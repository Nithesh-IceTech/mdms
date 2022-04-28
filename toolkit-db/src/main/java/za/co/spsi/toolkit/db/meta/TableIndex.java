package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.StringList;

/**
 * Created by jaspervdbijl on 2016/12/19.
 */
public class TableIndex extends EntityDB {

    public Field<String> TABLE_CAT = new Field<>(this);
    public Field<String> TABLE_SCHEM = new Field<>(this);
    public Field<String> TABLE_NAME = new Field<>(this);
    public Field<String> NON_UNIQUE = new Field<>(this);
    public Field<String> INDEX_QUALIFIER = new Field<>(this);
    public Field<String> INDEX_NAME = new Field<>(this);
    public Field<Integer> TYPE = new Field<>(this);
    public Field<Integer> ORDINAL_POSITION = new Field<>(this);
    public Field<String> COLUMN_NAME = new Field<>(this);
    public Field<String> ASC_OR_DESC = new Field<>(this);
    public Field<String> CARDINALITY = new Field<>(this);
    public Field<String> PAGES = new Field<>(this);
    public Field<String> FILTER_CONDITION = new Field<>(this);

    private StringList columnNames = new StringList();

    public TableIndex() {
        super("");
    }

    @Override
    public StringList getColumnNames() {
        return columnNames;
    }

    public boolean equals(Index index) {
        return index.getEntityDB().getName().equalsIgnoreCase(TABLE_NAME.get()) &&
                EntityDB.getColumnNames(index.getFields()).matchAnyOrderIgnoreCase(columnNames) &&
                ((NON_UNIQUE.get().equals("1") || NON_UNIQUE.get().equalsIgnoreCase("true")
                        || NON_UNIQUE.get().equalsIgnoreCase("Y")) == !index.isUnique());
    }

    public boolean equals(TableIndex index) {
        return index.TABLE_NAME.get().equalsIgnoreCase(TABLE_NAME.get()) && index.COLUMN_NAME.get().equalsIgnoreCase(COLUMN_NAME.get()) &&
                index.NON_UNIQUE.get().equalsIgnoreCase(NON_UNIQUE.get());
    }
}
