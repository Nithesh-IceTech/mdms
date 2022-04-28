package za.co.spsi.mdms.web.gui.layout.surveys;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.common.db.survey.*;
import za.co.spsi.mdms.common.services.SummaryService;
import za.co.spsi.mdms.kamstrup.services.utility.MeterRegisterUpdateService;
import za.co.spsi.mdms.web.gui.fields.AgencyField;
import za.co.spsi.mdms.web.gui.layout.IceImageCrudGallery;
import za.co.spsi.mdms.web.gui.layout.MDMSAuditLayout;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.*;
import za.co.spsi.toolkit.crud.gui.fields.*;
import za.co.spsi.toolkit.crud.gui.gis.MapComponent;
import za.co.spsi.toolkit.crud.login.UserDetailEntity;
import za.co.spsi.toolkit.dao.ToolkitConstants;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Timestamp;

import static za.co.spsi.toolkit.crud.gui.gis.GeoMap.*;
import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

public class PecMeterReadingLayout extends Layout<PecMeterReadingEntity> implements MapComponent.ValueUpdateCallback {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @EntityRef(main = true)
    private PecMeterReadingEntity meterReading = new PecMeterReadingEntity();

    private PecPropertyEntity property = new PecPropertyEntity();
    private PecMeterEntity meterEntity = new PecMeterEntity();
    private PecMeterRegisterEntity meterRegister = new PecMeterRegisterEntity();

    private AgencyField agency = new AgencyField(meterReading.agencyId, "", this);

    @UIGroup(column = 0)
    public Group meterDetail = new Group(MdmsLocaleId.METER_CAPTION, this);

    @UIField(enabled = false)
    private LField<String> propertyName = new LField<>(property.propertyName, MdmsLocaleId.PROPERTY_NAME, this);

    @UIField(enabled = false)
    private LField<String> company = new LField<>(property.company, MdmsLocaleId.COMPANY, this);

    @UIField(enabled = false)
    private LField<String> meterN = new LField<>(meterEntity.meterN, MdmsLocaleId.METER_NUMBER, this);
    @UIField(enabled = false)
    private LField<String> meterSeq = new LField<>(meterRegister.meterSeq, MdmsLocaleId.METER_SEQUENCE, this);
    private LField<String> loggerId = new LField<>(meterRegister.loggerId, MdmsLocaleId.LOGGER_ID, this);
    private LField<String> registerId = new LField<>(meterRegister.registerId, MdmsLocaleId.REGISTER_ID, this);

    private LField<String> timeOfUseStartTime = new LField<>(meterRegister.timeOfUseStartTime, "TOU Start", this);
    private LField<String> timeOfUseEndTime = new LField<>(meterRegister.timeOfUseEndTime, "TOU End", this);
    private LField<String> timeOfUseDayOfWeekName = new LField<>(meterRegister.timeOfUseDayOfWeekName, "TOU Week", this);
    private LField<String> dateRx = new LField<>(meterRegister.dateRx, "TOU Specific Date", this);

    @UIGroup(column = 0)
    public Group readingDetail = new Group(MdmsLocaleId.METER_READING_DETAIL, this);

    public LField<Double> reading = new LField<>(meterReading.reading, MdmsLocaleId.CURRENT_READING, this);

    public LField<Double> actualReading = new LField<>(meterReading.actualReading, MdmsLocaleId.ACTUAL_READING, this);

    public LField<Timestamp> readingDate = new LField<>(meterReading.readingDate, MdmsLocaleId.CURRENT_DATE, this);

    public LField<Double> smartReading = new LField<>(meterReading.smartReading, MdmsLocaleId.SMART_READING, this);

    @UIField(enabled = false)
    public LField<Double> prevReading1 = new LField<>(meterReading.prevReading1, MdmsLocaleId.READING_1, this);

    @UIField(enabled = false)
    public LField<Timestamp> prevReadingDate1 = new LField<>(meterReading.prevReadingDate1, MdmsLocaleId.READING_1_DATE, this);

    @UIField(enabled = false)
    public LField<Double> prevReading2 = new LField<>(meterReading.prevReading2, MdmsLocaleId.READING_2, this);

    @UIField(enabled = false)
    public LField<Timestamp> prevReadingDate2 = new LField<>(meterReading.prevReadingDate2, MdmsLocaleId.READING_2_DATE, this);

    public MLCSLookupField noReadingReasonCd = new MLCSLookupField(meterReading.noReadingReasonCd, MdmsLocaleId.NO_READING_REASON, this, MdmsConstants.NO_READING_REASON_CD);

    public TextAreaField comments = new TextAreaField(meterReading.comments, MdmsLocaleId.COMMENTS, this);

    public TextAreaField deviationComment = new TextAreaField(meterReading.deviationComment, MdmsLocaleId.DEVIATION_COMMENT, this);

    public GPSCoordinatesLField gpsCoordinates = new GPSCoordinatesLField(this);

