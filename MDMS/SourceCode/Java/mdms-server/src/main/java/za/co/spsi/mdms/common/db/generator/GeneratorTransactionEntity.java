package za.co.spsi.mdms.common.db.generator;

import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.generator.msg.GeneratorMessage;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import static za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity.Status.Active;

/**
 * Created by jaspervdbijl on 2017/07/26.
 */
@Table(version = 0)
public class GeneratorTransactionEntity extends EntityDB {


    public enum Status {
        Active(1), Cancelled(2);
        public int code;

        Status(int code) {
            this.code = code;
        }
    }

    @Id(uuid = true)
    public Field<String> id = new Field<>(this);

    @Column(name = "tx_start")
    public FieldLocalDate<Timestamp> txStart = new FieldLocalDate<>(this);

    @Column(name = "tx_end")
    public FieldLocalDate<Timestamp> txEnd = new FieldLocalDate<>(this);

    @Column(name = "gen_id", size = 50)
    @ForeignKey(table = GeneratorEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> genId = new Field<>(this);

    @Column(name = "start_comms_id", size = 50)
    @ForeignKey(table = CommunicationLogEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> startCommsId = new Field<>(this);

    @Column(name = "end_comms_id", size = 50)
    @ForeignKey(table = CommunicationLogEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> endCommsId = new Field<>(this);

    @Column(defaultValue = "1")
    public Field<Integer> status = new Field<>(this);

    @Column(name = "cancel_message", size = 1024, autoCrop = true)
    public Field<String> cancelMessage = new Field<>(this);

    public EntityRef<GeneratorEntity> generatorRef = new EntityRef<>(genId, this);

    public Index idx = new Index("GEN_TX_IDX", this, status, txStart, txEnd);

    public GeneratorTransactionEntity() {
        super("gen_transaction");
    }

    public boolean isClosed() {
        return endCommsId.get() != null || status.getNonNull() == Status.Cancelled.code;
    }

    public boolean isActive() {
        return status.getNonNull() == Active.code;
    }

    // de normalize all the links
    @Override
    public void afterInsertEvent(Connection connection) {
        super.afterInsertEvent(connection);
        // create the mapping links
        for (GeneratorEntity.MeterLink link : generatorRef.getOne(connection).meterLinkRef.get(connection)) {
            DataSourceDB.set(connection, new DetailLine(this, link));
        }
    }

    public static MeterReadingEntity updateForTx(Connection connection, MeterReadingEntity reading) {
        GeneratorEntity generator = GeneratorEntity.getActiveGeneratorForMeter(connection, reading.getMeterId());
        if (generator != null) {
            GeneratorTransactionEntity genTx = GeneratorTransactionEntity.getActive(connection, reading);
            if (genTx != null) {
                reading.mapTotalToXAndClearY(genTx.id.get(), "t2", "t1");
            } else {
                reading.mapTotalToXAndClearY(generator.id.get(), "t1", "t2");
            }
        }
        return reading;
    }

    public static GeneratorTransactionEntity getActive(Connection connection, MeterReadingEntity meterReadingEntity) {
        try {
            return DataSourceDB.get(GeneratorTransactionEntity.class, connection,
                    "select gen_transaction.* from gen_transaction,gen_tx_detail where gen_transaction.id = gen_tx_detail.gen_tx_id and " +
                            "gen_transaction.status = ? and gen_transaction.tx_start <= ? and " +
                            "(gen_transaction.tx_end is null or gen_transaction.tx_end >= ?) and " +
                            "(gen_tx_detail.kam_meter_id = ? or gen_tx_detail.nes_meter_id = ? or gen_tx_detail.els_meter_id = ? )", Active.code,
                    meterReadingEntity.entryTime.get(), meterReadingEntity.entryTime.get(),
                    meterReadingEntity.kamMeterId.get() != null ? meterReadingEntity.kamMeterId.get() : "",
                    meterReadingEntity.nesMeterId.get() != null ? meterReadingEntity.nesMeterId.get() : "",
                    meterReadingEntity.elsterMeterId.get() != null ? meterReadingEntity.elsterMeterId.get() : "");
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    /**
     * close the tx, update all the references meter readings
     *
     * @Column(name = "T1_KWHN")
     * @Column(name = "T1_KVARP")
     * @Column(name = "T1_KVARN")
     * @Column(name = "T2_KWHP")
     * @Column(name = "T2_KWHN")
     * @Column(name = "T2_KVARP")
     * @Column(name = "T2_KVARN")
     * public Field<Double> t2KVarN = new Field<>(this);
     */
    public void close(Connection connection) {
        try {
            DataSourceDB.executeUpdate(connection,
                    "update meter_reading set t2_kwhp = t1_kwhp,t2_kwhn = t1_kwhn,t2_kvarp = t1_kvarp,t2_kvarn = t1_kvarn where gen_tx_id = ?", id.get());
            DataSourceDB.executeUpdate(connection,
                    "update meter_reading set t1_kwhp = null,t1_kwhn = null,t1_kvarp = null,t1_kvarn = null where gen_tx_id = ?", id.get());
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public void close(Connection connection, CommunicationLogEntity log, GeneratorMessage msg) {
        endCommsId.set(log.id.get());
        txEnd.setLocal(msg.getReceiveTime(log.received.get()));
        DataSourceDB.set(connection, this);
//        close(connection);
    }

    @Table(version = 0)
    public static class DetailLine extends EntityDB {

        @Id(uuid = true)
        public Field<String> id = new Field<>(this);

        @Column(name = "gen_tx_id", size = 50)
        @ForeignKey(table = GeneratorTransactionEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
        public Field<String> genTxId = new Field<>(this);

        @Column(name = "kam_meter_id", size = 50)
        @ForeignKey(table = KamstrupMeterEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
        public Field<String> kamMeterId = new Field<>(this);

        @Column(name = "nes_meter_id", size = 50)
        @ForeignKey(table = NESMeterEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
        public Field<String> nesMeterId = new Field<>(this);

        @Column(name = "els_meter_id", size = 50)
        @ForeignKey(table = ElsterMeterEntity.class, onDeleteAction = ForeignKey.Action.SetNull)
        public Field<String> elsMeterId = new Field<>(this);

        public Index idx = new Index("GEN_TX_DETAIL_IDX", this, genTxId, kamMeterId, nesMeterId, elsMeterId);

        public DetailLine() {
            super("gen_tx_detail");
        }

        public DetailLine(GeneratorTransactionEntity genTx, GeneratorEntity.MeterLink meterLink) {
            this();
            genTxId.set(genTx.id.get());
            kamMeterId.set(meterLink.kamMeterId.get());
            nesMeterId.set(meterLink.nesMeterId.get());
            elsMeterId.set(meterLink.elsMeterId.get());
        }
    }
}
