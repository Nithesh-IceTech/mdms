package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class InvalidCredentialsException extends AuthorisationException {

    public InvalidCredentialsException() {
        init("invalid_credentials", "INV_CREDENTIALS", "Invalid username or password", 401);
    }

    public InvalidCredentialsException(String message) {
        super(message);
        init("invalid_credentials", "INV_CREDENTIALS", "", 401);
    }

    public InvalidCredentialsException(String message,Exception ex) {
        super(message,ex);
        init("invalid_credentials", "INV_CREDENTIALS", ex.getMessage(), 401);
    }

}
