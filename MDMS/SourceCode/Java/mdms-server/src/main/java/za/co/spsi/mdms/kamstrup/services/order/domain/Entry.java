package za.co.spsi.mdms.kamstrup.services.order.domain;

import za.co.spsi.mdms.kamstrup.services.ShortFormatDateAdapter;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

@XmlRootElement(name = "Entry")
public class Entry {

    @NotNull
    @XmlAttribute(name = "timestamp")
    @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
    public Timestamp timestamp;

    @NotNull
    @XmlAttribute(name = "logId")
    public String logId;

    @XmlElement(name = "Registers")
    public Registers registers;

}