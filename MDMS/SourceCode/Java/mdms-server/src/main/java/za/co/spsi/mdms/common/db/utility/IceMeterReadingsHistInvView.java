package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceMeterReadingsHistInvView extends EntityDB {


    @Column(name = "CURRENT_METERREADINGDATE")
    public Field<Timestamp> meterReadingDate = new Field<>(this);

    @Column(name = "ICE_METER_ID")
    public Field<Integer> meterId = new Field<>(this);

//    @Column(name = "ICE_METER_REGISTER_ID")
//    public Field<Integer> meterRegisterId = new Field<>(this);
//
//    @Column(name = "LATEST_ICE_METER_READING")
//    public Field<Integer> latesMeterReading = new Field<>(this);
//
//    @Column(name = "LATEST_METERREADINGS_ID")
//    public Field<Integer> latesMeterReadingId = new Field<>(this);
//
//    @Column(name = "LATEST_QTY")
//    public Field<Double> latestQty = new Field<>(this);
//
//    @Column(name = "LATEST_C_INVOICELINE_ID")
//    public Field<Integer> latestInvoiceLineId = new Field<>(this);
//
//    @Column(name = "LATEST_INV_METERREADINGDATE")
//    public Field<Timestamp> latestInvMeterReadingDate = new Field<>(this);

    @Column(name = "PREV_INV_METER_READING")
    public Field<Double> prevInvMeterReading = new Field<>(this);

    @Column(name = "PREPREV_INV_METER_READING")
    public Field<String> prePrevInvMeterReading = new Field<>(this);

//    @Column(name = "PREV_INV_ICE_METERREADINGS_ID")
//    public Field<Integer> prevIncIceMeterReadingdsId = new Field<>(this);
//
//    @Column(name = "PREPREVINV_ICEMETERREADINGSID")
//    //@ForeignKey() IceMeter
//    public Field<Integer> prePrevInvIceMeterReadingId = new Field<>(this);

    @Column(name = "PREV_INV_METERREADINGDATE")
    public Field<Timestamp> prevInvMeterReadingDate = new Field<>(this);

    @Column(name = "PREPREV_INV_METERREADINGDATE")
    public Field<Timestamp> prePrevInvMeterReadingDate = new Field<>(this);

//    @Column(name = "PREV_INV_QTY")
//    public Field<Integer> prevInvQty = new Field<>(this);
//
//    @Column(name = "PREPREV_INV_QTY")
//    public Field<Integer> prePrevInvQty = new Field<>(this);
//
//    @Column(name = "PREV_C_INVOICELINE_ID")
//    public Field<Integer> prevInvLineId = new Field<>(this);
//
//    @Column(name = "PREPREV_C_INVOICELINE_ID")
//    public Field<Integer> prePrevInvLineId = new Field<>(this);

    public IceMeterReadingsHistInvView() {
        super("ICE_METERREADINGS_HIST_INV_V");
    }
}
