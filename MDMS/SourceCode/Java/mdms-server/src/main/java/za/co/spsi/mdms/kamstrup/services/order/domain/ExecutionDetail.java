package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 * http://172.31.91.228/UtiliDriver/api/orders/iOoJd5t9JkKe9KaaAPqDlQ/executions/
 */
@XmlRootElement(name = "OrderExecutions")
public class ExecutionDetail {

    @XmlAttribute
    public String ref;

    @XmlElement(name = "OrderExecution")
    public OrderExecution orderExecution;

}
