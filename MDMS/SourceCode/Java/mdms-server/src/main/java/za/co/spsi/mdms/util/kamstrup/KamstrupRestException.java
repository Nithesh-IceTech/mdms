package za.co.spsi.mdms.util.kamstrup;

import za.co.spsi.mdms.kamstrup.services.order.domain.commands.Fault;

import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdbijl on 2017/05/17.
 */
public class KamstrupRestException extends Exception {

    public static Logger TAG = Logger.getLogger(KamstrupRestException.class.getName());

    public KamstrupRestException() {
    }

    public KamstrupRestException(String message) {
        super(message);
    }

    public KamstrupRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public static String readFaultDesc(Response response) {
        try {
            Fault fault = response.readEntity(Fault.class);
            return fault != null?fault.description:null;
        } catch (Exception ex) {
            TAG.log(Level.WARNING,ex.getMessage(),ex);
            return null;
        }
    }

    public KamstrupRestException(String msg,Response response) {
        this(String.format("%s. Http Code %d [%s]",msg,response.getStatus(),readFaultDesc(response)));
    }

}
