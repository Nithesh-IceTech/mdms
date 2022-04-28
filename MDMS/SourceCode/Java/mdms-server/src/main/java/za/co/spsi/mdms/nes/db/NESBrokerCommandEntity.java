package za.co.spsi.mdms.nes.db;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import static za.co.spsi.mdms.common.MdmsConstants.Status;

/**
 * Created by johan on 2016/12/15.
 */
@Table(version = 5)
public class NESBrokerCommandEntity extends EntityDB {

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

    @Id(uuid = true)
    @Column(name = "NES_BROKER_COMMAND_ID")
    public Field<String> nesBrokerCommandId = new Field<>(this);

    @Column(name = "METER_ID")
    @ForeignKey(table = NESMeterEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterId = new Field<>(this);

    public EntityRef<NESMeterEntity> meterRef = new EntityRef<>(meterId,this);

    @Column(name = "METER_SERIAL_NUMBER")
    public Field<String> meterSerialNumber = new Field<>(this);

    @Column(name = "METER_SERIAL_NUMBER_LONG")
    public Field<String> meterSerialNumberLong = new Field<>(this);

    @Column(name = "IS_CT_METER")
    public Field<Boolean> isCTMeter = new Field<>(this);

    @Column(name = "GATEWAY_SERIAL_NUMBER_LONG")
    public Field<String> gatewaySerialNumberLong = new Field<>(this);

    @Column(name = "COMMAND", notNull = true)
    public Field<Integer> command = new Field<>(this);

    @Column(name = "COMMAND_STATUS", notNull = true)
    public Field<Integer> commandStatus = new Field<>(this);

    @Column(name = "DEVICE_COMMAND_STATUS")
    public Field<Integer> deviceCommandStatus = new Field<>(this);

    @Column(name = "GATEWAY_COMMAND_STATUS")
    public Field<Integer> gatewayCommandStatus = new Field<>(this);

    @Column(name = "DEVICE_COMMAND_TRACKING_ID")
    public Field<String> deviceCommandTrackingID = new Field<>(this);

    @Column(name = "DEVICE_COMMAND_RESULT_STATUS")
    public Field<String> deviceCommandResultStatus = new Field<>(this);

    @Column(name = "GATEWAY_COMMAND_TRACKING_ID")
    public Field<String> gatewayCommandTrackingID = new Field<>(this);

    @Column(name = "SUBMIT_DATE")
    public Field<Timestamp> submitDate = new Field<>(this);

    @Column(name = "EFFECTED_DATE")
    public Field<Timestamp> effectedDate = new Field<>(this);

    @Column(name = "CREATED_DATE")
    public FieldTimestamp createdDate = new FieldTimestamp(this);

    @Column(autoCrop = true, size = 4000)
    public FieldError error = new FieldError(this);

    public NESBrokerCommandEntity() {
        super("NES_BROKER_COMMAND");
    }

    public static List<NESBrokerCommandEntity> getByCommandStatus(javax.sql.DataSource dataSource, Status status) {
        return new DataSourceDB<>(NESBrokerCommandEntity.class).getAllAsList(dataSource,
                "select * from NES_BROKER_COMMAND where COMMAND_STATUS = ?",status.getCode());
    }

    public static List<NESBrokerCommandEntity> getLatest(javax.sql.DataSource dataSource, Timestamp timeThreshold) {
        return new DataSourceDB<>(NESBrokerCommandEntity.class).getAllAsList(dataSource,
                "select * from NES_BROKER_COMMAND where SUBMIT_DATE > ?", timeThreshold);
    }

    public static List<NESBrokerCommandEntity> getPendingCommands(javax.sql.DataSource dataSource, String meterID) {
        return new DataSourceDB<>(NESBrokerCommandEntity.class).getAllAsList(dataSource,
                "select * from NES_BROKER_COMMAND where METER_ID = ? and COMMAND_STATUS not in (?,?,?,?) ",
                meterID,
                Status.FAILED_WITH_REASON.getCode(),
                Status.FAILED_TIME_OUT.getCode(),
                Status.SUCCESSFUL.getCode(),
                Status.ERROR.getCode());
    }

    public static boolean meterHasPendingCommands(javax.sql.DataSource dataSource, String meterID) {
        List<NESBrokerCommandEntity> pendingMeterCommands = getPendingCommands(dataSource, meterID);
        return pendingMeterCommands.size() > 0;
    }

    public static void scheduleCreated(javax.sql.DataSource dataSource) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp nowDT = new Timestamp(System.currentTimeMillis());

        String sql = String.format("update NES_BROKER_COMMAND SET COMMAND_STATUS = %d WHERE " +
                        "COMMAND_STATUS = %d and ( EFFECTED_DATE is null or TO_DATE('%s','YYYY-MM-DD HH24:MI:SS') >= EFFECTED_DATE) ",
                Status.PROCESSING.getCode(), Status.CREATED.getCode(), dateTimeFormat.format(nowDT.getTime()));

        DataSourceDB.executeUpdate(dataSource, sql);
    }

}
