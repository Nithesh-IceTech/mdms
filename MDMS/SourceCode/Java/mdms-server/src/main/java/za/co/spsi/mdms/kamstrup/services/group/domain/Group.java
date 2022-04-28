package za.co.spsi.mdms.kamstrup.services.group.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@XmlRootElement(name = "Group")
public class Group {

    @XmlAttribute(name="ref")
    public String ref;

    @XmlElement(name="Name")
    public String name;

    @XmlElement(name="Meters")
    public Meter meters;

    @XmlRootElement(name = "Meters")
    public static class Meter {

        @XmlAttribute(name="ref")
        public String ref;
    }

}
