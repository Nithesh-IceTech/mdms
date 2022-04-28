package za.co.spsi.mdms.web.gui.layout.surveys;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.common.db.survey.PecLocationSurveyEntity;
import za.co.spsi.mdms.web.gui.fields.*;
import za.co.spsi.mdms.web.gui.layout.IceImageCrudGallery;
import za.co.spsi.mdms.web.gui.layout.MDMSAuditLayout;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.PlaceOnToolbar;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.ano.UILayout;
import za.co.spsi.toolkit.crud.gui.fields.GPSCoordinatesLField;
import za.co.spsi.toolkit.crud.gui.fields.ImageGalleryField;
import za.co.spsi.toolkit.crud.gui.fields.ImageGeoField;
import za.co.spsi.toolkit.crud.gui.gis.MapComponent;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.service.NumberService;
import za.co.spsi.toolkit.crud.sync.db.SharedEntity;
import za.co.spsi.toolkit.crud.sync.gui.fields.EntityStatusActionField;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.MaskId;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Arrays;

public class PecLocationSurveyLayout extends MDMSSyncLayout<PecLocationSurveyEntity> implements MapComponent.ValueUpdateCallback {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private PecLocationSurveyEntity survey = new PecLocationSurveyEntity();

    @Inject
    private NumberService numberService;

    @Inject
 	@ConfValue(value = "iceCountryCd")
 	private String iceCountryCd;

    private AgencyField agency = new AgencyField(survey.agencyId, "", this);

    private LField<String> user = new LField<String>(MdmsLocaleId.USERNAME, "userName", this);

    @UIGroup(column = 0)
    public Group addressGroup = new Group(MdmsLocaleId.ADDRESS_PARTICULARS, null, this);

    public CountryCdField countryCd = new CountryCdField(survey.sharedLocation.countryCd, this, MdmsLocaleId.COUNTRY);

    public ProvinceCdField provinceCd = new ProvinceCdField(survey.sharedLocation.provinceCd, this,countryCd);

    public CityCdField cityCd = new CityCdField(survey.sharedLocation.cityCd, this, provinceCd);

    public SuburbCdField suburb = new SuburbCdField(survey.sharedLocation.suburbCd, this, cityCd);

    @UIField(agency = {0}, mask = MaskId.ANY, max = 50)
    public LField<String> standN = new LField<>(survey.sharedLocation.standNumber, MdmsLocaleId.STAND_N, this);

    @UIField(agency = {0}, mask = MaskId.ANY, max = 50)
    public LField<String> streetName = new LField<>(survey.sharedLocation.streetName, MdmsLocaleId.STREET_NAME_NUMBER, this);

    public GPSCoordinatesLField gpsCoordinates = new GPSCoordinatesLField(this);

    @UIGroup(column = 0)
    public Group detail = new Group(MdmsLocaleId.SURVEY_DETAILS, this);

    @UIField(isForCaption = true)
    public SurveyNumberField locationSurveyN = new SurveyNumberField(survey.locationSurveyN, this, MdmsLocaleId.LOCATION_SURVEY_N);


    @UIField(enabled = false)
    public LField<Timestamp> entityStatusD = new LField<>(survey.sharedEntity.entityStatusChgD, MdmsLocaleId.LOCATION_SURVEY_STATUS_D, this);

