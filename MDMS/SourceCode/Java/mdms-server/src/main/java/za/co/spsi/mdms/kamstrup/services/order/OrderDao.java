package za.co.spsi.mdms.kamstrup.services.order;

import za.co.spsi.mdms.kamstrup.services.PagingService;
import za.co.spsi.mdms.kamstrup.services.order.domain.ExecutionDetail;
import za.co.spsi.mdms.kamstrup.services.order.domain.Order;
import za.co.spsi.mdms.kamstrup.services.order.domain.OrderDetail;

/**
 * Created by jaspervdb on 2016/10/17.
 */
public class OrderDao {

    private Order order;
    private OrderDetail detail;

    public OrderDao(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public static OrderDetail getOrderDetail(PagingService pagingService,String ref) {
        return (OrderDetail) pagingService.get(OrderDetail.class,ref);
    }

    public OrderDetail getDetail(PagingService pagingService) {
        if (detail == null) {
            detail = (OrderDetail) pagingService.get(OrderDetail.class,order.ref);
        }
        return detail;
    }

    public ExecutionDetail getExecutionDetail(PagingService pagingService) {
        return (ExecutionDetail) pagingService.get(ExecutionDetail.class,
                getDetail(pagingService).executions.ref+"/");
    }


}
