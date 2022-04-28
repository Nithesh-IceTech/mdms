package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class AccountLockedException extends UAException {

    public AccountLockedException(String message) {
        super(message);
        init("account_locked","ACCOUNT_LOCKED","Account locked",401);
    }

    public AccountLockedException() {
        this("");
    }
}
