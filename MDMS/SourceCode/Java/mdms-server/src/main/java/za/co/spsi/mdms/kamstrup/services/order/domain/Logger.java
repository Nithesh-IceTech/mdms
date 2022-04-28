package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Logger")
public class Logger {

    @XmlAttribute(name = "id")
    public String loggerId;

    @XmlElement(name = "Entries")
    public Entries entries;

}
