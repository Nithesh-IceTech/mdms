package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.DBUtil;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Assert;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/11/23.
 */
public class DBTableRecord extends EntityDB {

    private static boolean EXISTS_CHECK = false;

    @Id
    @Column(name = "TABLE_NAME")
    public Field<String> tableName = new Field<>(this);

    @Column(name = "VERSION")
    public Field<Integer> version = new Field<>(this);

    public DBTableRecord() {
        super("db_table_record");
    }

    public static void createTableIfNotExists(Connection connection) {
        try {
            if (!EXISTS_CHECK) {
                EXISTS_CHECK = true;
                if (!DBUtil.checkTableIfExists(connection,"db_table_record")) {
                    DataSourceDB.executeUpdate(connection,
                            DriverFactory.getDriver().getCreateSql(DriverFactory.getDriver(),new DBTableRecord()),
                            null);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean shouldUpdate(Connection connection, EntityDB entity) {
        createTableIfNotExists(connection);
        Table table = entity.getClass().getAnnotation(Table.class);
        if (table != null) {
            if (table.version() > -1) {
                DBTableRecord record = new DBTableRecord();
                record.tableName.set(entity.getName());
                record = DataSourceDB.getFromSet(connection, record);
                return record == null || table.version() > record.version.get();
            } else if (table.version() == -2) {
                return true;
            }
        }
        return false;
    }

    public static void update(Connection connection, EntityDB entity) {
        Table table = entity.getClass().getAnnotation(Table.class);
        Assert.notNull(table,"May only update tables annotated with @Table. Entity " + entity.getClass());
        DBTableRecord record = DataSourceDB.getFromSet(connection, (DBTableRecord) new DBTableRecord().tableName.set(entity.getName()));
        if (record == null) {
            record = (DBTableRecord) new DBTableRecord().tableName.set(entity.getName());
        }
        record.version.set(table.version());
        DataSourceDB.set(connection,record);
    }

}
