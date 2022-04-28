package za.co.spsi.toolkit.db.fields;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.meta.TableIndex;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;

/**
 * Created by jaspervdbijl on 2016/12/20.
 */
public class Index {

    private String name;
    private FieldList fields = new FieldList();
    private EntityDB entityDB;
    private boolean unique;

    public Index(String name,EntityDB entityDB,Field... fields) {
        this.name = name;
        this.entityDB = entityDB;
        this.fields.add(fields);
        entityDB.addIndex(this);
    }

    public EntityDB getEntityDB() {
        return entityDB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldList getFields() {
        return fields;
    }

    public boolean isUnique() {
        return unique;
    }

    public Index setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public Index setUnique() {
        return setUnique(true);
    }

    public boolean equals(TableIndex index) {
        return index.equals(this);

    }
}
