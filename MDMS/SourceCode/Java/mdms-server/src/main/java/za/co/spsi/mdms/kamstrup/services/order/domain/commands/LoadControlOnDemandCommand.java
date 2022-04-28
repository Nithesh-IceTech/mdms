package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import za.co.spsi.mdms.kamstrup.services.ShortFormatDateAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/14.
 */
@XmlRootElement(name = "LoadControlOnDemandCommand")
public class LoadControlOnDemandCommand {

    @XmlAttribute
    public String action;

    @XmlElement(name = "StartTime")
    @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
    public Timestamp startTime;

    @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
    @XmlElement(name = "EndTime")
    public Timestamp endTime;

    @XmlElement(name = "Relay")
    public String relay;

    @XmlElement(name = "State")
    public String state;

    @Override
    public String toString() {
        return String.format("Load_Control_On_Demand_Command { StartTime %s, EndTime %s, Relay %s, State %s }",
                startTime,endTime,relay,state);
    }


}
