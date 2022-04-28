package za.co.spsi.mdms.common.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;

@Table(version = 1)
public class PrepaidMeterReadingBatch extends EntityDB {

    public enum Status {
        MARKED(-1),STARTED(0),COMPLETED(1),FAILED(2);

        int code;

        Status(int p_code) {
            code = p_code;
        }

        public int getCode() {
            return code;
        }
    }

    @Id(uuid=true)
    @Column(name = "PREPAID_METER_READING_BATCH_ID", size = 50)
    public Field<String> prepaidMeterReadingBatchId= new Field<>(this);

    @Column(name = "CREATED_DATE")
    public FieldTimestamp createdDate= new FieldTimestamp(this);

    @Column(name = "UPDATED_DATE")
    public FieldTimestamp updateDate= new FieldTimestamp(this).onUpdate();

    @Column(name = "STATUS_ID", defaultValue = "-1")
    public Field<Integer> statusId = new Field<>(this);

    @Column(name = "UTIL_STATUS_ID")
    public Field<String> utilStatusId = new Field<>(this);

    @Column(name = "SERIAL_N")
    public Field<String> serialN = new Field<>(this);

    @Column(name = "UTIL_DATA",size = 4000,autoCrop = true)
    public Field<String> utilData = new Field<>(this);

    @Column(size = 4000,autoCrop = true)
    public FieldError error = new FieldError(this);

    public EntityRef<MeterReadingEntity> readings = new EntityRef<>(this);

    public Index idxCreated = new Index("pmrbIdxCreated",this,createdDate);

    public PrepaidMeterReadingBatch() {
        super("PREPAID_METER_READING_BATCH");
    }


}
