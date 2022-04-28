package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.web.gui.fields.MeterTypeCdField;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;


public class GenericMeterLayout extends Layout<GenericMeterEntity> {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private GenericMeterEntity genericMeterEntity = new GenericMeterEntity();

    @UIGroup(column = 0)
    public Group detail = new Group("Generic Meter Detail", this);

    @UIField(enabled = false)
    public LField serialN = new LField(genericMeterEntity.meterSerialN, "Serial N", this);

    @UIField(mandatory = true)
    public MeterTypeCdField meterType = new MeterTypeCdField(genericMeterEntity.meterType, this);

    public Group nameGroup = new Group("", this, serialN, meterType).setNameGroup();
    public Pane detailPane = new Pane("Meter Details", this, detail);
    public Pane readingPane = new Pane("Readings",
            MeterReadingLayout.getSql() +
                    " where meter_reading.generic_meter_id = ? order by entry_time desc", MeterReadingLayout.class,
            new Permission(0), this);

    public Pane brokerCommandHistoryPane = new Pane("Broker Command History",
            "select * from GENERIC_BROKER_COMMAND where meter_id = ? order by created_date desc", GenericMeterBrokerCommandHistoryLayout.class,
            new Permission(0), this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor( "generic_meter", "generic_meter_id"), MDMSAuditLayout.class, new Permission(0), this);

    public GenericMeterLayout() {
        super("Generic Meter Detail");
        setPermission(new Permission(0));
        init();
    }

    private void init() {
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from GENERIC_METER where 1 = 1";
    }

    @Override
    public String[][] getFilters() {
        return MeterFilterHelper.getFilters("generic_meter", "generic_meter_id").toArray(new String[][]{});
    }

    @Override
    public String getExportSheetName() {
        if (serialN != null && serialN.get() != null && serialN.get() != "") {
            return "_" + serialN.getAsString();
        } else {
            return super.getExportSheetName();
        }
    }

}
