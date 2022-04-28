package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class UserAlreadyExistsException extends UAException {

    public UserAlreadyExistsException(String message) {
        super(message);
        init("user_already_exists", "USER_EXISTS", "User already exists", 400);
    }

    public UserAlreadyExistsException() {
        this("");
    }

}
