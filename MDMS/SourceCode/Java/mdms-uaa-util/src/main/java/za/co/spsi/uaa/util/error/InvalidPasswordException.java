package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class InvalidPasswordException extends UAException {

    public InvalidPasswordException() {
        this("");
    }

    public InvalidPasswordException(String message) {
        super(message);
        init("invalid_password", "INV_PASSWORD", "Invalid password", 401);
    }

    public InvalidPasswordException(String message, Exception ex) {
        super(message,ex);
        init("invalid_password", "INV_PASSWORD", ex.getMessage(), 401);
    }

}
