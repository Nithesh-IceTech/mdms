package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.services.order.domain.MeterResults;
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

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.common.MdmsConstants.Status;

/**
 * Created by johan on 2016/11/15.
 *
 */
@Table(version = 4)
public class KamstrupBrokerCommandEntity extends EntityDB {

    public static final Logger TAG = Logger.getLogger(KamstrupBrokerCommandEntity.class.getName());

    public enum Command {
        READ(0,"read"),
        CUT(1,"cutoff"),
        RELEASE(2,"release"),
        CONNECT(3,"connect");

        int code;
        String description;
        Command(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public int getCode() {
            return code;
        }
        public String getDescription() {return this.description;}

    }

    public static String[] getCommandOptions() {
        return Arrays.stream(Command.values()).map(s -> s.name()).collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});
    }

    public static String[] getCommandValues() {
        return Arrays.stream(Command.values()).map(s -> s.getCode()+"").collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});
    }

    public static String[] getStatusOptions() {
        return Arrays.stream(Status.values()).map(s -> s.name()).collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});
    }

    public static String[] getStatusValues() {
        return Arrays.stream(Status.values()).map(s -> s.getCode()+"").collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});
    }

    @Id(uuid = true)
    @Column(name = "BROKER_COMMAND_ID")
    public Field<String> brokerCommandId = new Field<>(this);

    @Column(name = "METER_ID")
    @ForeignKey(table = KamstrupMeterEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterId = new Field<>(this);


    @Column(name = "COMMAND", notNull = true)
    public Field<Integer> command = new Field<>(this);

    @Column(name = "STATUS")
    public Field<Integer> status = new Field<>(this);

    @Column(name = "FAILED_REASON")
    public Field<String> failedReason = new Field<>(this);

    @Column(name = "METER_ID_URL", size = 250)
    public Field<String> meterIDURL = new Field<>(this);

    @Column(name = "CREATED_DATE")
    public Field<Timestamp> createdDate = new Field<>(this);

    @Column(name = "STATUS_UPDATE_DATE")
    public Field<Timestamp> statusUpdateDate = new Field<>(this);

    @Column(name = "COMMAND_TIME_OUT_COUNT")
    public Field<Integer> commandTimeOutCount = new Field<>(this);

    @Column(name = "ORDER_URL")
    public Field<String> orderURL = new Field<>(this);

    @Column(name = "ORDER_STATUS_URL")
    public Field<String> orderStatusURL = new Field<>(this);

    @Column(name = "ORDER_COMPLETED_URL")
    public Field<String> orderCompletedURL = new Field<>(this);

    @Column(name = "USER_ID")
    public Field<String> userId = new Field<>(this);

    @Column(name = "EFFECTED_DATE")
    public Field<Timestamp> effectedDate = new Field<>(this);

    @Column(size = 1024)
    public FieldError error = new FieldError(this);

    private Index idxMeterId = new Index("idx_BROKER_CMD_METER_ID",this,meterId);

    public EntityRef<KamstrupMeterEntity> meterRef = new EntityRef<>(meterId,this);

    public KamstrupBrokerCommandEntity() {
        super("BROKER_COMMAND");
    }

    public static void scheduleCreated(javax.sql.DataSource dataSource) {
      DataSourceDB.executeUpdate(dataSource,"update BROKER_COMMAND SET STATUS = ? WHERE " +
              "STATUS = ? and ( EFFECTED_DATE is null or CURRENT_DATE >= EFFECTED_DATE) ", Status.PROCESSING.getCode(),Status.CREATED.getCode());
    }

    public static List<KamstrupBrokerCommandEntity> getTimedOutCommands(javax.sql.DataSource dataSource, long timeOutInMillis) {
        long thresoldTime = System.currentTimeMillis() - timeOutInMillis;
        Date thresholdTimeStamp = new Date(thresoldTime);
        return new DataSourceDB<>(KamstrupBrokerCommandEntity.class).getAllAsList(dataSource,
                "select * from BROKER_COMMAND where STATUS = ? and STATUS_UPDATE_DATE < ? ", Status.WAITING.getCode(), thresholdTimeStamp);

    }

    public static List<KamstrupBrokerCommandEntity> getByStatus(javax.sql.DataSource dataSource, Status status) {
            return new DataSourceDB<>(KamstrupBrokerCommandEntity.class).getAllAsList(dataSource,
                    "select * from BROKER_COMMAND where STATUS = ?",status.getCode());
    }

    public static List<KamstrupBrokerCommandEntity> getLatest(javax.sql.DataSource dataSource, Timestamp timeThreshold) {
        return new DataSourceDB<>(KamstrupBrokerCommandEntity.class).getAllAsList(dataSource,
                "select * from BROKER_COMMAND where CREATED_DATE > ?", timeThreshold);
    }

    public static List<KamstrupBrokerCommandEntity> getPendingCommands(javax.sql.DataSource dataSource, String meterID) {
        return new DataSourceDB<>(KamstrupBrokerCommandEntity.class).getAllAsList(dataSource,
                "select * from BROKER_COMMAND where METER_ID = ? and STATUS not in (?,?,?,?) ", meterID, Status.FAILED_TIME_OUT.getCode(), Status.FAILED_WITH_REASON.getCode(),
                Status.SUCCESSFUL.getCode(),Status.ERROR.getCode());
    }

    public static boolean meterHasPendingCommands(javax.sql.DataSource dataSource, String meterID) {
        List<KamstrupBrokerCommandEntity> pendingMeterCommands = getPendingCommands(dataSource, meterID);
        return pendingMeterCommands.size() > 0;
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

    public KamstrupBrokerCommandEntity failedWithReason(RestHelper helper) {
        setStatus(Status.FAILED_WITH_REASON);
        // get the reason code
        try {
            if (orderCompletedURL.get() != null) {
                MeterResults result = (MeterResults) helper.restGet(orderCompletedURL.get(),MeterResults.class);
                failedReason.set(Arrays.stream(result.meterResult).findFirst().get().commandResults.faultedCommandResult.description);
            }
        } catch (Exception ex) {
            // only log this
            TAG.log(Level.WARNING,ex.getMessage(),ex);
        }
        return this;
    }
}
