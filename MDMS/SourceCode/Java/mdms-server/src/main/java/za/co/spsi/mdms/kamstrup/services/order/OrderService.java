package za.co.spsi.mdms.kamstrup.services.order;

import za.co.spsi.mdms.kamstrup.services.PagingService;
import za.co.spsi.mdms.kamstrup.services.order.domain.Orders;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;


/**
 * Created by jaspervdb on 2016/10/12.
 */
@Dependent
public class OrderService {

    @Inject
    private PagingService<Orders> pagingService;

    public PagingService<Orders> getPagingService() {
        return pagingService;
    }

    public List<Orders> getOrders() {
        return pagingService.getObjects("orders",Orders.class);
    }
}
