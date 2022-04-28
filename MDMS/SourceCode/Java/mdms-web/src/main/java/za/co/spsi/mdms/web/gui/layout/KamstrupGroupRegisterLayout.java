package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.mdms.kamstrup.db.KamstrupGroupRegisterEntity;
import za.co.spsi.mdms.web.ui.UIConstants;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.LayoutException;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by johan on 2016/12/05.
 */
public class KamstrupGroupRegisterLayout extends Layout<KamstrupGroupRegisterEntity> {
    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private KamstrupGroupRegisterEntity register = new KamstrupGroupRegisterEntity();

    @UIGroup(column = 0)
    public Group detail = new Group("Register details", this).setNameGroup();

    @UIField(writeOnce = true)
    public LField description = new LField(register.description, "Description", this);

    @UIField(writeOnce = true, regex = UIConstants.LOGGER_ID_REGEX)
    public LField<String> registerId = new LField(register.registerId, "Register", this);

    public Pane detailPane = new Pane("", this, detail);

    public KamstrupGroupRegisterLayout() {
        super("Group Register");
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from kamstrup_group_register where 1 = 1";
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
    }

    @Override
    public void newEvent() {
        // if the group does not have a logger, then only one register may be created
        if (StringUtils.isEmpty(register.group.getOne(getDataSource()).loggerId.get()) &&
                DSDB.executeQuery(getDataSource(), Integer.class, "select count(*) from kamstrup_group_register where group_id = ?",register.groupId.get()) > 0) {
            throw new LayoutException("No logger specified. Only one register may be added");
        }
    }
}
