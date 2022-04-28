package za.co.spsi.toolkit.crud.service;

import za.co.spsi.toolkit.crud.db.DefaultMasterNodeConfig;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.db.DefaultConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import java.util.logging.Logger;

//@Startup
//@Singleton
public class MasterNodeService {
    //create table master_server(server_name varchar(20), last_update_guid varchar(50), last_update date);
    //insert into master_server values('NONE', 'fsfgsdfg', TO_DATE('01-JAN-2000', 'DD-MM-YYYY'))

    private static final Logger LOG = Logger.getLogger(MasterNodeService.class.getName());
    private static String servName;

    @Inject
    @ConfValue("serverNodeName")
    private String serverName;

    @Inject
    @ConfValue("multiNodeEnabled")
    private boolean multiNodeEnabled = false;

//    @Resource(mappedName = "java:/jdbc/IceFSS")
//    private DataSource dataSource;

    @Inject
    private DefaultMasterNodeConfig defaultConfig;

    private static Boolean isMaster = null;

    private static Boolean multiNodeStatic = null;

    @Schedule(hour = "*", minute = "*/5")
    public void poll() {
        pollMasterNodeStatus();
    }

    public synchronized void pollMasterNodeStatus() {
        try {
            LOG.info("Multi Node Mode Enabled: " + multiNodeEnabled);
            multiNodeStatic = multiNodeEnabled;
            if (!multiNodeEnabled) {
                isMaster = null;
                return;
            }
            MasterInstance inst = getCurrentMasterInstance();
            if (isMasterInSavePeriod(inst)) {
                //i am master and I updated my status less than 0.5h ago
                //keep my status up to date
                updateCurrentMaster(inst);
                isMaster = true;
            } else if (isMasterInOrangePeriod(inst)) {
                //compete to maintain position as master
                updateCurrentMaster(inst);
                inst = getCurrentMasterInstance();
                isMaster = inst.getServerName().equals(this.serverName);
            } else if (masterNotActive(inst)) {
                //current master was not active for more than an hour
                //bid with potential other to-be masters for the position
                updateCurrentMaster(inst);
                inst = getCurrentMasterInstance();
                isMaster = inst.getServerName().equals(this.serverName);
            }
        } catch (Exception e) {
            isMaster = false;
//            throw e;
        }

        finally {
            if (isMaster == null)
                isMaster = false;
            LOG.info("Status for Server Node " + serverName + ": " + isMaster);
        }
    }

    private void updateCurrentMaster(MasterInstance inst) {
        try (Connection connection = defaultConfig.getDataSource().getConnection()) {
            try {
                DataSourceDB.executeUpdate(connection,
                        "update master_server set server_name = ?, last_update = ? , last_update_guid = ? where last_update_guid = ? ",
                        this.serverName,
                        new Date(System.currentTimeMillis()),
                        java.util.UUID.randomUUID().toString(),
                        inst.getLastUpdateGUID()
                );
            } catch (Throwable ex) {
                //connection.rollback();
                throw ex;
            }
        } catch (Throwable ex) {

            throw new RuntimeException(ex);
        }


    }

    public static boolean getTest() {
        return false;
    }

    public static boolean getMaster() {
        return true;
        ///int timeOutCount = 5;

        /*while (multiNodeStatic == null) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeOutCount--;
            if (timeOutCount == 0)
                return false;
        }

        if (!multiNodeStatic)
            return true;

        while (isMaster == null) {
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timeOutCount--;
            if (timeOutCount == 0)
                return false;
        }
        return isMaster;*/
    }

    private boolean isMasterInSavePeriod(MasterInstance inst) {
        return inst.getServerName().equals(this.serverName) && System.currentTimeMillis() - inst.getLastUpdateTime() < 1800000;
    }

    private boolean isMasterInOrangePeriod(MasterInstance inst) {
        return inst.getServerName().equals(this.serverName) && System.currentTimeMillis() - inst.getLastUpdateTime() > 1800000;
    }

    private boolean masterNotActive(MasterInstance inst) {
        return System.currentTimeMillis() - inst.getLastUpdateTime() > 3600000;
    }


    public MasterInstance getCurrentMasterInstance() {
        List<List> masterRow;
        masterRow = DataSourceDB.executeQuery(defaultConfig.getDataSource(), new Class[]{String.class, String.class, Timestamp.class},
                "select * from master_server");

        MasterInstance inst = new MasterInstance();
        List list = masterRow.get(0);
        inst.setServerName((String) list.get(0));
        inst.setLastUpdateGUID((String) list.get(1));
        inst.setLastUpdateTime(((Timestamp) list.get(2)).getTime());
        return inst;

    }

    @PostConstruct
    public void doMasterCheckStart() {
        pollMasterNodeStatus();
    }


    private static class MasterInstance {
        private String serverName;
        private String lastUpdateGUID;
        private long lastUpdateTime;

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }

        public String getLastUpdateGUID() {
            return lastUpdateGUID;
        }

        public void setLastUpdateGUID(String lastUpdateGUID) {
            this.lastUpdateGUID = lastUpdateGUID;
        }

        public long getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(long lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }


    }


}
