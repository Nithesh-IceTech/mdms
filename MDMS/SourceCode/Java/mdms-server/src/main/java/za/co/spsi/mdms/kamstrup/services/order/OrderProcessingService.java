package za.co.spsi.mdms.kamstrup.services.order;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterOrderEntity;
import za.co.spsi.mdms.kamstrup.processor.KamstrupProcessor;
import za.co.spsi.mdms.kamstrup.services.meter.KamstrupMeterService;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Meters;
import za.co.spsi.mdms.kamstrup.services.order.domain.MeterResult;
import za.co.spsi.mdms.kamstrup.services.order.domain.MeterResults;
import za.co.spsi.mdms.kamstrup.services.order.domain.OrderDetail;
import za.co.spsi.mdms.kamstrup.services.order.domain.OrderExecutionStatus;
import za.co.spsi.mdms.util.MeterFilterService;
import za.co.spsi.mdms.util.XmlHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/10/13.
 * service is responsible for update the meter db with the meter info
 */
@Singleton
@Startup
@DependsOn({"PropertiesConfig"})
@TransactionManagement(value = TransactionManagementType.BEAN)
public class OrderProcessingService extends ProcessorService {

    public static final Logger TAG = Logger.getLogger(OrderProcessingService.class.getName());

    @Inject
    private OrderService orderService;

    @Inject
    private KamstrupMeterService meterService;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    @ConfValue(value = "order_service.error_retry", folder = "server")
    private Integer errorRetry;

    @Inject
    @ConfValue(value = "kamstrup.processing.enabled", folder = "server", defaultValue = "true")
    private boolean processingEnabled;

    @Inject
    @ConfValue(value = "kamstrup.processing.error_reprocessing.enabled", folder = "server",defaultValue = "true")
    private boolean errorReProcessingEnabled;

    @Inject
    private RestHelper restHelper;

    @Inject
    @ConfValue(value = "order_service.error_retry_time", folder = "server")
    private Integer errorRetryTimeout;

    @Inject
    private KamstrupProcessor kamstrupProcessor;

    @Inject
    private MeterFilterService meterFilterService;

    @Inject
    private PropertiesConfig propertiesConfig;

    private Processor processor = getProcessor(), syncProcessor = getProcessor();

    interface Callback<E extends KamstrupMeterOrderEntity> {
        void process(E entity) throws Exception;
    }

    private <E extends KamstrupMeterOrderEntity> void process(Callback<E> callback, DataSourceDB<E> ds) throws SQLException {
        for (E order : ds) {
            try {
                callback.process(order);
            } catch (Exception ex) {
                TAG.log(Level.WARNING, ex.getMessage(), ex);
                order.status.set(KamstrupMeterOrderEntity.Status.FAILED.getCode());
                order.error.set(ex);
                DataSourceDB.set(dataSource, order);
            }
        }
    }

    private <E extends KamstrupMeterOrderEntity> void process(KamstrupMeterOrderEntity.Status status, int limit,Callback<E> callback) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                process(callback, (DataSourceDB<E>) KamstrupMeterOrderEntity.getByStatus(connection, status,limit));
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    private void processStarted() {

        if(propertiesConfig.getKamstrup_processing_enabled()) {

            TAG.info("Process Started");
            AtomicInteger index = new AtomicInteger(0);
            process(KamstrupMeterOrderEntity.Status.STARTED, 1500,(order) -> {
                TAG.info("Process Started " + index.incrementAndGet() + ": " + order.ref.get());
                MeterResults meterResults = XmlHelper.unmarshall(MeterResults.class, order.data.getInflatedString());

                DataSourceDB.executeInTx(dataSource, connection -> {
                    for (MeterResult r : meterResults.meterResult) {
                        KamstrupMeterEntity meterEntity = KamstrupMeterEntity.findMeterByRef(dataSource, r.meter.meter);
                        Assert.notNull(meterEntity, "Could not find meter from ref %s", r.meter.meter);

                        if (meterFilterService.shouldProcessMeter(meterEntity.serialN.get())) {
                            KamstrupMeterOrderEntity.MeterHistory.create(connection,order,meterEntity);
                            kamstrupProcessor.process(connection, order, meterEntity, r);
                            DataSourceDB.set(connection, meterEntity);
                        }
                    }
                });
                order.updateState(dataSource, KamstrupMeterOrderEntity.Status.COMPLETED);
            });

        }

    }

