package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.mdms.kamstrup.db.KamstrupMeterRegisterEntity;
import za.co.spsi.mdms.web.ui.UIConstants;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by johan on 2016/12/05.
 */
public class KamstrupMeterRegisterLayout extends Layout<KamstrupMeterRegisterEntity> {
    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private KamstrupMeterRegisterEntity register = new KamstrupMeterRegisterEntity();

    @UIGroup(column = 0)
    public Group detail = new Group("Register details",this).setNameGroup();

    public LField id = new LField(register.id, "ID",this);
    public LField name = new LField(register.name, "Name",this);
    public LField command = new LField(register.command, "Command",this);
    public LField actions = new LField(register.actions, "Action",this);
    public LField autoCollect = new LField(register.autoCollect, "Auto Collect",this);

    public Pane detailPane = new Pane("",this, detail);

    public KamstrupMeterRegisterLayout() {
        super("Meter Register");
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from kamstrup_meter_register where 1 = 1";
    }
}
