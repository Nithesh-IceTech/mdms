package za.co.spsi.mdms.generic.meter.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

@Table(version = 12)
public class DbToDbMappingEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "DB_TO_DB_MAPPING_ID")
    public Field<String> dbToDbMappingId = new Field<>(this);

    public Field<String> driver = new Field<>(this);

    @Column(name = "SERVER_ADDRESS")
    public Field<String> serverAddress = new Field<>(this);

    @Column(name = "PORT_NUMBER")
    public Field<Integer> portNumber = new Field<>(this);

    @Column(name = "DB_NAME")
    public Field<String> dbName = new Field<>(this);

    @Column(name = "SERVICE_NAME")
    public Field<String> serviceName = new Field<>(this);

    @Column(name = "ACTIVE")
    public Field<Boolean> active = new Field<>(this);

    @Column(name = "DB_ACTIVE")
    public Field<Boolean> dbActive = new Field<>(this);

    @Column(name = "SQL_SELECT", size = 3000)
    public Field<String> sqlSelect = new Field<>(this);

    @Column(name = "SQL_FROM", size = 3000)
    public Field<String> sqlFrom = new Field<>(this);

    @Column(name = "COLUMN_FIELDS", size = 3000)
    public Field<String> columnFields = new Field<>(this);

    @Column(name = "LIVE")
    public Field<Boolean> live = new Field<>(this);

    @Column(name = "MAP_NAME")
    public Field<String> mapName = new Field<>(this);

    @Column(name = "USER_NAME")
    public Field<String> userName = new Field<>(this);

    public Field<String> password = new Field<>(this);

    @Column(name = "METER_ID")
    public Field<String> meterId = new Field<>(this);

    @Column(name = "METER_SERIAL_N")
    public Field<String> meterSerialN = new Field<>(this);

    @Column(name = "METER_READING_ID")
    public Field<String> meterReadingId = new Field<>(this);

    public Field<String> timestamp = new Field<>(this);

    @Column(name = "METER_MAN_ID")
    public Field<String> meterManId = new Field<>(this);

    @Column(name = "METER_TYPE")
    public Field<String> meterType = new Field<>(this);

    @Column(name = "VENDOR_PREFIX")
    public Field<String> vendorPrefix = new Field<>(this);

    @Column(name = "METER_PLATFORM_TYPE")
    public Field<String> meterPlatformType = new Field<>(this);

    @Column(name = "TIME_ZONE_OFFSET_TO_UTC")
    public Field<String> timeZoneOffsetToUtc = new Field<>(this);

    public EntityRef<DbToDbMappingDetailEntity> dbToDbMappingDetailEntityEntityRef = new EntityRef<>(this);

    public DbToDbMappingEntity() {
        super("DB_TO_DB_MAPPING");
    }

}
