package za.co.spsi.toolkit.db.cleanup.entity;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

@Table(version = 0)
public class CleanupLogEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "cleanup_log_id", size = 50)
    public Field<String> cleanupLogId = new Field<>(this);

    @Column(name = "start_time")
    public Field<Timestamp> startTime = new Field<>(this);

    @Column(name = "end_time")
    public Field<Timestamp> endTime = new Field<>(this);

    @Column(name = "row_count")
    public Field<Integer> rowCount = new Field<>(this);

    @Column(name = "statement", size = 1024)
    public Field<String> statement = new Field<>(this);

    @Column(name = "duration_Millisecond")
    public Field<Integer> durationMillisecond = new Field<>(this);

    public CleanupLogEntity() {
        super("CLEANUP_LOG");
    }


}
