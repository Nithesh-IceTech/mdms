package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.upgrade.UpgradeHelper;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StringUtils;
import za.co.spsi.toolkit.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by jaspervdb on 2016/11/22.
 */
public class Table {

    private TableColumnList columns = new TableColumnList();
    private String tableName;

    public Table load(Connection connection, Driver driver, String tableName) {
        this.tableName = tableName;
        try {

            ResultSet rs =
                    connection.getMetaData().getColumns(
                            connection.getCatalog(),
                            null,
                            tableName, //driver.tableNamesInCaps() ? tableName.toUpperCase() :
                            null ); // driver.getColumnNameWildcardPattern()

            while (rs.next()) {
                columns.add((TableColumn) DataSourceDB.initEntityFromRs(new TableColumn(), rs));
            }

            rs = connection.getMetaData().getPrimaryKeys(
                    connection.getCatalog(),
                    connection.getMetaData().getUserName(),
                    tableName);

            StringList keys = new StringList(rs, "COLUMN_NAME");
            // should only be one
            for (String key : keys) {
                columns.getByName(key).setPrimaryKey(true);
            }
            // update primary keys
            return this;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public FieldList toAdd(EntityDB entity) {
        return columns.diff(entity.getFields());
    }

    public TableColumnList toRemove(EntityDB entity) {
        return columns.subtract(entity.getFields());
    }

    public FieldList toUpdate(Driver driver, EntityDB entity, za.co.spsi.toolkit.db.ano.Table table) {
        return columns.misMatch(driver, entity.getFields(), table);
    }

    public FieldList addKeys(EntityDB entity) {
        FieldList pKeys = entity.getId();
        for (int i = 0; i < pKeys.size(); i++) {
            if (columns.getPrimaryKey().getByName(pKeys.get(i).getName()) != null) {
                pKeys.remove(i--);
            }
        }
        return pKeys;
    }

    public TableColumnList getColumns() {
        return columns;
    }

    public TableColumnList removeKeys(EntityDB entity) {
        return columns.getPrimaryKey().subtract(entity.getId());
    }


    public String toString() {
        return String.format("Table: %s\n%s", tableName, columns.toString());
    }

    public static String getPKeyConstraintName(EntityDB parent, int length) {
        za.co.spsi.toolkit.db.ano.Table table = parent.getClass().getAnnotation(za.co.spsi.toolkit.db.ano.Table.class);
        FieldList namedField = parent.getFields().getFieldsWithAnnotationWithMethodSignatureWithFilter(Id.class, "name", new Util.Filter() {
            @Override
            public boolean filter(Object value) {
                return !StringUtils.isEmpty(value != null ? value.toString() : "");
            }
        });
        String name = !namedField.isEmpty() ? ((Id) namedField.get(0).getAnnotation(Id.class)).name() :
                (table != null && table.shortName().length() > 0 ? table.shortName() : parent.getName() + "_PK");
        return name.length() > length ? UpgradeHelper.shortenName(name) + "_PK" : name;
    }

}
