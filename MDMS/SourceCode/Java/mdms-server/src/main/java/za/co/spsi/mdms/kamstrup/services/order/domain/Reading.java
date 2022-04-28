package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Reading")
public class Reading {

    @XmlElement(name = "LoggerReading")
    public LoggerReading loggerReading;

    @XmlElement(name = "Meter")
    public Reading.MeterRef meterRef;

    @XmlElement(name = "RegisterReading")
    public RegisterReading registerReading;

    public static class MeterRef {
        @XmlAttribute(name = "ref")
        public String meter;
    }

}
