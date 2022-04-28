package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Entries")
public class Entries {

    @XmlElement(name = "Entry")
    public Entry entry[];

}
