package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 * http://172.31.91.228/UtiliDriver/api/orders/7J7erHiNQUSJG6aXAPpuGA/
 */
@XmlRootElement(name = "MeterResults")
public class MeterResults {

    @XmlAttribute(name = "ref")
    public String ref;

    @XmlAttribute()
    public Integer total;

    @XmlAttribute()
    public Integer count;

    @XmlElement(name = "MeterResult")
    public MeterResult meterResult[];

}
