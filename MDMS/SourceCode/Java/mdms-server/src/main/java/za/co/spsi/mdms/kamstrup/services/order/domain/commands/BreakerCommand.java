package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 */
@XmlRootElement(name = "Executions")
public class BreakerCommand {

    public enum Cmd {
        read,cutoff,release,connect
    }

    @XmlAttribute(name = "action")
    public String action;

    @Override
    public String toString() {
        return String.format("Breaker_Command { %s }", action);
    }

    public BreakerCommand() {}

    public BreakerCommand(String action) {
        this.action = action;
    }
}
