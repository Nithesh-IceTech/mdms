package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Readings")
public class Readings {

    @XmlAttribute(name = "ref")
    public String ref;

    @XmlAttribute(name = "nextref")
    public String nextRef;

    @XmlAttribute(name = "count")
    public Integer count;

    @XmlElement(name = "Reading")
    public Reading reading[];

}
