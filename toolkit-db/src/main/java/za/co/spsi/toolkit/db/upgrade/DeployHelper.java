package za.co.spsi.toolkit.db.upgrade;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.DBUtil;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/11/28.
 */
public class DeployHelper implements AutoCloseable {

    public static final Logger TAG = Logger.getLogger(DeployHelper.class.getName());

    private DeployLogEntity deploy;
    private String upgradeUserRightsSql, rUser, oUser;
    private boolean upgraded = false;
    private Connection proxyConnection;
    private TableList tableList;

    public DeployHelper(DeployLogEntity deploy, String upgradeUserRightsSql, Connection proxyConnection,
                        String oUser, String rUser) {

        this.deploy = deploy;
        this.upgradeUserRightsSql = upgradeUserRightsSql;
        this.proxyConnection = proxyConnection;
        this.oUser = oUser;
        this.rUser = rUser;
    }

    public void upgradeTables(Connection connection, String upgradePaths[], List<Class> exclusions, boolean devMode) {
        TAG.info("Upgrade Tables. Paths " + new StringList(upgradePaths));
        try {
            tableList = new TableList();
            for (String path : upgradePaths) {
                tableList.addAll(new ArrayList(Util.getTypesAnnotatedWith(path, Table.class)));
            }
            for (Class type : tableList.removeDuplicates().sortOnDependencies()) {
                if (!exclusions.contains(type)) {
                    TAG.info("MAINTAIN TABLE : " + type.getSimpleName());
                    upgraded = DBUtil.maintain(connection, (EntityDB) type.newInstance(), devMode) | upgraded;
                    if (upgraded) connection.commit();
                } else {
                    TAG.info("MAINTAIN TABLE EXCLUDED : " + type.getSimpleName());
                }
            }
        } catch (Exception ex) {
            TAG.log(Level.WARNING, ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    public void setUserRights(Connection connection, String sql) {
        TAG.info("Set user rights");
        String[] statements = sql.split("_nl_");
        for (String sqlString : statements) {
            deploy.appendSql(sqlString);
            DataSourceDB.execute(connection, sqlString);
        }
    }

    public Connection getUpgradeConnection() {
        try {
            Class.forName(deploy.driver.get());
            return DriverManager.getConnection(deploy.url.get(), deploy.username.get(), deploy.password.get());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void updateUserRights(String sql) {
        try {
            if (deploy.roleName.get() != null && deploy.owner.get() != null) {
                setUserRights(proxyConnection, sql.replace("_ROLE_", deploy.roleName.get()).
                        replace("_OWNER_", deploy.username.get()).replace("_USERNAME_", deploy.owner.get()));

            } else {
                TAG.warning("Did not update rights. No owner or rolename was set");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void close() {

        if (upgradeUserRightsSql != null) {
            updateUserRights(upgradeUserRightsSql);
        }

        // Disable proxy user
//        DriverFactory.getHelper(proxyConnection).disableProxyUser(rUser, oUser, proxyConnection);
    }
}
