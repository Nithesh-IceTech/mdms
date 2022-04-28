package za.co.spsi.mdms.kamstrup.services.group.domain;

import za.co.spsi.mdms.kamstrup.services.Pageable;
import za.co.spsi.mdms.kamstrup.services.order.domain.Order;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/12.
 * http://172.31.91.228/UtiliDriver/api/orders/?offset=0
 */
@XmlRootElement(name = "Groups")
public class Groups implements Pageable {

    @XmlAttribute(name="total")
    public Integer total;
    @XmlAttribute(name="count")
    public Integer count;
    @XmlAttribute(name="ref")
    public String ref;
    @XmlElement(name="Group")
    public Group[] groups;

    public Integer getCount() {
        return count;
    }

    public Integer getTotal() {
        return total;
    }

}