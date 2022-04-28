package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceMeterRegister extends EntityDB {

    @Column(name = "CREATED")
    public Field<Timestamp> created = new Field<>(this);

    @Column(name = "DESCRIPTION")
    public Field<String> description = new Field<>(this);

    @Column(name = "ICE_LOADPROFILENO")
    public Field<Integer> profileNo = new Field<>(this);

    @Column(name = "ICE_METERREGISTERREADINGTYPE")
    public Field<String> meterRegisterReadingType = new Field<>(this);

    @Column(name = "ICE_METER_CHANNELID")
    public Field<Integer> meterChannelId = new Field<>(this);

    @Column(name = "ICE_METER_ID")
    //@ForeignKey() IceMeter
    public Field<Integer> meterId = new Field<>(this);

    @Column(name = "ICE_METER_LOGGERID")
    public Field<String> loggerId = new Field<>(this);

    @Column(name = "ICE_METER_REGISTERID")
    public Field<String> meterRegister = new Field<>(this);

    @Column(name = "ICE_METER_REGISTER_ID")
    public Field<String> meterRegisterId = new Field<>(this);

    @Column(name = "ISACTIVE")
    public Field<String> isActive = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "UPDATED")
    public Field<Timestamp> updated = new Field<>(this);

    @Column(name = "VALUE")
    public Field<String> value = new Field<>(this);

    @Column(name = "ICE_METER_AUTO_RESET")
    public Field<String> meterAutoReset = new Field<>(this);

    @Column(name = "ICE_METER_DIGITS")
    public Field<Integer> meterDigits = new Field<>(this);

    @Column(name = "ICE_METERREGISTERTYPE_ID")
    public Field<String> meterRegisterTypeId = new Field<>(this);

    @Column(name = "ICE_AUTORESETDAY")
    public Field<String> autoResetDay = new Field<>(this);

//    @Column(name = "ICE_NOTIFIEDMAXIMUMDEMAND")
//    public Field<Double> notifiedMaxDemand = new Field<>(this);

    public IceMeterRegister() {
        super("ICE_METER_REGISTER");
    }
}
