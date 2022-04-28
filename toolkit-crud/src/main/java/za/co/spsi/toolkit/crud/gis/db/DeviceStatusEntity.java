package za.co.spsi.toolkit.crud.gis.db;


import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;

@Exportable(name="deviceStatus")
@Table(version = 0)
public class DeviceStatusEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "DEVICE_STATUS_ID")
    public Field<String> deviceStatusId = new Field<String>(this);

    @Column(name = "TOTAL_DISK_SPACE")
    public Field<Long> totalDiskSpace = new Field<Long>(this);

    @Column(name = "FREE_DISK_SPACE")
    public Field<Long> freeDiskSpace = new Field<Long>(this);

    @Column(name = "ANDROID_VERSION")
    public Field<String> androidVersion = new Field<String>(this);

    @Column(name = "MOBILE_NETWORK")
    public Field<String> mobileNetwork = new Field<String>(this);

    @Column(name = "INSTALLED_APPS",size = 4000)
    public Field<String> installedApps = new Field<String>(this);

    @Column(name = "CAPTURE_TIME")
    public Field<Timestamp> captureTime = new Field<>(this);

    @Exportable(name = "deviceId")
    @ForeignKey(table = DeviceEntity.class, onDeleteAction = ForeignKey.Action.Cascade, deferrable = InitiallyDeferred)
    @Column(name = "DEVICE_ID")
    public Field<String> deviceId = new Field<String>(this);

    public DeviceStatusEntity() {
        super("DEVICE_STATUS");
    }

}
