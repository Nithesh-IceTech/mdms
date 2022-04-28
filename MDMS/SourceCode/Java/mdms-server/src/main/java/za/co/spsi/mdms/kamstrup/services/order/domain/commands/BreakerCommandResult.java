package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import za.co.spsi.mdms.kamstrup.services.LongFormatDateAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by johan on 2016/11/10.
 */
@XmlRootElement(name = "BreakerCommandResult")
public class BreakerCommandResult {

    @XmlAttribute(name = "action")
    public String action;

    @XmlAttribute(name = "executiontime")
    @XmlJavaTypeAdapter(LongFormatDateAdapter.class)
    public Timestamp executionTime;

    @XmlElement(name = "Command")
    public Command command;

    @XmlElement(name = "OutputState")
    public Boolean outputState;

    @XmlElement(name = "ControlState")
    public String controlState;

}

