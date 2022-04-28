package za.co.spsi.toolkit.db.entity;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@Table(version = 3, maintainStrict = true)
public class TestEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "METER_READING_ID",size = 50)
    public Field<String> meterReadingId = new Field<>(this);

    @Column(name = "METER_ID",size = 50)
    public Field<String> meterId = new Field<>(this);

    @Column(name = "METER_ORDER_ID",size = 50)
    public Field<String> meterOrderId = new Field<>(this);

    @Column(name = "LOGGER_ID",size = 50)
    public Field<String> loggerId = new Field<>(this);

    @Column(name = "LOG_ID",size = 50)
    public Field<String> logId = new Field<>(this);

    @Column(name = "REGISTER_ID")
    public Field<String> registerId = new Field<>(this);

    @Column(name = "ENTRY_TIME")
    public Field<Timestamp> entryTime = new Field<>(this);

    @Column(name = "UNIT")
    public Field<String> unit = new Field<>(this);

    @Column(name = "SCALE")
    public Field<Integer> scale = new Field<>(this);

//    @Column(name = "VALUE")
//    public Field<Double> value = new Field<>(this);

    public TestEntity() {
        super("TEST_METER_READING");
    }


}
