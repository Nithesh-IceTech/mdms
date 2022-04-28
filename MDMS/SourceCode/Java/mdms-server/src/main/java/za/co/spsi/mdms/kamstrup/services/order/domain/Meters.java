package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 */
@XmlRootElement(name = "Meters")
public class Meters {
    @XmlAttribute
    private String total;

    @XmlAttribute
    private String ref;

    @XmlElement(name = "Meter")
    public Meter meters[];

    @XmlAttribute
    private String count;

}
