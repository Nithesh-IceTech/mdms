package za.co.spsi.toolkit.db.drivers;

import lombok.SneakyThrows;
import oracle.jdbc.driver.OracleConnection;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.meta.Table;
import za.co.spsi.toolkit.db.meta.TableColumn;
import za.co.spsi.toolkit.db.meta.TableColumnList;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.StringUtils;
import za.co.spsi.toolkit.util.Util;

import javax.management.MBeanServer;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.NoAction;

/**
 * Created by arnoc on 2020/08/01.
 */
public class PostgresDriver extends AbstractDriver {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private int majorVersion;

    public PostgresDriver() {

    }

    public PostgresDriver init(Connection connection) throws SQLException {
        majorVersion = connection.getMetaData().getDriverMajorVersion();
        return this;
    }

    @Override
    public String currentTimestamp() {
        return "current_timestamp";
    }

    public String getColumnNameWildcardPattern() {
        return null;
    }

    @Override
    public int getIdentifierLength() {
        return 30;
    }

    @Override
    public String limitSql(String sql, int rowSize) {
        return String.format("select * from (%s) subquery limit %d", sql, rowSize);
    }

    @Override
    public String formatDate(String columnName, String asName) {
        return String.format("to_char(%s, 'YYYY-MM-DD HH24:MI:SS') as %s", columnName, asName);
    }

    @Override
    public String translateColumnClassName(String columnClassName) {
        switch (columnClassName) {
            case "???.sql.TIMESTAMP":
                return "java.sql.Timestamp";
            default:
                return super.translateColumnClassName(columnClassName);
        }
    }

    public String offset(String sql, Integer offset, Integer limit) {
        if (majorVersion > 11) {
            return String.format("select * from (%s) offsetquery OFFSET %s ROWS FETCH NEXT %s ROWS ONLY", sql, offset, limit);
        } else {
            throw new UnsupportedOperationException("Unsupported db version " + majorVersion);
        }
    }

    @Override
    public String addPrimaryKey(EntityDB entity, FieldList primaryKey) {
        // alternate column name
        FieldList namedField = primaryKey.getFieldsWithAnnotationWithMethodSignatureWithFilter(Column.class, "name", new Util.Filter() {
            @Override
            public boolean filter(Object value) {
                return !StringUtils.isEmpty(value != null ? value.toString() : "");
            }
        });
        return String.format("ALTER TABLE %s ADD CONSTRAINT %s PRIMARY KEY (%s)", entity.getName(),
                Table.getPKeyConstraintName(entity, 30), EntityDB.getColumnNames(primaryKey).toString(","));
    }

    @Override
    public String addColumn(Driver driver, EntityDB entity, za.co.spsi.toolkit.entity.Field field, boolean addNull) {
        return String.format("alter table %s add %s %s", entity.getName(), EntityDB.getColumnName(field), getColumnSql(driver, entity, field, addNull));
    }

    @Override
    public String dropColumn(EntityDB entity, TableColumn field) {
        return String.format("alter table %s drop column %s", entity.getName(), field.COLUMN_NAME.get());
    }

    @Override
    public String updateColumn(Driver driver, EntityDB entity, za.co.spsi.toolkit.entity.Field field, boolean nullChanged) {
        return String.format("alter table %s alter column %s type %s", entity.getName(), EntityDB.getColumnName(field),
                getColumnSql(driver, entity, field, nullChanged));
    }

    private String getAlterFieldTmpName(TableColumnList columns, AtomicInteger index, za.co.spsi.toolkit.entity.Field field) {
        String name = EntityDB.getColumnName(field) + "_a" + index.getAndIncrement();
        return columns.getByName(name) == null ? name : getAlterFieldTmpName(columns, index, field);
    }

    // TODO - disble and re enable constraints on the table
    @Override
    public void alterType(Driver driver, Connection connection, TableColumnList columns, EntityDB entity, za.co.spsi.toolkit.entity.Field field) throws SQLException {
        String fName = EntityDB.getColumnName(field);
        String alterName = getAlterFieldTmpName(columns, new AtomicInteger(0), field);
        try {
            field.setAlternateName(alterName);
            DataSourceDB.executeUpdate(connection, addColumn(driver, entity, field, false));
            DataSourceDB.executeUpdate(connection, String.format("update %s set %s = %s", entity.getName(), alterName, fName));
            // drop and rename
            DataSourceDB.executeUpdate(connection, dropColumn(entity, columns.getByName(fName)));
            DataSourceDB.executeUpdate(connection, String.format("alter table %s alter column %s type %s", entity.getName(), alterName, fName));
            // fix null
            Column col = (Column) field.getAnnotation(Column.class);
            boolean nullable = col == null || !col.notNull();
            if (!nullable) {
                alterNull(connection, entity, field);
            }
        } finally {
            field.setAlternateName(fName);
        }
    }

