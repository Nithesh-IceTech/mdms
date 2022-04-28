package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class UserNotFoundException extends UAException {

    public UserNotFoundException(String message) {
        super(message);
        init("user_not_found", "USER_NOT_FOUND", "User not found", 400);
    }

    public UserNotFoundException() {
        this("");
    }


}
