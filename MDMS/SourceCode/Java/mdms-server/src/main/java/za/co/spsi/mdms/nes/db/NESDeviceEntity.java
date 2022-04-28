package za.co.spsi.mdms.nes.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/12.
 */
public class NESDeviceEntity extends EntityDB {

    @Id(uuid = true)
    public Field<String> deviceId = new Field<>(this);

    public Field<String> name = new Field<>(this);

    public Field<String> description = new Field<>(this);

    public Field<String> gatewayId = new Field<>(this);

    public Field<String> serialNumber = new Field<>(this);

    public Field<Timestamp> installationDateTime = new Field<>(this);

    public Field<String> hardwareVersion = new Field<>(this);

    public Field<String> softwareVersion = new Field<>(this);

    public NESDeviceEntity() {
        super("Devices");
    }


}
