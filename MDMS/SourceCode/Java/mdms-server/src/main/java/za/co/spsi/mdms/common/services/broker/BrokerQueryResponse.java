package za.co.spsi.mdms.common.services.broker;

import lombok.Data;

@Data
public class BrokerQueryResponse {

    private String ref;
    private BrokerRequestResponse.Status status;
    private String message;

    public BrokerQueryResponse() {}

    public BrokerQueryResponse(String ref, BrokerRequestResponse.Status status, String message) {
        this.ref = ref;
        this.status = status;
        this.message = message;
    }

//        Status
//        C - Connected
//        CP - Connect Pending
//        D - Disconnect
//        DP - Disconnect Pending
//        E - Error

    private String getCommandFormatted(Integer commandInt) {

        String command = "";

        switch(commandInt) {
            case 0:
                command = "CONNECT";
                break;
            case 1:
                command = "DISCONNECT";
                break;
            case 2:
                command = "CONNECT";
                break;
            default:
                command = "UNKNOWN";
        }

        return command;
    }

    public String getIceBrokerCommandStatus(Integer commandInt) {

        String formattedStatus = "";
        String command = this.getCommandFormatted(commandInt);

        if(this.status.name().equals(BrokerRequestResponse.Status.PENDING.name()) &&
                command.equalsIgnoreCase("connect")) {
            formattedStatus = "CP";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.COMPLETED.name()) &&
                command.equalsIgnoreCase("connect")) {
            formattedStatus = "C";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.FAILED.name()) &&
                command.equalsIgnoreCase("connect")) {
            formattedStatus = "E";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.PENDING.name()) &&
                command.equalsIgnoreCase("disconnect")) {
            formattedStatus = "DP";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.COMPLETED.name()) &&
                command.equalsIgnoreCase("disconnect")) {
            formattedStatus = "D";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.FAILED.name()) &&
                command.equalsIgnoreCase("disconnect")) {
            formattedStatus = "E";
        } else {
            formattedStatus = "E";
        }

        return formattedStatus;
    }

    public String getIceBrokerCommandMessage(Integer commandInt) {

        String formattedMessage = "";
        String command = this.getCommandFormatted(commandInt);

        if(this.status.name().equals(BrokerRequestResponse.Status.PENDING.name()) &&
                command.equalsIgnoreCase("connect")) {
            formattedMessage = "CONNECT PENDING";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.COMPLETED.name()) &&
                command.equalsIgnoreCase("connect")) {
            formattedMessage = "CONNECTED";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.FAILED.name()) &&
                command.equalsIgnoreCase("connect")) {
            formattedMessage = "CONNECT ERROR";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.PENDING.name()) &&
                command.equalsIgnoreCase("disconnect")) {
            formattedMessage = "DISCONNECT PENDING";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.COMPLETED.name()) &&
                command.equalsIgnoreCase("disconnect")) {
            formattedMessage = "DISCONNECTED";
        } else if(this.status.name().equals(BrokerRequestResponse.Status.FAILED.name()) &&
                command.equalsIgnoreCase("disconnect")) {
            formattedMessage = "DISCONNECT ERROR";
        } else {
            formattedMessage = "INVALID COMMAND";
        }

        return formattedMessage;
    }

}

