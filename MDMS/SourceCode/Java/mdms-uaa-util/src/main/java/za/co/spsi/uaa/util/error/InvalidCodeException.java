package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class InvalidCodeException extends AuthorisationException {

    public InvalidCodeException() {
        init("invalid_code", "INV_CODE", "Invalid Code", 401);
    }

    public InvalidCodeException(String message) {
        super(message);
        init("invalid_code", "INV_CODE", message, 401);
    }

    public InvalidCodeException(String message, Exception ex) {
        super(message,ex);
        init("invalid_code", "INV_CODE", ex.getMessage(), 401);
    }

}
