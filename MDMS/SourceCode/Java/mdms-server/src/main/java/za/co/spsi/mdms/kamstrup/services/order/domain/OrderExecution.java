package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 * http://172.31.91.228/UtiliDriver/api/orders/7J7erHiNQUSJG6aXAPpuGA/
 */
@XmlRootElement(name = "OrderExecution")
public class OrderExecution {

    @XmlAttribute(name = "refstatus")
    public String refStatus;

    @XmlAttribute(name = "refcompleted")
    public String refCompleted;

}
