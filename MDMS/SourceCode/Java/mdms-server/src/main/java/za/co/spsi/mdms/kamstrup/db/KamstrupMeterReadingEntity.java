package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.kamstrup.services.order.domain.MeterResult;
import za.co.spsi.mdms.kamstrup.services.order.domain.Register;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.LoggerCommandResult;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@Table(version = 4,deleteOldRecords = true)
public class KamstrupMeterReadingEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "METER_READING_ID",size = 50)
    public Field<String> meterReadingId = new Field<>(this);

    @Column(name = "METER_ID")
    @ForeignKey(table = KamstrupMeterEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterId = new Field<>(this);

    @Column(name = "READING_ID")
    @ForeignKey(table = MeterReadingEntity.class, onDeleteAction = ForeignKey.Action.Cascade,name = "KMR_READID")
    public Field<String> readingId = new Field<>(this);

    @Column(name = "METER_ORDER_ID")
    @ForeignKey(table = KamstrupMeterOrderEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterOrderId = new Field<>(this);

    @Column(name = "LOGGER_ID")
    public Field<String> loggerId = new Field<>(this);

    @Column(name = "LOG_ID")
    public Field<String> logId = new Field<>(this);

    @Column(name = "REGISTER_ID")
    public Field<String> registerId = new Field<>(this);

    @Column(name = "ENTRY_TIME")
    public FieldTimestamp entryTime = new FieldTimestamp(this);

    @Column(name = "UNIT")
    public Field<String> unit = new Field<>(this);

    @Column(name = "SCALE")
    public Field<Integer> scale = new Field<>(this);

    @Column(name = "VALUE")
    public Field<Double> value = new Field<>(this);

    private Index idxReadingId = new Index("idx_READING_ID", this, readingId);
    private Index idxMeter = new Index("idx_METER_ID", this, meterId);
    private Index idxmeterOrderId = new Index("idx_METER_ORDER_ID", this, meterOrderId);

    public KamstrupMeterReadingEntity() {
        super("KAMSTRUP_METER_READING");
    }

    public KamstrupMeterReadingEntity init(KamstrupMeterOrderEntity order, KamstrupMeterEntity meter, MeterResult result, LoggerCommandResult.Entry entry, Register register) {
        this.meterId.set(meter.meterId.get());
        this.meterOrderId.set(order.meterOrderId.get());
        this.loggerId.set(result.commandResults.loggerCommandResult.logger.id);
        this.logId.set(entry.logId);
        this.registerId.set(register.id);
        this.unit.set(register.unit);
        this.scale.set(register.scale);
        this.value.set(register.value);
        return this;
    }

}
