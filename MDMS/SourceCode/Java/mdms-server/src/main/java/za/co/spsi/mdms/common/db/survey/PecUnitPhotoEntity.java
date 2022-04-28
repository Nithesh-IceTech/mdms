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

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.Cascade;


/**
 *
 * @author francoism
 */

@Table(version = 2)
public class PecUnitPhotoEntity extends AbstractPhotoEntity {

    @Id(uuid=true)
    @Column(name = "UNIT_PHOTO_ID", size = 50)
    public Field<String> unitPhotoId= new Field<>(this);

    @Column(name = "UNIT_ID", size = 50)
    @ForeignKey(table= PecUnitEntity.class, onDeleteAction = Cascade)
    public Field<String> unitId= new Field<>(this);

    public PecUnitPhotoEntity() {
        super("PEC_UNIT_PHOTO");
    }
}
