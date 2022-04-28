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
import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;

/**
 *
 * @author francoism
 */
@Table(version = 3, maintainStrict = false, allowFkDrop = true)
public class PecLocationSurveyPhotoEntity extends AbstractPhotoEntity {

    @Id(uuid=true)
    @Column(name = "LOCATION_SURVEY_PHOTO_ID", size = 50)
    public Field<String> locationSurveyPhotoId= new Field<>(this);

    @ForeignKey(table = PecLocationSurveyEntity.class, onDeleteAction = Cascade, deferrable = InitiallyDeferred, name = "LOCATION_SURVEY_PHOTO_1_FK")
    @Column(name = "LOCATION_SURVEY_ID", size = 50)
    public Field<String>locationSurveyId= new Field<>(this);

    public PecLocationSurveyPhotoEntity() {
        super("PEC_LOCATION_SURVEY_PHOTO");
    }

}
