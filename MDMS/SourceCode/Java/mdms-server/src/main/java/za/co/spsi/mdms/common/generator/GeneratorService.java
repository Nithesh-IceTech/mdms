package za.co.spsi.mdms.common.generator;

import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.generator.CommunicationLogEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorTransactionDetailView;
import za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity;
import za.co.spsi.mdms.common.generator.msg.G1Msg;
import za.co.spsi.mdms.common.generator.msg.GeneratorMessage;
import za.co.spsi.mdms.common.generator.msg.MessageFactory;
import za.co.spsi.mdms.common.generator.msg.StatusMsg;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.ObjectUtils;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.mdms.common.db.generator.CommunicationLogEntity.Status.*;
import static za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity.Status.Cancelled;

/**
 * Created by jaspervdbijl on 2017/07/26.
 */
@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class GeneratorService extends ProcessorService {

    public static final Logger TAG = Logger.getLogger(GeneratorService.class.getName());

    @Inject
    @ConfValue(value = "local_dialing_code", folder = "server")
    private String localDialingCode;

    @Inject
    private WinSmsHelper smsHelper;

    @Inject
    private PropertiesConfig propertiesConfig;

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    private List<GeneratorTransactionDetailView> openTx = new ArrayList<>();
    private Object txSyncObject = UUID.randomUUID().toString();
    private Processor processor = getProcessor();

    // expected format for
    private void verifySourceMsisdn(GeneratorEntity generator, CommunicationLogEntity log) {
        Assert.isTrue(
                (generator.msisdn.get().startsWith("0") ?
                        localDialingCode + generator.msisdn.get().substring(1) : generator.msisdn.get()).
                        equals(log.cli.get()), String.format("Generator missdn mismatch [%s] [%s]", generator.msisdn.get(),
                        log.cli.get()));
    }

    private GeneratorEntity getGenerator(Connection connection, CommunicationLogEntity log, GeneratorMessage msg) {
        GeneratorEntity generator = DataSourceDB.getFromSet(connection, (GeneratorEntity) new GeneratorEntity().serialN.set(msg.getSerialNo()));
        Assert.isTrue(generator != null, "Generator not found. Serial no " + msg.getSerialNo());
        verifySourceMsisdn(generator, log);
        log.generatorId.set(generator.id.get());
        DataSourceDB.set(connection, log);
        return generator;
    }

    private void process(Connection connection, CommunicationLogEntity log, StatusMsg msg) throws SQLException {
        // check that the generate is still on
        GeneratorEntity generator = getGenerator(connection, log, msg);
        if (!msg.isOn() && generator.isOn()) {
            // close the tx
            GeneratorTransactionEntity tx = generator.latestTxRef.getOne(connection);
            if (tx != null && !tx.isClosed() && log.received.get().compareTo(tx.txStart.get()) > 0) {
                tx.close(connection, log, msg);
                DataSourceDB.set(connection, generator.stateOn.set(false));
            }
        }
    }

    private void process(Connection connection, CommunicationLogEntity log, G1Msg msg) throws SQLException {
        try {
            GeneratorEntity generator = getGenerator(connection, log, msg);
            GeneratorTransactionEntity tx = generator.latestTxRef.getOne(connection);
            if (!msg.isValidDate() || Math.abs(msg.getReceiveTime().getTime() - log.received.get().getTime()) > TimeUnit.MINUTES.toMillis(30)) {
                generator.sendSyncTime(dataSource, smsHelper.getWinSmsMsisdn());
            }
            if (msg.isOn()) {
                if (tx == null || tx.isClosed()) {
                    tx = new GeneratorTransactionEntity();
                    tx.genId.set(generator.id.get());
                    tx.startCommsId.set(log.id.get());
                    tx.txStart.setLocal(msg.isValidDate() ? msg.getReceiveTime() : log.received.get());
                    DataSourceDB.set(connection, tx);
                    generator.stateOn.set(true);
                    generator.latestTxId.set(tx.id.get());
                    DataSourceDB.set(connection, generator);
                } else if (tx != null) {
                    tx.status.set(Cancelled.code);
                    tx.cancelMessage.set("Tx Cancelled. Start received out of order. Log Ref " + log.id.get());
                    DataSourceDB.set(connection, tx);
                    process(connection, log, msg);
                }
            } else {
                if (tx != null && !tx.isClosed()) {
                    tx.close(connection, log, msg);
                    DataSourceDB.set(connection, generator.stateOn.set(false));
                } else {
                    // ignore
                    TAG.info("Received multiple tx close. Ignoring. Comms Ref " + log.id.get());
                    DataSourceDB.set(connection, (EntityDB) log.message.set("Received multiple tx close. Ignoring. Comms Ref"));
                }
            }
            log.status.set(Processed.code);
            DataSourceDB.set(connection, log);
            connection.commit();
        } catch (Exception ex) {
            TAG.log(Level.WARNING, ex.getMessage(), ex);
            connection.rollback();
            log.error.set(ex);
            log.status.set(Failed.code);
            DataSourceDB.set(connection, log);
            connection.commit();
        }
    }

    private void process() {

        if(!propertiesConfig.getGenerator_service_enabled()) {
            TAG.info("Generator Services Disabled.");
            return;
        }

        TAG.info("Looking for sms");
        CommunicationLogEntity.getReceived(dataSource).stream().forEach( log ->
                DataSourceDB.executeInTx(dataSource, true, log,
                        connection -> {
                            log.status.set(Processed.code);
                            Optional<GeneratorMessage> message = MessageFactory.getMessage(log.data.get());

                            if (message.isPresent() && message.get().getClass().equals(G1Msg.class)) {
                                process(connection, log, (G1Msg) message.get());
                            } else if (message.isPresent() && message.get().getClass().equals(StatusMsg.class)) {
                                process(connection, log, (StatusMsg) message.get());
                            } else {
                                log.status.set(NoProcessorFound.code);
                            }
                        }, connection -> log.status.set(Failed.code)));
        // send status requests
        if(!propertiesConfig.getGenerator_status_msg_enabled()) {
            TAG.info("Generator Status Msg Disabled.");
        }else {
            DataSourceDB.executeInTx(dataSource, connection -> {
                for (GeneratorEntity generator : GeneratorEntity.getStatusRequests(connection)) {
                    generator.sendStatus(dataSource, smsHelper.getWinSmsMsisdn());
                }
            });
        }
        // send outgoing
        CommunicationLogEntity.getSend(dataSource).stream().forEach(
                log -> DataSourceDB.executeInTx(dataSource, false, log,
                        connection -> {
                            smsHelper.sendSms(log.sc.get(), log.data.get());
                            log.sent.set(new Timestamp(System.currentTimeMillis()));
                            log.status.set(Processed.code);
                        }, connection -> log.status.set(Failed.code)));
    }

    public boolean isGeneratorTx(MeterReadingEntity reading) {
        synchronized (txSyncObject) {
            return openTx.stream().filter(tx -> tx.txStart.get().compareTo(reading.entryTime.get()) <= 0 &&
                    (tx.txEnd.get() == null || tx.txEnd.get().compareTo(reading.entryTime.get()) >= 0) &&
                    (ObjectUtils.equals(tx.kamMeterId.get(), reading.kamMeterId.get()) ||
                            ObjectUtils.equals(tx.nesMeterId.get(), reading.nesMeterId.get()))).findAny().isPresent();
        }
    }

    @PostConstruct
    public void schedule() {
        TAG.info(String.format("Generator Services %s",
                propertiesConfig.getGenerator_service_enabled() ? "Enabled" : "Disabled"  ));
        processor.delay(5).seconds(15).repeat(() -> process());
    }

}
