package za.co.spsi.mdms.common.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by johan on 2017/03/15.
 */
@Table(version = -1)
public class ApprovedMeterReading extends EntityDB {
    @Id(uuid=true)
    @Column(name = "APPROVED_METER_READING_ID", size = 50)
    public Field<String> approvedMeterId= new Field<>(this);

    @Column(name = "CREATED_DATE")
    public Field<Timestamp> createdDate= new Field<>(this);

    @Column(name = "UPDATED_DATE")
    public Field<Timestamp> updatedDate= new Field<>(this);

    @Column(name = "METER_READING_DATE")
    public Field<Timestamp> meterReadingDate= new Field<>(this);

    @Column(name = "METER_READING_LIST_ID", size = 15)
    public Field<Integer> meterReadingListID = new Field<>(this);

    @Column(name = "METER_READINGS_ID", size = 15)
    public Field<Integer> meterReadingID = new Field<>(this);

    @Column(name = "METER_ID", size = 15)
    public Field<Integer> meterID = new Field<>(this);

    @Column(name = "METER_REGISTER_ID", size = 15)
    public Field<Integer> meterRegisterID = new Field<>(this);

    @Column(name = "PROPERTY_ID", size = 15)
    public Field<Integer> propertyID = new Field<>(this);


    public ApprovedMeterReading() {
        super("APPROVED_METER_READING");
    }


}
