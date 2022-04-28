package za.co.spsi.toolkit.crud.entity;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

@Table(version = 2)
public class ErrorLogEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "ERROR_LOG_ID", size = 50, notNull = true)
    public Field<String> errorLogId = new Field<>(this);

    @Column(name = "TIMESTAMP")
    public Field<Timestamp> timestamp = new Field<>(this);

    @Column(name = "IMEI")
    public Field<String> imei = new Field<>(this);

    @Column(name = "APK_VERSION")
    public Field<String> apkVersion = new Field<>(this);

    @Column(name = "ERROR", size = 4000)
    public Field<String> error = new Field<>(this);

    @Column(name = "MSG", size = 4000)
    public Field<String> msg = new Field<>(this);

    public ErrorLogEntity() {
        super("ERROR_LOG");
    }
}
