package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.mdms.kamstrup.db.KamstrupGroupEntity;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by johan on 2016/12/05.
 */
public class KamstrupGroupLogLayout extends Layout<KamstrupGroupEntity.LogEntity> {
    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private KamstrupGroupEntity.LogEntity log = new KamstrupGroupEntity.LogEntity();

    @UIGroup(column = 0)
    public Group detail = new Group("LogEntity details",this).setNameGroup();

    @UIField(enabled= false)
    public LField status = new LField(log.status, "Status",this);

    @UIField(enabled= false)
    public LField entryTime = new LField(log.entryTime, "Entry Time",this);

    @UIField(enabled= false,rows = 5,uppercase = false)
    public TextAreaField error = new TextAreaField(log.error, "Error",this);

    public Pane detailPane = new Pane("",this, detail);

    public KamstrupGroupLogLayout() {
        super("Group Logs");
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from kamstrup_group_log where 1 = 1";
    }
}
