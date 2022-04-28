package za.co.spsi.mdms.nes.services.order.domain;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by jaspervdb on 2016/11/25.
 */
public class Channel {

    @XmlElement(name = "ID")
    public Integer id;

    @XmlElement(name = "EXTENDEDSTATUS")
    public Integer extendedStatus;

    @XmlElement(name = "VALUE")
    public Double value;
}
