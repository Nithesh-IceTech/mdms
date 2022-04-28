package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.util.StringList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by jaspervdbijl on 2016/12/19.
 */
public class TableIndexList extends ArrayList<TableIndex> {

    public TableIndexList() {
    }

    public TableIndexList(Collection<? extends TableIndex> c) {
        super(c);
    }

    public TableIndex getByName(String name) {
        for (TableIndex index : this) {
            if (name.equalsIgnoreCase(index.INDEX_NAME.get())) {
                return index;
            }
        }
        return null;
    }

    public TableIndexList removeNull() {
        TableIndexList list = new TableIndexList();
        for (int i =0;i < size();i++) {
            if (get(i).INDEX_NAME.get() != null) {
                list.add(get(i));
            }
        }
        return list;
    }

    public TableIndex get(TableIndex index) {
        for (TableIndex idx : this) {
            if (idx.equals(index)) {
                return idx;
            }
        }
        return null;
    }

    public TableIndex get(Index index) {
        for (TableIndex idx : this) {
            if (idx.equals(index)) {
                return idx;
            }
        }
        return null;
    }

    public StringList getNames() {
        Driver driver = DriverFactory.getDriver();
        StringList names = new StringList();
        for (TableIndex i : this) {
            String idxName = driver.isOracle() ? i.INDEX_NAME.get() : i.INDEX_NAME.get().toLowerCase();
            names.add(idxName);
        }
        return names;
    }

    public TableIndexList group() {
        TableIndexList group = new TableIndexList();

        for (TableIndex index : this) {
            if (group.getByName(index.INDEX_NAME.get())==null) {
                group.add(index);
            }
            group.getByName(index.INDEX_NAME.get()).getColumnNames().add(index.COLUMN_NAME.get());
            group.getByName(index.INDEX_NAME.get()).getColumnNames().removeDuplicates();
        }
        return group;
    }

    public TableIndexList removeIdIndex(EntityDB entityDB) {
        for (int i =0;i < size();i++) {
            if (get(i).getColumnNames().matchAnyOrderIgnoreCase(EntityDB.getColumnNames(entityDB.getId()))) {
                remove(i--);
            }
        }
        return this;
    }

    public List<Index> toAdd(EntityDB entity) {
        Driver driver = DriverFactory.getDriver();
        List<Index> indexes = new ArrayList<>();
        for (Index index : entity.getIndexes()) {
            index.setName( driver.isOracle() ? index.getName() : index.getName().toLowerCase() );
            if (get(index) == null) {
                indexes.add(index);
            }
        }
        return indexes;
    }

    public TableIndexList toDrop(EntityDB entity) {
        Driver driver = DriverFactory.getDriver();
        TableIndexList drop = new TableIndexList();
        for (TableIndex index : this) {
            index.setName( driver.isOracle() ? index.getName() : index.getName().toLowerCase() );
            if (entity.getIndexes().get(index) == null) {
                drop.add(index);
            }
        }
        return drop;
    }

}
