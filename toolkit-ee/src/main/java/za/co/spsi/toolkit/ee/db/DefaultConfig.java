package za.co.spsi.toolkit.ee.db;

import za.co.spsi.toolkit.db.drivers.Driver;

import javax.sql.DataSource;

/**
 * Created by jaspervdbijl on 2017/07/04.
 * Implement to wire default data source
 */
public abstract class DefaultConfig {
    public abstract DataSource getDataSource();
    public abstract Driver getDriver();
}
