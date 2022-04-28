package za.co.spsi.mdms.common.db.survey;

import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.crud.sync.db.SharedEntity;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.AlwaysExport;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.SetNull;

/**
 * Created by jaspervdb on 2016/11/22.
 */
@Table(version = 2)
@Exportable(name="pecUnitList")
public class PecUnitEntity extends EntityDB {

    @Id(uuid=true)
    @Column(name = "UNIT_ID", size = 50)
    public Field<String> unitId= new Field<>(this);
    @Column(name = "AGENCY_ID", size = 8)
    public Field<Integer> agencyId= new Field<>(this);
    @Column(name = "USER_ID", size = 50)
    public UserIdField userId= new UserIdField(this);
    @Column(name = "UNIT_N", size = 12)
    public Field<String> unitN= new Field<>(this);
    @Column(name = "UNIT_TYPE_CD", size = 10)
    public Field<Integer> unitTypeCd= new Field<>(this);

    public SharedEntity sharedEntity = new SharedEntity(this);

    //PROPERTY
    @Column(name = "PROPERTY_ID", size = 50)
    @ForeignKey(table = PecPropertyEntity.class, onDeleteAction = SetNull)
    public Field<String> propertyId = new Field<>(this);

    @Exportable(parent = true,name = "pecProperty")
    public EntityRef<PecPropertyEntity> propertyEntity= new EntityRef<>(propertyId,this);

    //PHOTOS
    @AlwaysExport
    @Exportable(name = "pecUnitPhotos", deleteAllReferences = true, forceExport = true)
    public EntityRef<PecUnitPhotoEntity> unitPhotos = new EntityRef<>(this);

    //METERS
    @Exportable(name = "pecMeterList", forceExport = true)
    public EntityRef<PecMeterEntity> meterList = new EntityRef<>(this);

    public PecUnitEntity() {
        super("PEC_UNIT");
    }

}
