package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by johan on 2017/01/04.
 */
@XmlRootElement(name = "Group")
public class Group {
    @XmlAttribute
    public String ref;

}
