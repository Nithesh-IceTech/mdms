package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class NoMailAddressException extends UAException {

    public NoMailAddressException(String message) {
        super(message);
        init("no_mail_address", "NO_MAIL_ADDRESS", "No mail address", 400);
    }

    public NoMailAddressException() {
        this("");
    }

}
