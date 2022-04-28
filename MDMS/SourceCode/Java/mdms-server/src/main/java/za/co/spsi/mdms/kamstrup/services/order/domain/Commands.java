package za.co.spsi.mdms.kamstrup.services.order.domain;

import za.co.spsi.mdms.kamstrup.services.order.domain.commands.BreakerCommand;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.LoadControlOnDemandCommand;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.LoggerCommand;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.RegisterCommand;
import za.co.spsi.toolkit.util.StringList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.kamstrup.services.order.domain.commands.BreakerCommand.Cmd.read;

/**
 * Created by jaspervdb on 2016/10/14.
 */
@XmlRootElement(name = "Executions")
public class Commands {

    public enum Action {
        READ,WRITE,RELEASE;
    }

    @XmlElement(name = "BreakerCommand")
    public BreakerCommand breakerCommands[];

    @XmlElement(name = "LoadControlOnDemandCommand")
    public LoadControlOnDemandCommand loadControlOnDemandCommands[];

    @XmlElement(name = "LoggerCommand")
    public LoggerCommand loggerCommands[];

    @XmlElement(name = "RegisterCommand")
    public RegisterCommand registerCommands[];

    public Commands() {}

    public Commands(BreakerCommand [] breakerCommands) {
        this.breakerCommands = breakerCommands;
    }

    public boolean hasLoggerReadCommand() {
        return loggerCommands != null ? Arrays.stream(loggerCommands).anyMatch(l -> Action.READ.name().equalsIgnoreCase(l.action)):
                registerCommands != null ? Arrays.stream(registerCommands).anyMatch(l -> Action.READ.name().equalsIgnoreCase(l.action)):false;
    }

    public boolean hasBreakerReadCommand() {
        return breakerCommands != null  && breakerCommands.length > 0 && !read.name().equals(breakerCommands[0].action)
                || loadControlOnDemandCommands != null && loadControlOnDemandCommands.length > 0;
    }

    @Override
    public String toString() {
        return Arrays.asList(breakerCommands,loadControlOnDemandCommands,loggerCommands,registerCommands).stream().
                filter(f -> f != null).map(o -> Arrays.stream((o)).filter(c -> c != null).map(m -> m.toString()).
                collect(Collectors.toCollection(StringList::new)).toString()).collect(Collectors.toCollection(StringList::new)).toString();
    }


}
