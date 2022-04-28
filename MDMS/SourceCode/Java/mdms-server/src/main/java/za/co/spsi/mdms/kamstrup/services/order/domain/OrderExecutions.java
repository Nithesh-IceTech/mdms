package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by johan on 2016/11/24.
 */

@XmlRootElement(name = "OrderExecutions")
public class OrderExecutions {
    @XmlElement(name = "OrderExecution")
    public OrderExecution orderExecution;

}
