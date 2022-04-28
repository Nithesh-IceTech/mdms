package za.co.spsi.mdms.kamstrup.services.group;

import com.vaadin.ui.Notification;
import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.db.KamstrupGroupEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterOrderEntity;
import za.co.spsi.mdms.kamstrup.services.PagingService;
import za.co.spsi.mdms.kamstrup.services.group.domain.Groups;
import za.co.spsi.mdms.kamstrup.services.order.OrderDao;
import za.co.spsi.mdms.kamstrup.services.order.domain.Order;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.properties.TextFile;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by jaspervdbijl on 2017/01/11.
 */
@Dependent
public class KamstrupRestService {

    private SimpleDateFormat dateTimeFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Inject
    @TextFile("kamstrup/create_order.xml")
    private String createOrderXml;

    @Inject
    @TextFile("kamstrup/create_order_no_logger.xml")
    private String createOrderXmlNoLogger;

    @Inject
    @TextFile("kamstrup/create_meter_order.xml")
    private String createMeterOrderXml;

    @Inject
    @TextFile("kamstrup/create_meter_order_no_logger.xml")
    private String createMeterOrderXmlNoLogger;

    @Inject
    @ConfValue(folder = "server",value = "utilitydriver.uri")
    private String utilityDriverUri;

    @Inject
    private PagingService<Groups> pagingService;

    @Inject
    RestHelper restHelper;

    public List<Groups> getGroups() {
        return pagingService.getObjects("groups",Groups.class);
    }

    public Response createGroup(DataSource dataSource, KamstrupGroupEntity group) {
        Response response = restHelper.restPost("<Group><Name>"+group.name.get()+"</Name></Group>","groups/");
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            // set the ref
            group.ref.set(response.getHeaderString("Location"));
            DataSourceDB.set(dataSource,group);
        }
        return response;
    }

    public Response deleteGroup(KamstrupGroupEntity group) {
        return restHelper.restDelete(group.ref.get());
    }

    public Response allocateMeter(KamstrupGroupEntity group, KamstrupMeterEntity meter) {
        return restHelper.restPost(String.format("<Meters><Meter ref=\"%s\"/></Meters>",meter.ref.get()),
                group.ref.get().substring(group.ref.get().indexOf("/api/")+"/api/".length()),"meters/");
    }

    public Response deleteMeter(KamstrupGroupEntity group, KamstrupMeterEntity meter) {
        return restHelper.restDelete(group.ref.get()+"/meters/?id="+meter.ref.get());
    }

    private String getXml(KamstrupGroupEntity group,String logger) {
        return group != null?
                StringUtils.isEmpty(logger)?createOrderXmlNoLogger:createOrderXml:
                StringUtils.isEmpty(logger)?createMeterOrderXmlNoLogger:createMeterOrderXml;
    }

    private Response createOrder(Connection connection, KamstrupGroupEntity group, StringList meters,
                                StringList registries,String loggerId,
                                KamstrupMeterOrderEntity orderEntity, Timestamp fromDate, Timestamp toDate) {
        String createXml = getXml(group,loggerId).
                replace("_GROUP_REF_",group != null && meters == null?group.ref.get():"").
                replace("_METERS_",meters != null?meters.prepend("<Meter ref='").append("' />").toString("\n\t"):"").
                replace("_LOG_ID_",loggerId != null?loggerId:"").
                replace("_FROM_DATE_",dateTimeFormat.format(fromDate)).replace("_TO_DATE_",dateTimeFormat.format(toDate)).
                replace("_REGISTER_",registries.map(value -> String.format("<Register id='%s' />",value)).toString("\n"));
        Response response = restHelper.restPost(createXml,"orders/");
        if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
            orderEntity.init(new OrderDao(new Order(response.getHeaderString("Location"))),pagingService);
            orderEntity.fromDate.set(Timestamp.valueOf(fromDate.toLocalDateTime().plusMinutes(1)));
            orderEntity.toDate.set(toDate);
            DataSourceDB.set(connection,orderEntity);
        }
        return response;
    }

    public Response createOrder(Connection connection, KamstrupGroupEntity group, KamstrupMeterOrderEntity orderEntity, Timestamp fromDate, Timestamp toDate) {
        return createOrder(connection,group,null,
                group.registries.getAllAsList(connection,null).stream().map(r -> r.registerId.get()).collect(Collectors.toCollection(StringList::new)),
                group.loggerId.get(),
                orderEntity,fromDate,toDate);
    }

    public Response createOrder(Connection connection, StringList registers,String loggerId,StringList meters, KamstrupMeterOrderEntity orderEntity, Timestamp fromDate, Timestamp toDate) {
        return createOrder(connection,null,meters,registers,loggerId,orderEntity,fromDate,toDate);
    }

    public boolean executeRequest(Response.StatusType statusType, Callable<Response> callable, String msg) {
        try {
            Response response = callable.call();
            if (response.getStatus() != statusType.getStatusCode()) {
                Notification.show(msg + " failed. Http status " + response.getStatus(), Notification.Type.ERROR_MESSAGE);
            } else {
                Notification.show(msg, Notification.Type.TRAY_NOTIFICATION);
            }
            return response.getStatus() == statusType.getStatusCode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
