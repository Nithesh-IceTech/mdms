package za.co.spsi.toolkit.crud.sync.db;

import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

public class BaseSharedSyncEntity extends Entity {

    @Column(name = "PUSH_DOWN_NOTE")
    public Field<String> pushDownNote = new Field<String>(this);

    @Column(name = "NOTES",size = 2048,autoCrop = true)
    public Field<String> notes = new Field<>(this);

    @Column(name = "ENTITY_STATUS_CD",defaultValue = "2",notNull = true)
    public Field<Integer> entityStatusCd= new Field<>(this);

    @Column(name = "ENTITY_STATUS_CHANGE_D",defaultValue = "now")
    public Field<Timestamp> entityStatusChgD= new Field<>(this);

    @Column(name = "REVIEW_STATUS_CD",defaultValue = "1")
    public Field<Integer> reviewStatusCd= new Field<>(this);

    @Column(name = "REVIEW_STATUS_CHANGE_D",defaultValue = "now")
    public Field<Timestamp> reviewStatusChgD= new Field<>(this);

    @Column(name = "CAPTURED_D",defaultValue = "now")
    public Field<Timestamp> capturedD= new Field<>(this);

    public BaseSharedSyncEntity(Entity entity) {
        initEntity(entity);
    }


}
