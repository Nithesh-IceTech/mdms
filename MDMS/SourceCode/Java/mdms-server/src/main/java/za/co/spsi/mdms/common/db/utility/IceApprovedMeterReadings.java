package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceApprovedMeterReadings extends EntityDB {

    public Field<String> ice_meterreadings_id = new Field<>(this);
    public Field<String> ice_meterreadinglist_id = new Field<>(this);
    public Field<Integer> meterseq = new Field<>(this);
    public Field<Character> ice_isprocessed = new Field<>(this);
    public Field<Double> latest_ice_meter_reading = new Field<>(this);
    public Field<Timestamp> latest_inv_meterreadingdate = new Field<>(this);
    public Field<Double> prev_inv_meter_reading = new Field<>(this);
    public Field<Double> preprev_inv_meter_reading = new Field<>(this);
    public Field<Integer> prev_inv_ice_meterreadings_id = new Field<>(this);
    public Field<Integer> preprevinv_icemeterreadingsid = new Field<>(this);
    public Field<Timestamp> prev_inv_meterreadingdate = new Field<>(this);
    public Field<Timestamp> preprev_inv_meterreadingdate = new Field<>(this);

    public Field<String> ice_splitlist_indicator = new Field<>(this);

//    public Field<Integer> prev_inv_qty = new Field<>(this);
//    public Field<Integer> preprev_inv_qty = new Field<>(this);
//    public Field<Integer> prev_c_invoiceline_id = new Field<>(this);
//    public Field<Integer> preprev_c_invoiceline_id = new Field<>(this);
//    public Field<Integer> ice_property_id = new Field<>(this);
//    public Field<String> ice_stand_no = new Field<>(this);
//    public Field<Integer> ice_street_number = new Field<>(this);
//    public Field<String> ice_street_name = new Field<>(this);
//    public Field<String> property_name = new Field<>(this);
//    public Field<String> property_ice_status = new Field<>(this);
//    public Field<String> ice_unit_no = new Field<>(this);
//    public Field<String> ice_building_complex_name = new Field<>(this);
//    public Field<String> property_value = new Field<>(this);
//    public Field<String> ice_property_code = new Field<>(this);
//    public Field<String> property_isactive = new Field<>(this);
//    public Field<String> property_issummary = new Field<>(this);
//    public Field<Integer> ice_propertyentitytype_id = new Field<>(this);
//    public Field<String> ice_meter_number = new Field<>(this);
//    public Field<String> ice_meter_location = new Field<>(this);
//    public Field<String> ice_service_group_id = new Field<>(this);
//    public Field<String> meter_isactive = new Field<>(this);
    public Field<Integer> ice_meter_id = new Field<>(this);
//    public Field<String> ice_meter_registerid = new Field<>(this);
    public Field<Integer> ice_meter_register_id = new Field<>(this);
//    public Field<String> ice_meter_register_type = new Field<>(this);
//    public Field<String> meterregister_isactive = new Field<>(this);
//    public Field<Integer> ice_meterregistertype_id = new Field<>(this);
//    public Field<Integer> c_uom_id = new Field<>(this);
//    public Field<Integer> ice_meter_digits = new Field<>(this);
//    public Field<Integer> m_pricelist_version_id = new Field<>(this);

    public IceApprovedMeterReadings() {
        super("ICE_APPROVEDMETERREADINGS_V");
    }
}

