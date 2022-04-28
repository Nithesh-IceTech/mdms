package za.co.spsi.mdms.utility;

import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.LoginRequest;
import org.idempiere.webservice.client.base.WebServiceResponse;
import org.idempiere.webservice.client.net.WebServiceConnection;

/**
 * Created by jaspervdbijl on 2017/04/26.
 */
public class IceUtilityException extends RuntimeException{

    private Enums.WebServiceResponseStatus status;

    public IceUtilityException() {
    }

    public IceUtilityException(WebServiceConnection client, LoginRequest login,WebServiceResponse response) {
        super(String.format("Ice Utility Error: %s. Sending to [%s] User [%s] Received from Utility [%s]",
                response.getStatus(),client.getUrl(),login.getUser(),response.getErrorMessage()));
        this.status = response.getStatus();
    }

    public Enums.WebServiceResponseStatus getStatus() {
        return status;
    }
}
