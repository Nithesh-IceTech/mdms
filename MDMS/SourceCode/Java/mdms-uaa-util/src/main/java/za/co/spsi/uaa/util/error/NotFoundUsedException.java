package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class NotFoundUsedException extends UAException {

    public NotFoundUsedException(String msg) {
        super(msg);
        init("not_found", "NOT_FOUND", "Not found", 404);
    }

    public NotFoundUsedException() {
        this("");
    }

}
