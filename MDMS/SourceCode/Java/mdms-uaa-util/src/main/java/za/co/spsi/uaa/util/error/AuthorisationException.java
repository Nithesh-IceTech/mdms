package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class AuthorisationException extends UAException {

    public AuthorisationException(String message) {
        super(message);
        init("unauthorised", "UNAUTHORISED", "Unauthorised", 401);
    }

    public AuthorisationException(String message,Exception ex) {
        super(message,ex);
        init("unauthorised", "UNAUTHORISED", "", 401);
    }

    public AuthorisationException() {
        this("");
    }

}
