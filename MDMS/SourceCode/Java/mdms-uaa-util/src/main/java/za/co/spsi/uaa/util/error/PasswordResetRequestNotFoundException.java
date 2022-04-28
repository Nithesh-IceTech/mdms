package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class PasswordResetRequestNotFoundException extends UAException {

    public PasswordResetRequestNotFoundException(String msg) {
        super(msg);
        init("password_reset_request_not_found", "PASSWORD_RESET_REQUEST_NOT_FOUND", "Password reset request not found", 404);
    }

    public PasswordResetRequestNotFoundException() {
        this("");
    }

}
