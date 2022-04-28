package za.co.spsi.mdms.kamstrup.services.meter.domain;

import za.co.spsi.mdms.kamstrup.services.Pageable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/12.
 * http://172.31.91.228/utilidriver/api/meters/
 */
@XmlRootElement(name = "Meters")
public class Meters implements Pageable {
    @XmlAttribute
    public String ref;
    @XmlAttribute
    public Integer count;
    @XmlAttribute
    public Integer total;
    @XmlElement(name="Meter")
    public Meter[] meters;

    public Integer getCount() {
        return count;
    }

    public Integer getTotal() {
        return total;
    }

    public Meters() {}

    public Meters(Meter[] meters) {
        this.meters = meters;
    }

}
