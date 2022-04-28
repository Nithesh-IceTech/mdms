package za.co.spsi.mdms.kamstrup.services.order;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.kamstrup.db.KamstrupGroupEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterOrderEntity;
import za.co.spsi.mdms.kamstrup.services.PagingService;
import za.co.spsi.mdms.kamstrup.services.group.KamstrupRestService;
import za.co.spsi.mdms.kamstrup.services.order.domain.Order;
import za.co.spsi.mdms.kamstrup.services.order.domain.Orders;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/10/13.
 * service is responsible for placing orders from groups
 */
@Singleton
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class KamstrupGroupOrderService extends ProcessorService {

    public static final Logger TAG = Logger.getLogger(KamstrupGroupOrderService.class.getName());

    @Inject
    private PagingService<Orders> pagingService;

    @Inject
    private KamstrupRestService restService;

    @Inject
    private PropertiesConfig propertiesConfig;

    private Processor processor = getProcessor();

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    private void schedule(KamstrupGroupEntity group) {

        if(propertiesConfig.getKamstrup_processing_enabled()) {
            DataSourceDB.executeInTx(dataSource, connection -> {

                Boolean shouldSchedule = true;
                LocalDateTime currentTime = LocalDateTime.now();
                KamstrupMeterOrderEntity order = KamstrupMeterOrderEntity.getInstance(group);

                try {
                    KamstrupMeterOrderEntity prevMeterOrder = DataSourceDB.get(KamstrupMeterOrderEntity.class, dataSource, "select * from kamstrup_meter_order where meter_order_id = ?", group.lastOrderId.get());
                    Assert.notNull(prevMeterOrder.toDate.get(),"Previous Kamstrup Meter Order To Date => Null.");
                    shouldSchedule = KamstrupGroupEntity.shouldSchedule(currentTime, prevMeterOrder.toDate.get(), group.frequencyType.get(), group.frequency.get());
                } catch(Exception ex) {
                    TAG.info(String.format("%s", ex.getMessage()));
                }

                if (shouldSchedule) {
                    Timestamp times[] = group.scheduleTime(dataSource);
                    TAG.info("Creating group order from: " + times[0] + " to: " + times[1]);
                    Response response = restService.createOrder(connection, group, order, times[0], times[1]);
                    if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                        order.init(new OrderDao(new Order(response.getHeaderString("Location"))), pagingService);
                        order.created.set(getSystemTimestampUtc());
                        DataSourceDB.set(connection, order);
                        group.lastOrderId.set(order.meterOrderId.get());
                        DataSourceDB.set(connection, group);
                        KamstrupGroupEntity.LogEntity.create(connection, group, order, response);
                    } else {
                        KamstrupGroupEntity.LogEntity.create(connection, group, null, response);
                    }
                }

            });
        }

    }

    private Timestamp getSystemTimestampUtc() {
        Timestamp tsLocal = new Timestamp(System.currentTimeMillis());
        LocalDateTime tsUtc = LocalDateTime.ofEpochSecond(tsLocal.getTime()/1000, 0,
                ZoneOffset.ofHours(0) )
                .truncatedTo(ChronoUnit.MINUTES);
        return Timestamp.valueOf(tsUtc);
    }

    @PostConstruct
    private void setup() {
        processor.delay(5).seconds(30).repeat(() ->
            KamstrupGroupEntity
                    .getReadyGroups(dataSource, propertiesConfig.getKamstrup_processing_order_schedule_delay())
                    .stream()
                    .forEach(g -> schedule(g)));
    }

}
