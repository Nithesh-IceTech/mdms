package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class ElsterMeterLayout extends Layout<ElsterMeterEntity> {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private ElsterMeterEntity meter = new ElsterMeterEntity();

    @UIGroup(column = 0)
    public Group detail = new Group("Elster Meter Detail", this);

    @UIField(enabled = false)
    public LField serialN = new LField(meter.serialN, "Serial N", this);


    public Group nameGroup = new Group("", this, serialN).setNameGroup();

    //    public Pane detailPane = new Pane("Meter Details",this, detail,status,control);
    public Pane detailPane = new Pane("Meter Details", this, detail);

    public Pane readingPane = new Pane("Readings", MeterReadingLayout.getSql() + " where els_meter_id = ? order by entry_time desc", MeterReadingLayout.class,
            new Permission(0), this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor("elster_meter", "meter_id"), MDMSAuditLayout.class, new Permission(0), this);

    public ElsterMeterLayout() {
        super("Elster Meter Detail");
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
        return "select * from ELSTER_METER where 1 = 1";
    }

    @Override
    public String[][] getFilters() {
        return MeterFilterHelper.getFilters("elster_meter", "els_meter_id").toArray(new String[][]{});
    }

    @Override
    public String getExportSheetName() {
        if(serialN != null && serialN.get() != null && serialN.get() != "") {
            return  "_" + serialN.getAsString();
        }else{
            return super.getExportSheetName();
        }
    }

}
