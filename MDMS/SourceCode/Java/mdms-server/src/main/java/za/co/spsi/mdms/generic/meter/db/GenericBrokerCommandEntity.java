package za.co.spsi.mdms.generic.meter.db;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.common.MdmsConstants.Status;

@Table(version = 1)
public class GenericBrokerCommandEntity extends EntityDB {

    public static final Logger TAG = Logger.getLogger(GenericBrokerCommandEntity.class.getName());

    public enum Command {
        CONNECT(0),
        DISCONNECT(1);

        int code;

        Command(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

    }

    public static String[] getCommandOptions() {
        return Arrays.stream(GenericBrokerCommandEntity.Command.values()).map(s -> s.name()).collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});
    }

    public static String[] getCommandValues() {
        return Arrays.stream(GenericBrokerCommandEntity.Command.values()).map(s -> s.getCode()+"").collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});
    }

    @Id(uuid = true)
    @Column(name = "GENERIC_BROKER_COMMAND_ID")
    public Field<String> genericBrokerCommandId = new Field<>(this);

    @Column(name = "METER_ID")
    @ForeignKey(table = GenericMeterEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterId = new Field<>(this);

    @Column(name = "COMMAND", notNull = true)
    public Field<Integer> command = new Field<>(this);

    @Column(name = "STATUS")
    public Field<Integer> status = new Field<>(this);

    @Column(name = "FAILED_REASON")
    public Field<String> failedReason = new Field<>(this);

    @Column(name = "CREATED_DATE")
    public Field<Timestamp> createdDate = new Field<>(this);

    @Column(name = "STATUS_UPDATE_DATE")
    public Field<Timestamp> statusUpdateDate = new Field<>(this);

    @Column(name = "USER_ID")
    public Field<String> userId = new Field<>(this);

    @Column(name = "EFFECTED_DATE")
    public Field<Timestamp> effectedDate = new Field<>(this);

    @Column(name = "SUBMIT_RETRY_COUNT")
    public Field<Integer> submitRetryCount = new Field<>(this);

    @Column(name = "SCHEDULED_RETRY_COUNT")
    public Field<Integer> scheduledRetryCount = new Field<>(this);

    @Column(name = "REF")
    public Field<String> ref = new Field<>(this);

    @Column(size = 1024)
    public FieldError error = new FieldError(this);

    private Index idxMeterId = new Index("idx_GEN_BROKER_CMD_METER_ID", this, meterId);

    public EntityRef<GenericMeterEntity> meterRef = new EntityRef<>(meterId, this);

    public GenericBrokerCommandEntity() {
        super("GENERIC_BROKER_COMMAND");
    }

    public static void scheduleCreated(javax.sql.DataSource dataSource) {
        DataSourceDB.executeUpdate(dataSource, "update GENERIC_BROKER_COMMAND SET STATUS = ? WHERE " +
                "STATUS = ? and ( EFFECTED_DATE is null or CURRENT_DATE >= EFFECTED_DATE) ", Status.PROCESSING.getCode(), Status.CREATED.getCode());
    }

    public static List<GenericBrokerCommandEntity> getByStatus(javax.sql.DataSource dataSource, Status status) {
        return new DataSourceDB<>(GenericBrokerCommandEntity.class).getAllAsList(dataSource,
                "select * from GENERIC_BROKER_COMMAND where STATUS = ?", status.getCode());
    }

    public static List<GenericBrokerCommandEntity> getLatest(javax.sql.DataSource dataSource, Timestamp timeThreshold) {
        return new DataSourceDB<>(GenericBrokerCommandEntity.class).getAllAsList(dataSource,
                "select * from GENERIC_BROKER_COMMAND where CREATED_DATE > ?", timeThreshold);
    }

    public void setAsCreated() {
        setStatus(Status.CREATED);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        this.createdDate.set(currentTime);

    }

    public void setStatus(Status status) {
        this.status.set(status.getCode());
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        this.statusUpdateDate.set(currentTime);
    }

}
