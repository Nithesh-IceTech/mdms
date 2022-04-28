package za.co.spsi.mdms.generic.broker;

import lombok.Data;

@Data
public class GenericBrokerPollResponse {
    private String status;
    private String message;
}