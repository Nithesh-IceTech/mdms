package za.co.spsi.mdms.nes.db;

import za.co.spsi.mdms.nes.util.NESUtil;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldZip;

import javax.persistence.Lob;
import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2016/12/22.
 */
@Table(version = 3,deleteOldRecords = true, deleteRecordTimeField = "DATE_TIME_STAMP")
public class NESMeterResultEntity extends EntityDB {

    public enum Status {
        RECEIVED(0),PROCESSED(1),IGNORED(2),FAILED(3);

        public int code;

        Status(int code) {
            this.code = code;
        }
    }

    @Id(uuid = true)
    @Column(name = "METER_RESULT_ID")
    public Field<String> meterResultId = new Field<>(this);

    @ForeignKey(table = NESMeterEntity.class,onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "METER_ID")
    public Field<String> meterId = new Field<>(this);

    @Column(name = "RESULT_DATE_TIME")
    public Field<Timestamp> resultDateTime = new Field<>(this);

    @Column(name = "DATE_TIME_STAMP")
    public Field<Timestamp> dateTimeStamp = new Field<>(this);

    @Column(name = "ENTITY_SERIAL_NO")
    public Field<String> entitySerialNumber = new Field<>(this);

    @Column(name = "ROUTING_NAME")
    public Field<String> routingEntityName = new Field<>(this);

    @Column(name = "RESULT_DATA")
    public FieldZip resultData = new FieldZip(this);

    @Column(name = "STATUS", defaultValue = "0")
    public Field<Integer> status = new Field<>(this);

    @Column(name = "ERROR", size = 2048)
    public FieldError error = new FieldError(this);

    private Index idxMeterId = new Index("idx_NES_METER_RESULT_METER_ID",this,meterId);
    private Index idxMeterStatus = new Index("idx_NES_ESULT_METER_STATUS",this,meterId,status);
    private Index idxDateTime = new Index("IDX_NES_RS_DT",this,dateTimeStamp);

    public NESMeterResultEntity() {
        super("NES_METER_RESULT");
    }

    public NESMeterResultEntity init(NESMeterEntity meterEntity, NESResultEntity resultEntity) {
        this.status.set(Status.RECEIVED.code);
        this.meterResultId.set(resultEntity.resultId.get());
        this.meterId.set(meterEntity.meterId.get());
        this.resultDateTime.set(resultEntity.resultDateTime.get());
        this.dateTimeStamp.set(resultEntity.dateTimeStamp.get());
        this.entitySerialNumber.set(resultEntity.entitySerialNumber.get());
        this.routingEntityName.set(resultEntity.routingEntityName.get());
        this.resultData.setAndDeflate(NESUtil.Decompress(resultEntity.resultData.get()).getBytes());
        return this;
    }
}
