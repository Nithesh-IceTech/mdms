package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/17.
 */
@XmlRootElement(name = "FaultedCommandResult")
public class FaultedCommandResult {

    @XmlAttribute(name = "faulttype")
    public String faultType;

    @XmlElement(name = "Command")
    public Command command;

    @XmlElement(name = "Description")
    public String description;


    /*
    <Command ref="http://172.31.91.228/utilidriver/api/orders/iOoJd5t9JkKe9KaaAPqDlQ/commands/2aKxt0HXuEazyk8kennEkg/"/>
<Description>
Could not establish a TCP/IP connection to the meter.
</Description>
</FaultedCommandResult>
     */
}
