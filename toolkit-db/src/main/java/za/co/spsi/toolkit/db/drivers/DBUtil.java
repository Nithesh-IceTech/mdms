package za.co.spsi.toolkit.db.drivers;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.db.fields.IndexList;
import za.co.spsi.toolkit.db.meta.*;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/11/23.
 */
public class DBUtil {

    public static final Logger TAG = Logger.getLogger(DBUtil.class.getName());

    private static List<Class> maintained = new ArrayList<>();

    public static boolean checkTableIfExists(Connection connection, String tableName) {
        try {
            try (Statement smt = connection.createStatement()) {
                try (ResultSet rs = smt.executeQuery("select * from " + tableName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public static void print(ResultSet rs) {
        try {
            while (rs.next()) {
                StringList list = new StringList();
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                    list.add(rs.getMetaData().getColumnName(i + 1) + ": " + rs.getString(i + 1));
                }
                System.out.println(list.toString(", "));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createTable(Connection connection, EntityDB entityDB, boolean devMode) throws SQLException {
        Driver driver = DriverFactory.getDriver();
        za.co.spsi.toolkit.db.ano.Table anoTable = entityDB.getClass().getAnnotation(za.co.spsi.toolkit.db.ano.Table.class);

        entityDB.setName(DBUtil.generateNewName(connection, entityDB.getName(),null));
        DataSourceDB.executeUpdate(connection, driver.getCreateSql(driver, entityDB));

        // add primary keys
        if (!entityDB.getId().isEmpty()) {
            DataSourceDB.executeUpdate(connection, driver.addPrimaryKey(entityDB, entityDB.getId()));
        }
        // add foreign keys
        if (anoTable.maintainFK()) {
            TableExportKeyList keys = new TableExportKeyList(entityDB).load(driver, connection);
            for (Field key : keys.toAdd(driver)) {
                // ensure that the fkey table has already been processed
                DBUtil.maintain(connection, ((ForeignKey) key.getAnnotation(ForeignKey.class)).table(), devMode);
                DataSourceDB.executeUpdate(connection, driver.addForeignKey(entityDB, driver, key));
            }
        }
        // add indexes
        if (anoTable.maintainIndex()) {
            TableIndexList indexes = driver.getIndexes(connection, entityDB.getName()).removeIdIndex(entityDB);
            for (Index index : indexes.toAdd(entityDB)) {
                DataSourceDB.executeUpdate(connection, driver.addIndex(entityDB, index));
            }
        }
        // load defaults
        entityDB.onTableCreate(connection);
    }

    private static boolean updateTable(Connection connection, EntityDB entityDB, boolean devMode) throws SQLException {
        
        boolean updated = false;
        Driver driver = DriverFactory.getHelper(connection);
        za.co.spsi.toolkit.db.meta.Table table =
            new za.co.spsi.toolkit.db.meta.Table().load(connection, driver, entityDB.getName());
        za.co.spsi.toolkit.db.ano.Table anoTable =
            entityDB.getClass().getAnnotation(za.co.spsi.toolkit.db.ano.Table.class);

        // manage fields
        // add fields
        for (Field field : table.toAdd(entityDB)) {
            DataSourceDB.executeUpdate(connection, driver.addColumn(driver, entityDB, field, true));
            updated = true;
        }
        // drop fields
        for (TableColumn field : table.toRemove(entityDB)) {
            DataSourceDB.executeUpdate(connection, driver.dropColumn(entityDB, field));
            updated = true;
        }
        // update fields
        if (anoTable.maintainStrict()) {
            for (Field field : table.toUpdate(driver, entityDB, anoTable)) {
                TableColumn col = table.getColumns().getByName(EntityDB.getColumnName(field));
                Column column = (Column) field.getAnnotation(Column.class);
                boolean typeMatch =
                    col.typeMatch(driver, column,field.getType(), driver.getDataType(col), false) ||
                        col.lenDecrease(field, (Column) field.getAnnotation(Column.class));
                if (!typeMatch) {
                    // first alter the type
                    driver.alterType(driver, connection, table.getColumns(), entityDB, field);
                } else {
                    if (!col.lenMatch(driver, field, column, true)) {
                        if (driver.mayAlterType(col)) {
                            DataSourceDB.executeUpdate(connection, driver.updateColumn(driver, entityDB, field, false));
                        } else {
                            TAG.warning(
                                "May not alter column " + field.getColumnName() +
                                    " of type " + col.DATA_TYPE.get());
                        }
                    }
                    if (!col.nullMatch(field, (Column) field.getAnnotation(Column.class))) {
                        driver.alterNull(connection, entityDB, field);
                    }
                }
                updated = true;
            }
        }
        // TODO - maintain primary keys

        // manage foreign keys
        if (anoTable.maintainFK()) {
            TableExportKeyList keys = new TableExportKeyList(entityDB).load(driver, connection);
            if (anoTable.allowFkDrop()) {
                for (TableExportKey key : keys.toDrop(driver)) {
                    DataSourceDB.executeUpdate(connection, driver.dropForeignKey(entityDB, key.FK_NAME.get()));
                    updated = true;
                }
            }
            for (Field key : keys.toAdd(driver)) {
                keys.toAdd(driver);
                DBUtil.maintain(connection, ((ForeignKey) key.getAnnotation(ForeignKey.class)).table(), devMode);
                DataSourceDB.executeUpdate(connection, driver.addForeignKey(entityDB, driver, key));
                updated = true;
            }
            TableExportKeyList modify = keys.toModify(driver);
            FieldList fields = entityDB.getFieldsWithAnnotation(ForeignKey.class);
            for (TableExportKey key : modify) {
                DataSourceDB.executeUpdate(connection, driver.dropForeignKey(entityDB, key.FK_NAME.get()));
                DataSourceDB.executeUpdate(connection, driver.addForeignKey(entityDB, driver, fields.get(
                        modify.indexOf(fields, driver, key))));
                updated = true;
            }
        }

        // manage indices
        if (anoTable.maintainIndex()) {
            TableIndexList indexes = driver.getIndexes(connection, entityDB.getName()).removeIdIndex(entityDB);
            for (TableIndex index : indexes.toDrop(entityDB)) {
                DataSourceDB.executeUpdate(connection, driver.dropIndex(entityDB, index.INDEX_NAME.get()));
                updated = true;
            }
            for (Index index : indexes.toAdd(entityDB)) {
                driver.getIndexes(connection, entityDB.getName()).removeIdIndex(entityDB);
                DataSourceDB.executeUpdate(connection, driver.addIndex(entityDB, index));
                updated = true;
            }
        }
        return updated;
    }

    public static synchronized boolean maintain(Connection connection, Class<? extends EntityDB> eClass, boolean devMode) {
        try {
            return DBUtil.maintain(connection, eClass.newInstance(), devMode);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized boolean maintain(Connection connection, EntityDB entityDB, boolean devMode) {
        if (!maintained.contains(entityDB.getClass())) {
            maintained.add(entityDB.getClass());
            try {
                boolean updated = false;
                DBTableRecord.createTableIfNotExists(connection);
                
                Driver driver = DriverFactory.getDriver();
                entityDB.setName( driver.isOracle() ? entityDB.getName().toUpperCase() : entityDB.getName().toLowerCase() );
                
                if (devMode || DBTableRecord.shouldUpdate(connection, entityDB)) {
                    checkTableNameLength(connection, entityDB);
                    checkAllColumnNameLengths(connection, entityDB);
                    if (!checkTableIfExists(connection, entityDB.getName())) {
                        createTable(connection, entityDB, devMode);
                        updated = true;
                    } else {
                        updated = updateTable(connection, entityDB, devMode);
                    }
                    DBTableRecord.update(connection, entityDB);
                    connection.commit();
                }
                return updated;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    public static void updatePrimaryKeyName(Connection connection, EntityDB entity) throws SQLException {
        FieldList pkFields = entity.getId();
        if (!pkFields.isEmpty()) {
            for (Field pkField : pkFields) {
                Assert.notNull(pkField, "Unable to locate @Id Field by name %s referenced from Entity %s field %s",
                        entity.getName(), entity.getClass(), pkField.getName());
                pkField.setName( generateNewName(connection, pkField.getColumnName(), null) );
                pkField.set(pkField.get());
            }
        }
    }

    public static void updateForeignKeyNames(Connection connection, EntityDB fEntity) throws SQLException {
        FieldList fFields = fEntity.getForeignKeyFields(fEntity.getClass());
        if (!fFields.isEmpty()) {
            for (Field fField : fFields) {
                ForeignKey fE = (ForeignKey) fField.getAnnotation(ForeignKey.class);
                Field idField = !StringUtils.isEmpty(fE.field()) ? fEntity.getFields().getByName(fE.field()) : fEntity.getSingleId();
                Assert.notNull(idField, "Unable to locate @Id Field by name %s in referenced from Foreign Entity %s field %s",
                        fE.field(), fEntity.getClass(), fField.getName());
                idField.setName( generateNewName(connection, idField.getColumnName(), null) );
                fField.set(idField.get());
            }
        }
    }

    public static void updateIndexNames(Connection connection, EntityDB entity) throws SQLException {
        IndexList indexes = entity.getIndexes();
        if (!indexes.isEmpty()) {
            for(Index idx : indexes) {
                Field idxField = entity.getFields().getByName(idx.getName());
                Assert.notNull(idxField, "Unable to locate index Field by name %s referenced from Entity %s field %s",
                        idx.getName(), entity.getClass(), idxField.getName());
                idxField.setName( generateNewName(connection, idxField.getColumnName(), null) );
                idxField.set(idxField.get());
            }
        }
    }

    public static void checkAllColumnNameLengths(Connection connection, EntityDB entityDB) throws SQLException {
        StringList columnNames =  entityDB.getColumnNames();
        for(String col: columnNames) {
            checkColumnNameLength(connection,col);
        }
    }

    public static void checkColumnNameLength(Connection connection, String colName) throws SQLException {
        int lengthLimit =  connection.getMetaData().getMaxColumnNameLength();
        TAG.fine(String.format("Max column name length: %s", lengthLimit));
        Assert.isTrue( colName.length() <= lengthLimit,
                String.format("Column name %s is longer than the database column name length limit of %d chars.",
                        colName, lengthLimit));
    }

    public static void checkTableNameLength(Connection connection, EntityDB entityDB) throws SQLException {
        String tableName = entityDB.getName();
        int lengthLimit =  connection.getMetaData().getMaxTableNameLength();
        TAG.fine(String.format("Max table name length: %s", lengthLimit));
        Assert.isTrue( tableName.length() <= lengthLimit,
                String.format("Table name %s is longer than the database table name length limit of %d chars.",
                        tableName, lengthLimit));
    }

    public static String generateNewName(Connection connection, String name, String termSeparator) throws SQLException {

        Driver driver = DriverFactory.getDriver();

        termSeparator = termSeparator == null || StringUtils.isEmpty(termSeparator) ? "_" : termSeparator;

        int lengthLimit =  connection.getMetaData().getMaxTableNameLength();
        TAG.fine(String.format("Max table name length: %s", lengthLimit));

        int nameLengthBefore = name.length();
        TAG.fine(String.format("(BEFORE) Name: %s, Length: %d \n", name, nameLengthBefore));

        if(nameLengthBefore > lengthLimit) {

            int lengthDiff = nameLengthBefore - lengthLimit;

            String [] termArr = name.split(termSeparator);
            int numberOfTerms = termArr.length;

            String newName = "";

            if(numberOfTerms == 1) {
                TAG.fine(String.format("Name: %s is one long phrase or word, without separate terms.\n", name));
                newName = stringShrinker(name, lengthLimit);
            } else {

                TAG.fine(String.format("(BEFORE) Name: %s consists of the following terms ->\n", name));
                for (String s : termArr) {
                    TAG.fine(String.format("Term: %s\n", s));
                }

                for(int i = 0; i < lengthDiff; i++) {

                    for(int t = (numberOfTerms - 1); t >= 0; t--) {

                        String termBefore = termArr[t];
                        int termBeforeLength = termBefore.length();

                        if(termBeforeLength > 3) {
                            int middleLetterIdx = termBeforeLength / 2;
                            char [] letters = termBefore.toCharArray();
                            char [] newTermLetters = new char[ letters.length - 1 ];
                            for(int l = 0, k = 0; l < letters.length; l++) {
                                if(l != middleLetterIdx) {
                                    newTermLetters[k++] = letters[l];
                                }
                            }
                            termArr[t] = String.valueOf(newTermLetters);
                            break;
                        }

                    }

                }

                List<String> newNameTerms = Arrays.asList(termArr);
                newName = String.join("_" , newNameTerms  );

                int nameLengthAfter = newName.length();
                TAG.fine(String.format("(AFTER) Name: %s, Length: %d\n", newName, nameLengthAfter));

                newName = stringShrinker(newName, lengthLimit);

                TAG.fine(String.format("(FINAL) Name: %s consists of the following terms ->\n", newName));
                for (String s : newNameTerms) {
                    TAG.fine(String.format("Term: %s\n", s));
                }

            }

            return driver.isOracle() ? newName.toUpperCase() : newName.toLowerCase();

        }

        return driver.isOracle() ? name.toUpperCase() : name.toLowerCase();
    }

    private static String stringShrinker(String textString, int lengthLimit) {

        String tmpTextString = "";
        int textStringLength = textString.length();

        char [] textStringChars;
        int tmpTextStringLength;
        while(textStringLength > lengthLimit) {
            textStringChars = textString.toCharArray();
            tmpTextStringLength = textStringChars.length - 1;
            char [] tmpTextStringChars = new char[ tmpTextStringLength ];
            for(int l = 0, k = 0; l < tmpTextStringLength; l++) {
                tmpTextStringChars[k++] = textStringChars[l];
            }
            tmpTextString = String.valueOf(tmpTextStringChars);
            textStringLength = tmpTextString.length();
            TAG.fine(String.format("TextString: %s, Length: %d\n", tmpTextString, textStringLength));
            textString = tmpTextString;
        }

        return textString;
    }

}
