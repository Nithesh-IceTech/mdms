package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class InvalidMailAddressException extends AuthorisationException {

    public InvalidMailAddressException() {
        init("invalid_mail_address", "INV_MAIL_ADDRESS", "Invalid email address", 401);
    }

    public InvalidMailAddressException(String message) {
        super(message);
        init("invalid_mail_address", "INV_MAIL_ADDRESS", "", 401);
    }

    public InvalidMailAddressException(String message, Exception ex) {
        super(message,ex);
        init("invalid_mail_address", "INV_MAIL_ADDRESS", ex.getMessage(), 401);
    }

}
