package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import za.co.spsi.mdms.kamstrup.services.ShortFormatDateAdapter;
import za.co.spsi.mdms.kamstrup.services.order.domain.Register;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/17.
 */
@XmlRootElement(name = "RegisterCommandResult")
public class RegisterCommandResult {

    @XmlAttribute(name = "action")
    public String action;

    @XmlAttribute(name = "readouttime")
    @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
    public Timestamp readOutTime;

    @XmlElement(name = "Command")
    public Command command;

    @XmlElement(name = "Register")
    public Register register;



    /*
    Entries>
<FromDate>2016-10-09T06:01:00Z</FromDate>
<ToDate>2016-10-09T18:00:00Z</ToDate>
     */
}
