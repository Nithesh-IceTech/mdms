package za.co.spsi.mdms.kamstrup.services.meter.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/13.
 */
@XmlRootElement(name = "Register")
public class Register {
    @XmlAttribute
    public String id;
    @XmlAttribute
    public String name;
    @XmlAttribute
    public String command;
    @XmlAttribute
    public String actions;

    @Override
    public String toString() {
        return String.format("Register {ID %s, Name {%s}, Command %s, Actions %s}",id,name,command,actions);
    }
}