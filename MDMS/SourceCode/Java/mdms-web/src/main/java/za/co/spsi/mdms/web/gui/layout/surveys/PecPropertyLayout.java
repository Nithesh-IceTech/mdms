package za.co.spsi.mdms.web.gui.layout.surveys;


import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.common.db.survey.PecLocationSurveyEntity;
import za.co.spsi.mdms.common.db.survey.PecPropertyEntity;
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
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.sync.db.SharedEntity;
import za.co.spsi.toolkit.crud.sync.gui.fields.EntityStatusActionField;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.MaskId;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Timestamp;

public class PecPropertyLayout extends MDMSSyncLayout<PecPropertyEntity> {

    @Inject
    @ConfValue(value = "billingEnabled", agency = true)
    private boolean billingEnabled;

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    @ConfValue(value = "iceCountryCd")
    private String iceCountryCd;


    @EntityRef(main = true)
    private PecPropertyEntity property = new PecPropertyEntity();

    private AgencyField agency = new AgencyField(property.agencyId, "", this);
    private LField<String> user = new LField<String>(MdmsLocaleId.USERNAME, "userName", this);

    @UIGroup(column = 0)
    public Group detail = new Group(MdmsLocaleId.PROPERTY_DETAILS, this);

    @UIField(mask = MaskId.ANY, max = 250)
    public LField<String> propertyName = new LField<>(property.propertyName, MdmsLocaleId.PROPERTY_NAME, this);

    @UIField(mask = MaskId.ANY)
    public PropertyTypeCdField propertyTypeCd = new PropertyTypeCdField(property.propertyTypeCd, this);

    @UIField(mask = MaskId.ANY )
    public PropertyEntityTypeCdField propertyEntityTypeCd = new PropertyEntityTypeCdField(property.propertyEntityTypeCd, this);

    @UIField(mask = MaskId.ANY, max = 20)
    public LField<String> propertyCode = new LField<>(property.propertyCode, MdmsLocaleId.PROPERTY_CODE, this);

    @UIField(mask = MaskId.ANY, max = 20)
    public LField<String> company = new LField<>(property.company, MdmsLocaleId.COMPANY, this);

    @UIField(mask = MaskId.ANY, max = 20)
    public LField<String> area = new LField<>(property.area, MdmsLocaleId.AREA, this);

    @UIField(max = 20)
    public LField<String> externalRef = new LField<>(property.externalRef, MdmsLocaleId.EXTERNAL_REF, this);

    @UIField(enabled = false)
    public LField<Timestamp> capturedD = new LField<Timestamp>(property.sharedEntity.capturedD, MdmsLocaleId.ENTITY_CAPTURE_D, this);


    @UIGroup(column = 1)
    public Group addressGroup = new Group(MdmsLocaleId.ADDRESS_PARTICULARS, null, this);

//    public SharedLocationLayout ll = new SharedLocationLayout();

    //@UIField(mandatory = true)
    public CountryCdField countryCd = new CountryCdField(property.sharedLocation.countryCd, this, MdmsLocaleId.COUNTRY);

    //@UIField(mandatory = true)
    public ProvinceCdField provinceCd = new ProvinceCdField(property.sharedLocation.provinceCd, this,countryCd);

    //@UIField(mandatory = true)
    public CityCdField cityCd = new CityCdField(property.sharedLocation.cityCd, this, provinceCd);

    //@UIField(mandatory = true)
    public SuburbCdField suburb = new SuburbCdField(property.sharedLocation.suburbCd, this, cityCd);

    @UIField(agency = {0}, mask = MaskId.ANY, max = 50)
    public LField<String> standN = new LField<>(property.sharedLocation.standNumber, MdmsLocaleId.STAND_N, this);

    @UIField(agency = {0}, mask = MaskId.ANY, max = 50)
    public LField<String> streetName = new LField<>(property.sharedLocation.streetName, MdmsLocaleId.STREET_NAME_NUMBER, this);

    public GPSCoordinatesLField gpsCoordinates = new GPSCoordinatesLField(this);

