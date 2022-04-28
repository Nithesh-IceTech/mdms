package za.co.spsi.toolkit.db.audit;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by jaspervdb on 2016/10/31.
 */
@Table(version = 1)
public class AuditEntity extends EntityDB {

    private static CallbackHook CALLBACK_HOOK = null;
    public static boolean METRICS_ENABLED = false;

    public static void setCallbackHook(CallbackHook callbackHook) {
        CALLBACK_HOOK = callbackHook;
    }

    public enum Type {
        CREATE((short) 0), UPDATE((short) 1), DELETE((short) 2);

        private short code;

        Type(short code) {
            this.code = code;
        }

        public short getCode() {
            return code;
        }

        public static Type fromCode(int code) {
            for (Type type : values()) {
                if (type.getCode() == code) {
                    return type;
                }
            }
            throw new RuntimeException("Unknown type " + code);
        }
    }

    @Id(uuid = true)
    @Column(name = "audit_log_id", size = 50)
    public Field<String> auditLogId = new Field<>(this);

    @Column(name = "entity_id", size = 50)
    public Field<String> entityId = new Field<>(this);

    @Column(name = "entity_name", size = 250)
    public Field<String> entityName = new Field<>(this);

    @Column(name = "audit_type")
    public Field<Short> auditType = new Field<>(this);

    @Column(name = "user_id", size = 50)
    public Field<String> userId = new Field<>(this);

    @Column(name = "create_time")
    public FieldTimestamp createTime = new FieldTimestamp(this);

    @Column(name = "supervisor")
    public Field<Boolean> supervisor = new Field<>(this);

    public AuditEntity() {
        super("audit_log");
    }

    private Index idxLookupAudit = new Index("idxAudLookup",this,entityId,entityName,auditType);

    public Type getType() {
        Assert.notNull(auditType.get(),"Audit type not set");
        return Type.fromCode(auditType.get());
    }

    public static AuditEntity getPrevAudit(
            Connection connection, AuditEntity newAudit, EntityDB entity, Type type, String fieldName) {
        
        try {
            return
                DataSourceDB.get(
                    AuditEntity.class,
                    connection,
                    DriverFactory.getHelper(connection)
                        .limitSql(
                            String.format(
                                "select audit_log.* from audit_log,audit_log_detail where " +
                                    "audit_log.audit_log_id <> '%s' and " +
                                    "audit_log.audit_log_id = audit_log_detail.audit_log_id and audit_log.entity_id = ? and audit_log.entity_name = ? and " +
                                    "%s audit_log_detail.field_name = ? order by audit_log.create_time desc",
                                newAudit.auditLogId.get(),
                                type != null ? "audit_log.audit_type = " + type.getCode() + " and " : ""),
                            1),
                    entity.getSingleId().get(),
                    entity.getName(),
                    fieldName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * check if this is a background service
     *
     * @param auditable
     * @return
     */
    private static boolean mayAudit(Audit auditable) {
        return auditable == null || auditable.services() || AuditHelper.getUID() != null;
    }

    public static AuditEntity createAuditLog(Connection connection, EntityDB entityDB, Type type) {
        Audit auditable = entityDB.getClass().getAnnotation(Audit.class);
        FieldList fields = type != Type.DELETE ? entityDB.getFields().getChanged().getAudit() : entityDB.getFields().getAudit();
        if (!fields.isEmpty() && mayAudit(auditable)) {
            AuditEntity audit = new AuditEntity();
            audit.entityName.set(entityDB.getName());
            audit.entityId.set(entityDB.getSingleId().getSerial());
            audit.supervisor.set(AuditHelper.isSupervisor());

            if (auditable != null && !StringUtils.isEmpty(auditable.uidField()) &&
                    entityDB.getFields().getByName(auditable.uidField()) != null) {

                audit.userId.set(entityDB.getFields().getByName(auditable.uidField()).getSerial());
            } else if (AuditHelper.getUID() != null) {
                audit.userId.set(AuditHelper.getUID());
            }

            audit.auditType.set(type.getCode());
            DataSourceDB.set(connection, audit);
            // create all the fields
            for (Field field : fields) {
                // only process fields that can be inserted as a string
                if (AuditDetailEntity.isAuditableField(field)) {
                    DataSourceDB.set(connection, new AuditDetailEntity(audit, field));
                }
            }
            if (CALLBACK_HOOK != null && METRICS_ENABLED) {
                CALLBACK_HOOK.audit(connection,audit,entityDB,fields);
            }
            return audit;
        } else {
            return null;
        }
    }

    public static StringList getReviewFields(DataSource dataSource, EntityDB entityDB) {
        StringList sl = new StringList();
        for (List list : DataSourceDB.executeQuery(dataSource, "\n" +
                ("\n" +
                        "select * from (\n" +
                        "  SELECT\n" +
                        "    AUDIT_LOG_DETAIL.FIELD_NAME AS MAX_FIELD_NAME,\n" +
                        "    max(CREATE_TIME)            AS MAX_CREATE_TIME,\n" +
                        "    AUDIT_LOG.ENTITY_NAME       AS MAX_ENTITY_NAME,\n" +
                        "    COUNT(*)                    AS MAX_COUNT\n" +
                        "  FROM AUDIT_LOG, AUDIT_LOG_DETAIL\n" +
                        "  WHERE\n" +
                        "    AUDIT_LOG.AUDIT_LOG_ID = AUDIT_LOG_DETAIL.AUDIT_LOG_ID AND\n" +
                        "    AUDIT_LOG.ENTITY_ID = '_ENTITY_ID_' AND\n" +
                        "    AUDIT_LOG.ENTITY_NAME = '_ENTITY_NAME_' AND\n" +
                        "    AUDIT_LOG.SUPERVISOR = _BOOL_VAL1_\n" +
                        "  GROUP BY AUDIT_LOG.ENTITY_NAME, AUDIT_LOG_DETAIL.FIELD_NAME\n" +
                        ") innerquery where not EXISTS (\n" +
                        "    select * from AUDIT_LOG a, AUDIT_LOG_DETAIL b where\n" +
                        "      a.AUDIT_LOG_ID = b.AUDIT_LOG_ID AND\n" +
                        "      a.ENTITY_ID = '_ENTITY_ID_' AND\n" +
                        "      a.ENTITY_NAME = '_ENTITY_NAME_' AND\n" +
                        "      a.SUPERVISOR = _BOOL_VAL2_ and\n" +
                        "      a.CREATE_TIME > MAX_CREATE_TIME and\n" +
                        "    b.FIELD_NAME = MAX_FIELD_NAME\n" +
                        ") AND MAX_COUNT > 1").replace("_ENTITY_ID_", entityDB.getSingleId().getAsString())
                        .replace("_ENTITY_NAME_", entityDB.getName())
                        .replace("_BOOL_VAL1_", DriverFactory.getDriver().boolToNumber(false))
                        .replace("_BOOL_VAL2_", DriverFactory.getDriver().boolToNumber(true))
                )) {
            sl.add((String) list.get(0));
        }
        return sl;
    }

    public interface CallbackHook {
        void audit(Connection connection,AuditEntity auditEntity,EntityDB entityDB, FieldList changed);
    }


}
