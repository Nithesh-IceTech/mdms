package za.co.spsi.mdms.common.db.generator;

import lombok.SneakyThrows;
import za.co.spsi.mdms.common.db.survey.SharedLocation;
import za.co.spsi.mdms.common.generator.WinSmsHelper;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.util.ExpiringCacheMap;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdbijl on 2017/07/26.
 */
@Audit
@Table(version = 0)
public class GeneratorEntity extends AuditEntityDB {


    @Id(uuid = true)
    public Field<String> id = new Field<>(this);

    @Column(name = "serial_n", size = 50)
    public Field<String> serialN = new Field<>(this);
    public Field<String> msisdn = new Field<>(this);

    public Field<String> make = new Field<>(this);
    public Field<String> model = new Field<>(this);
    public Field<String> description = new Field<>(this);

    public SharedLocation sharedLocation = new SharedLocation(this);

    public FieldTimestamp created = new FieldTimestamp(this);
    public FieldTimestamp updated = new FieldTimestamp(this).onUpdate();

    // current state
    @Column(name = "state_on")
    public Field<Boolean> stateOn = new Field<>(this);

    @Column(name = "enabled",defaultValue = "1")
    public Field<Boolean> enabled = new Field<>(this);

    @Column(name = "last_status_request")
    public Field<Timestamp> lastStatusRequest = new Field<>(this);

    @Column(name = "latest_tx_id", size = 50)
    @ForeignKey(table= GeneratorTransactionEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> latestTxId= new Field<>(this);

    public Index idxEnabled = new Index("genEnabledIdx",this,enabled);

    public EntityRef<GeneratorTransactionEntity> latestTxRef = new EntityRef<>(latestTxId,this);

    public EntityRef<MeterLink> meterLinkRef = new EntityRef<>(this);


    public GeneratorEntity() {
        super("generator");
    }

    private static ExpiringCacheMap<String,GeneratorEntity> genMap = new ExpiringCacheMap<>(TimeUnit.MINUTES.toMillis(1));

    @SneakyThrows
    public static GeneratorEntity getActiveGeneratorForMeter(Connection connection,String meterId) {
        if (!genMap.containsKey(meterId)) {
            Driver driver = DriverFactory.getDriver();
            String query = String.format("select generator.* from generator,gen_meter_link where " +
                    "generator.enabled = %s and generator.id = gen_meter_link.gen_id and " +
                    "(kam_meter_id = ? or nes_meter_id = ? or els_meter_id = ?)",
                    driver.isOracle() ? driver.boolToNumber(true) : "true");
            genMap.put(meterId,DSDB.get(GeneratorEntity.class, connection, query, meterId, meterId, meterId));
        }
        return genMap.get(meterId);
    }

    public static DataSourceDB<GeneratorEntity> getStatusRequests(Connection connection) {
        Driver driver = DriverFactory.getDriver();

        String query = String.format("select * from generator where state_on = %s " +
                "and (last_status_request is null or last_status_request < current_timestamp %s ) ",
                driver.isOracle() ? driver.boolToNumber(true) : "true",
                driver.subtractTimezoneOffset());
        return new DataSourceDB<>(GeneratorEntity.class).getAll(connection, query);
    }

    public boolean isOn() {
        return stateOn.getNonNull();
    }

    @SneakyThrows
    public GeneratorTransactionEntity getLastTxBefore(Connection connection,Timestamp timestamp) {
        Driver driver = DriverFactory.getDriver();
        String query = String.format("select * from gen_transaction where gen_id = ? " +
                "and tx_end > %s order by tx_end asc", driver.toDate(timestamp.toLocalDateTime()));
        return DSDB.get(GeneratorTransactionEntity.class,connection
                , driver.limitSql(query,2),id.get());
    }

    public CommunicationLogEntity sendStatus(javax.sql.DataSource dataSource,String sourceMsisdn) {
        lastStatusRequest.set(new Timestamp(System.currentTimeMillis()));
        DataSourceDB.set(dataSource,this);
        return CommunicationLogEntity.send(dataSource,this,new WinSmsHelper.Sms(msisdn.get(),sourceMsisdn,"TEST GSMC",new java.util.Date()));
    }

    public CommunicationLogEntity sendSyncTime(javax.sql.DataSource dataSource,String sourceMsisdn) {
        return CommunicationLogEntity.send(dataSource,this,new WinSmsHelper.Sms(msisdn.get(),sourceMsisdn,"SETTIME",new java.util.Date()));
    }

    @Table(version = 0)
    public static class MeterLink extends EntityDB {

        @Id(uuid = true)
        public Field<String> id = new Field<>(this);

        @Column(name = "gen_id", size = 50)
        @ForeignKey(table= GeneratorEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
        public Field<String> generatorId= new Field<>(this);

        @Column(name = "kam_meter_id", size = 50)
        @ForeignKey(table= KamstrupMeterEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
        public Field<String> kamMeterId= new Field<>(this);

        @Column(name = "nes_meter_id", size = 50)
        @ForeignKey(table= NESMeterEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
        public Field<String> nesMeterId= new Field<>(this);

        @Column(name = "els_meter_id", size = 50)
        @ForeignKey(table= ElsterMeterEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
        public Field<String> elsMeterId= new Field<>(this);

        @Column(name = "generic_meter_id", size = 50)
        @ForeignKey(table= za.co.spsi.mdms.generic.meter.db.GenericMeterEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
        public Field<String> genericMeterId= new Field<>(this);

        public FieldTimestamp created = new FieldTimestamp(this);

        public MeterLink() {
            super("gen_meter_link");
        }

    }

}
