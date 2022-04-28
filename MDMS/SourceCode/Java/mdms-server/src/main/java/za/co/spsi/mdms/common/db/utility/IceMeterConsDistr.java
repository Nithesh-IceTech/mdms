package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by johan on 2017/03/30.
 */
public class IceMeterConsDistr extends EntityDB {
    
    public Field<Integer> ice_meterconsdistr_id = new Field<>(this);
    public Field<Character> isactive = new Field<>(this);
    public Field<String> name = new Field<>(this);
    public Field<Integer> c_uom_id = new Field<>(this);
    public Field<Integer> ice_property_id = new Field<>(this);
    public Field<Integer> seqno = new Field<>(this);
    public Field<String> ice_calculationmethod = new Field<>(this);
    public Field<String> ice_distributionmethod = new Field<>(this);
    public Field<Character> ice_performdistribution = new Field<>(this);
    public Field<Integer> ice_distributionvalue = new Field<>(this);

    public IceMeterConsDistr() {
        super("ICE_METERCONSDISTR");
    }

}