    /**
     *
     */
    private void processReady() {

        if(propertiesConfig.getKamstrup_processing_enabled()) {

            TAG.info("Process Ready");
            AtomicInteger index = new AtomicInteger(0);
            process(KamstrupMeterOrderEntity.Status.READY,1500, (order) -> {
                TAG.info("Process Ready " + index.incrementAndGet() + ": " + order.ref.get());
                // check if the order should be rejected

                OrderDetail detail = orderService.getPagingService().get(OrderDetail.class, order.ref.get());
                order.initOrderDetail(detail);

                if (detail.commands.hasLoggerReadCommand() || !detail.commands.hasBreakerReadCommand()) {
                    // ensure all the meters have been captured
                    order.updateState(dataSource, KamstrupMeterOrderEntity.Status.STARTED);

                    meterService.updateMeters(Arrays.asList(new Meters[]{detail.subjects.getMetersFromSource(restHelper)}), false,false);
                    // process the meter results
                    Response response = orderService.getPagingService().get(order.completedRef.get(), true);
                    // check that all the data is there
                    order.data.setAndDeflate(response.readEntity(String.class).getBytes());
                    order.updateState(dataSource, KamstrupMeterOrderEntity.Status.STARTED);

                    // validate data
                    XmlHelper.unmarshall(MeterResults.class, order.data.getInflatedString());
                } else {
                    order.updateState(dataSource, KamstrupMeterOrderEntity.Status.REJECTED);
                }
            });

        }

    }

    private void processWaiting() {

        if (propertiesConfig.getKamstrup_processing_enabled()) {

            TAG.info("Process Waiting");

            AtomicInteger index = new AtomicInteger(0);
            process(KamstrupMeterOrderEntity.Status.RECEIVED, 1500,(order) -> {
                TAG.info("Process Waiting " + index.incrementAndGet() + ": " + order.ref.get());
                OrderExecutionStatus status = orderService.getPagingService().get(OrderExecutionStatus.class, order.statusRef.get());
                order.init(status);
                order.status.set(status.isCompleted() ? KamstrupMeterOrderEntity.Status.READY.getCode() : order.status.get());
                DataSourceDB.set(dataSource, order);
            });

        }

    }

    private void insertOrder(OrderDao order) {
        KamstrupMeterOrderEntity orderEntity = KamstrupMeterOrderEntity.findMeterOrderByRef(dataSource, order.getOrder().ref);
        // only insert orders from mapped meters
        if (orderEntity == null) {
            DataSourceDB.set(dataSource, new KamstrupMeterOrderEntity().init(order, orderService.getPagingService()));
        }
    }

    public void reprocessFailed() {
        if (processingEnabled) {
            syncProcessor.delay(5).minutes(15).repeat(() -> {
                TAG.info("Sync Kamstrup Orders");
                new OrderDaoList(orderService.getOrders()).stream().forEach(orderDO -> insertOrder(orderDO));
            });
        }
    }


    public void scheduleOrders() {
        if (processingEnabled) {
            syncProcessor.delay(5).minutes(15).repeat(() -> {
                TAG.info("Sync Kamstrup Orders");
                new OrderDaoList(orderService.getOrders()).stream().forEach(orderDO -> insertOrder(orderDO));
            });
        }
    }

    @PostConstruct
    public void startServices() {
        KamstrupMeterOrderEntity.resetProcessState(dataSource, KamstrupMeterOrderEntity.Status.STARTED, KamstrupMeterOrderEntity.Status.READY);
        scheduleOrders();
        processor.delay(5).minutes(5).repeat(() -> {
            processWaiting();
            processReady();
            processStarted();
            if (errorReProcessingEnabled) {
                KamstrupMeterOrderEntity.rescheduleFailed(dataSource, errorRetry, errorRetryTimeout);
            }
        });
    }

}
