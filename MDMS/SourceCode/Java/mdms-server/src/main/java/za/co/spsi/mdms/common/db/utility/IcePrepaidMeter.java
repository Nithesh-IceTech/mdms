package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by johan on 2017/03/30.
 */
public class IcePrepaidMeter extends EntityDB {

    @Column(name = "ICE_METER_ID")
    public Field<String> iceMeterID = new Field<>(this);

    @Column(name = "ICE_METER_NUMBER")
    public Field<String> iceMeterNumber = new Field<>(this);

    @Column(name = "ICE_METER_PREPAID")
    public Field<String> iceMeterPrepaid = new Field<>(this);

    @Column(name = "ICE_METER_REGISTER_ID")
    public Field<String> iceMeterRegisterId = new Field<>(this);

    @Column(name = "REGISTER_NUMBER")
    public Field<String> meterRegister = new Field<>(this);

    public IcePrepaidMeter() {
        super("ICE_PREPAID_METER");
    }

}