package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.mdms.kamstrup.services.meter.MeterDao;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Meter;
import za.co.spsi.mdms.kamstrup.services.meter.domain.MeterDetail;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;
import za.co.spsi.toolkit.entity.ano.Audit;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@Table(version = 11)
@Audit(services = false)
public class KamstrupMeterEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "METER_ID")
    public Field<String> meterId = new Field<>(this);

    @Column(name = "GROUP_ID")
    @ForeignKey(table = KamstrupGroupEntity.class,onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> groupId = new Field<>(this);

    public Field<String> ref = new Field<>(this);
    public Field<String> state = new Field<>(this);

    @Column(name = "VENDOR_ID")
    public Field<String> vendorId = new Field<>(this);

    @Column(name = "SERIAL_N")
    public Field<String> serialN = new Field<>(this);

    @Column(name = "METER_N")
    public Field<String> meterN = new Field<>(this);

    @Column(name = "CONFIG_UPDATED")
    public Field<Timestamp> configurationUpdated = new Field<>(this);

    @Column
    public Field<String> firmware = new Field<>(this);

    @Column(name = "TYPE_DESC")
    public Field<String> typeDescription = new Field<>(this);

    @Column(name = "CONSUMPTION_TYPE")
    public Field<String> consumptionType = new Field<>(this);

    @Column(name = "PROFILE_REF")
    public Field<String> profileRef = new Field<>(this);

    @Column(name = "ROUTES_REF")
    public Field<String> routesRef = new Field<>(this);

    @Column(name = "STATUS_ON", defaultValue = "1")
    public Field<Boolean> statusOn = new Field<>(this);

    @Column(name = "STATUS_OFF", defaultValue = "0")
    public Field<Boolean> statusOff = new Field<>(this);

    @Column(name = "REQUEST_STATUS_ON", defaultValue = "0")
    public Field<Boolean> requestStatusOn = new Field<>(this);

    @Column(name = "REQUEST_STATUS_OFF", defaultValue = "0")
    public Field<Boolean> requestStatusOff = new Field<>(this);

    @Column(name = "FAILED_N", defaultValue = "0", size = 3,decimalPlaces = 0)
    public Field<Integer> failedNumber = new Field<>(this);

    @Column(name = "LAST_COMMS_D")
    public FieldLocalDate<Timestamp> lastCommsD = new FieldLocalDate<>(this);

    @Column(name = "LAST_TOTAL_ENERGY_UPDATE")
    public FieldLocalDate<Timestamp> lastTotalEnergyUpdateTimestamp = new FieldLocalDate<>(this);

    @Column(name = "LAST_T1_ENERGY_UPDATE")
    public FieldLocalDate<Timestamp> lastT1EnergyUpdateTimestamp = new FieldLocalDate<>(this);

    @Column(name = "LAST_T2_ENERGY_UPDATE")
    public FieldLocalDate<Timestamp> lastT2EnergyUpdateTimestamp = new FieldLocalDate<>(this);

    @Column(name = "LAST_VOLTAGE_UPDATE")
    public FieldLocalDate<Timestamp> lastVoltageUpdateTimestamp = new FieldLocalDate<>(this);

    @Column(name = "LAST_CURRENT_UPDATE")
    public FieldLocalDate<Timestamp> lastCurrentUpdateTimestamp = new FieldLocalDate<>(this);

    public Index idxRef = new Index("IDX_KM_REF",this,ref).setUnique();
    private Index idxGroupId= new Index("idx_GROUP_ID", this, groupId);

    // prepaid / postpaid

    @Column(name = "METER_TYPE")
    public Field<String> meterType = new Field<>(this);

    public EntityRef<KamstrupGroupEntity.GroupMeter> meterGroups = new EntityRef<>(this);

    public EntityRef<KamstrupGroupEntity> group = new EntityRef<>("select kamstrup._group from kamstrup_group,kamstrup_group_meter where " +
            "kamstrup_group.group_id = kamstrup_group_meter.group_id and kamstrup_group_meter.meter_id = ?",this);

    public KamstrupMeterEntity() {
        super("KAMSTRUP_METER");
    }

    public KamstrupMeterEntity init(Meter meter) {
        this.ref.set(meter.ref);
        this.state.set(meter.state);
        this.serialN.set(meter.serialNumber);
        this.meterN.set(meter.meterNumber);
        return this;
    }

    public KamstrupMeterEntity init(MeterDetail detail) {
        this.ref.set(detail.ref);
        this.state.set(detail.state);
        this.vendorId.set(detail.vendorId);
        this.serialN.set(detail.serialNumber);
        this.meterN.set(detail.meterNumber);
        this.configurationUpdated.set(detail.configurationUpdated);
        this.typeDescription.set(detail.typeDescription);
        this.consumptionType.set(detail.consumptionType);
        this.profileRef.set(detail.profileRef != null?detail.profileRef.ref:null);
        this.routesRef.set(detail.routesRef != null?detail.routesRef.ref:null);
        this.firmware.set(detail.firmware);
        return this;
    }

    public void clearRegisters(DataSource ds) {
        DataSourceDB.execute(ds,"delete from KAMSTRUP_METER_REGISTER where METER_ID = ?",meterId.get());
    }

    public void updateRegisters(DataSource dataSource, List<MeterDao.MeterRegister> registers) {
            clearRegisters(dataSource);
            registers.stream().forEach(c -> {
                DataSourceDB.set(dataSource,new KamstrupMeterRegisterEntity().init(KamstrupMeterEntity.this,c));
            });
    }

    public static String getRefNoFromRef(String ref) {
        return ref.substring(ref.toLowerCase().indexOf("utilidriver/api/meters/")+"utilidriver/api/meters/".length(),
                ref.lastIndexOf("/"));
    }

    // http://196.44.197.67/utilidriver/api/meters/4B414DFF0201260BA9/
    /**
     * ip address might differ
     * @param dataSource
     * @param ref
     * @return
     */
    public static KamstrupMeterEntity findMeterByRef(DataSource dataSource, String ref) {
        return DataSourceDB.get(KamstrupMeterEntity.class,dataSource,
                String.format("select * from kamstrup_meter where ref like '%%%s%%'",getRefNoFromRef(ref)));
    }

    /**
     * @param dataSource
     * @param serialN
     * @return
     */
    public static KamstrupMeterEntity findMeterBySerialN(DataSource dataSource, String serialN) {
        return DataSourceDB.get(KamstrupMeterEntity.class,dataSource,
                String.format("select * from kamstrup_meter where serial_n = '%s'",serialN));
    }

    public boolean isWater() {
        return consumptionType.getNonNull().toLowerCase().contains("water");
    }

    public static void main(String args[]) {
        System.out.println(getRefNoFromRef("http://196.44.197.67/utilidriver/api/meters/4B414DFF0201260BA9/"));
    }
}
