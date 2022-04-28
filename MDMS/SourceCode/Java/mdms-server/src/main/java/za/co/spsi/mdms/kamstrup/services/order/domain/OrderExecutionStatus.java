package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/14.
 * http://172.31.91.228/UtiliDriver/api/orders/iOoJd5t9JkKe9KaaAPqDlQ/executions/LnIWGLUerkWvl6xloKt1YQ/status/
 */
@XmlRootElement(name = "Status")
public class OrderExecutionStatus {

    @XmlElement(name = "SucceededCommandCount")
    public Integer succeededCommandCount;

    @XmlElement(name = "WaitingCommandCount")
    public Integer waitingCommandCount;

    @XmlElement(name = "FailedCommandCount")
    public Integer failedCommandCount;

    @XmlElement(name = "NotSupportedCommandCount")
    public Integer notSupportedCommandCount;

    @XmlElement(name = "WaitingMeterCount")
    public Integer waitingMeterCount;

    @XmlElement(name = "CompletedMeterCount")
    public Integer completedMeterCount;

    @XmlElement(name = "AbortedCommandCount")
    public Integer abortedCommandCount;

    public boolean isCompleted() {
        return waitingCommandCount == 0 && waitingMeterCount == 0;
    }

    public boolean hasFailed() {
        return failedCommandCount > 0;
    }
}
