package za.co.spsi.toolkit.crud.login;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.metrics.user.UserMetricLayout;
import za.co.spsi.toolkit.ee.db.DefaultConfig;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Created by jaspervdbijl on 2017/07/03.
 */
@Dependent
public class UserDetailLayout extends Layout {

    @Inject
    private DefaultConfig defaultConfig;

    @EntityRef(main = true)
    private UserDetailEntity userDetail = new UserDetailEntity();

    public Group detail = new Group(ToolkitLocaleId.USER_DETAIL, this).setNameGroup();

    @UIField(enabled = false)
    public LField<String> username = new LField<>(userDetail.username, ToolkitLocaleId.USERNAME, this);

    @UIField(enabled = false)
    public LField<String> firstName = new LField<>(userDetail.firstName, ToolkitLocaleId.FIRST_NAME, this);


    @UIField(enabled = false)
    public LField<String> lastName = new LField<>(userDetail.lastName, ToolkitLocaleId.LAST_NAME, this);

    @UIField(enabled = false)
    public LField<String> lastLogin = new LField<>(userDetail.lastLogin, ToolkitLocaleId.LAST_LOGIN, this);

    @UIField(enabled = false)
    public LField<String> metricTotal = new LField<>(userDetail.metricTotal, ToolkitLocaleId.METRIC_TOTAL, this);

    public Pane detailPane = new Pane(ToolkitLocaleId.USER_DETAIL,this,detail);

    public Pane metricPane = new Pane(ToolkitLocaleId.USER_METRIC,
            UserMetricLayout.getSql()+" where user_metric_log.username = user_detail.user_name order by user_metric_log.log_time desc",
            UserMetricLayout.class,new Permission(0),this);

    public UserDetailLayout() {
        super(ToolkitLocaleId.USER_DETAIL);
        getPermission().setMayCreate(false).setMayDelete(false);
    }

    @Override
    public String getMainSql() {
        return super.getMainSql()  +
                String.format(" where ou is null or ou in (%s) order by user_name asc", ToolkitCrudConstants.getChildrenAgencyIds() != null?
                        ToolkitCrudConstants.getChildrenAgencyIds().stream().map(a -> "'"+a+"'").reduce((a, b)->a+","+b).get():"'NONE'");
    }

}
