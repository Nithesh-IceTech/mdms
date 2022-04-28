package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;

@Table(version = 5)
public class KamstrupGapProcessorJobEntity extends EntityDB {

    public enum Status {
        CREATED,
        STARTED,
        COMPLETED,
        DATASET_EMPTY,
        FAILED;
    }

    public enum JobTypes {
        GENERAL,
        TOTAL_ENERGY,
        T1_ENERGY,
        T2_ENERGY,
        CURRENT,
        VOLTAGE,
        WATER;
    }

    @Id(uuid = true)
    @Column(name = "KAMSTRUP_GAP_PROCESSOR_ID")
    public Field<String> kamstrupGapProcessorId = new Field<>(this);

    @Column(name = "METER_ID")
    @ForeignKey(table = KamstrupMeterEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterId = new Field<>(this);

    @Column(name = "JOB_TYPE")
    public Field<String> jobType = new Field<>(this);

    @Column(name = "GROUP_ID")
    public Field<String> groupId = new Field<>(this);

    @Column(name = "START_TIME")
    public FieldLocalDate<Timestamp> startTime = new FieldLocalDate<>(this);

    @Column(name = "END_TIME")
    public FieldLocalDate<Timestamp> endTime = new FieldLocalDate<>(this);

    @Column(name = "RUN_TIME")
    public Field<Timestamp> runTime = new Field<>(this);

    @Column(name = "STATUS")
    public Field<String> status = new Field<>(this);

    @Column(name = "INTERVAL", defaultValue = "30")
    public Field<Integer> interval = new Field<>(this);

    @Column(name = "ERROR", size = 2048)
    public FieldError error = new FieldError(this);

    public EntityRef<KamstrupMeterEntity> meter = new EntityRef<>(meterId,this);

    public KamstrupGapProcessorJobEntity() {
        super("KAMSTRUP_GAP_PROCESSOR");
    }

    public static void resetStarted(DataSource dataSource) {
        DSDB.executeUpdate(dataSource,"update kamstrup_gap_processor set status = ? where status = ?"
                ,Status.CREATED.name(),Status.STARTED.name());
    }

    public static KamstrupGapProcessorJobEntity create(Connection connection, KamstrupMeterEntity meter,
            String jobType, String groupId, Timestamp start, Timestamp end, Timestamp runTime,Integer interval) {
        KamstrupGapProcessorJobEntity entity = new KamstrupGapProcessorJobEntity();
        entity.meterId.set(meter.meterId.get());
        entity.jobType.set(jobType);
        entity.groupId.set(groupId);
        entity.startTime.set(start);
        entity.endTime.set(end);
        entity.runTime.set(runTime);
        entity.status.set(Status.CREATED.name());
        entity.interval.set(interval);
        return DSDB.set(connection,entity);
    }

}
