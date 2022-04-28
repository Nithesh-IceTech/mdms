package za.co.spsi.mdms.generic.meter.db;

import za.co.spsi.mdms.common.db.interfaces.MeterEntity;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;

import java.sql.Timestamp;

import static za.co.spsi.mdms.common.db.interfaces.MeterEntity.Type.GENERIC;
import static za.co.spsi.mdms.common.db.MeterReadingGapProcessorJobEntity.JobTypes.WATER;

@Table(version = 13)
public class GenericMeterEntity extends EntityDB implements MeterEntity  {

    @Id(uuid = true)
    @Column(name = "GENERIC_METER_ID")
    public Field<String> genericMeterId = new Field<>(this);

    @Column(name = "METER_ID")
    public Field<String> meterId = new Field<>(this);

    @Column(name = "METER_SERIAL_N")
    public Field<String> meterSerialN = new Field<>(this);

    @Column(name = "METER_READING_ID")
    public Field<String> meterReadingId = new Field<>(this);

    public Field<Timestamp> timestamp = new Field<>(this);

    @Column(name = "METER_MAN_ID")
    public Field<String> meterManId = new Field<>(this);

    @Column(name = "METER_TYPE")
    public Field<String> meterType = new Field<>(this);

    @Column(name = "LIVE")
    public Field<Boolean> live = new Field<>(this);

    @Column(name = "LAST_COMMS_D")
    public FieldLocalDate<Timestamp> lastCommsD = new FieldLocalDate<>(this);

    @Column(name = "MAX_ENTRY")
    public FieldLocalDate<Timestamp> maxEntryTime = new FieldLocalDate<>(this);


    public GenericMeterEntity() {
        super("GENERIC_METER");
    }

    public GenericMeterEntity(String name, Field<String> genericMeterId, Field<String> meterId, Field<String> meterSerialN,
                              Field<String> meterReadingId, Field<Timestamp> timestamp, Field<String> meterManId,
                              Field<String> meterType, Field<Boolean> live, FieldLocalDate<Timestamp> lastCommsD,
                              FieldLocalDate<Timestamp> maxEntryTime) {
        super(name);
        this.genericMeterId = genericMeterId;
        this.meterId = meterId;
        this.meterSerialN = meterSerialN;
        this.meterReadingId = meterReadingId;
        this.timestamp = timestamp;
        this.meterManId = meterManId;
        this.meterType = meterType;
        this.live = live;
        this.lastCommsD = lastCommsD;
        this.maxEntryTime = maxEntryTime;
    }

    public boolean isWater() {
        return WATER.name().equals(meterType.get());
    }

    @Override
    public String getMeterId() {
        return meterId.get();
    }

    @Override
    public String getMeterSerialN() {
        return meterSerialN.get();
    }

    @Override
    public Timestamp getInstallationDate() {
        return null;
    }

    @Override
    public String getMeterType() {
        return GENERIC.name();
    }

    @Override
    public Timestamp getLastCommsD() {
        return lastCommsD.get();
    }

    @Override
    public Timestamp getMaxEntryTime() {
        return maxEntryTime.get();
    }
}
