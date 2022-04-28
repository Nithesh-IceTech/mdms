package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/19.
 */
@XmlRootElement(name = "Command ")
public class Command {

    @XmlAttribute(name = "ref")
    public String ref;

    @XmlElement(name = "Description")
    public String description;
}