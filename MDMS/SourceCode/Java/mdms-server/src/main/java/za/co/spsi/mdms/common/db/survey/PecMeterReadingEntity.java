/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.mdms.common.db.survey;

import za.co.spsi.mdms.common.db.utility.IceApprovedMeterReadingsView;
import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.AlwaysExport;
import za.co.spsi.toolkit.entity.ano.Audit;

import java.sql.Connection;
import java.sql.Timestamp;

import static za.co.spsi.mdms.common.dao.MeterResultData.getNonNull;

/**
 *
 * @author jaspervdb
 */

@Table(version = 4)
@Audit
public class PecMeterReadingEntity extends EntityDB {

    @Id(uuid=true,name = "MREAD_PK")
    @Column(name = "METER_READING_ID", size = 50)
    public Field<String> meterReadingId= new Field<>(this);

    @Column(name = "METER_REGISTER_ID", size = 50)
    @ForeignKey(table= PecMeterRegisterEntity.class,name = "MREAD_METER_ID",onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterRegisterId= new Field<>(this);

    @Column(name = "UTILITY_METER_READING_LIST_ID", size = 50)
    @ForeignKey(table= PecUtilityMeterReadingListEntity.class,name = "PMRU_METER_READING_LIST_ID",onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> utilityMeterReadingListId= new Field<>(this);


    @Column(name = "AGENCY_ID", size = 8)
    public Field<Integer> agencyId= new Field<>(this);

    @Column(name = "USER_ID", size = 50)
    public UserIdField userId= new UserIdField(this);

    @Column(name = "IS_PROCESSED", defaultValue = "N")
    public Field<Character> isProcessed = new Field<>(this);

    @Column(name = "PREV_READING_1")
    public Field<Double> prevReading1 = new Field<>(this);

    @Column(name = "PREV_READING_DATE_1")
    public Field<Timestamp> prevReadingDate1 = new Field<>(this);

    @Column(name = "PREV_READING_2")
    public Field<Double> prevReading2 = new Field<>(this);

    @Column(name = "PREV_READING_DATE_2")
    public Field<Timestamp> prevReadingDate2 = new Field<>(this);

    @Column(name = "ACTUAL_READING")
    public Field<Double> actualReading = new Field<>(this);

    @Column(name = "CURRENT_READING")
    public Field<Double> reading = new Field<>(this);

    @Column(name = "SMART_READING")
    public Field<Double> smartReading = new Field<>(this);

    @Column(name = "CURRENT_READING_DATE")
    public Field<Timestamp> readingDate = new Field<>(this);

    @Column(name = "NO_READING_REASON_CD")
    public Field<Integer> noReadingReasonCd = new Field<>(this);

    @Column(name = "CREATED")
    public FieldTimestamp created = new FieldTimestamp(this);

    @Column(name = "COMMENTS", size = 500)
    public Field<String> comments = new Field<>(this);

    @Column(name = "DEVIATION_COMMENT", size = 500)
    public Field<String> deviationComment = new Field<>(this);

    public Field<String> reference_id = new Field<>(this);

    @AlwaysExport
    @Exportable(name = "pecMeterReadingPhotos", forceExport = true)
    public EntityRef<PecMeterReadingPhotoEntity> photo = new EntityRef<>(this);

    public EntityRef<PecMeterRegisterEntity> register = new EntityRef<>(meterRegisterId,this);

    public EntityRef<PecMeterEntity> meter = new EntityRef<>(
            "select pec_meter.* from pec_meter,pec_meter_register where pec_meter.meter_id = pec_meter_register.meter_id and " +
                    "pec_meter_register.meter_register_id = pec_meter_reading.meter_register_id",this);

    public Index idxExport = new Index("PEC_MR_E_EXP",this,meterReadingId,meterRegisterId);

    public PecMeterReadingEntity() {
        super("PEC_METER_READING");
    }

    public PecMeterReadingEntity initReading(IceApprovedMeterReadingsView view) {
        reference_id.set(view.approved.ice_meterreadings_id.get());
        isProcessed.set(view.approved.ice_isprocessed.get() == null?'N':view.approved.ice_isprocessed.get());
//        prevReading1.set(getNonNull(view.approved.prev_inv_meter_reading.get()));
//        prevReadingDate1.set(view.approved.prev_inv_meterreadingdate.get());
        prevReading1.set(getNonNull(view.approved.latest_ice_meter_reading.get()));
        prevReadingDate1.set(view.approved.latest_inv_meterreadingdate.get());
        prevReading2.set(getNonNull(view.approved.preprev_inv_meter_reading.get()));
        prevReadingDate2.set(view.approved.preprev_inv_meterreadingdate.get());

        reading.set(view.readings.iceMeterReading.get());
        readingDate.set(view.list.meterReadingDate.get());

        noReadingReasonCd.set(view.readings.iceReasonNotCapturedID.get());
        created.set(view.readings.created.get());
        comments.set(view.readings.comments.get());

        return this;
    }

    public static PecMeterReadingEntity create(Connection connection, PecMeterRegisterEntity register,IceApprovedMeterReadingsView view) {
        PecMeterReadingEntity reading = new PecMeterReadingEntity();
        reading.initReading(view);
        reading.meterRegisterId.set(register.meterRegisterId.get());
        reading.utilityMeterReadingListId.set(register.utilityMeterReadingListId.get());
        DataSourceDB.set(connection,reading);
        return reading;
    }


}
