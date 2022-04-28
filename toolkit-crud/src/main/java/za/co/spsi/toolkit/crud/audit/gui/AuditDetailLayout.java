package za.co.spsi.toolkit.crud.audit.gui;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.db.audit.AuditDetailEntity;
import za.co.spsi.toolkit.ee.db.DefaultConfig;

import javax.inject.Inject;
import javax.sql.DataSource;

@Qualifier(roles = {@Role(value = "Supervisor",write = false,create = false)})
public class AuditDetailLayout extends Layout {

    @Inject
    private DefaultConfig defaultConfig;

    @EntityRef(main = true)
    private AuditDetailEntity audit = new AuditDetailEntity();

    @UIGroup(column = 0)
    public Group detail = new Group(ToolkitLocaleId.AUDIT_DETAIL_CAPTION, this);


    public LField fieldName = new LField(audit.fieldName,ToolkitLocaleId.AUDIT_FIELD,this);
    public LField oldValue = new LField(audit.oldValue,ToolkitLocaleId.AUDIT_OLD_VALUE,this);
    public LField newValue = new LField(audit.newValue,ToolkitLocaleId.AUDIT_NEW_VALUE,this);

    public Group nameGroup = new Group("", this, fieldName,oldValue,newValue).setNameGroup();

    public Pane detailPane = new Pane(ToolkitLocaleId.AUDIT_DETAIL_CAPTION,this,detail);

    public AuditDetailLayout() {
        super(ToolkitLocaleId.AUDIT_CAPTION);
    }

    public String getMainSql() {
        return "select * from audit_log_detail";
    }

    @Override
    public DataSource getDataSource() {
        return defaultConfig.getDataSource();
    }
}
