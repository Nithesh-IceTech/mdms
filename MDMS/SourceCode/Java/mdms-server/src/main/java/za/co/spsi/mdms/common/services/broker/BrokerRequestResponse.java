package za.co.spsi.mdms.common.services.broker;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties
public class BrokerRequestResponse {

    public enum Status {
        PENDING,COMPLETED,FAILED
    }

    private BrokerRequest.Command command;
    private Status status;
    private String ref;

    public BrokerRequestResponse() {}

    public BrokerRequestResponse(BrokerRequest.Command command, String ref) {
        this.status = Status.PENDING;
        this.command = command;
        this.ref = ref;
    }

}
