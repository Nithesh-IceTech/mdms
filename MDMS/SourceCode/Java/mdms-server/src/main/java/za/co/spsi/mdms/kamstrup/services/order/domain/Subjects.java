package za.co.spsi.mdms.kamstrup.services.order.domain;

import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Meters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 */
@XmlRootElement(name = "Subjects")
public class Subjects {

    @XmlElement(name = "Meters")
    public Meters meters;

    @XmlElement(name = "Group")
    public Group group;

    public Subjects() {
    }

    public Subjects(Meters meters) {
        this.meters = meters;
    }

    public Meters getMetersFromSource(RestHelper restHelper) {
        if (group != null && group.ref != null) {
            return restHelper.restGet(
                    restHelper.restGet(group.ref, za.co.spsi.mdms.kamstrup.services.group.domain.Group.class).
                            meters.ref,Meters.class);
        } else {
            return meters;
        }
    }

}
