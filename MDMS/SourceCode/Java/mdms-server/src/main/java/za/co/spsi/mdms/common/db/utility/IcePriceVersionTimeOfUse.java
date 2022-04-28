package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IcePriceVersionTimeOfUse extends EntityDB {

    @Column(name = "CREATED")
    public Field<Timestamp> created = new Field<>(this);

    @Column(name = "ENDTIME")
    public Field<Time> endTime = new Field<>(this);

    @Column(name = "ISACTIVE")
    public Field<Character> isActive = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "STARTTIME")
    public Field<Time> startTime = new Field<>(this);

    @Column(name = "UPDATED")
    public Field<Time> updated = new Field<>(this);

    @Column(name = "VALUE")
    public Field<String> value = new Field<>(this);

    @Column(name = "ICE_TIMEUSETYPE_ID")
    public Field<Integer> timeOfUseId = new Field<>(this);

    @Column(name = "ICE_DAYOFWEEK_ID")
    public Field<Integer> dayIfWeekId = new Field<>(this);

    @Column(name = "DATETRX")
    public Field<java.sql.Date> dateTrx = new Field<>(this);

    public IcePriceVersionTimeOfUse() {
        super("ICE_PRICEVERSIONTIMEOFUSE");
    }
}
