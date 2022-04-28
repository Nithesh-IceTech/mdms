package za.co.spsi.mdms.common.db;

import lombok.SneakyThrows;
import za.co.spsi.mdms.common.db.interfaces.MeterEntity;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;

@Table(version = 4, maintenanceSql = "delete from meter_reading_gap_processor where run_time < {LAST_MONTH} and STATUS = 'COMPLETED'")
public class MeterReadingGapProcessorJobEntity extends EntityDB {

    public enum Status {
        CREATED,
        STARTED,
        COMPLETED,
        DATASET_EMPTY,
        FAILED,
        TEST;
    }

    public enum MeterTypes {
        NES,
        KAMSTRUP,
        ELSTER,
        GENERIC
    }

    public enum JobTypes {
        GENERAL,
        TOTAL_ENERGY,
        T1_ENERGY,
        T2_ENERGY,
        CURRENT,
        VOLTAGE,
        WATER
    }

    @Id(uuid = true)
    @Column(name = "METER_READING_GAP_PROCESSOR_ID")
    public Field<String> meterReadingGapProcessorId = new Field<>(this);

    @Column(name = "METER_ID")
    public Field<String> meterId = new Field<>(this);

    @Column(name = "METER_SERIAL_N")
    public Field<String> meterSerialN = new Field<>(this);

    @Column(name = "METER_TYPE")
    public Field<String> meterType = new Field<>(this);

    @Column(name = "JOB_TYPE")
    public Field<String> jobType = new Field<>(this);

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

    @Column(name = "TEST", size = 2048)
    public Field<String> test = new Field<>(this);

    public MeterReadingGapProcessorJobEntity() {
        super("METER_READING_GAP_PROCESSOR");
    }

    public static void resetStarted(DataSource dataSource) {
        DSDB.executeUpdate(dataSource,"update meter_reading_gap_processor set status = ? where status = ?"
                , MeterReadingGapProcessorJobEntity.Status.CREATED.name(), MeterReadingGapProcessorJobEntity.Status.STARTED.name());
    }

    @SneakyThrows
    public static MeterReadingGapProcessorJobEntity create(Connection connection, MeterEntity meter, String jobType, Timestamp start, Timestamp end, Timestamp runTime, Integer interval) {
        MeterReadingGapProcessorJobEntity entity = new MeterReadingGapProcessorJobEntity ();
        entity.meterId.set(meter.getMeterId());
        entity.meterSerialN.set(meter.getMeterSerialN());
        entity.meterType.set(meter.getMeterType());
        entity.jobType.set(jobType);
        entity.startTime.set(start);
        entity.endTime.set(end);
        entity.runTime.set(runTime);
        entity.status.set(MeterReadingGapProcessorJobEntity.Status.CREATED.name());
        entity.interval.set(interval);
        return DataSourceDB.set(connection,entity);
    }

}
