package za.co.spsi.mdms.common.services.broker;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Arrays;

@Data
@JsonIgnoreProperties
public class BrokerRequest {
    public enum Command {
        CUT,RELEASE;

        @JsonValue
        public String toSerial(){
            return name();
        }

        @JsonCreator
        public static Command fromSerial(String serial) {
            return Arrays.stream(Command.values()).filter(c -> c.toSerial().equals(serial)).findFirst().get();
        }
    }

    private Command command;
    private String serialN;
    // optional effect date to perform command
    private Timestamp effectDate;

    public BrokerRequest() {}

}
