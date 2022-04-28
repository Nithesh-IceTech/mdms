package za.co.spsi.mdms.generic.meter.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;

@Table(version = 1)
public class DbToDbMapMeterEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "DB_TO_DB_MAP_METER_ID")
    public Field<String> dbToDbMapMeterId = new Field<>(this);

    @Column(name = "METER_ID", size = 50)
    public Field<String> meterId = new Field<>(this);

    @ForeignKey(table = DbToDbMappingDetailEntity.class, name = "DB_TO_DB_MAP_DET_FK", onDeleteAction = ForeignKey.Action.NoAction)
    @Column(name = "DB_TO_DB_MAPPING_DETAIL_ID", size = 50)
    public Field<String> dbToDbMappingDetailId = new Field<>(this);

    @ForeignKey(table = DbToDbMappingEntity.class, name = "DB_TO_DB_MAP_FK", onDeleteAction = ForeignKey.Action.NoAction)
    @Column(name = "DB_TO_DB_MAPPING_ID", size = 50)
    public Field<String> dbToDbMapping = new Field<>(this);

    private Index idxMeterId = new Index("idx_MAP_METER_METER_ID", this, meterId);
    private Index idxDbToDbMappingDetailId = new Index("idx_MAP_METER_MAP_DET_ID", this, dbToDbMappingDetailId);
    private Index idxDbToDbMapping = new Index("idx_MAP_METER_MAP_ID", this, dbToDbMapping);

    public DbToDbMapMeterEntity() {
        super("DB_TO_DB_MAP_METER");
    }

}
