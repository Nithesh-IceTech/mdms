package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LoggerReading")
public class LoggerReading {

    @XmlElement(name = "Logger")
    public Logger loggers[];

}
