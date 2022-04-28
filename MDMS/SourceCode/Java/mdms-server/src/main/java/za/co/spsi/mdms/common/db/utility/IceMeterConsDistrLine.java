package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by johan on 2017/03/30.
 */
public class IceMeterConsDistrLine extends EntityDB {

    public enum Action {
        ADD, SUBTRACT, DISTRIBUTE;
    }

    public Field<Integer> ice_meterconsdistrline_id = new Field<>(this);
    public Field<Integer> ad_client_id = new Field<>(this);
    public Field<Integer> ad_org_id = new Field<>(this);
    public Field<Character> isactive = new Field<>(this);
    public Field<String> name = new Field<>(this);
    public Field<Integer> ice_meterconsdistr_id = new Field<>(this);
    public Field<Integer> seqno = new Field<>(this);
    public Field<String> ice_operationtype = new Field<>(this);
    public Field<Integer> ice_meter_id = new Field<>(this);
    public Field<Integer> c_uom_id = new Field<>(this);
    public Field<Integer> ice_distributionvalue = new Field<>(this);
    public Field<Integer> ice_meter_register_id = new Field<>(this);
    public Field<Integer> ice_distributionproperty_id = new Field<>(this);

    public IceMeterConsDistrLine() {
        super("ICE_METERCONSDISTRLINE");
    }

    public boolean isVM() {
        return Action.DISTRIBUTE.name().equals(ice_operationtype.get());
    }

}