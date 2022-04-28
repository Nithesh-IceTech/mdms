package za.co.spsi.toolkit.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import za.co.spsi.toolkit.db.audit.AuditDetailEntity;
import za.co.spsi.toolkit.db.drivers.DBUtil;
import za.co.spsi.toolkit.db.drivers.MysqlDriver;
import za.co.spsi.toolkit.db.meta.Table;
import za.co.spsi.toolkit.util.StringList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class DBUtilMysqlTest {

    private Connection connection;

    @Before
    public void setUp() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:32768/tolling", "root", "cessna210");
    }

    @Test
    public void test() {
        try {
            System.out.println(new StringList(connection.getMetaData().getExportedKeys(null, null, "audit_log_detail".toUpperCase()), "*"));
            System.out.println(new StringList(connection.getMetaData().getImportedKeys(null, null, "audit_log_detail"), "*"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void metaTable() {
        new Table().load(connection, new MysqlDriver(), "audit_log_detail");
        DBUtil.maintain(connection, new AuditDetailEntity(), true);
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }
}
