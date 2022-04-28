package za.co.spsi.mdms.nes.services.order.domain;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by jaspervdb on 2016/11/25.
 *
 */
public class Intervals {

    @XmlElement(name = "INTERVAL")
    public Interval intervals[];

}
