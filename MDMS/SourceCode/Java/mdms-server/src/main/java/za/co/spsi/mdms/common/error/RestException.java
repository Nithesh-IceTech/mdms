package za.co.spsi.mdms.common.error;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by jaspervdbijl on 2017/01/09.
 */
public class RestException extends ClientErrorException {

    private Response.Status status;
    private String msg;

    public RestException(Response.Status status, String msg) {
        super(status);
        this.status = status;
        this.msg = msg;
    }

    public Response.Status getStatus() {
        return status;
    }

    public void setStatus(Response.Status status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
