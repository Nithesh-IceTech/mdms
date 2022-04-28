package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class BadRequestException extends UAException {

    public BadRequestException(String message) {
        super(message);
        init("invalid_request", "INVALID_REQUEST", "Bad Request", 400);
    }

    public BadRequestException() {
        this("");
    }

    public boolean logFullStackTrace() {
        return true;
    }
}
