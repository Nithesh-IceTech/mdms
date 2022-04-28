package za.co.spsi.toolkit.crud.gis.db;


import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;

@Exportable(name = "trip")
@Table(version = 6)
public class TripEntity extends AuditEntityDB {

    @Id(uuid = true)
    @Column(name = "TRIP_ID")
    public Field<String> tripId = new Field<>(this);

    @Column(name = "START_TIME")
    public Field<Timestamp> startTime = new Field<Timestamp>(this);

    @Column(name = "END_TIME")
    public Field<Timestamp> endTime = new Field<Timestamp>(this);

    @Column(name = "FROM_LON", size = 21, decimalPlaces = 7)
    public Field<Double> fromLon = new Field<Double>(this);

    @Column(name = "FROM_LAT", size = 21, decimalPlaces = 7)
    public Field<Double> fromLat = new Field<Double>(this);

    @Column(name = "TO_LON", size = 21, decimalPlaces = 7)
    public Field<Double> toLon = new Field<Double>(this);

    @Column(name = "TO_LAT", size = 21, decimalPlaces = 7)
    public Field<Double> toLat = new Field<Double>(this);

    @Column(name = "MAX_SPEED")
    public Field<Float> maxSpeed = new Field<Float>(this);

    @Column(name = "DISTANCE")
    public Field<Double> distance = new Field<Double>(this);

    @Column(name = "FROM_ADDRESS")
    public Field<String> fromAddress = new Field<String>(this);

    @Column(name = "TO_ADDRESS")
    public Field<String> toAddress = new Field<String>(this);

    @Column(defaultValue = "0")
    public Field<Boolean> completed = new Field<Boolean>(this);

    @Column(name = "device_id")
    @ForeignKey(table = DeviceEntity.class, onDeleteAction = ForeignKey.Action.Cascade, deferrable = InitiallyDeferred)
    public Field<String> deviceId = new Field<String>(this);

    public EntityRef<DeviceEntity> device = new EntityRef<>(deviceId,this);

    public EntityRef<DeviceLocationEntity> locations = new EntityRef<>("select * from device_location where trip_id = ? order by CAPTURE_TIME asc",this);

    public TripEntity() {
        super("TRIP");
    }
}
