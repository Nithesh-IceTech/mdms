package za.co.spsi.toolkit.db;

import java.sql.ResultSet;

// short hand notation
public class DSDB<E extends EntityDB> extends DataSourceDB<E> {
    public DSDB(Class<E> entityClass) {
        super(entityClass);
    }

    public DSDB(E entity) {
        super(entity);
    }

    public DSDB() {
    }

    public DSDB(Class<E> entityClass, ResultSet rs) {
        super(entityClass, rs);
    }

    public DSDB(E entity, ResultSet rs, boolean keepHistory) {
        super(entity, rs, keepHistory);
    }

    public DSDB(Class<E> entityClass, ResultSet rs, boolean keepHistory) {
        super(entityClass, rs, keepHistory);
    }

    public DSDB(ResultSet rs) {
        super(rs);
    }
}
