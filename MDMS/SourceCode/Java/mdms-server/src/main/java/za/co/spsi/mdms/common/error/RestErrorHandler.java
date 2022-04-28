package za.co.spsi.mdms.common.error;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by jaspervdbijl on 2017/01/09.
 */
@Provider
public class RestErrorHandler implements ExceptionMapper<RestException> {

    @Override
    public Response toResponse(RestException exception) {
        return Response.status(exception.getStatus()).entity(exception.getMsg()).build();
    }
}
