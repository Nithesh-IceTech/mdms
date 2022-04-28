package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.mdms.kamstrup.services.meter.MeterDao;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.Audit;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.Cascade;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@Table(version = 1)
@Audit(services = false)
public class KamstrupMeterRegisterEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "METER_REGISTER_ID")
    public Field<String> meterRegisterId = new Field<>(this);

    @Column(name = "METER_ID")
    @ForeignKey(table =  KamstrupMeterEntity.class, onDeleteAction = Cascade)
    public Field<String> meterId = new Field<>(this);

    public Field<String> id = new Field<>(this);
    public Field<String> name = new Field<>(this);
    public Field<String> command = new Field<>(this);
    public Field<String> actions = new Field<>(this);

    @Column(name = "AUTO_COLLECT")
    public Field<Boolean> autoCollect = new Field<>(this);

    private Index idxMeterId = new Index("idx_KAM_METER_REGISTER_METER_ID",this,meterId);

    public KamstrupMeterRegisterEntity() {
        super("KAMSTRUP_METER_REGISTER");
    }

    public KamstrupMeterRegisterEntity init(KamstrupMeterEntity meterEntity, MeterDao.MeterRegister register) {
        this.meterId.set(meterEntity.meterId.get());
        this.id.set(register.getRegister().id);
        this.name.set(register.getRegister().name);
        this.command.set(register.getRegister().command);
        this.actions.set(register.getRegister().actions);
        this.autoCollect.set(register.isAutoColllect());
        return this;
    }

}
