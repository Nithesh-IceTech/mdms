package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by jaspervdb on 2016/10/14.
 */
public class Meter {

    @XmlAttribute
    public String ref;
    @XmlAttribute
    public String subType;
    @XmlAttribute
    public String state;
    @XmlAttribute
    public String serialNumber;
    @XmlAttribute
    public String meterNumber;
    @XmlAttribute
    public String mainType;
}
