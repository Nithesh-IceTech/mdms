package za.co.spsi.mdms.generic.broker;

import lombok.Data;

import java.util.List;

@Data
public class GenericBrokerPayload {
    private String meterSerialNumber;
    private List<GenericBrokerResources> resources;
}