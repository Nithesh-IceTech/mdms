package za.co.spsi.toolkit.db.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import za.co.spsi.toolkit.db.audit.AuditDetailEntity;
import za.co.spsi.toolkit.db.drivers.DBUtil;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.drivers.OracleDriver;
import za.co.spsi.toolkit.db.meta.Table;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class DBUtilOracleTest {

    private MdmsOracleDB mdmsOracleDB;
    private DataSource mdmsDS;

    private Connection connection;

    @Before
    public void setUp() throws Exception {
        mdmsOracleDB = new MdmsOracleDB();
        mdmsDS = mdmsOracleDB.getMdmsDS();
        DriverFactory.setDataSource(mdmsDS);
        connection = mdmsDS.getConnection();
        connection.setAutoCommit(false);
    }

    @Test
    public void metaTable() {
        new Table().load(connection, new OracleDriver(), "audit_log_detail");
        DBUtil.maintain(connection, new AuditDetailEntity(), true);
//        String tableName = "TEST_VERY_LONG_TABLE_NAME_SHRINKING_FUNCTION";
//        new Table().load(connection, new OracleDriver(), tableName);
//        DBUtil.maintain(connection, new TestEntityWithAVeryLongName(), false);

    }

    @Test
    public void generateTableNames() throws SQLException {
        String tableName = "ICE_BROKER_COMMAND_STATUS_UPDATE";
        String newTableName = DBUtil.generateNewName(connection, tableName, null);
        System.out.printf("Before: %s, After: %s", tableName, newTableName);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }



}
