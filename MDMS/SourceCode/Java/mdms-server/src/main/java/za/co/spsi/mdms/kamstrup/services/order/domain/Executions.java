package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 */
@XmlRootElement(name = "Executions")
public class Executions {

    @XmlAttribute(name="ref")
    public String ref;

}
