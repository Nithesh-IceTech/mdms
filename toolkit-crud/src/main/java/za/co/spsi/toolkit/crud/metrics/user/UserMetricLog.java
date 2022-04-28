package za.co.spsi.toolkit.crud.metrics.user;

import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.login.UserDetailEntity;
import za.co.spsi.toolkit.crud.util.broadcast.BroadcastRegister;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.audit.AuditEntity;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.SetNull;

/**
 * Created by jaspervdbijl on 2017/06/22.
 */
@Table(version = 1)
public class UserMetricLog extends EntityDB {

    @Id(uuid = true)
    @Column(name = "ID", size = 50)
    public Field<String> id = new Field<>(this);

    @ForeignKey(table = UserDetailEntity.class, onDeleteAction = SetNull)
    @Column(name = "username", size = 50)
    public Field<String> username = new Field<>(this);

    @ForeignKey(table = AuditEntity.class,onDeleteAction = SetNull)
    @Column(name = "audit_log_id", size = 50, notNull = true)
    public Field<String> auditLogId = new Field<>(this);

    @ForeignKey(table = AuditEntity.class,onDeleteAction = SetNull)
    @Column(name = "prev_audit_log_id", size = 50)
    public Field<String> prevAuditLogId = new Field<>(this);

    @ForeignKey(table = MetricCategory.class,onDeleteAction = SetNull)
    @Column(name = "metric_category_id", size = 50, notNull = true)
    public Field<String> metricCategoryId = new Field<>(this);

    @Column(size = 1024,autoCrop = true)
    public Field<String> field = new Field<>(this);

    @Column(size = 1024)
    public Field<String> description = new Field<>(this);

    public Field<Integer> point = new Field<>(this);

    @Column(name = "log_time")
    public FieldTimestamp logTime = new FieldTimestamp(this);

    public Index userIdx = new Index("umlUserIdx", this, username);

    public Index userCatIdx = new Index("umlUserCatIdx", this, username, metricCategoryId);

    public UserMetricLog() {
        super("USER_METRIC_LOG");
    }

    private static String getMessage(AuditEntity.Type type, String field, Integer point, String username, String prevUser) {
        return type.equals(AuditEntity.Type.UPDATE) ?
                String.format("%s fixed field %s from %s. %d points deducted", username, field, prevUser, Math.abs(point)) :
                type.equals(AuditEntity.Type.DELETE) ?
                        String.format("%s deleted field %s from %s. %d points deducted", username, field, prevUser, Math.abs(point)) :
                        String.format("%s created field %s. %d points added", username, field, prevUser, Math.abs(point));
    }

    public UserMetricLog init(String username, AuditEntity auditEntity, AuditEntity prevAuditEntity, MetricCategory mc, String field, String desc, Integer point) {
        this.username.set(username);
        this.auditLogId.set(auditEntity.auditLogId.get());
        this.prevAuditLogId.set(prevAuditEntity != null ? prevAuditEntity.auditLogId.get() : null);
        this.metricCategoryId.set(mc.id.get());
        this.field.set(field);
        this.point.set(point);
        this.description.set(desc);
        return this;
    }

    private static void updateUser(Connection connection,String userId,Integer point) {
        UserDetailEntity user = (UserDetailEntity) DataSourceDB.getFromSet(connection, (EntityDB) new UserDetailEntity().username.set(userId.toUpperCase()));
        if (user != null) {
            DataSourceDB.set(connection, (EntityDB) user.metricTotal.set(user.metricTotal.getNonNull() + point));
        }
    }

    public static void createLog(Connection connection, EntityDB entity, AuditEntity auditEntity, MetricCategory mc, FieldList changed) {
        List<String> messages = new ArrayList<>();
        AuditEntity.Type type = auditEntity.getType();
        if (auditEntity.getType().equals(AuditEntity.Type.CREATE)) {
            messages.add(String.format("%s created %s. %d points added", auditEntity.userId.get(), entity.getName(),Math.abs(mc.getPoint(auditEntity.getType()) * changed.size())));
            DataSourceDB.set(connection, new UserMetricLog().init(auditEntity.userId.get(), auditEntity, null, mc, changed.getNames().toString(","),
                    messages.get(messages.size()-1),
                    mc.getPoint(auditEntity.getType()) * changed.size()));
            updateUser(connection,auditEntity.userId.get(),mc.getPoint(auditEntity.getType()) * changed.size());
        } else {
            changed.stream().forEach(f -> {
                AuditEntity prevUser = AuditEntity.getPrevAudit(connection, auditEntity,entity, type, f.getName());
                if (prevUser != null && prevUser.userId.get() != null) {
                    messages.add(type.equals(AuditEntity.Type.UPDATE) ?
                            String.format("%s fixed field %s from %s. %d points deducted", auditEntity.userId.get(), f.getName(), prevUser.userId.get(), Math.abs(mc.getPoint(type))) :
                            String.format("%s deleted field %s from %s. %d points deducted", auditEntity.userId.get(), f.getName(), prevUser.userId.get(), Math.abs(mc.getPoint(type))));
                    DataSourceDB.set(connection, new UserMetricLog().init(prevUser.userId.get(), auditEntity, prevUser, mc, f.getName(),
                            messages.remove(messages.size()-1),mc.getPoint(type) * -1));
                    updateUser(connection,prevUser.userId.get(),mc.getPoint(type)*-1);
                }
                messages.add(type.equals(AuditEntity.Type.UPDATE) ?
                        String.format("%s fixed field %s%s. %d points added", auditEntity.userId.get(), f.getName(),
                                prevUser != null && prevUser.userId.get() != null ? String.format(" from %s", prevUser.userId.get()) : "",
                                Math.abs(mc.getPoint(type))) :
                        String.format("%s deleted field %s%s. %d points added", auditEntity.userId.get(), f.getName(),
                                prevUser != null && prevUser.userId.get() != null ? String.format(" from %s", prevUser.userId.get()) : "",
                                Math.abs(mc.getPoint(type))));
                DataSourceDB.set(connection, new UserMetricLog().init(auditEntity.userId.get(), auditEntity, prevUser, mc, f.getName(),
                        messages.get(messages.size()-1),mc.getPoint(type)));
                updateUser(connection,auditEntity.userId.get(),mc.getPoint(type));
            });
        }
        if (!messages.isEmpty() && ToolkitCrudConstants.isAgencySet()) {
            BroadcastRegister.broadcast(ToolkitCrudConstants.getChildAgencyId(), messages.stream().reduce((a, b) -> a + "\n" + b).get());
        }
    }

    @Override
    public boolean beforeUpdateEvent(Connection connection) {
        super.beforeUpdateEvent(connection);
        username.set(username.get().toUpperCase());
        return true;
    }

    @Override
    public boolean beforeInsertEvent(Connection connection) {
        super.beforeInsertEvent(connection);
        username.set(username.get().toUpperCase());
        return true;
    }
}
