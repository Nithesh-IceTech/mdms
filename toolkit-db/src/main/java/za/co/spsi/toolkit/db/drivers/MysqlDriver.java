package za.co.spsi.toolkit.db.drivers;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.meta.TableColumn;
import za.co.spsi.toolkit.db.meta.TableColumnList;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.StringUtils;
import za.co.spsi.toolkit.util.Util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.NoAction;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class MysqlDriver extends AbstractDriver {

    private int majorVersion;

    public MysqlDriver() {
    }

    public String getColumnNameWildcardPattern() {
        return "%";
    }

    @Override
    public String currentTimestamp() {
        return null;
    }

    public MysqlDriver init(Connection connection) throws SQLException {
        majorVersion = connection.getMetaData().getDriverMajorVersion();
        return this;
    }

    public String getClobType() {
        return "longtext";
    }

    public String getBlobType() {
        return "longblob";
    }

    @Override
    public Connection enableProxyUser(String rUser, String oUser, Connection connection) {
        return null;
    }

    @Override
    public void disableProxyUser(String rUser, String oUser, Connection connection) {

    }

    @Override
    public int getIdentifierLength() {
        return 30;
    }

    @Override
    public String limitSql(String sql, int rowSize) {
        return String.format("%s %s", sql, rowSize + 1);
    }

    @Override
    public String formatDate(String columnName, String asName) {
        return String.format("date_format (%s,'%Y-%m-%d %H:%i') as %s", columnName, asName);
    }

    public String offset(String sql, Integer offset, Integer limit) {
        // add row no
        if (majorVersion > 11) {
            return String.format("%s limit %s OFFSET %s", sql, limit,offset);
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
        return String.format("ALTER TABLE %s ADD PRIMARY KEY (%s)", entity.getName(),
                EntityDB.getColumnNames(primaryKey).toString(","));
    }

    @Override
    public String addColumn(Driver driver,EntityDB entity, Field field,boolean addNull) {
        return String.format("ALTER TABLE %s ADD %s %s", entity.getName(),
                EntityDB.getColumnName(field), getColumnSql(driver, entity, field, addNull));
    }

    @Override
    public String dropColumn(EntityDB entity, TableColumn field) {
        return String.format("ALTER TABLE %s drop column %s", entity.getName(), field.COLUMN_NAME.get());
    }

    @Override
    public String updateColumn(Driver driver,EntityDB entity, Field field, boolean nullChanged) {
        return String.format("alter table %s change %s %s", entity.getName(), EntityDB.getColumnName(field),
                getColumnSql(driver,entity, field, nullChanged));
    }

    @Override
    public void alterType(Driver driver, Connection connection, TableColumnList columns, EntityDB entity, Field field) throws SQLException {
        DataSourceDB.executeUpdate(connection, String.format("ALTER TABLE %s modify column %s %s", entity.getName(),
                EntityDB.getColumnName(field), getColumnSql(driver, entity, field, true)));
    }

    @Override
    public void alterNull(Connection connection, EntityDB entity, Field field) throws SQLException {

    }

    @Override
    public String dropForeignKey(EntityDB entity, String cName) {
        return String.format("ALTER TABLE %s drop constraint %s", entity.getName(), cName);
    }

    private String getColumnSqlForColumnType(Field field, String type, Column column, boolean addNullable) {
        Id id = field != null ? (Id) field.getAnnotation(Id.class) : null;
        String defValue = column == null || column.defaultValue().length() == 0 ? "" : String.format("default '%s'", column.defaultValue());
        String notNull = id == null && (column == null || !column.notNull()) ? "null" : "not null";
        defValue = id != null && id.autoIncrement() ?
                " AUTO_INCREMENT " :
                column != null && "now".equalsIgnoreCase(column.defaultValue()) ? "default CURRENT_TIMESTAMP" : defValue;
        return String.format("%s %s %s", type, defValue, addNullable ? notNull : "");
    }

    public String getColumnSqlForString(Field field, boolean addNullable) {
        Column column = (Column) field.getAnnotation(Column.class);
        int size = column == null || column.size() == -1 ? 200 : column.size();
        return getColumnSqlForColumnType(field, String.format("varchar(%d)", size), column, addNullable);
    }

    public String getColumnSqlForNumber(String type, Field field, boolean addNullable) {
        Column column = (Column) field.getAnnotation(Column.class);
        return getColumnSqlForColumnType(field, type, column, addNullable);
    }

    public String getColumnSqlForDouble(Field field, boolean addNullable) {
        return getColumnSqlForNumber("double", field, addNullable);
    }

    public String getColumnSqlForFloat(Field field, boolean addNullable) {
        return getColumnSqlForNumber("float", field, addNullable);
    }

    public String getColumnSqlForLong(Field field, boolean addNullable) {
        return getColumnSqlForNumber("bigint", field, addNullable);
    }

    public String getColumnSqlForInteger(Field field, boolean addNullable) {
        return getColumnSqlForNumber("int", field, addNullable);
    }

    public String getColumnSqlForShort(Field field, boolean addNullable) {
        return getColumnSqlForNumber("smallint", field, addNullable);
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
        return 65535;
    }

    @Override
    public String dropIndex(EntityDB entity, String name) {
        return String.format("drop index %s on %s", name,entity.getName());
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
            return "SET DEFAULT";
        } else if (action.equals(ForeignKey.Action.Restrict)) {
            return "RESTRICT";
        } else {
            throw new UnsupportedOperationException("Unknown cascade type " + action);
        }
    }

    @Override
    public String getDeferRule(ForeignKey.Deferrability deferrable) {
        return "";
    }


    @Override
    public String getColumnSql(Driver driver,EntityDB entity, Field field, boolean addNullable) {
        if (String.class.equals(field.getType()) && field.getAnnotation(Column.class) != null &&
                (((Column) field.getAnnotation(Column.class)).size() > driver.getMaxVarcharSize() ||
                        ((Column) field.getAnnotation(Column.class)).clob())) {
            return getColumnSqlForColumnType(field, "longtext", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (String.class.equals(field.getType())) {
            return getColumnSqlForString(field, addNullable);
        } else if (byte[].class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "longblob", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (Long.class.equals(field.getType()) || BigInteger.class.equals(field.getType())) {
            return getColumnSqlForLong(field, addNullable);
        } else if (Short.class.equals(field.getType())) {
            return getColumnSqlForShort(field, addNullable);
        } else if (Boolean.class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "bool", (Column) field.getAnnotation(Column.class),addNullable);
        } else if (Integer.class.equals(field.getType())) {
            return getColumnSqlForInteger(field, addNullable);
        } else if (Double.class.equals(field.getType()) || BigDecimal.class.equals(field.getType())) {
            return getColumnSqlForDouble(field, addNullable);
        } else if (Float.class.equals(field.getType())) {
            return getColumnSqlForFloat(field, addNullable);
        } else if (Timestamp.class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "timestamp", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (Date.class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "date", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (Time.class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "time", (Column) field.getAnnotation(Column.class), addNullable);
        } else if (Character.class.equals(field.getType())) {
            return getColumnSqlForColumnType(field, "char", (Column) field.getAnnotation(Column.class), addNullable);
        } else {
            throw new UnsupportedOperationException(String.format("Unsupported type %s. %s ", field.getName(), field.getType()));
        }
    }

    @Override
    public String getInterval(Integer frequency, String type) {
        return null;
    }

    @Override
    public String addInterval(Timestamp ts, Integer frequency, String type) {
        return null;
    }

    public static void main(String args[]) throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:32771:xe","icemoodev","icemoodev");
        connection.createStatement().executeQuery("select * from deploy_log").next();
    }

    @Override
    public String boolToNumber(Boolean val) {
        return null;
    }

    @Override
    public String orderBy(String sql, String columnName, Boolean desc) {
        return null;
    }

    @Override
    public String limitSqlAndOrderBy(String sql, Integer rowSize, String columnName, Boolean desc) {
        return null;
    }

    @Override
    public String getRowNum() {
        return null;
    }

    @Override
    public String formatColumnCase(String columnName) {
        return columnName;
    }

    @Override
    public String addTimezoneOffset() {
        return null;
    }

    @Override
    public String subtractTimezoneOffset() {
        return null;
    }

    @Override
    public String toChar(String columnName) {
        return null;
    }

    @Override
    public String aggregateList(String ename, String delimiter) {
        return null;
    }

    @Override
    public Boolean isOracle() {
        return false;
    }
}
