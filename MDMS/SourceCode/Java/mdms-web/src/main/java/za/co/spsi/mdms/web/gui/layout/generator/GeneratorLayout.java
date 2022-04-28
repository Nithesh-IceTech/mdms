package za.co.spsi.mdms.web.gui.layout.generator;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.db.generator.GeneratorEntity;
import za.co.spsi.mdms.web.gui.layout.MDMSAuditLayout;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.custom.SwitchField;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class GeneratorLayout extends Layout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private GeneratorEntity generator = new GeneratorEntity();

    @UIGroup(column = 0)
    public Group detailGroup = new Group("Generator Detail", this).setNameGroup();

    @UIField(enabled = false)
    public LField serialN = new LField(generator.serialN,"Serial Number",this);

    @UIField(enabled = false)
    public LField msisdn = new LField(generator.msisdn,"Msisdn",this);

    @UIField(enabled = false)
    public LField description = new LField(generator.description,"Description",this);

    @UIField()
    public LField  make = new LField(generator.make,"Make",this);
    @UIField()
    public LField model = new LField(generator.model,"Model",this);

    @UIField(enabled = false)
    @UI(width = "-1px")
    public SwitchField statusOn = new SwitchField(generator.stateOn, "Status On/Off", this);

    @UIField(enabled = false)
    @UI(width = "-1px")
    public SwitchField enabled = new SwitchField(generator.enabled, "Active", this);

    public Pane detailPane = new Pane("Group Details", this, detailGroup);

    public Pane txLog = new Pane("Tx Logs","select * from gen_transaction where gen_transaction.gen_id = ? order by tx_start desc",
            GeneratorTransactionLayout.class,new Permission(0),this);

    public Pane meters = new Pane("Meters",GeneratorMeterLinkLayout.getSQL() + " where gen_meter_link.gen_id = ?",
            GeneratorMeterLinkLayout.class,this);

    public Pane commsLog = new Pane("Comms Logs","select * from gen_comms_log where gen_comms_log.gen_id = ? order by received desc",
            CommunicationLogLayout.class,new Permission(0),this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor("generator","id"), MDMSAuditLayout.class,new Permission(0),this);

    public GeneratorLayout() {
        super("Generator");
        getPermission().setMayCreate(false);
    }

    @Override
    public String getMainSql() {
        return "select * from generator order by description asc";
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }


}
