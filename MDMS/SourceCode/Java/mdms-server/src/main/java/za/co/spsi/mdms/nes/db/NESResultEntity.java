package za.co.spsi.mdms.nes.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/12.
 */
public class NESResultEntity extends EntityDB {

    /*

    Results.ResultDateTime,Results.DateTimeStamp,EntitySerialNumber,Results.RoutingEntityName,Results.ResultData

     */
    @Id(uuid = true)
    public Field<String> resultId = new Field<>(this);

    public Field<Timestamp> resultDateTime = new Field<>(this);
    public Field<Timestamp> dateTimeStamp = new Field<>(this);
    public Field<String> entitySerialNumber = new Field<>(this);
    public Field<String> routingEntityName = new Field<>(this);
    public Field<String> resultData = new Field<>(this);


    public NESResultEntity() {
        super("Results");
    }

    public NESResultEntity(NESDeviceEntity device) {
        this();
    }

}
