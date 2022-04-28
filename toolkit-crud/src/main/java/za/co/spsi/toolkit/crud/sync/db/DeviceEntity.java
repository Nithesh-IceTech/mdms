/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.toolkit.crud.sync.db;

import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.crud.gis.db.DeviceLocationEntity;
import za.co.spsi.toolkit.crud.gis.db.DeviceStatusEntity;
import za.co.spsi.toolkit.crud.gis.db.DeviceTrackedEntity;
import za.co.spsi.toolkit.crud.gis.db.TripEntity;
import za.co.spsi.toolkit.dao.RegisterDeviceReq;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;

import javax.sql.DataSource;
import java.sql.Timestamp;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;


/**
 * @author francoism
 */
@Table(version = 5)
@Exportable(name = "deviceTracked")
public class DeviceEntity extends AuditEntityDB {

    @Id(uuid = true)
    @Column(name = "DEVICE_ID")
    public Field<String> deviceId = new Field<>(this);

    @Column(name = "REG_ID")
    public Field<String> regId = new Field<>(this);

    @Column(name = "IMEI")
    public Field<String> imei = new Field<>(this);

    @Column(name = "APK_VERSION", size = 50)
    public Field<String> apkVersion = new Field<>(this);

    @Column(name = "APK_DATE")
    public Field<Timestamp> apkD = new Field<>(this);

    @Column(name = "LAST_COMMS_DATE")
    public Field<Timestamp> lastCommsDate = new Field<>(this);

    @Column(name = "DEVICE_ALIAS")
    public Field<String> deviceAlias = new Field<String>(this);

    public DeviceTrackedEntity deviceTracked = new DeviceTrackedEntity(this);

    @Column(name = "LATEST_DEVICE_LOCATION_ID")
    @ForeignKey(table = DeviceLocationEntity.class, field = "DEVICE_LOCATION_ID", deferrable = InitiallyDeferred, onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> latestDeviceLocationId = new Field<String>(this);

    // tracking ref's
    @Exportable(name = "deviceStatus")
    public EntityRef<DeviceStatusEntity> deviceStatus = new EntityRef<DeviceStatusEntity>(this);

    @Exportable(name = "deviceLocations")
    public EntityRef<DeviceLocationEntity> locations = new EntityRef<DeviceLocationEntity>(this);

    @Exportable(name = "trips")
    public EntityRef<TripEntity> trips = new EntityRef<TripEntity>(this);

    public EntityRef<DeviceLocationEntity> latestLocation = new EntityRef<>(latestDeviceLocationId, this);

    public Index imeiIndex = new Index("imeiIndex", this, imei).setUnique();

    public DeviceEntity() {
        super("DEVICE");
    }

    public DeviceEntity init(RegisterDeviceReq registerDeviceReq) {
        deviceId.set(registerDeviceReq.getDeviceId());
        regId.set(registerDeviceReq.getRegId());
        imei.set(registerDeviceReq.getImei());
        agencyId.set(registerDeviceReq.getAgencyId());
        return this;
    }

    public String getDisplayName() {
        return deviceAlias.get() != null ? String.format("%s - %s",deviceAlias.get(),imei.get()) : imei.get();
    }

    public static void registerDevice(RegisterDeviceReq registerDeviceReq, DataSource dataSource) {
        final DeviceEntity deviceEntity = DataSourceDB.get(DeviceEntity.class, dataSource, "select * from device where imei = ?", registerDeviceReq.getImei());

        if (deviceEntity != null) {
            // relink device
            DataSourceDB.executeInTx(dataSource, connection -> {
                DataSourceDB.executeUpdate(connection, "update device set device_id = ? where device_id = ?", registerDeviceReq.getDeviceId(), deviceEntity.deviceId.get());
                DataSourceDB.executeUpdate(connection, "update device_status set device_id = ? where device_id = ?", registerDeviceReq.getDeviceId(), deviceEntity.deviceId.get());
                DataSourceDB.executeUpdate(connection, "update device_location set device_id = ? where device_id = ?", registerDeviceReq.getDeviceId(), deviceEntity.deviceId.get());
                DataSourceDB.executeUpdate(connection, "update trip set device_id = ? where device_id = ?", registerDeviceReq.getDeviceId(), deviceEntity.deviceId.get());
            });
        }
        DataSourceDB.set(dataSource, deviceEntity == null ? new DeviceEntity().init(registerDeviceReq) : deviceEntity.init(registerDeviceReq));

    }

    public static void updateApkVersionForDevice(String deviceId, String version, DataSource dataSource) {
        DeviceEntity deviceEntity = DataSourceDB.get(DeviceEntity.class, dataSource, "select * from device where DEVICE_ID = ?", deviceId);

        if (deviceEntity != null && !deviceEntity.apkVersion.getNonNull().equals(version)) {
            deviceEntity.apkVersion.set(version);
            deviceEntity.apkD.set(new Timestamp(System.currentTimeMillis()));
            DataSourceDB.set(dataSource, deviceEntity);
        }
    }

    public static void updateLastCommsForDevice(String deviceId, DataSource dataSource) {
        DeviceEntity deviceEntity = DataSourceDB.get(DeviceEntity.class, dataSource, "select * from device where DEVICE_ID = ?", deviceId);

        if (deviceEntity != null) {
            deviceEntity.lastCommsDate.set(new Timestamp(System.currentTimeMillis()));
            DataSourceDB.set(dataSource, deviceEntity);
        }
    }

}
