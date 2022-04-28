package za.co.spsi.mdms.kamstrup.services.order.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by johan on 2016/11/14.
 */

@XmlRootElement(name = "Order")
public class OrderDetailCommand {

    public enum Priority {
        High
    };

    @XmlElement(name = "Subjects")
    public Subjects subjects;

    @XmlAttribute
    public String ref;

    @XmlAttribute
    public String priority;

    @XmlElement(name = "Executions")
    public Executions executions;

    @XmlElement(name = "Commands")
    public Commands commands;

    public OrderDetailCommand() {}

    public OrderDetailCommand(Priority priority,Subjects subjects,Commands commands) {
        this.priority = priority.name();
        this.subjects = subjects;
        this.commands = commands;
    }

}
