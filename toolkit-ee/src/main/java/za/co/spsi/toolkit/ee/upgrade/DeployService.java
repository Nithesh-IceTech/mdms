package za.co.spsi.toolkit.ee.upgrade;


import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.upgrade.DeployHelper;
import za.co.spsi.toolkit.db.upgrade.DeployLogEntity;
import za.co.spsi.toolkit.ee.maintenance.DatabaseMaintenance;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.properties.TextFile;

import javax.annotation.PostConstruct;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Log
public abstract class DeployService {

    public static final Logger TAG = Logger.getLogger(DeployService.class.getName());

    private static boolean upgraded = false;

    private static final String SYNC_OBJECT = UUID.randomUUID().toString();

    @Inject
    @ConfValue("db.frw.engineering.r_user")
    private String frwEngineeringRUser;

    @Inject
    @ConfValue("db.frw.engineering.o_user")
    private String frwEngineeringOUser;

    @Inject
    @ConfValue("db.frw.engineering.enabled")
    @Getter
    private Boolean frwEngineeringEnabled;

    @Inject
    @TextFile("database/oracle_user_rights.sql")
    private String userRightsOracleSql;

    @Inject
    @TextFile("database/postgres_user_rights.sql")
    private String userRightsPostgreSql;

    @Inject
    @ConfValue(value = "db.upgrade.update_rights")
    private Boolean updateRights;

    @Inject
    @ConfValue(value = "db.upgrade.liquibase")
    private Boolean runLiquibase = false;

    @Inject
    @ConfValue(value = "db.upgrade.clear_user")
    private boolean clearUser;

    @Inject
    @ConfValue(value = "db.upgrade.devmode")
    private boolean devMode = false;

    @Inject
    @ConfValue(value = "db.upgrade.use_current_user")
    private boolean userCurrentUser = false;

    @Inject
    private DatabaseMaintenance databaseMaintenance;

    protected abstract String[] getUpgradePaths();

    protected abstract DataSource getDataSource();

    protected Class[] getExclusions() {
        return new Class[]{};
    }

    private Connection getUpgradeConnection(DeployHelper deployHelper) throws SQLException {
        return userCurrentUser ? getDataSource().getConnection() : deployHelper.getUpgradeConnection();
    }

    @SneakyThrows
    public void testProxyConnection() {
        log.warning(String.format("Testing FE with options R User [%s] O User [%s]",frwEngineeringRUser,frwEngineeringOUser));
        Connection uUserConnection = getDataSource().getConnection();
        try (Connection proxyConn = DriverFactory.getDriver().enableProxyUser(frwEngineeringRUser, frwEngineeringOUser, uUserConnection)) {
        }
    }

    @PostConstruct
    public void upgrade() throws SQLException {
        synchronized (SYNC_OBJECT) {
            if (!upgraded) {
                upgraded = true;

                if (frwEngineeringEnabled) {

                    TAG.log(Level.INFO, "Starting FE procedure");
                    Connection uUserConnection = getDataSource().getConnection();

                    Connection proxyConn = isOracle(uUserConnection) ?
                            DriverFactory.getHelper(uUserConnection).enableProxyUser(frwEngineeringRUser, frwEngineeringOUser, uUserConnection) :  // Oracle Datasource
                            uUserConnection;

                    if (runLiquibase) {
                        try {
                            TAG.log(Level.INFO, "Pre-FE Liquibase Procedure");
                            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(proxyConn));
                            Liquibase liquibase = new Liquibase("liquibase/masterlog/prefe/db.changelog.master.xml", new ClassLoaderResourceAccessor(), database);
                            liquibase.update((Contexts) null);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    // Create DeployLogEntity
                    DeployLogEntity deployLogEntity = new DeployLogEntity();
                    deployLogEntity.setDeployLogId();
                    deployLogEntity.startTime.set(new Timestamp(System.currentTimeMillis()));
                    deployLogEntity.createTime.set(new Timestamp(System.currentTimeMillis()));
                    deployLogEntity.roleName.set(frwEngineeringRUser.toUpperCase());
                    deployLogEntity.owner.set(uUserConnection.getMetaData().getUserName().toUpperCase());
                    deployLogEntity.username.set(frwEngineeringOUser.toUpperCase());
                    DataSourceDB.set(getDataSource(), deployLogEntity);

                    try {
                        try (DeployHelper deployHelper = new DeployHelper(deployLogEntity,
                                updateRights ? (isOracle(proxyConn) ? userRightsOracleSql : userRightsPostgreSql) : null,
                                proxyConn, frwEngineeringOUser, frwEngineeringRUser)) {
                            try {
                                DataSourceDB.set(getDataSource(), (EntityDB) deployLogEntity.status.set(DeployLogEntity.Status.STARTED.getCode()));
                                DataSourceDB.SQLEventCallback event = DataSourceDB.registerEvent(sql -> {
                                    TAG.warning(sql);
                                    deployLogEntity.appendSql(sql);
                                });
                                try {
                                    deployHelper.upgradeTables(proxyConn, getUpgradePaths(), Arrays.asList(getExclusions()),devMode);
                                } finally {
                                    DataSourceDB.deRegisterEvent(event);
                                }

                                if (runLiquibase) {
                                    try {
                                        TAG.log(Level.INFO, "Post-FE Liquibase Procedure");
                                        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(proxyConn));
                                        Liquibase liquibase = new Liquibase("liquibase/masterlog/postfe/db.changelog.master.xml", new ClassLoaderResourceAccessor(), database);
                                        liquibase.update((Contexts) null);
                                    } catch (Exception ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                                deployLogEntity.status.set(DeployLogEntity.Status.COMPLETED.getCode());
                                TAG.log(Level.INFO, "Completed FE procedure");
                            } catch (Exception ex) {
                                deployLogEntity.error.set(ex);
                                deployLogEntity.status.set(DeployLogEntity.Status.FAILED.getCode());
                            }
                        }
                    } finally {
                        if (clearUser && !devMode) {
                            deployLogEntity.clearProperties(getDataSource());
                        }
                        DataSourceDB.set(getDataSource(), deployLogEntity);
                    }
                }
            }
        }
    }

    private boolean isOracle(Connection connection) {
        boolean is_oracle = false;
        try {
            is_oracle = connection.getMetaData().getDatabaseProductName().toLowerCase().contains("oracle");
        } catch(Exception ex) {
            ex.getMessage();
        }
        return is_oracle;
    }

    @Schedule(hour = "1", dayOfMonth = "*", persistent = false)
    @AccessTimeout(value = 24, unit = TimeUnit.HOURS)
    public void maintainDb() {
        if (LocalDateTime.now().getHour() <= 2) {
            databaseMaintenance.maintain(getDataSource(), getUpgradePaths());
        }
    }
}
