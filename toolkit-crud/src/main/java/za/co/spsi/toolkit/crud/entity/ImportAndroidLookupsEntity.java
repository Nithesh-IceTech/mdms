package za.co.spsi.toolkit.crud.entity;

import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;


/**
 * Created by jaspervdb on 2/3/16.
 */
@Table(version = 6)
public class ImportAndroidLookupsEntity extends AuditEntityDB {


    @Id(uuid = true)
    @Column(name = "IMPORT_ANDROID_LOOKUPS_ID",size = 50, notNull = true)
    public Field<String> importAndroidLookupsId = new Field<>(this);

    @Column(name = "APK_VERSION")
    public Field<String> apkVersion = new Field<String>(this);

    @Column(name = "LOOKUP_VERSION")
    public Field<Integer> lookupVersion = new Field<Integer>(this);

    @Column(name = "FILE_DATA")
    public Field<byte[]> fileData = new Field<>(this);

    @Column(name = "CREATE_T")
    public Field<Timestamp> createT= new Field<>(this);

    // IMPORT / UPDATE
    @Column(name = "FILENAME",size = 250)
    public Field<String> filename = new Field<String>(this);

    @Column(name = "NOTES",size = 250)
    public Field<String> notes = new Field<>(this);

    public ImportAndroidLookupsEntity() {
        super("IMPORT_ANDROID_LOOKUPS");
    }


}
