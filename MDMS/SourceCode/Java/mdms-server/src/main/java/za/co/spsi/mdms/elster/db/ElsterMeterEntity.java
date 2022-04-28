package za.co.spsi.mdms.elster.db;

import za.co.spsi.mdms.common.db.interfaces.MeterEntity;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;

import java.sql.Timestamp;

import static za.co.spsi.mdms.common.db.interfaces.MeterEntity.Type.ELSTER;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@Table(version = 6)
public class ElsterMeterEntity extends EntityDB implements MeterEntity  {

    @Id(uuid = true)
    @Column(name = "METER_ID")
    public Field<String> meterId = new Field<>(this);

    public Field<String> name = new Field<>(this);
    public Field<String> description = new Field<>(this);

    @Column(name = "SERIAL_N")
    public Field<String> serialN = new Field<>(this);

    @Column(name = "LAST_COMMS_D")
    public FieldLocalDate<Timestamp> lastCommsD = new FieldLocalDate<>(this);

    @Column(name = "MAX_ENTRY")
    public FieldLocalDate<Timestamp> maxEntryTime = new FieldLocalDate<>(this);

    public ElsterMeterEntity() {
        super("ELSTER_METER");
    }

    @Override
    public String getMeterId() {
        return meterId.get();
    }

    @Override
    public String getMeterSerialN() {
        return serialN.get();
    }

    @Override
    public Timestamp getInstallationDate() {
        return null;
    }

    @Override
    public String getMeterType() {
        return ELSTER.name();
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
