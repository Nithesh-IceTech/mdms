package za.co.spsi.toolkit.crud.entity;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.Cascade;

/**
 * Created by jaspervdb on 2016/07/25.
 * Entity is utilised to define a data export for a specific view
 */
@Table(version = 0)
public class DataImportMapEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "DATA_IMPORT_MAP_ID",size = 50, notNull = true)
    public Field<String> dataImportMapId = new Field<>(this);

    @ForeignKey(table = DataImportEntity.class,name = "DIM_IDX_DATA_IMPORT_ID", onDeleteAction = Cascade)
    @Column(name = "DATA_IMPORT_ID", size = 50)
    public Field<String> dataImportId = new Field<>(this);

    @Column(name = "ENTITY_CLASS", size = 250)
    public Field<String> entityClass = new Field<>(this);

    @Column(name = "ENTITY_ID", size = 50)
    public Field<String> entityId = new Field<>(this);

    private Index idxShapeImportId = new Index("idx_DATA_IMPORT_IMPORT_ID",this,dataImportId);

    public DataImportMapEntity() {
        super("DATA_IMPORT_MAP");
    }

    public DataImportMapEntity(DataImportEntity dataImportEntity,EntityDB entity) {
        this();
        this.dataImportId.set(dataImportEntity.dataImportId.get());
        this.entityClass.set(entity.getClass().getName());
        this.entityId.set(entity.getSingleId().getAsString());
    }

}
