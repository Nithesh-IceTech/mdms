package za.co.spsi.mdms.kamstrup.services.order.domain;

import za.co.spsi.mdms.kamstrup.services.order.domain.commands.BreakerCommandResult;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.FaultedCommandResult;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.LoggerCommandResult;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.RegisterCommandResult;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 * http://172.31.91.228/UtiliDriver/api/orders/7J7erHiNQUSJG6aXAPpuGA/
 */
@XmlRootElement(name = "CommandResults")
public class CommandResults {

    @XmlElement(name = "BreakerCommandResult")
    public BreakerCommandResult breakerCommandResult;

    @XmlElement(name = "FaultedCommandResult")
    public FaultedCommandResult faultedCommandResult;

    @XmlElement(name = "LoggerCommandResult")
    public LoggerCommandResult loggerCommandResult;

    @XmlElement(name = "RegisterCommandResult")
    public RegisterCommandResult registerCommandResult;

}
