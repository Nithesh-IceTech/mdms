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


/**
 *
 * @author francoism
 */
@Table(version = 0)
public class PecMeterPhotoEntity extends AbstractPhotoEntity {

    @Id(uuid=true)
    @Column(name = "METER_PHOTO_ID", size = 50)
    public Field<String> propertyMeterPhotoId= new Field<>(this);

    @Column(name = "METER_ID", size = 50)
    @ForeignKey(table= PecMeterEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterId= new Field<>(this);

    public PecMeterPhotoEntity() {
        super("PEC_METER_PHOTO");
    }

}
