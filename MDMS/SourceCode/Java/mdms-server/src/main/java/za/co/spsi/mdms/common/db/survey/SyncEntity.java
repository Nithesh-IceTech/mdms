/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.mdms.common.db.survey;

import org.json.JSONObject;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.toolkit.crud.sync.SyncableEntity;
import za.co.spsi.toolkit.crud.sync.db.SharedEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.VirtuallyDeleted;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Connection;
import java.sql.Timestamp;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;

public abstract class SyncEntity extends EntityDB implements VirtuallyDeleted, SyncableEntity {

    // todo fix this length name

    @ForeignKey(table = PecLocationSurveyEntity.class, deferrable = InitiallyDeferred, onDeleteAction = ForeignKey.Action.SetNull)
    @Column(name = "LOCATION_SURVEY_ID", size = 50)
    public Field<String> locationSurveyId = new Field<>(this);

    @Exportable(parent = true,name = "pecLocationSurvey")
    public EntityRef<PecLocationSurveyEntity> locationSurveyEntity = new EntityRef<>(locationSurveyId, this);

    public SharedEntity sharedEntity = new SharedEntity(this);

    public SyncEntity(String name) {
        super(name);
    }


    @Override
    public boolean beforeDeleteEvent(Connection connection) {
        // only allow deletes when if the state is correct
        if (MdmsConstants.ENTITY_STATUS_TABLET_PROCESSING.equals(sharedEntity.entityStatusCd.get())) {
            return false;
        }
        // update to deleted status - do a virtual delete
        sharedEntity.entityStatusCd.set(MdmsConstants.ENTITY_STATUS_DELETED);
        DataSourceDB.set(connection, this);
        return false;
    }

    @Override
    public void initFromJson(JSONObject jsonObject) {
        // only init from json if its in tablet processing mode
        if (!isInDatabase() || MdmsConstants.ENTITY_STATUS_TABLET_PROCESSING.equals(sharedEntity.entityStatusCd.get())) {
            super.initFromJson(jsonObject);
            if (MdmsConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING.equals(sharedEntity.entityStatusCd.get())) {
                //Set the Review status to To Be Reviewed after changes received from Tablet
                sharedEntity.reviewStatusCd.set(MdmsConstants.REVIEW_STATUS_TO_BE_REVIEWED);
                sharedEntity.reviewStatusChgD.set(new Timestamp(System.currentTimeMillis()));
            }
        }
    }

    @Override
    public void virtualDelete(Connection connection) {
        sharedEntity.entityStatusCd.set(MdmsConstants.ENTITY_STATUS_DELETED);
        DataSourceDB.set(connection, this);
    }

    public SharedEntity getBaseSharedSyncEntity() {
        return sharedEntity;
    }

}
