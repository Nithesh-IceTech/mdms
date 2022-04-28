package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@XmlRootElement(name = "Order")
public class Order {

    @XmlAttribute(name="ref")
    public String ref;

    public Order() {}

    public Order(String ref) {
        this.ref = ref;
    }
}
