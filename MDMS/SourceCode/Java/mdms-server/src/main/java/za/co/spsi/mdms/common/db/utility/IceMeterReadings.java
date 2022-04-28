package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceMeterReadings extends EntityDB {

    @Column(name = "AD_CLIENT_ID")
    public Field<Integer> adClientId = new Field<>(this);

    @Column(name = "AD_ORG_ID")
    public Field<Integer> adOrgId = new Field<>(this);

    @Column(name = "CREATED")
    public Field<Timestamp> created = new Field<>(this);

//    @Column(name = "CREATEDBY")
//    public Field<Integer> createdBy = new Field<>(this);
//
//    @Column(name = "ICE_ESTIMATIONMETHOD_ID")
//    public Field<Integer> iceEstimationMethod = new Field<>(this);
//
//    @Column(name = "ICE_ISESTIMATE")
//    public Field<String> iceEstimate = new Field<>(this);

    @Column(name = "ICE_METERREADINGDATE")
    public Field<Timestamp> iceMeterReadingDate = new Field<>(this);

    @Column(name = "ICE_METERREADINGLIST_ID")
    public Field<Integer> iceMeterReadingListID = new Field<>(this);

    @Column(name = "ICE_METERREADINGS_ID")
    public Field<String> iceMeterReadingsID = new Field<>(this);

//    @Column(name = "ICE_METERREADINGS_UU")
//    public Field<String> iceMeterReadingsUU = new Field<>(this);

    @Column(name = "ICE_METER_READING")
    public Field<Double> iceMeterReading = new Field<>(this);

//    @Column(name = "ISACTIVE")
//    public Field<String> isActive = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "UPDATED")
    public Field<Timestamp> updated = new Field<>(this);

//    @Column(name = "UPDATEDBY")
//    public Field<String> updatedBy = new Field<>(this);

    @Column(name = "VALUE")
    public Field<String> value = new Field<>(this);

    @Column(name = "ICE_METER_READING_STATUS_DATE")
    public Field<Timestamp> iceMeterReadingStatusDate = new Field<>(this);

//    @Column(name = "QTY")
//    public Field<Integer> qty = new Field<>(this);
//
    @Column(name = "ICE_METER_REGISTER_ID")
    public Field<Integer> iceMeterRegisterID = new Field<>(this);

    @Column(name = "ICE_REASONNOTCAPTURED_ID")
    public Field<Integer> iceReasonNotCapturedID = new Field<>(this);

//    @Column(name = "ICE_OTHER")
//    public Field<String> iceOther = new Field<>(this);
//
//    @Column(name = "ICE_METERREADINGSTATUS_ID")
//    public Field<Integer> iceMeterReadingStatusID = new Field<>(this);
//
//    @Column(name = "C_INVOICE_ID")
//    public Field<Integer> cInvoiceID = new Field<>(this);

    @Column(name = "COMMENTS")
    public Field<String> comments = new Field<>(this);


    public IceMeterReadings() {
        super("ICE_METERREADINGS");
    }
}
