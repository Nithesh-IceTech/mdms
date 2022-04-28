package za.co.spsi.mdms.nes.db;

import za.co.spsi.mdms.common.db.interfaces.MeterEntity;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;

import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@Table(version = 5)
public class NESMeterEntity extends EntityDB implements MeterEntity  {

    @Id(uuid = true)
    @Column(name = "METER_ID")
    public Field<String> meterId = new Field<>(this);

    public Field<String> name = new Field<>(this);
    public Field<String> description = new Field<>(this);
    public Field<String> state = new Field<>(this);

    @Column(name = "TYPE_DESC")
    public Field<String> typeDescription = new Field<>(this);

    @Column(name = "GATEWAY_ID")
    public Field<String> gatewayId = new Field<>(this);

    @Column(name = "SERIAL_N")
    public Field<String> serialN = new Field<>(this);

    @Column(name = "INSTALLATION_DATE")
    public Field<Timestamp> installationDate = new Field<>(this);

    @Column(name = "HARDWARE_VERSION")
    public Field<String> hardwareVersion = new Field<>(this);

    @Column(name = "SOFTWARE_VERSION")
    public Field<String> softwareVersion = new Field<>(this);

    @Column(name = "STATUS_ON", defaultValue = "1")
    public Field<Boolean> statusOn = new Field<>(this);

    @Column(name = "REQUEST_STATUS_ON", defaultValue = "0")
    public Field<Boolean> requestStatusOn = new Field<>(this);

    @Column(name = "LAST_COMMS_D")
    public FieldLocalDate<Timestamp> lastCommsD = new FieldLocalDate<>(this);

    @Column(name = "MAX_ENTRY")
    public FieldLocalDate<Timestamp> maxEntryTime = new FieldLocalDate<>(this);


    public NESMeterEntity() {
        super("NES_METER");
    }

    public NESMeterEntity(NESDeviceEntity device) {
        this();
        init(device);
    }

    public NESMeterEntity init(NESDeviceEntity device) {
        name.set(device.name.get());
        description.set(device.description.get());
        serialN.set(device.serialNumber.get());
        installationDate.set(device.installationDateTime.get());
        hardwareVersion.set(device.hardwareVersion.get());
        softwareVersion.set(device.softwareVersion.get());
        return this;
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
        return installationDate.get();
    }

    @Override
    public String getMeterType() {
        return "NES";
    }

//    @Override
//    public Boolean getWater() {
//        return null;
//    }
//
//    @Override
//    public Boolean getLive() {
//        return null;
//    }

    @Override
    public Timestamp getLastCommsD() {
        return lastCommsD.get();
    }

    @Override
    public Timestamp getMaxEntryTime() {
        return maxEntryTime.get();
    }
}
