package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceMeterReadingListItems extends EntityDB {

    /*

INSERT INTO MZICEUTILODEV.ICE_METERREADINGLISTITEMS_V (AD_CLIENT_ID, AD_ORG_ID, ICE_METERREADINGLIST_ID, 
CREATED, CREATEDBY, ISACTIVE, ICE_METER_READING_DATE, VALUE, ICE_METERREADING_ROUTE_ID, MRR_ISACTIVE, MRR_VALUE, ICE_PROPERTY_ID, 
ICE_STAND_NO, ICE_STREET_NUMBER, ICE_STREET_NAME, PROPERTY_NAME, PROPERTY_ICE_STATUS, ICE_UNIT_NO, ICE_BUILDING_COMPLEX_NAME, PROPERTY_VALUE, ICE_PROPERTY_CODE, PROPERTY_ISACTIVE, PROPERTY_ISSUMMARY, ICE_PROPERTYENTITYTYPE_ID, ICE_ENTITY_HIERARCHY_ID, METERSEQ, EH_ISSUMMARY, ICE_ENTITY_HIERARCHY_TYPE, ICE_METERREAD, ICE_METER_NUMBER, ICE_METER_LOCATION, ICE_SERVICE_GROUP_ID, METER_ISACTIVE, ICE_METER_ID, ICE_METER_REGISTERID, ICE_METER_REGISTER_ID, ICE_METER_REGISTER_TYPE, METERREGISTER_ISACTIVE, ICE_METERREGISTERTYPE_ID, C_UOM_ID, ICE_METER_DIGITS, ICE_METERREADINGLIST_UU, UPDATED, UPDATEDBY, C_COUNTRY_ID, C_REGION_ID, C_CITY_ID, ICE_DISTRICT_ID, ICE_ISCURRENT, BPRELATIONSHIP, BP_VALUE, BP_NAME, C_BPARTNER_ID, LATEST_QTY, LATEST_ICE_METER_READING, LATEST_METERREADINGS_ID, LATEST_C_INVOICELINE_ID, PREV_INV_METER_READING, PREPREV_INV_METER_READING, PREV_INV_ICE_METERREADINGS_ID, PREPREVINV_ICEMETERREADINGSID, PREV_INV_METERREADINGDATE, PREPREV_INV_METERREADINGDATE, PREV_INV_QTY, PREPREV_INV_QTY, LATEST_INV_METERREADINGDATE, PREV_C_INVOICELINE_ID, PREPREV_C_INVOICELINE_ID) 
VALUES (1000007, 2010159, 1000060, TO_DATE('2017-03-02 10:21:44', 'YYYY-MM-DD HH24:MI:SS'), 1000218, 'Y', TO_DATE('2017-03-03 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), '1000060', 1000000, 'Y', '1000044', 2010704, null, null, 'Peter Mokaba Ridge 430', 'The Atrium Overport shop 44.', 'I', null, 'The Atrium Overport shop 44.', '2010704', 'EXT90909', 'Y', 'N', 1000004, 1000326, 0, 'N', '0002', 'Y', 'M0002', null, 2010129, 'Y', 1000142, 'REG0002', 1000122, null, 'Y', 1000048, 2010025, 10, 'dcd17c57-1a6e-41b4-be08-21a1d486a973', TO_DATE('2017-03-24 19:15:07', 'YYYY-MM-DD HH24:MI:SS'), 1000008, 305, 2010096, null, null, 'Y', 'Tenant', '2012714', 'MARCUS JAY', 2012714, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
 */

    public Field<Integer> ad_client_id = new Field<>(this);
    public Field<Integer> ad_org_id = new Field<>(this);

//    public Field<Integer> ice_meterreadinglist_id = new Field<>(this);

    public Field<Timestamp> created = new Field<>(this);
//    public Field<String> createdby = new Field<>(this);

    public Field<String> isactive = new Field<>(this);

//    public Field<Integer> ice_meterreadings_id  = new Field<>(this);
//    public Field<Timestamp> ice_meter_reading_date = new Field<>(this);

    public Field<Long> value = new Field<>(this);

//    public Field<String> mrr_isactive = new Field<>(this);
//    public Field<Integer> mrr_value = new Field<>(this);
//    public Field<String> ice_stand_no = new Field<>(this);
//    public Field<String> ice_street_number  = new Field<>(this);
//    public Field<String> ice_street_name  = new Field<>(this);
//    public Field<String> property_name  = new Field<>(this);
//    public Field<String> property_ice_status  = new Field<>(this);
//    public Field<String> ice_unit_no  = new Field<>(this);
//    public Field<String> ice_building_complex_name  = new Field<>(this);
//    public Field<String> property_value  = new Field<>(this);
//    public Field<String> ice_property_code  = new Field<>(this);
//    public Field<String> property_isactive  = new Field<>(this);
//    public Field<String> property_issummary  = new Field<>(this);
//    public Field<String> ice_propertyentitytype_id  = new Field<>(this);
//    public Field<String> ice_entity_hierarchy_id  = new Field<>(this);
//    public Field<String> meterseq  = new Field<>(this);
//    public Field<String> eh_issummary  = new Field<>(this);
//    public Field<String> ice_entity_hierarchy_type  = new Field<>(this);
//    public Field<String> ice_meterread  = new Field<>(this);
//    public Field<String> ice_meter_number  = new Field<>(this);
//    public Field<String> ice_meter_location  = new Field<>(this);
//    public Field<String> ice_service_group_id  = new Field<>(this);
//    public Field<String> meter_isactive  = new Field<>(this);

    public Field<String> ice_meter_id  = new Field<>(this);

//    public Field<String> ice_meter_registerid  = new Field<>(this);

    public Field<String> ice_meter_register_id  = new Field<>(this);

//    public Field<String> ice_meter_register_type  = new Field<>(this);
//    public Field<String> meterregister_isactive  = new Field<>(this);
//    public Field<String> ice_meterregistertype_id  = new Field<>(this);
//    public Field<String> c_uom_id  = new Field<>(this);
//    public Field<Integer> ice_meter_digits  = new Field<>(this);
//    public Field<String> ice_meterreadinglist_uu  = new Field<>(this);

    public Field<Timestamp> updated  = new Field<>(this);

//    public Field<String> updatedby  = new Field<>(this);
//    public Field<String> c_country_id  = new Field<>(this);
//    public Field<String> c_region_id  = new Field<>(this);
//    public Field<String> c_city_id  = new Field<>(this);
//    public Field<String> ice_district_id  = new Field<>(this);
//    public Field<String> ice_iscurrent  = new Field<>(this);
//    public Field<String> bprelationship  = new Field<>(this);
//    public Field<String> bp_value  = new Field<>(this);
//    public Field<String> bp_name  = new Field<>(this);
//    public Field<String> c_bpartner_id  = new Field<>(this);
//    public Field<String> latest_qty  = new Field<>(this);
//    public Field<String> latest_ice_meter_reading  = new Field<>(this);
//    public Field<Integer> latest_meterreadings_id  = new Field<>(this);
//    public Field<String> latest_c_invoiceline_id  = new Field<>(this);
//    public Field<Long> prev_inv_meter_reading  = new Field<>(this);
//    public Field<Long> preprev_inv_meter_reading  = new Field<>(this);
//    public Field<String> prev_inv_ice_meterreadings_id  = new Field<>(this);
//    public Field<String> preprevinv_icemeterreadingsid  = new Field<>(this);
//    public Field<Timestamp> prev_inv_meterreadingdate  = new Field<>(this);
//    public Field<Timestamp> preprev_inv_meterreadingdate  = new Field<>(this);
//    public Field<String> prev_inv_qty  = new Field<>(this);
//    public Field<String> preprev_inv_qty  = new Field<>(this);
//    public Field<Timestamp> latest_inv_meterreadingdate  = new Field<>(this);
//    public Field<String> prev_c_invoiceline_id  = new Field<>(this);
//    public Field<String> preprev_c_invoiceline_id = new Field<>(this);
    
    public IceMeterReadingListItems() {
        super("ICE_METERREADINGLISTITEMS_V");
    }
}

