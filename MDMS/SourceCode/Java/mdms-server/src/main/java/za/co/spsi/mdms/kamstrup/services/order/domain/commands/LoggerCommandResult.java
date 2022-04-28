package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import za.co.spsi.mdms.kamstrup.services.ShortFormatDateAdapter;
import za.co.spsi.mdms.kamstrup.services.order.domain.Registers;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/17.
 */
@XmlRootElement(name = "LoggerCommandResult")
public class LoggerCommandResult {

    @XmlAttribute(name = "action")
    public String action;

    @XmlAttribute(name = "readouttime")
    @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
    public Timestamp readOutTime;

    @XmlElement(name = "Command")
    public Command command;

    @XmlElement(name = "Logger")
    public Logger logger;

    @XmlRootElement(name = "Logger")
    public static class Logger {

        @XmlAttribute(name = "id")
        public String id;

        @XmlElement(name = "Entries")
        public Entries entries;

        @XmlElement(name = "FromDate")
        @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
        public Timestamp fromDate;

        @XmlElement(name = "ToDate")
        @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
        public Timestamp toDate;


    }

    @XmlRootElement(name = "Entries")
    public static class Entries {

        @XmlElement(name = "Entry")
        public Entry entries[];

    }

    @XmlRootElement(name = "Entry")
    public static class Entry {

        @NotNull
        @XmlAttribute()
        @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
        public Timestamp timestamp;

        @NotNull
        @XmlAttribute()
        public String logId;

        @XmlElement(name = "Registers")
        public Registers registers;

    }

    /*
    Entries>
<FromDate>2016-10-09T06:01:00Z</FromDate>
<ToDate>2016-10-09T18:00:00Z</ToDate>
     */
}
