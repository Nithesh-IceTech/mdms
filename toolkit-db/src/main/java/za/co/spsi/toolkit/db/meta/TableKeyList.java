package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.StringList;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by jaspervdbijl on 2016/12/19.
 */
public class TableKeyList extends ArrayList<TableKey> {

    public TableKeyList(Collection<? extends TableKey> c) {
        super(c);
    }

    public StringList getPrimaryKeyNames() {
        StringList names = new StringList();
        for (TableKey key : this) {
            names.add(key.PKCOLUMN_NAME.get());
        }
        return names.removeDuplicates();
    }


}
