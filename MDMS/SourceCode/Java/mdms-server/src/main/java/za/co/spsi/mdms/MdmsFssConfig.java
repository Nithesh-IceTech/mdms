package za.co.spsi.mdms;

import za.co.spsi.toolkit.db.audit.AuditEntity;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.db.DefaultConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * Created by jaspervdbijl on 2017/07/04.
 */
@Dependent
@Startup
public class MdmsFssConfig extends DefaultConfig {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    @ConfValue(value = "metrics.enabled")
    private boolean metricsEnabled;

    @PostConstruct
    private void init() {
        AuditEntity.METRICS_ENABLED = metricsEnabled;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Driver getDriver() {
        return DriverFactory.getDriver();
    }
}
