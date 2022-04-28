package za.co.spsi.mdms.kamstrup.services.order.domain;

import za.co.spsi.mdms.kamstrup.services.ShortFormatDateAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

@XmlRootElement(name = "RegisterReading")
public class RegisterReading {

    @XmlAttribute(name = "readouttime")
    @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
    public Timestamp readouttime;

    @XmlElement(name = "Register")
    public Register register;
}
