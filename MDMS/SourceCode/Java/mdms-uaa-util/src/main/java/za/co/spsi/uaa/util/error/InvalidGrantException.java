package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class InvalidGrantException extends InvalidCredentialsException {

    public InvalidGrantException(String msg) {
        super(msg);
        init("invalid_grant", "UNAUTHORISED", "Unauthorised Invalid Grant", 401);
    }

    public InvalidGrantException() {
        this("");
    }


}
