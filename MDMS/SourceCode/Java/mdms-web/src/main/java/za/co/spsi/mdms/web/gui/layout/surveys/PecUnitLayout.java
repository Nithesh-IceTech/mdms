package za.co.spsi.mdms.web.gui.layout.surveys;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.common.db.survey.PecLocationSurveyEntity;
import za.co.spsi.mdms.common.db.survey.PecUnitEntity;
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
import za.co.spsi.toolkit.crud.gui.fields.ImageGalleryField;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class PecUnitLayout extends Layout<PecUnitEntity> {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private PecUnitEntity unit = new PecUnitEntity();

    private AgencyField agency = new AgencyField(unit.agencyId, "", this);
    private LField<String> user = new LField<String>(MdmsLocaleId.USERNAME, "userName", this);

    @UIGroup(column = 0)
    public Group detail = new Group(MdmsLocaleId.UNIT_DETAIL, this);

    @UIField(max = 4)
    public LField<Integer> unitNumber = new LField<>(unit.unitN, MdmsLocaleId.UNIT_N, this);

    public UnitTypeCdField unitTypeCd = new UnitTypeCdField(unit.unitTypeCd, this);

    @PlaceOnToolbar
    public ImageGalleryField photos = new ImageGalleryField(MdmsLocaleId.PHOTOS, IceImageCrudGallery.class, unit.unitPhotos, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group reviewGroup = new Group(MdmsLocaleId.REVIEW_STATUS, null, this);

    public ReviewStatusCdField reviewStatusCd = new ReviewStatusCdField(unit.sharedEntity.reviewStatusCd, this);

    @UIField(enabled = false)
    public ReviewStatusChdDateField reviewStatusD = new ReviewStatusChdDateField(unit.sharedEntity.reviewStatusChgD, reviewStatusCd, this);


    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group syncGroup = new Group(MdmsLocaleId.SYNC_STATUS, null, this);

    public EntityStatusCdField entityStatusCdField = new EntityStatusCdField(unit.sharedEntity.entityStatusCd, this);

    public Group nameGroup = new Group("", this, unitNumber, unitTypeCd, user).setNameGroup();

    public Pane detailPane = new Pane("", this, detail, reviewGroup, syncGroup);

    public Pane propertyPane = new Pane(MdmsLocaleId.PROPERTY_DETAILS,
            String.format("select *, UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName " +
                    "from pec_property " +
                    " left join User_Detail on TRIM(UPPER(pec_property.user_Id)) = TRIM(User_Detail.user_name) " +
                    " where " +
                    "pec_property.property_id = pec_unit.property_id and pec_property.entity_Status_Cd <> " + MdmsConstants.ENTITY_STATUS_DELETED),
            PecPropertyLayout.class, new Permission().setMayCreate(false), this);

    public Pane meterPane = new Pane(MdmsLocaleId.METER_DETAIL,
            String.format("select *, UPPER(User_Detail.FIRST_NAME || ' ' || User_Detail.LAST_NAME) as userName " +
                    "from pec_meter " +
                    " left join User_Detail on TRIM(UPPER(pec_meter.user_Id)) = TRIM(User_Detail.user_name) " +
                    " where " +
                    "pec_meter.unit_id = pec_unit.unit_id"), PecMeterLayout.class, this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor("pec_unit", "unit_id"), MDMSAuditLayout.class,new Permission(0),this);

    public PecUnitLayout() {
        super(MdmsLocaleId.UNIT_DETAIL);
        init();
    }

    private void init() {
        getPermission().setMayCreate(true);

    }

    PecLocationSurveyEntity getLocationSurveyEntity() {
        try {
            try (Connection connection = getDataSource().getConnection()) {
                return unit.propertyEntity.getOne(connection, null).locationSurveyEntity.getOne(connection, null);
            }
        } catch (SQLException sqlE) {
            throw new RuntimeException(sqlE);
        }
    }


    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from pec_unit where 1 = 1";
    }


}
