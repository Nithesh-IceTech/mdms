package za.co.spsi.mdms.kamstrup.services.order.domain;

import za.co.spsi.mdms.kamstrup.services.LongFormatDateAdapter;
import za.co.spsi.mdms.util.XmlHelper;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/14.
 * http://172.31.91.228/UtiliDriver/api/orders/akoI3oQl-kKZz6aaATl4Mw/
 */
@XmlRootElement(name = "Order")
public class OrderDetail {

    @XmlElement(name = "Subjects")
    public Subjects subjects;

    @XmlAttribute
    public String ref;

    @XmlAttribute
    @XmlJavaTypeAdapter(LongFormatDateAdapter.class)
    public Timestamp created;

    @XmlAttribute
    public String priority;

    @XmlElement(name = "Executions")
    public Executions executions;

    @XmlElement(name = "Commands")
    public Commands commands;


}
