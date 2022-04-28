package za.co.spsi.mdms.nes.services.order.domain;

import za.co.spsi.mdms.nes.services.LongFormatDateAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/11/25.
 *
 */
public class Interval {

    @XmlJavaTypeAdapter(LongFormatDateAdapter.class)
    @XmlElement(name = "DATETIME")
    public Timestamp dateTime;

    @XmlElement(name = "STATUS")
    public String status;

    @XmlElement(name = "CHANNEL")
    public Channel channels[];

}
