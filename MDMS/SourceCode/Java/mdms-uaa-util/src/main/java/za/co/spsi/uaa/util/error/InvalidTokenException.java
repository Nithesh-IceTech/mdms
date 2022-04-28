package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class InvalidTokenException extends UAException {

    public InvalidTokenException() {
        init("invalid_token", "INV_TOKEN", "Invalid token", 401);
    }

    public InvalidTokenException(String message) {
        super(message);
        init("invalid_token", "INV_TOKEN", "", 401);
    }

    public InvalidTokenException(String message, Exception ex) {
        super(message,ex);
        init("invalid_token", "INV_TOKEN", ex.getMessage(), 401);
    }

}
