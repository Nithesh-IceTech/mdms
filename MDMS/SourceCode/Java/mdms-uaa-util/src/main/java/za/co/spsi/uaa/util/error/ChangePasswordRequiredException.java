package za.co.spsi.uaa.util.error;

/**
 * Created by jaspervdb on 2016/05/31.
 */
public class ChangePasswordRequiredException extends UAException {

    public ChangePasswordRequiredException(String msg) {
        super(msg);
        init("change_password_required", "CHANGE_PASSWORD", "Change password required", 401);
    }

    public ChangePasswordRequiredException() {
        this("");
    }

}
