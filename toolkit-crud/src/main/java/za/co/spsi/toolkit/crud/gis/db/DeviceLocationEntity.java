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

@Exportable(name="deviceLocation")
@Table(version = 2)
public class DeviceLocationEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "DEVICE_LOCATION_ID")
    public Field<String> deviceLocationId = new Field<>(this);

    @Column(notNull = true, size = 21, decimalPlaces = 7)
    public Field<Double> lon = new Field<Double>(this);
    @Column(notNull = true, size = 21, decimalPlaces = 7)
    public Field<Double> lat = new Field<Double>(this);
    public Field<Float> speed = new Field<Float>(this);
    public Field<Float> heading = new Field<Float>(this);

    @Column(size = 1024)
    public Field<String> address = new Field<>(this);

    @Column(name = "CAPTURE_TIME",notNull = true)
    public Field<Timestamp> captureTime = new Field<>(this);

    @Column(name = "BATTERY_LEVEL")
    public Field<Float> batteryLevel = new Field<Float>(this);

    @Column(name = "DEVICE_ID")
    @ForeignKey(table = DeviceEntity.class, onDeleteAction = ForeignKey.Action.Cascade,deferrable = InitiallyDeferred)
    public Field<String> deviceId = new Field<String>(this);

    @Column(name = "TRIP_ID")
    @ForeignKey(table = TripEntity.class,onDeleteAction = ForeignKey.Action.Cascade,deferrable = InitiallyDeferred)
    public Field<String> tripId = new Field<>(this);

    public DeviceLocationEntity() {
        super("DEVICE_LOCATION");
    }

}
