package za.co.spsi.toolkit.db.drivers;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.db.meta.*;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public interface Driver {

    public enum DBType {
        POSTGRES, ORACLE, H2, MSSQL, MYSQL, NONE;
    }

    public String getColumnNameWildcardPattern();

    public String currentTimestamp();

    public int getIdentifierLength();

    public String limitSql(String sql, int rowSize);

    public String formatDate(String columnName, String asName);

    public String addTimezoneOffset();

    public String subtractTimezoneOffset();

    public String offset(String sql, Integer offset, Integer limit);

    public String translateColumnClassName(String columnClassName);

    public Class getDataType(TableColumn column);

    public String getCreateSql(Driver driver, EntityDB entity);

    public String addPrimaryKey(EntityDB entity, FieldList primaryKey);

    public String addColumn(Driver driver, EntityDB entity, Field field, boolean addNull);

    public String dropColumn(EntityDB entity, TableColumn field);

    public String updateColumn(Driver driver, EntityDB entity, Field field, boolean nullChanged);

    public boolean manualAlterTypeRequired();

    public void alterType(Driver driver, Connection connection, TableColumnList columns, EntityDB entity, Field field) throws SQLException;

    public void alterNull(Connection connection, EntityDB entity, Field field) throws SQLException;

    public String addForeignKey(EntityDB entity, Driver driver, Field field);

    public String dropForeignKey(EntityDB entity, String cName);

    public String addIndex(EntityDB entity, Index index);

    public String dropIndex(EntityDB entity, String name);

    public String getColumnSql(Driver driver, EntityDB entity, Field field, boolean addNullable);

    public boolean allowTypeAlter();

    public boolean allowNumberScaleDecrease();

    public boolean tableNamesInCaps();

    public int getMaxVarcharSize();

    public TableKeyList getPrimaryKeys(Connection connection, String owner, String tableName);

    public TableExportKeyList getForeignKeys(Connection connection, String tableName);

    public String getCascadeRule(ForeignKey.Action action);

    public String getDeferRule(ForeignKey.Deferrability deferrable);

    public TableIndexList getIndexes(Connection connection, String tableName);

    public String getClobType();

    public String getBlobType();

    public String getInterval(Integer frequency, String type);

    public String boolToNumber(Boolean val);

    public String orderBy(String sql, String columnName, Boolean desc);

    public String limitSqlAndOrderBy(String sql, Integer rowSize, String columnName, Boolean desc);

    public String getRowNum();

    public String formatColumnCase(String columnName);

    public String toChar(String columnName);

    public String aggregateList(String ename, String delimiter);

    public String addInterval(Timestamp ts, Integer frequency, String type);

    public boolean mayAlterType(TableColumn column);

    public default Connection enableProxyUser(String rUser, String oUser, Connection connection) {
        return connection;
    }

    public default void disableProxyUser(String rUser, String oUser, Connection connection) {
    }

    public String executeGrantAll(Class eClass, String oUser, String rUser) throws IllegalAccessException, InstantiationException;

    public String toDate(LocalDateTime value);

    public Boolean isOracle();

}
