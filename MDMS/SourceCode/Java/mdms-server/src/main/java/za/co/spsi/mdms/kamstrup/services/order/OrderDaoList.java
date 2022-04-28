package za.co.spsi.mdms.kamstrup.services.order;

import za.co.spsi.mdms.kamstrup.services.PagingService;
import za.co.spsi.mdms.kamstrup.services.order.domain.OrderDetail;
import za.co.spsi.mdms.kamstrup.services.order.domain.Orders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jaspervdb on 2016/10/13.
 */
public class OrderDaoList extends ArrayList<OrderDao> {

    public OrderDaoList() {}


    public OrderDaoList(List<Orders> ordersList) {
        ordersList.stream().forEach(orders -> Arrays.asList(orders.orders).stream().forEach(
                o->  add(new OrderDao(o))
        ));
    }

    public OrderDaoList getMeterReadingOrders(PagingService pagingService) {
        OrderDaoList orders = new OrderDaoList();
        stream().forEach(o -> {
            OrderDetail detail = o.getDetail(pagingService);
            if (detail.commands.loggerCommands != null) {
                orders.add(o);
            }
        });
        return orders;
    }

}
