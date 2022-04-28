package za.co.spsi.toolkit.crud.db;

import javax.sql.DataSource;

public abstract class DefaultMasterNodeConfig {
    public abstract DataSource getDataSource();
}
