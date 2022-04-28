package za.co.spsi.mdms.generic.meter.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

@Table(version = 6)
public class DbToDbMappingDetailEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "DB_TO_DB_MAPPING_DETAIL_ID")
    public Field<String> dbToDbMappingDetailId = new Field<>(this);

    @Column(name = "SQL_SELECT", size = 3000)
    public Field<String> sqlSelect = new Field<>(this);

    @Column(name = "SQL_FROM", size = 3000)
    public Field<String> sqlFrom = new Field<>(this);

    @Column(name = "SQL_WHERE", size = 3000)
    public Field<String> sqlWhere = new Field<>(this);

    @Column(name = "COLUMN_FIELDS", size = 3000)
    public Field<String> columnFields = new Field<>(this);

    @Column(name = "IMPORT_KWH")
    public Field<String> importKWH = new Field<>(this);

    @Column(name = "IMPORT_KWH_SCALING_FACTOR")
    public Field<Double> importKWHScalingFactor = new Field<>(this);

    @Column(name = "EXPORT_KWH")
    public Field<String> exportKWH = new Field<>(this);

    @Column(name = "EXPORT_KWH_SCALING_FACTOR")
    public Field<Double> exportKWHScalingFactor = new Field<>(this);

    @Column(name = "INDUCTIVE_KVARH")
    public Field<String> inductiveKVARH = new Field<>(this);

    @Column(name = "INDUCTIVE_KVARH_SCALING_FACTOR")
    public Field<Double> inductiveKVARHScalingFactor = new Field<>(this);

    @Column(name = "CAPACITIVE_KVARH")
    public Field<String> capacitiveKVARH = new Field<>(this);

    @Column(name = "CAP_KVARH_SCALING_FACTOR")
    public Field<Double> capacitiveKVARHScalingFactor = new Field<>(this);

    @Column(name = "VOLTAGE_L1")
    public Field<String> voltageL1 = new Field<>(this);

    @Column(name = "VOLTAGE_L1_SCALING_FACTOR")
    public Field<Double> voltageL1ScalingFactor = new Field<>(this);

    @Column(name = "VOLTAGE_L2")
    public Field<String> voltageL2 = new Field<>(this);

    @Column(name = "VOLTAGE_L2_SCALING_FACTOR")
    public Field<Double> voltageL2ScalingFactor = new Field<>(this);

    @Column(name = "VOLTAGE_L3")
    public Field<String> voltageL3 = new Field<>(this);

    @Column(name = "VOLTAGE_L3_SCALING_FACTOR")
    public Field<Double> voltageL3ScalingFactor = new Field<>(this);

    @Column(name = "CURRENT_L1")
    public Field<String> currentL1 = new Field<>(this);

    @Column(name = "CURRENT_L1_SCALING_FACTOR")
    public Field<Double> currentL1ScalingFactor = new Field<>(this);

    @Column(name = "CURRENT_L2")
    public Field<String> currentL2 = new Field<>(this);

    @Column(name = "CURRENT_L2_SCALING_FACTOR")
    public Field<Double> currentL2ScalingFactor = new Field<>(this);

    @Column(name = "CURRENT_L3")
    public Field<String> currentL3 = new Field<>(this);

    @Column(name = "CURRENT_L3_SCALING_FACTOR")
    public Field<Double> currentL3ScalingFactor = new Field<>(this);

    @Column(name = "VOLUME_WATER")
    public Field<String> volumeWater = new Field<>(this);

    @Column(name = "VOLUME_WATER_SCALING_FACTOR")
    public Field<Double> volumeWaterScalingFactor = new Field<>(this);

    @Column(name = "VOLUME_GAS")
    public Field<String> volumeGas = new Field<>(this);

    @Column(name = "VOLUME_GAS_SCALING_FACTOR")
    public Field<Double> volumeGasScalingFactor = new Field<>(this);

    @Column(name = "LAST_SYNC_TIME")
    public Field<Timestamp> lastSyncTime = new Field<>(this);

    @Column(name = "TIME_ZONE_OFFSET_TO_UTC")
    public Field<String> timeZoneOffsetToUtc = new Field<>(this);

    @Column(name = "METER_PLATFORM_TYPE")
    public Field<String> meterPlatformType = new Field<>(this);

    @Column(name = "MAP_NAME")
    public Field<String> mapName = new Field<>(this);

    @Column(name = "BATCH_IMPORT_RUNNING")
    public Field<Boolean> batchImportRunning = new Field<>(this);

    @Column(name = "MAN_IMPORT_RUNNING")
    public Field<Boolean> importRunning = new Field<>(this);

    @ForeignKey(table = DbToDbMappingEntity.class, name = "DB_TO_DB_MAPPING_DET_FK", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "DB_TO_DB_MAPPING_ID", size = 50)
    public Field<String> dbToDbMappingId = new Field<>(this);

    public DbToDbMappingDetailEntity() {
        super("DB_TO_DB_MAPPING_DETAIL");
    }

}
