package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class PasswordPreviouslyUsedException extends UAException {

    public PasswordPreviouslyUsedException(String msg) {
        super(msg);
        init("password_previously_used", "PREV_PASSWORD", "Password has previously been used", 400);
    }

    public PasswordPreviouslyUsedException() {
        this("");
    }

}
