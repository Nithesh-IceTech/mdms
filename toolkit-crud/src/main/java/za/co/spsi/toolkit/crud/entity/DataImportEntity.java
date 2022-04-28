package za.co.spsi.toolkit.crud.entity;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;

import java.sql.*;

/**
 * Created by jaspervdb on 2016/07/25.
 * Entity is utilised to define a data export for a specific view
 */
@Table(version = 0)
public class DataImportEntity extends EntityDB {

    public enum Type {
        Insert,Update
    };

    @Id(uuid = true)
    @Column(name = "DATA_IMPORT_ID",size = 50, notNull = true)
    public Field<String> dataImportId = new Field<>(this);

    @Column(name = "TYPE",size = 10)
    public Field<String> type = new Field<>(this);

    @Column(name = "DESCRIPTION",size = 250)
    public Field<String> description = new Field<>(this);

    @Column(name = "NOTES",size = 250)
    public Field<String> notes = new Field<>(this);

    @Column(name = "USERNAME",size = 250)
    public Field<String> username = new Field<>(this);

    // IMPORT / UPDATE
    @Column(name = "FILENAME",size = 250)
    public Field<String> filename = new Field<String>(this);

    @Column(name = "IMPORT_TIME")
    public FieldTimestamp importTime = new FieldTimestamp(this);

    @Column(name = "FILE_DATA")
    public Field<byte[]> fileData = new Field<>(this);

    public DataImportEntity() {
        super("DATA_IMPORT");
    }

    @Override
    public boolean beforeDeleteEvent(Connection connection) throws SQLException {
        try {
            // delete all the created children
            try (PreparedStatement ps = DataSourceDB.prepareStatement(connection,
                    "select distinct(entity_class) from data_import_map where data_import_id = ?", dataImportId.get())) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        EntityDB entity = (EntityDB) Class.forName(rs.getString(1)).newInstance();
                        DataSourceDB.executeUpdate(connection, String.format("delete from %s where %s in (" +
                                        "select entity_id from data_import_map where entity_class = ? and entity_id = ?)",
                                entity.getName(), EntityDB.getColumnName(entity.getSingleId())),
                                entity.getClass().getName(), dataImportId.get());
                    }
                }
            }
            return super.beforeDeleteEvent(connection);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new SQLException(e.getMessage(),e);
        }
    }
}
