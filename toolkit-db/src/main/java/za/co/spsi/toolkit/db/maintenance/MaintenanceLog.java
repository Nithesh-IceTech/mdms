package za.co.spsi.toolkit.db.maintenance;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdbijl on 2017/03/02.
 */
@Table(version = 2,deleteOldRecords = true)
public class MaintenanceLog extends EntityDB {

    public static final Logger TAG = Logger.getLogger(MaintenanceLog.class.getName());

    @Id(uuid = true)
    @Column(name = "ID")
    public Field<String> id = new Field<>(this);

    @Column(size = 220)
    public Field<String> name = new Field<>(this);

    @Column(size = 8000,name = "sql_value")
    public Field<String> sqlValue = new Field<>(this);

    public FieldError error = new FieldError(this);

    public Field<Integer> updated = new Field<>(this);

    public Field<String> status = new Field<>(this);

    @Column(name = "start_time")
    public FieldTimestamp startTime = new FieldTimestamp(this);

    @Column(name = "completed_time")
    public FieldTimestamp completedTime = new FieldTimestamp(this).onUpdate();


    public enum Status {
        STARTED,OK,FAILED
    }

    public MaintenanceLog init(String name,String sql) {
        this.status.set(Status.STARTED.name());
        this.name.set(name);
        this.sqlValue.set(sql);
        return this;
    }


    public MaintenanceLog() {
        super("MAINTENANCE_LOG");
    }

    public static MaintenanceLog execute(final DataSource dataSource, final String name, final String sql) {
        TAG.info("Executing db maintenance " + name);
        return DataSourceDB.executeResultInTx(dataSource, new DataSourceDB.Callback<MaintenanceLog>() {
            @Override
            public MaintenanceLog run(Connection connection) throws Exception {
                MaintenanceLog maintenanceLog = new MaintenanceLog();
                maintenanceLog.name.set(name);
                maintenanceLog.sqlValue.set(sql);
                try {
                    try (Statement statement = connection.createStatement()) {
                        maintenanceLog.updated.set(statement.executeUpdate(sql));
                        maintenanceLog.status.set(Status.OK.name());
                        DataSourceDB.set(dataSource,maintenanceLog);
                        TAG.info("Executing db maintenance " + name + " completed");
                    }
                } catch (Exception ex) {
                    maintenanceLog.status.set(Status.FAILED.name());
                    maintenanceLog.error.set(Util.getExceptionAsString(ex,4096));
                    DataSourceDB.set(dataSource,maintenanceLog);
                    TAG.log(Level.WARNING,"Executing db maintenance " + name + " Failed",ex);
                }
                return maintenanceLog;
            }
        });
    }
}
