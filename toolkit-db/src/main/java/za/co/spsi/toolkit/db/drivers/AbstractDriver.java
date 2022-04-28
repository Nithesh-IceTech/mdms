package za.co.spsi.toolkit.db.drivers;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.db.meta.*;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.NoAction;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public abstract class AbstractDriver implements Driver {

    public static final Logger TAG = Logger.getLogger(AbstractDriver.class.getName());

    public boolean isCaseInsensitive() {
        return false;
    }

    public String translateColumnClassName(String columnClassName) {
        return columnClassName;
    }

    public Class getDataType(TableColumn column) {
        switch (column.TYPE_NAME.get().toUpperCase()) {
            case "CHAR":
            case "BPCHAR":
                return column.COLUMN_SIZE.get() == 1 ? Character.class : String.class;
            case "VARCHAR2":
            case "VARCHAR":
            case "CLOB":
            case "LONGTEXT":
            case "TEXT":
            case "MEDIUMTEXT":
            case "UUID":
                return String.class;
            case "BYTEA":
            case "BLOB":
            case "LONGBLOB":
            case "MEDIUMBLOB":
                //case "mediumblob":
                return byte[].class;
            case "TIMESTAMP(6)":
            case "TIMESTAMP(8)":
            case "TIMESTAMP(9)":
            case "TIMESTAMP":
            case "TIMESTAMP WITHOUT TIME ZONE":
                return Timestamp.class;
            case "DATE":
                return Date.class;
            case "TIME":
                return Time.class;
            case "SMALLINT":
            case "INT":
            case "INT4":
            case "INTEGER":
            case "SERIAL":
                return Integer.class;
            case "BIGINT":
            case "BIGSERIAL":
                return Long.class;
            case "DOUBLE":
            case "FLOAT":
            case "FLOAT4":
                return Double.class;
            case "BIT":
                return Boolean.class;
            case "BOOL":
                return Boolean.class;
            case "NUMERIC":
            case "NUMBER": {
                if (column.DECIMAL_DIGITS.get() != null && column.DECIMAL_DIGITS.get() == 0) {
                    int size = column.COLUMN_SIZE.get();
                    return size > 20 ? BigInteger.class : size > 10 ? Long.class : size > 5 ? Integer.class : Short.class;
                } else {
                    int size = column.COLUMN_SIZE.get() + column.DECIMAL_DIGITS.get();
                    return size > 20 ? BigDecimal.class : size > 10 ? Double.class : Float.class;
                }
            }
            default:
                throw new UnsupportedOperationException("Unmapped data type " + column);
        }
    }

    public boolean mayAlterType(TableColumn column) {
        return !(column.TYPE_NAME.get().equalsIgnoreCase(getClobType()) || column.TYPE_NAME.get().equalsIgnoreCase(getBlobType()));
    }

    public String getClobType() {
        return "clob";
    }

    public String getBlobType() {
        return "blob";
    }


    public String getCreateSql(Driver driver, EntityDB entity) {
        return String.format("create table %s (%s)", entity.getName(), getFieldSql(driver, entity, entity.getFields(), true).toString(", "));
    }

    private StringList getFieldSql(Driver driver, EntityDB entity, FieldList fields, boolean addNullable) {
        StringList sl = new StringList();
        for (Field field : fields) {
            sl.add(String.format("%s %s", EntityDB.getColumnName(field), getColumnSql(driver, entity, field, addNullable)));
        }
        return sl;
    }

    @Override
    public String addIndex(EntityDB entity, Index index) {
        return String.format("CREATE %s INDEX %s ON %s (%s)", index.isUnique() ? "unique" : "", index.getName(), entity.getName(),
                EntityDB.getColumnNames(index.getFields()).toString(","));
    }

    @Override
    public String dropIndex(EntityDB entity, String name) {
        return String.format("drop index %s", name);
    }

    @Override
    public TableKeyList getPrimaryKeys(Connection connection, String owner, String tableName) {
        try {
            DatabaseMetaData dm = connection.getMetaData();
            try (ResultSet rs = dm.getExportedKeys("", owner, tableName)) {
                try (DataSourceDB<TableKey> dataSourceDB = new DataSourceDB<>(new TableKey(), rs, true)){
                    return new TableKeyList(dataSourceDB.getAllAsList());
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public TableIndexList getIndexes(Connection connection, String tableName) {
        try {
            DatabaseMetaData dm = connection.getMetaData();
    
            try (ResultSet rs =
                     dm.getIndexInfo(
                        connection.getCatalog(), connection.getSchema(),
                        tableName.toUpperCase(), false, true)){
    
                return new TableIndexList(DataSourceDB.getAllAsList(TableIndex.class, rs)).removeNull().group();
            }
            
        } catch (SQLException sqle) {
            TAG.severe("TABLE error : " + tableName);
            throw new RuntimeException(sqle);
        }
    }

    @Override
    public TableExportKeyList getForeignKeys(Connection connection, String tableName) {
        try {
            DatabaseMetaData dm = connection.getMetaData();
            return new TableExportKeyList(DataSourceDB.getAllAsList(TableExportKey.class, dm.getImportedKeys(connection.getCatalog(),
                    connection.getSchema(), tableName)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String addForeignKey(EntityDB entity, Driver driver, Field field) {
        try {
            ForeignKey key = (ForeignKey) field.getAnnotation(ForeignKey.class);
            EntityDB parent = (EntityDB) key.table().newInstance();
            return String.format("ALTER TABLE %s add constraint %s foreign key (%s) references %s (%s) %s ",
                    entity.getName(), TableExportKey.getFKeyConstraintName(entity, field, key, driver),
                    EntityDB.getColumnName(field), parent.getName(), TableExportKey.getFKeyName(parent, key, driver),
                    (key.onDeleteAction() != NoAction ? ("on delete " + getCascadeRule(key.onDeleteAction())) : " ")
                            + " " + getDeferRule(key.deferrable()));
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean manualAlterTypeRequired() {
        return false;
    }

    @Override
    public String executeGrantAll(Class eClass, String oUser, String rUser) throws IllegalAccessException, InstantiationException {
        EntityDB entityDB = (EntityDB) eClass.newInstance();
        return String.format("GRANT UPDATE,SELECT,INSERT,DELETE ON %s.%s TO %s", oUser, entityDB.getName(), rUser);
    }

    @Override
    public String toDate(LocalDateTime value) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public static String getTableName(Class eType) {
        Table table = (Table) eType.getAnnotation(Table.class);
        return table != null && !StringUtils.isEmpty(table.name()) ? table.name().toUpperCase() : eType.getSimpleName().toUpperCase();
    }

    public static String getColumnName(java.lang.reflect.Field field) {
        Column column = field.getAnnotation(Column.class);
        return column != null && !StringUtils.isEmpty(column.name()) ? column.name() : field.getName();
    }

    public static List<String> getColumnNames(List<java.lang.reflect.Field> fields) {
        return fields.stream().map(f -> getColumnName(f)).collect(Collectors.toList());
    }

    public static String getPKeyConstraintName(Class eClass, int length) {
        String tableName = getTableName(eClass);
        return (tableName.length() > length ? tableName.substring(0, length) : tableName) + "_PK";
    }

}