    public void alterNull(Connection connection, EntityDB entity, za.co.spsi.toolkit.entity.Field field) throws SQLException {
        Column col = (Column) field.getAnnotation(Column.class);
        DataSourceDB.executeUpdate(connection, String.format("alter table %s alter column %s %s", entity.getName(), EntityDB.getColumnName(field),
                col != null && col.notNull() ? "drop NOT NULL" : ""));
    }

    @Override
    public String dropForeignKey(EntityDB entity, String cName) {
        return String.format("alter table %s drop constraint %s", entity.getName(), cName);
    }

    private String getColumnSqlForColumnType(za.co.spsi.toolkit.entity.Field field, String type, Column column, boolean addNullable) {
        Id id = field != null ? (Id) field.getAnnotation(Id.class) : null;
        String defValue = column == null || column.defaultValue().length() == 0 ? "" : String.format("default '%s'", column.defaultValue());
        String notNull = id == null && (column == null || !column.notNull()) ? "null" : "not null";
        defValue = id != null && id.autoIncrement() ?
                " generated by default on null AS IDENTITY " :
                column != null && "now".equalsIgnoreCase(column.defaultValue()) ? "default CURRENT_TIMESTAMP" : defValue;
        return String.format("%s %s %s", type, defValue, addNullable ? notNull : "");
    }

    public String getColumnSqlForString(za.co.spsi.toolkit.entity.Field field, boolean addNullable) {
        Column column = (Column) field.getAnnotation(Column.class);
        int size = column == null || column.size() == -1 ? 200 : column.size();
        return getColumnSqlForColumnType(field, String.format("VARCHAR(%d)", size), column, addNullable);
    }

    public String getColumnSqlForNumber(int size, int decimal, za.co.spsi.toolkit.entity.Field field, boolean addNullable) {
        Column column = (Column) field.getAnnotation(Column.class);
        decimal = column == null || column.decimalPlaces() == -1 ? decimal : column.decimalPlaces();
        size = column == null || column.size() == -1 ? size : column.size();
        return getColumnSqlForColumnType(field, String.format("NUMERIC(%d,%d)", size, decimal), column, addNullable);
    }

    public String getColumnSqlForDouble(za.co.spsi.toolkit.entity.Field field, boolean addNullable) {
        return getColumnSqlForNumber(20, 6, field, addNullable);
    }

    public String getColumnSqlForFloat(za.co.spsi.toolkit.entity.Field field, boolean addNullable) {
        return getColumnSqlForNumber(10, 4, field, addNullable);
    }

    public String getColumnSqlForLong(za.co.spsi.toolkit.entity.Field field, boolean addNullable) {
        return getColumnSqlForNumber(20, 0, field, addNullable);
    }

    public String getColumnSqlForInteger(za.co.spsi.toolkit.entity.Field field, boolean addNullable) {
        return getColumnSqlForNumber(10, 0, field, addNullable);
    }

    public String getColumnSqlForShort(za.co.spsi.toolkit.entity.Field field, boolean addNullable) {
        return getColumnSqlForNumber(5, 0, field, addNullable);
    }

    public boolean allowNumberScaleDecrease() {
        return false;
    }

    public boolean allowTypeAlter() {
        return false;
    }

    @Override
    public boolean tableNamesInCaps() {
        return true;
    }

    @Override
    public int getMaxVarcharSize() {
        return 4000;
    }

    @Override
    public String getCascadeRule(ForeignKey.Action action) {
        if (action.equals(ForeignKey.Action.Cascade)) {
            return "CASCADE";
        } else if (action.equals(NoAction)) {
            return "NO ACTION";
        } else if (action.equals(ForeignKey.Action.SetNull)) {
            return "SET NULL";
        } else if (action.equals(ForeignKey.Action.SetDefault)) {
            throw new UnsupportedOperationException("Unsupported cascade type " + action);
        } else {
            throw new UnsupportedOperationException("Unknown cascade type " + action);
        }
    }

    @Override
    public String getDeferRule(ForeignKey.Deferrability deferrable) {
        switch (deferrable) {
            case InitiallyDeferred:
                return "INITIALLY DEFERRED";
            case InitiallyImmediate:
                return "INITIALLY IMMEDIATE";
            case NotDeferrable:
                return "";
        }
        throw new UnsupportedOperationException("Not implemented " + deferrable);
    }

