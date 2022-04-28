package za.co.spsi.toolkit.crud.sync.db;

import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

public class SharedEntity extends BaseSharedSyncEntity {

    // foreign ref id
    public Field<String> reference_id = new Field<>(this);

    public SharedEntity(Entity entity) {
        super(entity);
        initEntity(entity);
    }


}
