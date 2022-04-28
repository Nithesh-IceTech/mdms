package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/17.
 */
@XmlRootElement(name = "Register")
public class Register {

    @XmlAttribute
    public String id;

    @XmlElement(name = "Unit")
    public String unit;
    @XmlElement(name = "Scale")
    public Integer scale;
    @XmlElement(name = "Value")
    public Double value;

}
