package za.co.spsi.mdms.web.gui.layout.surveys;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.common.db.survey.PecLocationSurveyEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterEntity;
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
import za.co.spsi.toolkit.crud.gui.fields.GPSCoordinatesLField;
import za.co.spsi.toolkit.crud.gui.fields.ImageGalleryField;
import za.co.spsi.toolkit.crud.gui.fields.ImageGeoField;
import za.co.spsi.toolkit.crud.gui.gis.MapComponent;
import za.co.spsi.toolkit.crud.login.UserDetailEntity;
import za.co.spsi.toolkit.crud.service.NumberService;
import za.co.spsi.toolkit.dao.ToolkitConstants;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class PecMeterLayout extends Layout<PecMeterEntity> implements MapComponent.ValueUpdateCallback {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private PecMeterEntity meter = new PecMeterEntity();

    @Inject
    private NumberService numberService;

    private AgencyField agency = new AgencyField(meter.agencyId, "", this);

    private LField<String> user = new LField<String>(meter.userId, MdmsLocaleId.USERNAME, this);

    @UIGroup(column = 0)
    public Group detail = new Group(MdmsLocaleId.METER_DETAIL, this);

    @UIField(enabled = false)
    public LField<String> meterReference = new LField<>(meter.meterReference, MdmsLocaleId.METER_REFERENCE, this);

    @UIField(max = 50)
    public LField<String> meterN = new LField<>(meter.meterN, MdmsLocaleId.METER_NUMBER, this);

    @UIField(max = 20)
    public LField<String> externalRef = new LField<>(meter.externalRef, MdmsLocaleId.EXTERNAL_REF, this);

    @UIField(max = 255)
    public LField<String> description = new LField<>(meter.description, MdmsLocaleId.DESCRIPTION, this);

    public ServiceGroupCdField serviceGroupCd = new ServiceGroupCdField(meter.serviceGroupCd, this);

    public SupplyTypeCdField supplyTypeCd = new SupplyTypeCdField(meter.supplyTypeCd, this);

    public MeterMakeCdField meterMakeCd = new MeterMakeCdField(meter.meterMakeCd, this);

    public MeterModelCdField meterModelCd = new MeterModelCdField(meter.meterModelCd, meterMakeCd,this);

    public MeterConfigCdField meterConfigurationCd = new MeterConfigCdField(meter.meterConfigurationCd, this);

    @UIGroup(column = 1)
    public Group detailContinued = new Group("", this);

    public YesNoLookupField prepaidCd = new YesNoLookupField(meter.prepaidCd, MdmsLocaleId.PRE_PAID, this);

    public YesNoLookupField autoAverageCd = new YesNoLookupField(meter.autoAverageCd, MdmsLocaleId.AUTO_AVERAGE, this);

    public YesNoLookupField meterReadCd = new YesNoLookupField(meter.meterReadCd, MdmsLocaleId.METER_READ, this);

    @UIField(max = 4)
    public LField<Integer> actualSizeWater = new LField<>(meter.actualSizeWater, MdmsLocaleId.ACTUAL_SIZE, this);

    @UIField(max = 4)
    public LField<Integer> relevantSizeWater = new LField<>(meter.relevantSizeWater, MdmsLocaleId.RELEVANT_SIZE, this);

    @UIField(max = 50)
    public LField<String> meterLocation = new LField<>(meter.meterLocation, MdmsLocaleId.METER_LOCATION, this);

    public GPSCoordinatesLField gpsCoordinates = new GPSCoordinatesLField(this);

    public MeterOwnerCdField meterOwnerCd = new MeterOwnerCdField(meter.meterOwnerCd, this);

    public YesNoLookupField activeCd = new YesNoLookupField(meter.meterReadCd, MdmsLocaleId.ACTIVE, this);

    @PlaceOnToolbar
    public ImageGalleryField photos = new ImageGalleryField(MdmsLocaleId.PHOTOS, IceImageCrudGallery.class, meter.meterPhotos, this);

    @UIGroup(column = -1)
    public Group geoGroup = new Group(MdmsLocaleId.LOCATION, null, this);

    public ImageGeoField imageGeoField =
            new ImageGeoField("", meter.geoImage, this, this, true, true, ToolkitConstants.GeoType.LOCATION,ToolkitConstants.GeoType.BUILDING);

    public Group nameGroup = new Group("", this, meterReference, meterN, description, meterMakeCd, meterModelCd, user).setNameGroup();

    public Pane detailPane = new Pane("", this, detail, detailContinued, geoGroup);

    public Pane meterRegister = new Pane(MdmsLocaleId.METER_REGISTER_CAPTION,
            PecMeterRegisterLayout.getMainSQL() + " where pec_meter_register.meter_id = ?",
            PecMeterRegisterLayout.class, new Permission().setMayCreate(true), this);

    public Pane meterReadingPane = new Pane(MdmsLocaleId.METER_READING_DETAIL,
            PecMeterReadingLayout.getMainSQL() + " where pec_meter_register.meter_id = ?",
            PecMeterReadingLayout.class, new Permission().setMayCreate(false), this);

    public Pane propertyPane = new Pane(MdmsLocaleId.PROPERTY_DETAILS,
            UserDetailEntity.formatSql(String.format("select * from pec_property where " +
                    "pec_property.property_id = pec_meter.property_id and pec_property.entity_Status_Cd <> " + MdmsConstants.ENTITY_STATUS_DELETED),"pec_property.user_id"),
            PecPropertyLayout.class, new Permission().setMayCreate(false), this);

    public Pane unitPane = new Pane(MdmsLocaleId.UNIT_DETAIL,
            UserDetailEntity.formatSql(String.format("select * from pec_unit where pec_meter.unit_id = pec_unit.unit_id"),"pec_unit.user_id"),
            PecUnitLayout.class, new Permission().setMayCreate(false), this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor("pec_Meter","meter_id"), MDMSAuditLayout.class,new Permission(0),this);

    public PecMeterLayout() {
        super(MdmsLocaleId.METER_DETAIL);
        init();
    }

    private void init() {
        getPermission().setMayCreate(true);
    }

    PecLocationSurveyEntity getLocationSurveyEntity() {
        try {
            try (Connection connection = getDataSource().getConnection()) {
                if (meter.propertyId.get() != null) {
                    return meter.propertyEntity.getOne(connection, null).locationSurveyEntity.getOne(connection, null);
                } else if (meter.unitId.get() != null) {
                    return meter.unitEntity.getOne(connection, null).propertyEntity.getOne(
                            connection, null).locationSurveyEntity.getOne(connection, null);
                }
                return null;
            }
        } catch (SQLException sqlE) {
            throw new RuntimeException(sqlE);
        }
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        if (meter.meterList.getOne(getDataSource()) != null && !PecMeterReadingListLayout.isInEditableState(meter.meterList.getOne(getDataSource()))) {
            getPermission().setMayUpdate(false);
        }
    }

    @Override
    public void newEvent() {
        meterReference.getField().set(numberService.getHexString(10));
        if (meter.lat.get() == null && getLocationSurveyEntity() != null) {
            updateLocation(getLocationSurveyEntity().sharedLocation.lat.get(),
                    getLocationSurveyEntity().sharedLocation.lon.get());
        }
        super.newEvent();
    }

    @Override
    public void updateLocation(double lat, double lon) {
        meter.lat.set(lat);
        meter.lon.set(lon);
        gpsCoordinates.update(lat, lon);

    }

    @Override
    public void updateValue(ToolkitConstants.GeoType geoType, Double value) {
    }

    @Override
    public double[] getGpsCoordinates() {
        return meter.lat.get() != null ? new double[]{meter.lat.get(), meter.lon.get()} : null;
    }

    @Override
    public boolean save() {
        if (meter.lat.get() == null) {
            updateLocation(imageGeoField.getMapComponent().getMarker().getPoint().getLat(),
                    imageGeoField.getMapComponent().getMarker().getPoint().getLon());
        }
        return super.save();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from pec_meter where 1 = 1";
    }


}
