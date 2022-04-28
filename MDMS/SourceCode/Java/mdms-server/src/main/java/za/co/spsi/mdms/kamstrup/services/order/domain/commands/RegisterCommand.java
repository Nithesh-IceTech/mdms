package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import za.co.spsi.mdms.kamstrup.services.ShortFormatDateAdapter;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Register;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Registers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/14.
 */
@XmlRootElement(name = "RegisterCommand")
public class RegisterCommand {

    @XmlAttribute
    public String action;

    @XmlElement(name = "Register")
    public Register register;

    @Override
    public String toString() {
        return String.format("Register_Command {Action %s, Register {%s}",action,register);
    }

}
