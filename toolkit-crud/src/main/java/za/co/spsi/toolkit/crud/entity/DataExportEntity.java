package za.co.spsi.toolkit.crud.entity;

import za.co.spsi.toolkit.crud.excel.DataSourceXls;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.FormattedSql;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 2016/07/25.
 * Entity is utilised to define a data export for a specific view
 */
@Table(version = 0)
public class DataExportEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "DATA_EXPORT_ID",size = 50, notNull = true)
    public Field<String> dataExportId = new Field<>(this);

    @Column(name = "VIEW_NAME",size = 50)
    public Field<String> viewName = new Field<>(this);

    @Column(name = "EXPORT_NAME",size = 30)
    public Field<String> exportName = new Field<>(this);

    @Column(name = "EXPORT_DESCRIPTION",size = 250)
    public Field<String> exportDescription = new Field<>(this);

    @Column(name = "EXPORT_SQL",size = 8192)
    public Field<String> exportsQL = new Field<>(this);

    public DataExportEntity() {
        super("DATA_EXPORT");
    }

    public static List<DataExportEntity> getExportsForView(DataSource dataSource, String viewName) {
        return new DataSourceDB<>(DataExportEntity.class).getAllAsList(
                dataSource, "select * from data_export where view_name = ? order by export_name asc", viewName);
    }

    public File export(DataSource dataSource, Entity refEntity) throws SQLException, IOException {
        StringList names = EntityDB.getFullColumnNames(refEntity.getFields());
        List values = new ArrayList();
        FormattedSql sql = new FormattedSql(exportsQL.get());
        String where = sql.getWhere();
        while (where != null && where.contains("{")) {
            String fieldName = where.substring(where.indexOf("{") + 1, where.indexOf("}"));
            Assert.isTrue(names.containsIgnoreCase(fieldName), "Could not find parameter %s \nfrom entity %s \nfor data export %s \nin query [%s]",
                    fieldName, refEntity.getClass().getSimpleName(), dataExportId.get(), exportsQL.get());
            values.add(refEntity.getFields().get(names.indexOfIgnoreCase(fieldName)).getAsString());
            where = where.substring(0, where.indexOf("{")) + "?" + where.substring(where.indexOf("}") + 1);
        }
        sql.setWhere(where);
        return export(dataSource,refEntity.getName()+".xls",sql.toString(), values.toArray());
    }

    public static File export(DataSource dataSource,String sheetName,String sql,Object ... values) throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
                for (int i = 0;values != null &&  i < values.length; i++) {
                    if (values[i] != null) {
                        ps.setObject(i + 1, values[i]);
                    }
                }
                try (ResultSet rs = ps.executeQuery()) {
                    File file = File.createTempFile(sheetName,".xls");
                    file.deleteOnExit();
                    DataSourceXls.export(rs,file,sheetName);
                    return file;
                }
            }
        }
    }
}
