package za.co.spsi.toolkit.ee.maintenance;

import lombok.extern.java.Log;
import za.co.spsi.pjtk.util.Call;
import za.co.spsi.pjtk.util.Call1;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.maintenance.MaintenanceLog;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.Util;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jaspervdbijl on 2017/03/07.
 */
@Dependent
@Log
public class DatabaseMaintenance {

    private static Map<String, Call1<DataSource,String>> MAINTAIN_MAP = new HashMap<>();

    static {
        MAINTAIN_MAP.put("{LAST_MONTH}", ds -> DriverFactory.getDriver().toDate(LocalDateTime.now().minus(Duration.of(1, ChronoUnit.MONTHS))));
    }

    @Inject
    @ConfValue(value = "db.maintenance.enabled")
    private boolean enabled;

    @Inject
    @ConfValue(value = "db.maintenance.days")
    private Integer maintenanceDays;

    @Inject
    @ConfValue("db.maintenance.incremental.factor")
    private Integer maintenanceIncremental;


    public void maintain(DataSource dataSource, String tableName, String fieldName, LocalDateTime from, LocalDateTime to) {
        MaintenanceLog maintenanceLog = new MaintenanceLog().init(getClass().getName(), String.format("delete from %s where %s. Between %s and %s",
                tableName, fieldName, from != null ? from.toString() : "", to.toString()));
        DataSourceDB.executeInTx(dataSource, true, maintenanceLog, connection -> {
            maintenanceLog.updated.set(
                    from != null ? DataSourceDB.executeUpdate(connection, String.format("delete from %s where %s >= ? and %s < ?", tableName, fieldName, fieldName),
                            Timestamp.valueOf(from), Timestamp.valueOf(to)) :
                            DataSourceDB.executeUpdate(connection, String.format("delete from %s where %s < ?", tableName, fieldName), Timestamp.valueOf(to))
            );
            maintenanceLog.status.set(MaintenanceLog.Status.OK.name());
            DataSourceDB.set(dataSource, maintenanceLog);
        }, entity -> ((MaintenanceLog) entity).status.set(MaintenanceLog.Status.FAILED.name()));
    }

    public void maintain(DataSource dataSource, String tableName, String fieldName) {
        List<List> values = DataSourceDB.executeQuery(dataSource, new Class[]{Timestamp.class}, String.format("select min(%s) from %s", fieldName, tableName));
        LocalDateTime to = LocalDateTime.now().minusDays(maintenanceDays);
        LocalDateTime from = !values.isEmpty() && !values.get(0).isEmpty() && values.get(0).get(0) != null ? ((Timestamp) values.get(0).get(0)).toLocalDateTime() : to;
        if (from.isBefore(to)) {
            long days = from.until(to, ChronoUnit.DAYS);
            for (int i = 0; i < maintenanceIncremental - 1; i++) {
                maintain(dataSource, tableName, fieldName, from.plusDays((days / maintenanceIncremental) * i).truncatedTo(ChronoUnit.DAYS),
                        from.plusDays((days / maintenanceIncremental) * (i + 1)).truncatedTo(ChronoUnit.DAYS));
            }
        }
        maintain(dataSource, tableName, fieldName, null, to.truncatedTo(ChronoUnit.DAYS));
    }

    private FieldTimestamp getCreateField(EntityDB entity) {
        Optional<Field> field = entity.getFields().getFieldsOfInstance(FieldTimestamp.class).stream().filter(f -> !((FieldTimestamp) f).isSetUpdate()).findFirst();
        return field.isPresent() ? (FieldTimestamp) field.get() : null;
    }

    public void maintain(DataSource dataSource, Table table, Class entityClass) {
        try {
            EntityDB entity = (EntityDB) entityClass.newInstance();
            FieldTimestamp createField = getCreateField(entity);
            Assert.isTrue(createField != null || !table.deleteRecordTimeField().isEmpty(), "deleteRecordTimeField not set and no FieldTimestamp.create in entity for " + entityClass);
            maintain(dataSource, entity.getName(), table.deleteRecordTimeField().isEmpty() ? EntityDB.getColumnName(createField) : table.deleteRecordTimeField());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void maintainSql(DataSource dataSource, Table table, Class entityClass) {
        for (String sql : table.maintenanceSql()) {
            for (String key : MAINTAIN_MAP.keySet()) {
                if (sql.contains(key)) {
                    sql = sql.replace(key, MAINTAIN_MAP.get(key).call(dataSource));
                }
            }
            log.info("EXEC DB Maintenance: " + sql);
            DataSourceDB.execute(dataSource,sql);
        }
    }

    public void maintain(DataSource dataSource, String paths[]) {
        if (enabled) {
            for (String p : paths) {
                for (Class clazz : Util.getTypesAnnotatedWith(p, Table.class)) {
                    Table table = (Table) clazz.getAnnotation(Table.class);
                    if (table.deleteOldRecords()) {
                        maintain(dataSource, table, clazz);
                    } else if (table.maintenanceSql() != null && table.maintenanceSql().length > 0) {
                        maintainSql(dataSource, table, clazz);
                    }
                }
            }
        }
    }

}
