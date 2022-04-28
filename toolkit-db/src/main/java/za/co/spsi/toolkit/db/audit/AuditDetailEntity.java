package za.co.spsi.toolkit.db.audit;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.Cascade;

/**
 * Created by jaspervdb on 2016/10/31.
 */
@Table(version = 1, allowFkDrop = true)
public class AuditDetailEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "audit_log_detail_id", size = 50)
    public Field<String> auditLogDetailId = new Field<>(this);

    @ForeignKey(table = AuditEntity.class,name = "AD_AUDITLOG_ID_FKEY", onDeleteAction = Cascade)
    @Column(name = "audit_log_id", size = 50, notNull = true)
    public Field<String> auditLogId = new Field<>(this);

    @Column(name = "field_name", size = 250)
    public Field<String> fieldName = new Field<>(this);

    @Column(name = "old_value", size = 4000)
    public Field<String> oldValue = new Field<>(this);

    @Column(name = "new_value", size = 4000)
    public Field<String> newValue = new Field<>(this);

    private Index idxAuditId = new Index("idx_AUD_AUDIT_LOG_ID",this,auditLogId);

    private Index idxAuditFieldId = new Index("idxAudFieldId",this,auditLogId,fieldName);

    public AuditDetailEntity() {
        super("audit_log_detail");
    }

    public AuditDetailEntity(AuditEntity auditEntity, Field field) {
        this();
        auditLogId.set(auditEntity.auditLogId.get());
        fieldName.set(field.getName());
        oldValue.set(field.getSerial(field.getOldValue()));
        newValue.set(field.getSerial());
    }

    public static boolean isAuditableField(Field field) {
        return !(byte[].class.equals(field.getType()) || Byte[].class.equals(field.getType()) ||
                field.getSerial().length() > 4000);
    }


}
