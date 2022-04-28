package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class SecurityException extends UAException {

    public SecurityException() {
        init("security", "UNAUTHORISED", "Unauthorised", 401);
    }

    public SecurityException(String message) {
        super(message);
        init("security", "UNAUTHORISED", "", 401);
    }

    public SecurityException(String message,Exception ex) {
        super(message,ex);
        init("security", "UNAUTHORISED", ex.getMessage(), 401);
    }


}
