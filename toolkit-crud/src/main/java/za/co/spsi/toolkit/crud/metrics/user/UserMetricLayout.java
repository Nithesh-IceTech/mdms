package za.co.spsi.toolkit.crud.metrics.user;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.ee.db.DefaultConfig;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/07/03.
 */
@Dependent
public class UserMetricLayout extends Layout {

    @EntityRef(main = true)
    private UserMetricLog userMetric = new UserMetricLog();

    @EntityRef
    private MetricCategory  metricCategory = new MetricCategory();

    public Group detail = new Group(ToolkitLocaleId.METRIC_SCORE, this).setNameGroup();

    @UIField(enabled = false)
    public LField<String> username = new LField<>(userMetric.username, ToolkitLocaleId.USERNAME, this);

    @UIField(enabled = false)
    public TextAreaField description = new TextAreaField(userMetric.description, ToolkitLocaleId.DESCRIPTION, this);

    @UIField(enabled = false)
    public LField<String> point = new LField<>(userMetric.point, ToolkitLocaleId.METRIC_POINT, this);

    @UIField(enabled = false)
    public LField<String> metricType = new LField<>(metricCategory.entityName, ToolkitLocaleId.METRIC_TYPE, this);

    @UIField(enabled = false)
    public LField<Timestamp> logTime = new LField<>(userMetric.logTime, ToolkitLocaleId.METRIC_LOGTIME, this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION,
            AuditConfig.getSql("where audit_log.audit_log_id = user_metric_log.audit_log_id","audit_log.create_time"),AuditLayout.class,new Permission(0),this);

    public Pane detailPane = new Pane(ToolkitLocaleId.USER_DETAIL,this,detail);

    public UserMetricLayout() {
        super(ToolkitLocaleId.USER_DETAIL);
        getPermission().setMayCreate(false).setMayDelete(false);
    }

    public static String getSql() {
        return "select * from user_metric_log left join metric_category on user_metric_log.metric_category_id = metric_category.id ";
    }

    @Override
    public String getMainSql() {
        return getSql() + " order by user_metric_log.log_time desc";
    }

}
