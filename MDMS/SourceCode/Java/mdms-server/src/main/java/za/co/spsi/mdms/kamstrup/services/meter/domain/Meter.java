package za.co.spsi.mdms.kamstrup.services.meter.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@XmlRootElement(name = "Meter")
public class Meter {

    @XmlAttribute(name = "ref")
    public String ref;
    @XmlAttribute(name = "serialNumber")
    public String serialNumber;
    @XmlAttribute(name = "meterNumber")
    public String meterNumber;
    @XmlAttribute(name = "state")
    public String state;

    public Meter() {}

    public Meter(String ref) {
        this.ref = ref;
    }
}
