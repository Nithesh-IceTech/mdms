package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.entity.Field;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class TableColumn extends EntityDB {

    public static Logger TAG = Logger.getLogger(TableColumn.class.getName());

    //    @Column(clob = true)
    public Field<String> COLUMN_DEF = new Field<>(this);

//    public Field<String> TABLE_CAT = new Field<>(this);
//    public Field<String> TABLE_SCHEM = new Field<>(this);
//    public Field<String> TABLE_NAME = new Field<>(this);
    public Field<String> COLUMN_NAME = new Field<>(this);
    public Field<Integer> DATA_TYPE = new Field<>(this);
    public Field<String> TYPE_NAME = new Field<>(this);
    public Field<Integer> COLUMN_SIZE = new Field<>(this);
//    public Field<String> BUFFER_LENGTH = new Field<>(this);
    public Field<Integer> DECIMAL_DIGITS = new Field<>(this);
//    public Field<String> NUM_PREC_RADIX = new Field<>(this);
    public Field<Integer> NULLABLE = new Field<>(this);
//    public Field<String> REMARKS = new Field<>(this);

//    public Field<String> SQL_DATA_TYPE = new Field<>(this);
//    public Field<String> SQL_DATETIME_SUB = new Field<>(this);
//    public Field<String> CHAR_OCTET_LENGTH = new Field<>(this);
//    public Field<String> ORDINAL_POSITION = new Field<>(this);
//    public Field<String> IS_NULLABLE = new Field<>(this);
//    public Field<String> SCOPE_CATALOG = new Field<>(this);
//    public Field<String> SCOPE_SCHEMA = new Field<>(this);
//    public Field<String> SCOPE_TABLE = new Field<>(this);
//    public Field<String> SOURCE_DATA_TYPE = new Field<>(this);
//    public Field<String> IS_AUTOINCREMENT = new Field<>(this);


    private boolean primaryKey = false;


    public TableColumn() {
        super("");
    }

    public boolean match(Column column) {
        boolean match = COLUMN_DEF.get() == null || COLUMN_DEF.get().equals(column.defaultValue());
        match = match || column.size() == -1 || COLUMN_SIZE.get() != null && COLUMN_SIZE.get().equals(column.size());
        return match;
    }

    private boolean isLongType(Class type, boolean strict) {
        return Long.class.equals(type) || BigInteger.class.equals(type) || !strict && Integer.class.equals(type);
    }

    private boolean matchLongType(Class fieldType, Class dbType, boolean strict) {
        return isLongType(fieldType, strict) && isLongType(dbType, strict);
    }

    private boolean isIntType(Class type, boolean strict) {
        return Integer.class.equals(type) || Short.class.equals(type);
    }

    private boolean matchIntType(Class fieldType, Class dbType, boolean allowDecrease, boolean strict) {
        return isIntType(fieldType, strict) && isIntType(dbType, strict) ||
                !allowDecrease && isIntType(fieldType, strict) && isLongType(dbType, strict);
    }

    private boolean isDoubleType(Class type, boolean strict) {
        return Double.class.equals(type) || BigDecimal.class.equals(type) || !strict && Float.class.equals(type);
    }

    private boolean matchDoubleType(Class type1, Class type2, boolean strict) {
        return isDoubleType(type1, strict) && isDoubleType(type2, strict);
    }

    private boolean matchTimestampType(Class fieldType, Class dbType) {
        return Timestamp.class.equals(dbType) && (Timestamp.class.equals(fieldType) ||
                Date.class.equals(fieldType) || java.util.Date.class.equals(fieldType) || Time.class.equals(fieldType));
    }

    private boolean isBooleanType(Class type) {
        return Boolean.class.equals(type) || Short.class.equals(type);
    }

    private boolean matchBooleanType(Class type1, Class type2) {
        return isBooleanType(type1) && isBooleanType(type2);
    }

    public boolean isClob(Driver driver) {
        return driver.getClobType().equals(TYPE_NAME.get());
    }

    public boolean isFieldClob(Driver driver, Column column) {
        return column.size() > driver.getMaxVarcharSize();
    }

    /**
     * db types are not allowed to decrease in scale
     *
     * @param fieldType
     * @param dbType
     * @param strict
     * @return
     */
    public boolean typeMatch(Driver driver, Column column, Class fieldType, Class dbType, boolean strict) {
        // check if the type match
        return
                (column != null && (isClob(driver) || isFieldClob(driver, column)))
                        ? isClob(driver) == isFieldClob(driver, column)
                        :
                        fieldType.equals(dbType) || matchLongType(fieldType, dbType, strict) ||
                                matchIntType(fieldType, dbType, driver.allowNumberScaleDecrease(), strict) ||
                                matchTimestampType(fieldType, dbType) ||
                                matchDoubleType(fieldType, dbType, strict) || matchBooleanType(fieldType, dbType);
    }

    public boolean lenDecrease(Field field, Column column) {
        return column != null && column.size() > -1 && (column.size() < COLUMN_SIZE.get() ||
                column.decimalPlaces() != -1 && (DECIMAL_DIGITS.get() == null || column.decimalPlaces() < DECIMAL_DIGITS.get()));
    }

    public boolean lenMatch(Driver driver, Field field, Column column, boolean strict) {
        return !strict || column == null || column.size() == -1 ||
                column.size() == COLUMN_SIZE.get() &&
                        (column.decimalPlaces() == -1 || (DECIMAL_DIGITS.getNonNull() == column.decimalPlaces())) ||
                column.size() > driver.getMaxVarcharSize() && driver.getClobType().equals(TYPE_NAME.get());
    }

    private boolean nullable(Field field, Column column) {
        return field.getAnnotation(Id.class) == null && (column == null || !column.notNull());
    }

    public boolean nullMatch(Field field, Column column) {
        return nullable(field, column) != (NULLABLE.get() == 0);
    }

    public boolean match(Driver driver, Field field, boolean strictType) {
        Column column = (Column) field.getAnnotation(Column.class);
        boolean match = typeMatch(driver, column, field.getType(), driver.getDataType(this), strictType) &&
                lenMatch(driver, field, column, strictType) && nullMatch(field, column);
        if (!match) {
            TAG.info(String.format("Field mismatch. %s, ", COLUMN_NAME.get()));
        }
        return match;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
