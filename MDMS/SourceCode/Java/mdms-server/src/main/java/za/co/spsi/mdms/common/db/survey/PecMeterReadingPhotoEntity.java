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
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;


/**
 *
 * @author francoism
 */
@Table(version = 2, maintainStrict = false, allowFkDrop = true)
public class PecMeterReadingPhotoEntity extends AbstractPhotoEntity {

    @Id(uuid=true)
    @Column(name = "METER_READING_PHOTO_ID", size = 50)
    public Field<String> meterReadingPhotoId= new Field<>(this);

    @ForeignKey(table = PecMeterReadingEntity.class, deferrable = InitiallyDeferred,name = "MREADP_METER_READING_ID",onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "METER_READING_ID", size = 50)
    public Field<String>meterReadingId= new Field<>(this);

    @Column(name = "PHOTO_EXPORTED", defaultValue = "N")
    public Field<Character> photoExported= new Field<>(this);

    public Index idxExport = new Index("PEC_MR_P_EXP",this,photoExported,meterReadingId);

    public PecMeterReadingPhotoEntity() {
        super("PEC_METER_READING_PHOTO");
    }

    
}
