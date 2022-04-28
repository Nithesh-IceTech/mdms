package za.co.spsi.mdms.common.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

public class MeterReadingTsdbEntity extends EntityDB {

    public Field<Timestamp> entry_time   = new Field<>(this);

    public Field<Double>    total_kwhp_orig   = new Field<>(this);
    public Field<Double>    total_kwhp        = new Field<>(this);
    public Field<Double>    total_kwhn        = new Field<>(this);
    public Field<Double>    total_kvarp       = new Field<>(this);
    public Field<Double>    total_kvarn       = new Field<>(this);

    public Field<Double>    t1_kwhp_orig = new Field<>(this);
    public Field<Double>    t1_kwhp      = new Field<>(this);
    public Field<Double>    t1_kwhn      = new Field<>(this);
    public Field<Double>    t1_kvarp     = new Field<>(this);
    public Field<Double>    t1_kvarn     = new Field<>(this);

    public Field<Double>    t2_kwhp_orig = new Field<>(this);
    public Field<Double>    t2_kwhp      = new Field<>(this);
    public Field<Double>    t2_kwhn      = new Field<>(this);
    public Field<Double>    t2_kvarp     = new Field<>(this);
    public Field<Double>    t2_kvarn     = new Field<>(this);

    public Field<Double>    rms_l1_v_orig = new Field<>(this);
    public Field<Double>    rms_l1_v      = new Field<>(this);
    public Field<Double>    rms_l2_v      = new Field<>(this);
    public Field<Double>    rms_l3_v      = new Field<>(this);

    public Field<Double>    rms_l1_c_orig = new Field<>(this);
    public Field<Double>    rms_l1_c      = new Field<>(this);
    public Field<Double>    rms_l2_c      = new Field<>(this);
    public Field<Double>    rms_l3_c      = new Field<>(this);

    public Field<Double>    volume_1_orig = new Field<>(this);
    public Field<Double>    volume_1      = new Field<>(this);

    public MeterReadingTsdbEntity() {
        super("meter_reading");
    }
}