    @UIField(enabled = false)
    public LField<Timestamp> capturedD = new LField<Timestamp>(survey.sharedEntity.capturedD, MdmsLocaleId.ENTITY_CAPTURE_D, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group noteGroup = new Group(MdmsLocaleId.NOTES_GROUP_CAPTION, null, this);

    public NotesField notes = new NotesField(survey.sharedEntity.notes, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group photoGroup = new Group(MdmsLocaleId.PHOTOS, null, this);

    @PlaceOnToolbar
    public ImageGalleryField photos = new ImageGalleryField(MdmsLocaleId.PHOTOS, IceImageCrudGallery.class, survey.locationSurveyPhotos, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group reviewGroup = new Group(MdmsLocaleId.REVIEW_STATUS, null, this);

    public ReviewStatusCdField reviewStatusCd = new ReviewStatusCdField(survey.sharedEntity.reviewStatusCd, this);

    @UIField(enabled = false)
    public ReviewStatusChdDateField reviewStatusD = new ReviewStatusChdDateField(survey.sharedEntity.reviewStatusChgD, reviewStatusCd, this);


    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group syncGroup = new Group(MdmsLocaleId.SYNC_STATUS, null, this);

    public EntityStatusCdField entityStatusCdField = new EntityStatusCdField(survey.sharedEntity.entityStatusCd, this);

    @PlaceOnToolbar
    public EntityStatusActionField entityStatusActionField = new EntityStatusActionField(entityStatusCdField, this, this);

    @UIGroup(column = -1)
    public Group geoGroup = new Group(MdmsLocaleId.LOCATION, null, this);

    public ImageGeoField imageGeoField =
            new ImageGeoField("", survey.imageGeo, this, this, false, false, ToolkitConstants.GeoType.LOCATION);

    public Group nameGroup = new Group("", this, locationSurveyN, countryCd, provinceCd, cityCd, suburb, standN, entityStatusCdField, entityStatusD, reviewStatusCd, reviewStatusD, capturedD, user).setNameGroup();

    public Pane detailPane = new Pane("", this, addressGroup, detail, noteGroup, photoGroup, reviewGroup, syncGroup, geoGroup);

    public Pane propertyPane = new Pane(MdmsLocaleId.PROPERTY_DETAILS,
            String.format("select *, UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName " +
                    "from Pec_Property " +
                    " left join User_Detail on TRIM(UPPER(pec_property.user_Id)) = TRIM(User_Detail.user_name) " +
                    " where " +
                    "pec_property.location_Survey_Id = pec_location_survey.location_Survey_Id and pec_property.entity_Status_Cd <> " +
                    MdmsConstants.ENTITY_STATUS_DELETED), PecPropertyLayout.class, this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor("pec_location_survey", "location_survey_id"), MDMSAuditLayout.class,new Permission(0),this);


/*
    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditLayout.getSqlFor("location_survey"),
            MDMSAuditLayout.class, new za.co.spsi.toolkit.crud.gui.Permission(0), this);
*/

    public PecLocationSurveyLayout() {
        super(MdmsLocaleId.LOCATION_SURVEY_CAPTION);
        init();
//        initLayout(ll);
    }

    @Override
    public SharedEntity getSharedEntity() {
        return survey.sharedEntity;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void newEvent() {
        super.newEvent();
        updateLocation(0, 0);
        // set random no
        survey.initSurveyN(numberService);
        getSharedEntity().capturedD.set(new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        entityStatusActionField.setBillingEnabled(false);
        entityStatusActionField.setBillingVisible(false);
        if (survey.sharedLocation.lat.get() != null) {
            gpsCoordinates.update(survey.sharedLocation.lat.get(), survey.sharedLocation.lon.get());
        }
        imageGeoField.getMapComponent().setGeoTypes(Arrays.asList(ToolkitConstants.GeoType.LOCATION));
    }

    @Override
    public String getMainSql() {
        return String.format("select *, UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName " +
                " from pec_location_survey " +
                " left join User_Detail on TRIM(UPPER(pec_location_survey.user_Id)) = TRIM(User_Detail.user_name) " +
                " where entity_status_cd <> %d order by ENTITY_STATUS_CHANGE_D desc", MdmsConstants.ENTITY_STATUS_DELETED);
    }

    private void init() {
        getPermission().setMayCreate(true);

        // If supervisor then enable recall
        if (ToolkitUI.getToolkitUI().getUserRoles().containsIgnoreCase(ToolkitCrudConstants.ROLE_SUPERVISOR)) {
            entityStatusActionField.setRecallEnabled(true);
        } else {
            entityStatusActionField.setRecallEnabled(false);
        }

    }

    @PostConstruct
 	private void initDefaults(){
        countryCd.getProperties().setDefault(iceCountryCd).setEnabled(false);

    }

    @Override
    public String[][] getFilters() {
        return new String[][]{
                {MdmsLocaleId.REVIEW_STATUS_TO_BE_REVIEWED_FILTER, MdmsLocaleId.REVIEW_STATUS_TO_BE_REVIEWED_FILTER_HINT,
                        "select * from pec_location_survey where pec_location_survey.review_status_cd = " + MdmsConstants.REVIEW_STATUS_TO_BE_REVIEWED},
                {MdmsLocaleId.REVIEW_STATUS_REVIEWED_FILTER, MdmsLocaleId.REVIEW_STATUS_REVIEWED_FILTER_HINT,
                        "select * pec_location_survey where pec_location_survey.review_status_cd = " + MdmsConstants.REVIEW_STATUS_REVIEWED},
                {MdmsLocaleId.REVIEW_STATUS_APPROVED_FILTER, MdmsLocaleId.REVIEW_STATUS_APPROVED_FILTER_HINT,
                        "select * from pec_location_survey where pec_location_survey.review_status_cd = " + MdmsConstants.REVIEW_STATUS_APPROVED}
        };
    }

    @Override
    public double[] getGpsCoordinates() {
        return survey.sharedLocation.lat.get() != null ? new double[]{survey.sharedLocation.lat.get(), survey.sharedLocation.lon.get()} : null;
    }


    @Override
    public void updateValue(ToolkitConstants.GeoType geoType, Double value) {

    }

    @Override
    public void updateLocation(double lat, double lon) {
        survey.sharedLocation.lat.set(lat);
        survey.sharedLocation.lon.set(lon);
        gpsCoordinates.update(lat, lon);

    }

    @Override
    public boolean save() {
        if (imageGeoField.getMapComponent().getMarker() != null &&
                survey.sharedLocation.lat.get() == null || survey.sharedLocation.lat.get().equals((double) 0)) {

            updateLocation(imageGeoField.getMapComponent().getMarker().getPoint().getLat(),
                    imageGeoField.getMapComponent().getMarker().getPoint().getLon());
        }
        return super.save();
    }
}
