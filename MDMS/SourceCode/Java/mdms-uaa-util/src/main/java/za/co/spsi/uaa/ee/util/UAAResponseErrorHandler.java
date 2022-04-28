package za.co.spsi.uaa.ee.util;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/05/30.
 */
//@Provider
public class UAAResponseErrorHandler implements ExceptionMapper<ClientErrorException> {

    private static final Logger log = Logger.getLogger(UAAResponseErrorHandler.class.getName());

    public Response toResponse(ClientErrorException exception) {
        throw ExceptionHandlerFactory.buildException(exception.getResponse());
    }
}
