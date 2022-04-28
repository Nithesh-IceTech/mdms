package za.co.spsi.toolkit.db.upgrade;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.entity.Field;

import java.util.ArrayList;

/**
 * Created by jaspervdbijl on 2017/04/11.
 */
public class TableList extends ArrayList<Class<? extends EntityDB>> {

    private TableList getDependencies(Class<? extends EntityDB> eClass) {
        try {
            TableList dependencies = new TableList();
            EntityDB entity = eClass.newInstance();
            for (EntityRef ref : entity.getEntityRefs()) {
                dependencies.add(ref.getType());
            }
            for (Field field : entity.getFieldsWithAnnotation(ForeignKey.class)) {
                dependencies.add(((ForeignKey)field.getAnnotation(ForeignKey.class)).table());
            }
            return dependencies.removeDuplicates();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public TableList removeDuplicates() {
        TableList list = new TableList();
        for (Class<? extends EntityDB> eClass : this) {
            if (!list.contains(eClass)) {
                list.add(eClass);
            }
        }
        return list;
    }

    public TableList sublist(int index, int end) {
        TableList list = new TableList();
        for (int i = index; i < size() && i < end; i++) {
            list.add(get(i));
        }
        return list;
    }

    public TableList sublist(int index) {
        return sublist(index, size());
    }

    public TableList intersection(TableList list) {
        TableList intersection = new TableList();
        for (Class<? extends EntityDB> type : this) {
            if (list.contains(type)) {
                intersection.add(type);
            }
        }
        return intersection;
    }

    public void insert(TableList list, int index) {
        list = list.removeDuplicates();
        for (Class type : list) {
            while (contains(type)) {
                remove(type);
            }
            add(index - 1, type);
        }
    }

    public TableList sortOnDependencies() {
        for (int e = 0; e < size(); e++) {
            for (int i = 1; i < size(); i++) {
                insert(getDependencies(get(i - 1)).intersection(sublist(i)), i);
            }
        }
        return this;
    }

}
