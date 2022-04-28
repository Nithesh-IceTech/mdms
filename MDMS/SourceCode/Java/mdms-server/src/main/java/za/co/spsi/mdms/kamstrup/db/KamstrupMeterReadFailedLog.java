package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.mdms.kamstrup.services.order.domain.commands.FaultedCommandResult;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.LoggerCommand;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.LocalTimestamp;
import za.co.spsi.toolkit.util.StringList;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by jaspervdbijl on 2017/03/08.
 */
@Table(version = 0, deleteOldRecords = true)
public class KamstrupMeterReadFailedLog extends EntityDB {

    public static final int ERROR_LEN = 3096;

    public enum Status {
        LOGGED(0), UPDATED(1), STARTED(2), PROCESSED(3), FAILED(4), CANCELLED(5), IGNORED(6),DELETED(7);

        int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    @Id(uuid = true)
    @Column(name = "ID")
    public Field<String> id = new Field<>(this);

    @Column(name = "METER_ID")
    @ForeignKey(table = KamstrupMeterEntity.class, onDeleteAction = ForeignKey.Action.Cascade,name = "KAMRFL_METER_ID_FKEY")
    public Field<String> meterId = new Field<>(this);

    @Column(name = "ORDER_ID")
    @ForeignKey(table = KamstrupMeterOrderEntity.class, onDeleteAction = ForeignKey.Action.Cascade,name = "KMRFL_ORDER_ID_FKEY")
    public Field<String> orderId = new Field<>(this);

    @Column(name = "RE_ORDER_ID")
    @ForeignKey(table = KamstrupMeterOrderEntity.class, onDeleteAction = ForeignKey.Action.Cascade,name = "KMRFL_RE_ORDER_ID_FKEY")
    public Field<String> reOrderId = new Field<>(this);

    @Column(name = "CREATED", notNull = true)
    public FieldTimestamp created = new FieldTimestamp(this);

    @Column(name = "FROM_DATE")
    public Field<Timestamp> fromDate = new Field<Timestamp>(this);

    @Column(name = "TO_DATE")
    public Field<Timestamp> toDate = new Field<Timestamp>(this);

    @Column(name = "LOGGER_ID")
    public Field<String> loggerId = new Field<>(this);

    @Column(name = "REGISTERS")
    public Field<String> registers = new Field<>(this);

    @Column(name = "COMMAND_REF", size = 2048)
    public Field<String> commandRef = new Field<>(this);

    public Field<Integer> status = new Field<>(this);

    @Column(name = "ERROR", defaultValue = "0", size = ERROR_LEN)
    public FieldError error = new FieldError(this);

    public Index idxCancel = new Index("KMRFL_IDX_CANCEL",this,meterId,status,fromDate,toDate);


    public KamstrupMeterReadFailedLog() {
        super("KAM_METER_READ_FAILED_LOG");
    }

    public KamstrupMeterReadFailedLog(KamstrupMeterEntity meter, KamstrupMeterOrderEntity order, FaultedCommandResult result) {
        this();
        this.meterId.set(meter.meterId.get());
        this.orderId.set(order.meterOrderId.get());
        this.status.set(Status.LOGGED.code);
        this.commandRef.set(result.command.ref);
    }

    public static DataSourceDB<KamstrupMeterReadFailedLog> getByStatus(Connection connection, Status status) {
        return new DataSourceDB<>(KamstrupMeterReadFailedLog.class).getAll(connection,
                "select * from KAM_METER_READ_FAILED_LOG where STATUS = ? order by created asc", status.getCode());
    }

    public KamstrupMeterReadFailedLog update(LoggerCommand command) {
        registers.set(
                Arrays.stream(command.logger.registers.registers)
                        .map(s -> s.id).collect(Collectors.toCollection(StringList::new)).toString(","));
        loggerId.set(command.logger.id);
        fromDate.set(command.logger.fromDate);
        toDate.set(command.logger.toDate);
        return this;
    }

    public static void cancel(Connection connection,String meterId,Timestamp fromDate,Timestamp toDate) {
        try {
            DataSourceDB.executeUpdate(connection,"update kam_meter_read_failed_log set status = ? where meter_id = ? and status in (?,?) and from_date = ? and to_date = ?",
                    Status.CANCELLED.getCode(),meterId,Status.LOGGED.getCode(),Status.UPDATED.getCode(),fromDate,toDate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearOldLogs(DataSource dataSource,int days) {
        DataSourceDB.executeUpdate(dataSource,"update kam_meter_read_failed_log set status = ? where created < ?",Status.DELETED.code,new LocalTimestamp().plusDays(days*-1));
        DataSourceDB.execute(dataSource,"delete from kam_meter_read_failed_log where created < ?",new LocalTimestamp().plusDays(days*-30));
    }

}
