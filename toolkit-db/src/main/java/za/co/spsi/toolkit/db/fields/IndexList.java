package za.co.spsi.toolkit.db.fields;

import za.co.spsi.toolkit.db.meta.TableIndex;

import java.util.ArrayList;

/**
 * Created by jaspervdbijl on 2016/12/20.
 */
public class IndexList extends ArrayList<Index> {

    public Index get(String name) {
        for (Index index : this) {
            if (index.getName().equalsIgnoreCase(name)) {
                return index;
            }
        }
        return null;
    }

    public Index get(TableIndex index) {
        for (Index i : this) {
            if (index.equals(i)) {
                return i;
            }
        }
        return null;
    }
}
