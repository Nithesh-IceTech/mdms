package za.co.spsi.toolkit.db.upgrade;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Connection;
import java.util.UUID;

/**
 * Created by jaspervdbijl on 2017/03/10.
 */
@Table(version = 10)
public class DeployLogEntity extends EntityDB {

    public enum Status {
        CREATED((short) 0), STARTED((short) 1), COMPLETED((short) 2), FAILED((short) 3);

        short code;

        Status(short code) {
            this.code = code;
        }

        public short getCode() {
            return code;
        }
    }

    @Id(uuid = true)
    @Column(name = "deploy_log_id", notNull = true, size = 50)
    public Field<String> deployLogId = new Field<>(this);

    @Column(name = "create_time", notNull = true, size = 50)
    public FieldTimestamp createTime = new FieldTimestamp(this);
    @Column(name = "start_time", size = 50)
    public FieldTimestamp startTime = new FieldTimestamp(this);
    @Column(name = "complete_time", size = 50)
    public FieldTimestamp completeTime = new FieldTimestamp(this).onUpdate();
    public Field<Long> duration = new Field<Long>(this);

    public Field<Short> status = new Field<Short>(this);

    public Field<String> driver = new Field<>(this);
    @Column(name = "url", size = 500)
    public Field<String> url = new Field<>(this);
    public Field<String> username = new Field<>(this);
    public Field<String> password = new Field<>(this);

    @Column(name = "role_name")
    public Field<String> roleName = new Field<>(this);
    public Field<String> owner = new Field<>(this);


    @Column(size = 10240)
    public FieldError error = new FieldError(this);

    @Column(size = 8000)
    public Field<String> sql = new Field<>(this);

    public DeployLogEntity() {
        super("deploy_log");
    }

    public void appendSql(String sql) {
        this.sql.set((this.sql.get() == null ? "" : this.sql.get()) + "\n--------------\n\n" + sql);
    }

    @Override
    public boolean beforeUpdateEvent(Connection connection) {
        super.beforeUpdateEvent(connection);
        duration.set(completeTime.get().getTime() - startTime.get().getTime());
        return true;
    }

    public void clearProperties(javax.sql.DataSource dataSource) {
        username.set(null);
        password.set(null);
        DataSourceDB.executeUpdate(dataSource, "update deploy_log set username = null, password = null");
    }

    public void setDeployLogId() {
        deployLogId.set(UUID.randomUUID().toString());
    }

}
