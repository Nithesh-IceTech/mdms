package za.co.spsi.toolkit.db.upgrade;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.DBUtil;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.Util;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/11/28.
 */
public class UpgradeHelper implements AutoCloseable {

    public static final Logger TAG = Logger.getLogger(UpgradeHelper.class.getName());

    private String connectionPath, upgradeUserRightsSql;
    private boolean deleteFile, upgraded = false, lockUser;

    public UpgradeHelper(String connectionPath, String upgradeUserRightsSql, boolean deleteFile, boolean lockUser) {
        this.connectionPath = connectionPath;
        this.upgradeUserRightsSql = upgradeUserRightsSql;
        this.deleteFile = deleteFile;
        this.lockUser = lockUser;
    }

    public static String shortenName(String name) {
        String shortName = "";
        for (String value : name.split("_")) {
            shortName += value.substring(0,1).toUpperCase();
        }
        return shortName;
    }

    static class PropertyStruct extends Entity {
        public Field<String> driver = new Field<>(this);
        public Field<String> classPath = new Field<>(this);
        public Field<String> url = new Field<>(this);
        public Field<String> username = new Field<>(this);
        public Field<String> password = new Field<>(this);
        public Field<String> ext1 = new Field<>(this);
        public Field<String> ext2 = new Field<>(this);

        public PropertyStruct() {
        }

        public PropertyStruct init(String connectionPath) {
            StringList properties = new StringList().readFile(new File(connectionPath));
            hackForDevEnv(properties);
            for (int i = 0; i < properties.size(); i++) {
                getFields().get(i).set(properties.get(i));
            }
            Assert.isTrue(properties.size() >= 6, "Incorrect properties file. Needs 6 lines");
            return this;
        }

        private void hackForDevEnv(List<String> properties) {
            for (int i = 0; i < properties.size(); i++) {
                if (properties.get(i).startsWith("username: ")) {
                    properties.remove(i--);
                }
            }
            properties.set(0, properties.get(0).replace("driver: ", ""));
            properties.set(2, properties.get(2).replace("url: ", ""));

        }

    }

    public boolean isUpgradePropertyAvailable() {
        return new File(connectionPath).exists();
    }


    public void upgradeTables(Connection connection, String upgradePaths[], boolean devMode,String owner) {
        TAG.info("Upgrade Tables. Paths " + new StringList(upgradePaths));
        try {
            PropertyStruct p = getPropertyStruct();
            for (String path : upgradePaths) {
                for (Class type : Util.getTypesAnnotatedWith(path, Table.class)) {
                    upgraded = DBUtil.maintain(connection, (EntityDB) type.newInstance(), devMode) | upgraded;
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setUserRights(Connection connection, String sql) {
        TAG.info("Set user rights");
        String[] statements = sql.split("_nl_");
        for (String sqlString : statements) {
            DataSourceDB.execute(connection, sqlString);
        }
    }

    public PropertyStruct getPropertyStruct() {
        return new PropertyStruct().init(connectionPath);
    }

    public Connection getUpgradeConnection() {
        try {
            PropertyStruct p = getPropertyStruct();
            Class.forName(p.driver.get());
            return DriverManager.getConnection(p.url.get(), p.username.get(), p.password.get());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void upgradeTables(String upgradePaths[],boolean devMode) {
        try {
            PropertyStruct p = getPropertyStruct();
            try (Connection connection = getUpgradeConnection()) {
                upgradeTables(connection, upgradePaths, devMode,p.username.get());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void upgradeTables(DataSource dataSource, String upgradePaths[],boolean devMode) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                upgradeTables(connection, upgradePaths, devMode,getPropertyStruct().username.get());
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateUserRights(String sql) {
        try {
            StringList properties = new StringList().readFile(new File(connectionPath));
            Assert.isTrue(properties.size() >= 6, "Incorrect properties file. Needs 6 lines");

            try (Connection connection = getUpgradeConnection()) {
                setUserRights(connection, sql.replace("_ROLE_", properties.get(5))
                        .replace("_OWNER_", properties.get(3)).replace("_USERNAME_", properties.get(6)));

            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void lockUser() {
        try {
            PropertyStruct p = new PropertyStruct().init(connectionPath);
            try (Connection connection = getUpgradeConnection()) {
                try (Statement smt = connection.createStatement()) {
                    smt.execute(String.format("ALTER USER %s IDENTIFIED BY A%s", p.username.get(),
                            Integer.toHexString((int) Math.round(Math.random() * 10000))));
                    smt.execute(String.format("ALTER USER %s ACCOUNT LOCK", p.username.get()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        if (isUpgradePropertyAvailable()) {
            try {
                if (upgraded && upgradeUserRightsSql != null ) {
                    updateUserRights(upgradeUserRightsSql);
                }
                if (lockUser) {
                    lockUser();
                }
            } finally {
                if (deleteFile) {
                    new File(connectionPath).delete();
                }
            }
        }
    }
}
