/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.mdms.common.db.survey;

import za.co.spsi.toolkit.crud.db.AbstractPhotoEntity;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;


/**
 *
 * @author francoism
 */
@Table(version = 2, maintainStrict = false, allowFkDrop = true)
public class PecPropertyPhotoEntity extends AbstractPhotoEntity {

    @Id(uuid=true)
    @Column(name = "PROPERTY_PHOTO_ID", size = 50)
    public Field<String> propertyPhotoId= new Field<>(this);

    @ForeignKey(table = PecPropertyEntity.class, deferrable = InitiallyDeferred,name = "PE_PP_PROPERTY_ID_FK",onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "PROPERTY_ID", size = 50)
    public Field<String>propertyId= new Field<>(this);

    public PecPropertyPhotoEntity() {
        super("PEC_PROPERTY_PHOTO");
    }

    
}
