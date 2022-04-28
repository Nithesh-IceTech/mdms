package za.co.spsi.mdms.nes.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by Arno Combrinck on 2020-10-29.
 */
public class NESDevicesMeterEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "DeviceID")
    public Field<String> deviceId = new Field<>(this);

    @Column(name = "SerialNumber")
    public Field<String> serialNumber = new Field<>(this);

    @Column(name = "LoadVoltageStatusTypeID")
    public Field<String> loadVoltageStatusTypeId = new Field<>(this);

    @Column(name = "LoadVltgeStatusTypeIDUpdteDtTm")
    public Field<String> loadVoltageStatusUpdateTime = new Field<>(this);

    @Column(name = "ControlRelayStatusTypeID")
    public Field<String> controlRelayStatusTypeId = new Field<>(this);

    @Column(name = "CntrlRelayStatusTypeIDUpdtDtTm")
    public Field<Timestamp> controlRelayStatusUpdateTime = new Field<>(this);

    public NESDevicesMeterEntity() {
        super("Devices_Meter");
    }

}
