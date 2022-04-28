package za.co.spsi.mdms.web.gui.layout.surveys;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.mdms.web.gui.fields.AgencyField;
import za.co.spsi.mdms.web.gui.fields.MeterRegisterTypeCdField;
import za.co.spsi.mdms.web.gui.fields.YesNoLookupField;
import za.co.spsi.mdms.web.gui.layout.MDMSAuditLayout;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.ano.UILayout;
import za.co.spsi.toolkit.crud.gui.fields.EntityLookupField;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.login.UserDetailEntity;
import za.co.spsi.toolkit.entity.Field;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class PecMeterRegisterLayout extends Layout<PecMeterRegisterEntity> {

    private static final Map<String, String> registerMap = new HashMap<>();

    static {
        registerMap.put("1.1.1.8.0.255", "TOTAL_KWHP");
        registerMap.put("1.1.2.8.0.255", "TOTAL_KWHN");
        registerMap.put("1.1.3.8.0.255", "TOTAL_KVARP");
        registerMap.put("1.1.4.8.0.255", "TOTAL_KVARN");
        registerMap.put("1.1.1.8.1.255", "T1_KWHP");
        registerMap.put("1.1.2.8.1.255", "T1_KWHN");
        registerMap.put("1.1.3.8.1.255", "T1_KVARP");
        registerMap.put("1.1.4.8.1.255", "T1_KVARN");
        registerMap.put("1.1.1.8.2.255", "T2_KWHP");
        registerMap.put("1.1.2.8.2.255", "T2_KWHN");
        registerMap.put("1.1.3.8.2.255", "T2_KVARP");
        registerMap.put("1.1.4.8.2.255", "T2_KVARN");
        registerMap.put("1.1.9.6.0.255", "TOTAL_MAX_DEMAND_PO_KVA");
        registerMap.put("0.2.1.130.0.255", "TOTAL_MAX_DEMAND_PO_RTC");
        registerMap.put("1.1.32.25.0.255", "RMS_L1_V");
        registerMap.put("1.1.52.25.0.255", "RMS_L2_V");
        registerMap.put("1.1.72.25.0.255", "RMS_L3_V");
        registerMap.put("1.1.31.25.0.255", "RMS_L1_C");
        registerMap.put("1.1.51.25.0.255", "RMS_L2_C");
        registerMap.put("1.1.71.25.0.255", "RMS_L3_C");
        registerMap.put("8.1.1.0.0.255", "VOLUME_1");
        registerMap.put("9.1.1.0.0.255", "VOLUME_1");
    }

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private PecMeterRegisterEntity meterRegister = new PecMeterRegisterEntity();

    private AgencyField agency = new AgencyField(meterRegister.agencyId, "", this);

    private LField<String> user = new LField<String>(meterRegister.userId, MdmsLocaleId.USERNAME, this);

    @UIGroup(column = 0)
    public Group meterInstallationDetail = new Group(MdmsLocaleId.METER_INSTALLATION_DETAIL, this);

    @UIField(writeOnce = true, mandatory = true)
    public EntityLookupField meterLookupField = new EntityLookupField(meterRegister.meterId, MdmsLocaleId.METER_CAPTION, PecMeterLayout.class, this);

    @UIGroup(column = 0)
    public Group meterRegisterDetail = new Group(MdmsLocaleId.METER_REGISTER_DETAIL, this);

    public MeterRegisterTypeCdField meterRegisterTypeCd = new MeterRegisterTypeCdField(meterRegister.meterRegisterTypeCd, this);

    public LField<Integer> digits = new LField<>(meterRegister.digits, MdmsLocaleId.METER_REGISTER_DIGITS, this);

    public YesNoLookupField autoResetCd = new YesNoLookupField(meterRegister.autoResetCd, MdmsLocaleId.AUTO_RESET, this);

    public MLCSLookupField<String> autoResetDayCd = new MLCSLookupField<>(meterRegister.autoResetDayCd, MdmsLocaleId.AUTO_RESET_DAY, this, MdmsConstants.DAY_OF_MONTH);

    public MLCSLookupField<String> readingTypeCd = new MLCSLookupField<>(meterRegister.readingTypeCd, MdmsLocaleId.READING_TYPE, this, MdmsConstants.READING_TYPE);

    public MLCSLookupField<String> unitOfMeasureCd = new MLCSLookupField<>(meterRegister.unitOfMeasureCd, MdmsLocaleId.UNIT_OF_MEASURE, this, MdmsConstants.UNIT_OF_MEASURE);

    public TextAreaField description = new TextAreaField(meterRegister.description, MdmsLocaleId.DESCRIPTION, this);

    private LField<String> loggerId = new LField<>(meterRegister.loggerId, MdmsLocaleId.LOGGER_ID, this);
    private LField<String> registerId = new LField<>(meterRegister.registerId, MdmsLocaleId.REGISTER_ID, this);
    private LField<String> registerName = new LField<>((Field)null, MdmsLocaleId.REGISTER_NAME, this);
    private LField<String> channelId = new LField<>(meterRegister.channelId, MdmsLocaleId.CHANNEL_ID, this);

    private LField<String> timeOfUseStartTime = new LField<>(meterRegister.timeOfUseStartTime, "TOU Start", this);
    private LField<String> timeOfUseEndTime = new LField<>(meterRegister.timeOfUseEndTime, "TOU End", this);
    private LField<String> timeOfUseDayOfWeekName = new LField<>(meterRegister.timeOfUseDayOfWeekName, "TOU Week", this);
    private LField<String> dateRx = new LField<>(meterRegister.dateRx, "TOU Specific Date", this);

    public YesNoLookupField activeCd = new YesNoLookupField(meterRegister.activeCd, MdmsLocaleId.ACTIVE, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group photoGroup = new Group(MdmsLocaleId.PHOTOS, null, this);

    public Group nameGroup = new Group("", this, meterRegisterTypeCd, loggerId, registerId).setNameGroup();

    public Pane detailPane = new Pane("", this, meterInstallationDetail, meterRegisterDetail);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor( "pec_meter_register", "meter_register_id"), MDMSAuditLayout.class, new Permission(0), this);

    public PecMeterRegisterLayout() {
        super(MdmsLocaleId.METER_REGISTER_CAPTION);
        init();
    }

    private void init() {
        getPermission().setMayCreate(true);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public static String getMainSQL() {
        return UserDetailEntity.formatSql("select * from pec_meter_register ", "pec_meter_register.user_id");
    }

    @Override
    public String getMainSql() {
        return getMainSQL() + "where 1 = 1";
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        if (meterRegister.list.getOne(getDataSource()) != null && !PecMeterReadingListLayout.isInEditableState(meterRegister.list.getOne(getDataSource()))) {
            getPermission().setMayUpdate(false);
        }

        registerName.set(registerMap.get(registerId.get()));
    }
}