    @UIGroup(column = 0, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group noteGroup = new Group(MdmsLocaleId.NOTES_GROUP_CAPTION, null, this);

    public NotesField notes = new NotesField(property.sharedEntity.notes, this);

    @UIGroup(column = 0, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group photoGroup = new Group(MdmsLocaleId.PHOTOS, null, this);

    @PlaceOnToolbar
    public ImageGalleryField photos = new ImageGalleryField(MdmsLocaleId.PHOTOS, IceImageCrudGallery.class, property.propertyPhotos, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group reviewGroup = new Group(MdmsLocaleId.REVIEW_STATUS, null, this);

    public ReviewStatusCdField reviewStatusCd = new ReviewStatusCdField(property.sharedEntity.reviewStatusCd, this);

    @UIField(enabled = false)
    public ReviewStatusChdDateField reviewStatusD = new ReviewStatusChdDateField(property.sharedEntity.reviewStatusChgD, reviewStatusCd, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group syncGroup = new Group(MdmsLocaleId.SYNC_STATUS, null, this);

    public EntityStatusCdField entityStatusCd = new EntityStatusCdField(property.sharedEntity.entityStatusCd, this);

    @UIField(enabled = false)
    public LField<Timestamp> entityStatusD = new LField<>(property.sharedEntity.entityStatusChgD, MdmsLocaleId.PROPERTY_STATUS_D, this);

    @PlaceOnToolbar
    public EntityStatusActionField entityStatusActionField = new EntityStatusActionField(entityStatusCd, this, this);

    public Group nameGroup = new Group("", this, propertyName, company, area, countryCd, provinceCd, cityCd, suburb, user, entityStatusCd, entityStatusD).setNameGroup();

    public Pane propertyPane = new Pane("", this, detail, addressGroup,noteGroup, photoGroup, reviewGroup, syncGroup);

    public Pane surveyPane = new Pane(MdmsLocaleId.SURVEY_DETAILS,
            String.format("select *, UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName " +
                            "from pec_location_survey " +
                            "left join User_Detail on TRIM(UPPER(pec_location_survey.user_Id)) = TRIM(User_Detail.user_name) " +
                            "where pec_location_survey.location_survey_id = pec_property.location_survey_id and entity_status_cd <> %d ",
                    MdmsConstants.ENTITY_STATUS_DELETED), PecLocationSurveyLayout.class, new Permission().setMayCreate(false), this);

    public Pane meterPane = new Pane(MdmsLocaleId.METER_DETAIL,
            String.format("select *, UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName " +
                    "from pec_meter " +
                    " left join User_Detail on TRIM(UPPER(pec_meter.user_Id)) = TRIM(User_Detail.user_name) " +
                    " where " +
                    "pec_meter.property_id = pec_property.property_id"), PecMeterLayout.class, this);

    public Pane unitPane = new Pane(MdmsLocaleId.UNIT_DETAIL,
            String.format("select *, UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName " +
                    "from pec_unit " +
                    " left join User_Detail on TRIM(UPPER(pec_unit.user_Id)) = TRIM(User_Detail.user_name) " +
                    " where " +
                    "pec_property.property_id = pec_unit.property_id"), PecUnitLayout.class, this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor("pec_property","property_id"), MDMSAuditLayout.class,new Permission(0),this);

    public PecPropertyLayout() {
        super(MdmsLocaleId.PROPERTY_CAPTION);
        init();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }


    private void init() {
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
    public SharedEntity getSharedEntity() {
        return property.sharedEntity;
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        entityStatusActionField.setBillingEnabled(billingEnabled);
    }

    @Override
    public void newEvent() {
        super.newEvent();

    }

    PecLocationSurveyEntity getLocationSurveyEntity() {
        return property.locationSurveyEntity.getOne(getDataSource());
    }

    @Override
    public String getMainSql() {
        return String.format("select *,UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName " +
                "from pec_property " +
                "left join User_Detail on TRIM(UPPER(pec_property.user_Id)) = TRIM(User_Detail.user_name) " +
                "where pec_property.entity_status_cd <> %d ", MdmsConstants.ENTITY_STATUS_DELETED);
    }


    @Override
    public String[][] getFilters() {
        return new String[][]{

                {MdmsLocaleId.REVIEW_STATUS_TO_BE_REVIEWED_FILTER, MdmsLocaleId.REVIEW_STATUS_TO_BE_REVIEWED_FILTER_HINT,
                        "select * from pec_property where pec_property.review_status_cd = " + MdmsConstants.REVIEW_STATUS_TO_BE_REVIEWED},
                {MdmsLocaleId.REVIEW_STATUS_REVIEWED_FILTER, MdmsLocaleId.REVIEW_STATUS_REVIEWED_FILTER_HINT,
                        "select * from pec_property where pec_property.review_status_cd = " + MdmsConstants.REVIEW_STATUS_REVIEWED},
                {MdmsLocaleId.REVIEW_STATUS_APPROVED_FILTER, MdmsLocaleId.REVIEW_STATUS_APPROVED_FILTER_HINT,
                        "select * from pec_property where pec_property.review_status_cd = " + MdmsConstants.REVIEW_STATUS_APPROVED},

        };
    }

}
