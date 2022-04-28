package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/05/03.
 */
public class IceTimeOfUseType extends EntityDB {

    public Field<String> ad_client_id = new Field<>(this);
    public Field<String> ad_org_id = new Field<>(this);
    public Field<Timestamp> created = new Field<>(this);

//    public Field<Integer> createdby = new Field<>(this);
//    public Field<Integer> ice_timeusetype_id = new Field<>(this);
//    public Field<String> ice_timeusetype_uu = new Field<>(this);

    public Field<Character> isactive = new Field<>(this);
    public Field<String> name = new Field<>(this);
    public Field<Timestamp> updated = new Field<>(this);

//    public Field<Integer> updatedby = new Field<>(this);
    public Field<String> value = new Field<>(this);


    public IceTimeOfUseType() {
        super("ICE_TIMEUSETYPE");
    }
}