    @SneakyThrows
    public String getColumnSql(Driver driver, EntityDB entity, za.co.spsi.toolkit.entity.Field field, boolean addNullable) {
        if (String.class.equals(field.getType()) && field.getAnnotation(Column.class) != null &&
                (((Column) field.getAnnotation(Column.class)).size() > driver.getMaxVarcharSize() ||
                        ((Column) field.getAnnotation(Column.class)).clob())) {
            return getColumnSqlForColumnType(field, "text", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (String.class.equals(field.getType())) {
            return getColumnSqlForString(field, addNullable);
        } else if (byte[].class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "bytea", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (Long.class.equals(field.getType()) || BigInteger.class.equals(field.getType())) {
            return getColumnSqlForLong(field, addNullable);
        } else if (Short.class.equals(field.getType())) {
            return getColumnSqlForShort(field, addNullable);
        } else if (Boolean.class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "boolean", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (Integer.class.equals(field.getType())) {
            return getColumnSqlForInteger(field, addNullable);
        } else if (Double.class.equals(field.getType()) || BigDecimal.class.equals(field.getType())) {
            return getColumnSqlForDouble(field, addNullable);
        } else if (Float.class.equals(field.getType())) {
            return getColumnSqlForFloat(field, addNullable);
        } else if (Date.class.isAssignableFrom(field.getType()) || java.util.Date.class.isAssignableFrom(field.getType()) || Temporal.class.isAssignableFrom(field.getType())) {
            return getColumnSqlForColumnType(field, "timestamp", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (Character.class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "char(1)", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (UUID.class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "uuid", (Column) field.getAnnotation(Column.class), addNullable);
        } else {
            throw new RuntimeException(String.format("Unsupported type %s. %s ", field.getName(), field.getType()));
        }
    }

    public boolean manualAlterTypeRequired() {
        return true;
    }

    @Override
    public String toDate(LocalDateTime value) {
        return String.format("\'%s\'", value.format(DATE_TIME_FORMATTER));
    }

    private DataSource findDataSourceByName(String dsName) {
        Set<ObjectInstance> datasources = null;
        DataSource dataSource = null;
        try {
            Context ctx = new InitialContext();
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            datasources = server.queryMBeans(new ObjectName("jboss.as:subsystem=datasources,data-source=*"), null);
            for (ObjectInstance ds : datasources) {
                ObjectName mbeanName = ds.getObjectName();
                String bindName = (String) server.getAttribute(mbeanName, "jndiName");
                if (bindName.contains(dsName.toLowerCase())) {
                    dataSource = (DataSource) ctx.lookup(bindName);
                    break;
                }
            }
        } catch (Exception ex) {
            TAG.severe(ex.getMessage());
        }

        return dataSource;
    }

    @SneakyThrows
    @Override
    public void disableProxyUser(String rUser, String oUser, Connection connection) {
        if (!StringUtils.isEmpty(rUser)) {
            connection.commit();
            ((OracleConnection) connection).close(OracleConnection.PROXY_SESSION);
        }
    }

    @Override
    public String getInterval(Integer frequency, String type) {
        return String.format("interval '%d %ss'", frequency, type);
    }

    @Override
    public String addInterval(Timestamp ts, Integer frequency, String type) {
        if (type.contains("second") || type.contains("minute") || type.contains("hour"))
            type = type + "s";
        return String.format("'%s' + interval '%d %s'", ts.toString(), frequency, type);
    }

    @Override
    public String boolToNumber(Boolean val) {
        return String.format("'%d'", val ? 1 : 0);
    }

    @Override
    public String orderBy(String sql, String columnName, Boolean desc) {
        return String.format("%s order by %s %s", sql, columnName, desc ? "desc" : "asc");
    }

    @Override
    public String limitSqlAndOrderBy(String sql, Integer rowSize, String columnName, Boolean desc) {
        sql = orderBy(sql, columnName, desc);
        sql = limitSql(sql, rowSize);
        return sql;
    }

    @Override
    public String getRowNum() {
        return String.format("row_number() over()");
    }

    @Override
    public String formatColumnCase(String columnName) {
        return columnName.toLowerCase();
    }

    @Override
    public String addTimezoneOffset() {
        return String.format(" + interval '%d hours' ", TimeZone.getDefault().getRawOffset() / 1000 / 60 / 60);
    }

    @Override
    public String subtractTimezoneOffset() {
        return String.format(" - interval '%d hours' ", TimeZone.getDefault().getRawOffset() / 1000 / 60 / 60);
    }

    @Override
    public String toChar(String columnName) {
        return String.format("cast(%s as text)", columnName);
    }

    @Override
    public String aggregateList(String ename, String delimiter) {
        return String.format("string_agg(%s, '%s' order by %s)", ename, delimiter, ename);
    }

    @Override
    public Boolean isOracle() {
        return false;
    }
}
