package za.co.spsi.toolkit.crud.entity;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
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
public class ShapeImportEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "SHAPE_IMPORT_ID",size = 50, notNull = true)
    public Field<String> shapeImportId = new Field<>(this);

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

    public EntityRef<ShapeEntity> shapes = new EntityRef<>(this);

    public ShapeImportEntity() {
        super("SHAPE_IMPORT");
    }

}
