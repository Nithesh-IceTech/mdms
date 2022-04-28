package za.co.spsi.mdms.kamstrup.services.order;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterOrderEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterReadFailedLog;
import za.co.spsi.mdms.kamstrup.services.group.KamstrupRestService;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.LoggerCommand;
import za.co.spsi.mdms.util.kamstrup.KamstrupRestException;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.ExpiringCacheMap;
import za.co.spsi.toolkit.util.ObjectUtils;
import za.co.spsi.toolkit.util.Processor;
import za.co.spsi.toolkit.util.StringList;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/10/13.
 * service is responsible for update the meter db with the meter info
 */
@Singleton
@DependsOn({"PropertiesConfig"})
@TransactionManagement(value = TransactionManagementType.BEAN)
@Startup
public class FailedMeterReadingProcessingService extends ProcessorService {

    public static final Logger TAG = Logger.getLogger(FailedMeterReadingProcessingService.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    Processor processor = getProcessor();

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    private KamstrupRestService restService;

    @Inject
    private RestHelper restHelper;

    @Inject
    private OrderService orderService;

    private ExpiringCacheMap<String, LoggerCommand> loggerMap = new ExpiringCacheMap<>(TimeUnit.MINUTES.toMillis(30));

    interface Callback<E extends KamstrupMeterReadFailedLog> {
        void process(E entity) throws Exception;
    }

    private <E extends KamstrupMeterReadFailedLog> void process(Callback<E> callback, DataSourceDB<E> ds) throws SQLException {
        for (E order : ds) {
            try {
                callback.process(order);
            } catch (NotLoggerCommandException ex) {
                order.status.set(KamstrupMeterReadFailedLog.Status.IGNORED.getCode());
                DataSourceDB.set(dataSource, order);
            } catch (Exception ex) {
                TAG.log(Level.WARNING, ex.getMessage(), ex);
                order.status.set(KamstrupMeterReadFailedLog.Status.FAILED.getCode());
                order.error.set(ex);
                DataSourceDB.set(dataSource, order);
            }
        }
    }

    private <E extends KamstrupMeterReadFailedLog> void process(KamstrupMeterReadFailedLog.Status status, Callback<E> callback) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                process(callback, (DataSourceDB<E>) KamstrupMeterReadFailedLog.getByStatus(connection, status));
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public static class NotLoggerCommandException extends RuntimeException {

        public NotLoggerCommandException() {
            super();
        }

        public NotLoggerCommandException(ProcessingException pe) {
            super(pe.getMessage());
            setStackTrace(pe.getStackTrace());
        }

    }

    private LoggerCommand getLoggerCommand(String commandRef) {
        try {
            if (!loggerMap.containsKey(commandRef)) {
                loggerMap.put(commandRef, orderService.getPagingService().get(LoggerCommand.class, commandRef));
            }
            if (!loggerMap.containsKey(commandRef)) {
                throw new NotLoggerCommandException();
            }
            return loggerMap.get(commandRef);
        } catch (ProcessingException pe) {
            loggerMap.put(commandRef, null);
            return getLoggerCommand(commandRef);
        }
    }

    private void processLogged() {
        if (!propertiesConfig.getKamstrup_processing_failed_orders_enabled()) return;
        KamstrupMeterReadFailedLog.clearOldLogs(dataSource, propertiesConfig.getKamstrup_processing_failed_orders_days());
        process(KamstrupMeterReadFailedLog.Status.LOGGED, entity -> {
            // update registers and logger id
            LoggerCommand command = getLoggerCommand(entity.commandRef.get());
            entity.status.set(KamstrupMeterReadFailedLog.Status.UPDATED.getCode());
            DataSourceDB.set(dataSource, entity.update(command));
        });
    }

    private void processUpdated() {
        if (!propertiesConfig.getKamstrup_processing_failed_orders_enabled()) return;
        // find all the
        String sql = "select from_date,to_date,logger_id,registers " +
                "from kam_meter_read_failed_log where status = ? group by from_date,to_date,logger_id,registers";
        List<List> values = DataSourceDB.executeQuery(dataSource,
                new Class[]{Timestamp.class, Timestamp.class, String.class,String.class}, sql, KamstrupMeterReadFailedLog.Status.UPDATED.getCode());
        values.stream().forEach(v -> {
            Timestamp fromDate = (Timestamp)v.get(0), toDate = (Timestamp)v.get(1);
            String loggerId = (String)v.get(2), registerId = (String)v.get(3);

            String update = "update kam_meter_read_failed_log set status = ?, error = ? where status = ? and logger_id = ? and registers = ? and " +
                    "from_date = ? and to_date = ? ";
            DataSourceDB.executeUpdate(dataSource, update, KamstrupMeterReadFailedLog.Status.STARTED.getCode(), null,
                    KamstrupMeterReadFailedLog.Status.UPDATED.getCode(), loggerId, registerId, fromDate, toDate);

            StringList meterList = new StringList();
            DataSourceDB.executeQuery(dataSource, result -> meterList.add((result.get(0).toString())), new Class[]{String.class},
                    "select kamstrup_meter.ref from kamstrup_meter,kam_meter_read_failed_log where " +
                            "kam_meter_read_failed_log.status = ? and kam_meter_read_failed_log.meter_id = kamstrup_meter.meter_id",
                    KamstrupMeterReadFailedLog.Status.STARTED.getCode());
            meterList.removeDuplicates();

            KamstrupMeterOrderEntity order = new KamstrupMeterOrderEntity();
            order.status.set(KamstrupMeterOrderEntity.Status.RECEIVED.getCode());

            DataSourceDB.executeInTx(dataSource, connection -> {
                try {
                    Response response = restService.createOrder(connection, new StringList(registerId.split(",")),loggerId,meterList, order, fromDate, toDate);

                    if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                        String linkUpdate = "update kam_meter_read_failed_log set status = ?, error = ?,re_order_id = ? where status = ? and logger_id = ? and registers = ? and " +
                                "from_date = ? and to_date = ? ";

                        DataSourceDB.executeUpdate(connection, linkUpdate, KamstrupMeterReadFailedLog.Status.PROCESSED.getCode(), null,order.meterOrderId.get(),
                                KamstrupMeterReadFailedLog.Status.STARTED.getCode(), loggerId, registerId, fromDate, toDate);
                    } else {
                        throw new KamstrupRestException("Failed to create order for failed log. Failed with code ",response);
                    }
                } catch (Exception ex) {
                    connection.rollback();
                    // update status to failed
                    DataSourceDB.executeUpdate(dataSource, update, KamstrupMeterReadFailedLog.Status.FAILED.getCode(),
                            ObjectUtils.convertStackTraceToString(ex, KamstrupMeterReadFailedLog.ERROR_LEN),
                            KamstrupMeterReadFailedLog.Status.FAILED.getCode(), loggerId, registerId, fromDate, toDate);
                    connection.commit();
                }
            });


        });
    }

    @PostConstruct
    public void startServices() {
        KamstrupMeterOrderEntity.resetProcessState(dataSource, KamstrupMeterOrderEntity.Status.STARTED, KamstrupMeterOrderEntity.Status.READY);
        processor.delay(5).minutes(1).repeat(() -> {
            processLogged();
            processUpdated();
        });
    }

}
