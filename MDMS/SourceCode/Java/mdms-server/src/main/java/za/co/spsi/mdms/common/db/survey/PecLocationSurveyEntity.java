/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.mdms.common.db.survey;

import org.json.JSONObject;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.crud.db.gis.ImageGeoEntity;
import za.co.spsi.toolkit.crud.service.NumberService;
import za.co.spsi.toolkit.crud.sync.SyncableEntity;
import za.co.spsi.toolkit.crud.sync.db.SharedEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.ExportPathObject;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.AlwaysExport;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.entity.ano.ExportableEntity;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;


@Audit
@Table(version = 6, allowFkDrop = true)
public class PecLocationSurveyEntity extends EntityDB implements SyncableEntity, ExportableEntity {

    @Override
    public ExportPathObject getExportObject() {
        ExportPathObject locationExport = new ExportPathObject(PecLocationSurveyEntity.class).
                set(PecLocationSurveyPhotoEntity.class);
        return locationExport;
    }

    private static final Logger LOG = Logger.getLogger(PecLocationSurveyEntity.class.getName());

    @Id(uuid = true,name = "PEC_LS_ID")
    @Column(name = "LOCATION_SURVEY_ID", size = 50)
    public Field<String> locationSurveyId = new Field<>(this);

    @Column(name = "AGENCY_ID", size = 8)
    public Field<Integer> agencyId = new Field<>(this);

    @Column(name = "USER_ID", size = 50)
    public UserIdField userId = new UserIdField(this);

    public SharedLocation sharedLocation = new SharedLocation(this);

    @Column(name = "LOCATION_SURVEY_N", size = 20)
    public Field<String> locationSurveyN = new Field<>(this);

    public SharedEntity sharedEntity = new SharedEntity(this);

    @Exportable(name = "pecPropertyList")
    public EntityRef<PecPropertyEntity> propertyList = new EntityRef<>(this);

    @AlwaysExport
    @Exportable(name = "pecLocationSurveyPhotos", deleteAllReferences = true, forceExport = true)
    public EntityRef<PecLocationSurveyPhotoEntity> locationSurveyPhotos = new EntityRef<>(this);

    @AlwaysExport
    @Column(name = "IMAGE_ID")
    @ForeignKey(table = ImageGeoEntity.class, deferrable = InitiallyDeferred,name = "PEC_LS_IMAGE_ID")
    @Exportable(name = "imageId")
    public Field<String> imageId= new Field<>(this);

    @AlwaysExport
    @Exportable(name = "imageGeo", deleteAllReferences = true, forceExport = true)
    public EntityRef<ImageGeoEntity> imageGeo = new EntityRef<>(imageId, this);


    public PecLocationSurveyEntity() {
        super("PEC_LOCATION_SURVEY");
    }

    public void initSurveyN(NumberService numberService) {
        locationSurveyN.set(numberService.getHexString(13));
    }

    @Override
    public boolean beforeDeleteEvent(Connection connection) {
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

            //Remove the tablet imei and description from entity if the entity is being synced back up as backoffice processing
            if (MdmsConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING.equals(sharedEntity.entityStatusCd.get())) {
                //Set the Review status to To Be Reviewed after changed/new entity is received from Tablet
                sharedEntity.reviewStatusCd.set(MdmsConstants.REVIEW_STATUS_TO_BE_REVIEWED);
                sharedEntity.reviewStatusChgD.set(new Timestamp(System.currentTimeMillis()));
            }
        }
    }

    @Override
    public SharedEntity getBaseSharedSyncEntity() {
        return sharedEntity;
    }
}
