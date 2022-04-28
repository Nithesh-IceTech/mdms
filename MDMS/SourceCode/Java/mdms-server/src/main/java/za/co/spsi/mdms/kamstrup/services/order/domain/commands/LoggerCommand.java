package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import za.co.spsi.mdms.kamstrup.services.ShortFormatDateAdapter;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Registers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/14.
 * http://172.31.91.228/UtiliDriver/api/orders/akoI3oQl-kKZz6aaATl4Mw/
 */
@XmlRootElement(name = "LoggerCommand")
public class LoggerCommand {

    @XmlAttribute
    public String action;

    @XmlElement(name = "Logger")
    public Logger logger;

    @XmlRootElement(name = "Logger")
    public static class Logger {

        @XmlAttribute
        public String id;

        @XmlElement(name = "Registers")
        public Registers registers;

        @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
        @XmlElement(name = "FromDate")
        public Timestamp fromDate;

        @XmlJavaTypeAdapter(ShortFormatDateAdapter.class)
        @XmlElement(name = "ToDate")
        public Timestamp toDate;

        @Override
        public String toString() {
            return String.format("Logger { ID %s, Registers {%s}, FromDate %s, ToDate %s }",id,registers,fromDate,toDate);
        }

    }

    @Override
    public String toString() {
        return String.format("Logger_Command {Action %s, Logger {%s}",action,logger);
    }

    /*
    <FromDate>2016-10-09T06:01:00Z</FromDate>
<ToDate>2016-10-09T18:00:00Z</ToDate>
     */
}
