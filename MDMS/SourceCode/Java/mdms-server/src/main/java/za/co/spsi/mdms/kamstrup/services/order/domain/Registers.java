package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/17.
 */
@XmlRootElement(name = "Registers")
public class Registers {

    @XmlElement(name = "Register")
    public Register registers[];

}