    @UIGroup(column = 0)
    public Group meterLinkGroup = new Group(MdmsLocaleId.METER_CAPTION, null, this);
    public LookupField<String> kamMeterId = new LookupField(meterEntity.kamMeterId, "Kamstrup Meter",
            "select meter_id,serial_n from kamstrup_meter where serial_n is not null", this);

    public LookupField<String> nesMeterId = new LookupField(meterEntity.nesMeterId, "Nes Meter",
            "select meter_id,serial_n from nes_meter where serial_n is not null", this);

    public LookupField<String> elsMeterId = new LookupField(meterEntity.elsMeterId, "Els Meter",
            "select meter_id,serial_n from elster_meter where serial_n is not null", this);

    public LookupField<String> genMeterId = new LookupField(meterEntity.genericMeterId, "Generic Meter",
            "select generic_meter_id,meter_serial_n from generic_meter where meter_serial_n is not null", this);

    @UIGroup(column = -1)
    public Group geoGroup = new Group(MdmsLocaleId.LOCATION, null, this);

    public GeoField geoField =  new GeoField("", meterEntity.lon, meterEntity.lat, this, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group photoGroup = new Group(MdmsLocaleId.PHOTOS, null, this);

    @PlaceOnToolbar
    public ImageGalleryField photos = new ImageGalleryField(MdmsLocaleId.PHOTOS, IceImageCrudGallery.class, meterReading.photo, this);

    public Group nameGroup = new Group("", this, propertyName, meterN, registerId, reading, smartReading
            , readingDate, timeOfUseStartTime, timeOfUseEndTime, timeOfUseDayOfWeekName, dateRx
            , company, meterSeq, noReadingReasonCd, comments, prevReading1, prevReadingDate1, prevReading2, prevReadingDate2).setNameGroup();

    public Pane detailPane = new Pane("", this, readingDetail, meterDetail, meterLinkGroup, geoGroup);

    @OneToOne
    public Pane meterRegisterPane = new Pane("Meter register detail",
            PecMeterRegisterLayout.getMainSQL() + " where pec_meter_register.meter_register_id = pec_meter_reading.meter_register_id",
            PecMeterRegisterLayout.class, new Permission().setMayCreate(false).setMayDelete(false), this);


    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor( "pec_meter_reading","meter_reading_id"), MDMSAuditLayout.class, new Permission(0), this);

    public PecMeterReadingLayout() {
        super(MdmsLocaleId.METER_READING_DETAIL);
        init();
    }

    private void init() {
    }

    @Override
    public boolean shouldAddFilterOnPaneList() {
        return true;
    }


    @Override
    public void beforeOnScreenEvent() {
        Object view = getBean(getBeanManager(), MeterRegisterUpdateService.class);
        System.out.print(view);
        // if closed then it becomes read only
        final PecMeterEntity meter = meterReading.meter.getOne(getDataSource());
        this.meterEntity.copyStrict(meter);
        meterRegister.copyStrict(meterReading.register.getOne(getDataSource()));
        this.meterEntity.setInDatabase(true);
        final PecMeterReadingListEntity list = meterReading.register.getOne(getDataSource()).list.getOne(getDataSource());
        getPermission().setMayUpdate(PecMeterReadingListLayout.isInEditableState(list) && !meter.isSmartMeter());
        geoField.getMapComponent().updateState(STATE_LOCATE | STATE_SEARCH | STATE_ENLARGE);
        super.beforeOnScreenEvent();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public static String getMainSQL() {
        return UserDetailEntity.formatSql(
                "select * from pec_meter_reading left join pec_meter_register on pec_meter_register.meter_register_id = pec_meter_reading.meter_register_id " +
                        "left join pec_meter_reading_list on pec_meter_reading_list.METER_READING_LIST_ID = pec_meter_register.METER_READING_LIST_ID " +
                        "left join pec_meter on pec_meter_register.meter_id = pec_meter.meter_id " +
                        "left join pec_property on pec_property.property_id = pec_meter.property_id ",
                "pec_meter_reading.user_id");
    }

    @Override
    public String getMainSql() {
        return getMainSQL() + "where 1 = 1";
    }

    @Override
    public double[] getGpsCoordinates() {

        if (!this.meterEntity.isInDatabase()) {
            final PecMeterEntity meter = meterReading.meter.getOne(getDataSource());
            this.meterEntity.copyStrict(meter);
            this.meterEntity.setInDatabase(true);
        }
        return meterEntity.lat.get() != null ? new double[]{meterEntity.lat.get(), meterEntity.lon.get()} : null;
    }


    @Override
    public void updateValue(ToolkitConstants.GeoType geoType, Double value) {

    }

    @Override
    public void updateLocation(double lat, double lon) {
        meterEntity.lat.set(lat);
        meterEntity.lon.set(lon);
        gpsCoordinates.update(lat, lon);

    }

}