package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.util.List;

/**
 * Created by johan on 2017/01/03.
 */

@Table(version = 0)
public class KamstrupReadingCommandEntity extends EntityDB {

    public enum Status {
        CREATED(0),
        PROCESSING(1),
        SUBMITED(2),
        WAITING(3),
        FAILED_WITH_REASON(4),
        FAILED_TIME_OUT(5),
        SUCCESSFUL(6);

        int code;
        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

    }

    @Id(uuid = true)
    @Column(name = "READING_COMMAND_ID")
    public Field<String> readingCommandId = new Field<>(this);

    @Column(name = "LOG_ID")
    public Field<String> logId = new Field<>(this);

    @Column(name = "GROUP_REF",size = 250)
    public Field<String> groupRef = new Field<>(this);

    public EntityRef<KamstrupReadingCommandRegistersEntity> registers = new EntityRef<>(this);

    @Column(name = "STATUS")
    public Field<Integer> status = new Field<>(this);

    @Column(name = "ORDER_URL")
    public Field<String> orderURL = new Field<>(this);


    public static void scheduleCreated(javax.sql.DataSource dataSource) {
        DataSourceDB.executeUpdate(dataSource,"update KAMSTRUP_READING_COMMAND SET STATUS = ? WHERE STATUS = ?", Status.PROCESSING.getCode(), Status.CREATED.getCode());
    }

    public static List<KamstrupReadingCommandEntity> getByStatus(javax.sql.DataSource dataSource, Status status) {
        return new DataSourceDB<>(KamstrupReadingCommandEntity.class).getAllAsList(dataSource,
                "select * from KAMSTRUP_READING_COMMAND where STATUS = ?",status.code);
    }

    public KamstrupReadingCommandEntity() {
        super("KAMSTRUP_READING_COMMAND");
    }
}
