package za.co.spsi.mdms.kamstrup.services.meter.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/13.
 *
 * http://172.31.91.228/utilidriver/api/profiles/3266815674/
 */
@XmlRootElement(name = "Profile")
public class Profile {

    @XmlAttribute(name = "ref")
    public String ref;

    @XmlElement(name = "AutoCollection")
    public Profile.AutoCollection autoCollection;

    @XmlElement(name = "Registers")
    public Registers registers;

    public static class AutoCollection {
        @XmlElement(name = "Registers")
        public Registers registers;
    }


}