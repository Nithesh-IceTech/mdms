package za.co.spsi.uaa.util.error;


/**
 * Created by jaspervdb on 2016/05/31.
 */
public class InvalidCellNumberException extends AuthorisationException {

    public InvalidCellNumberException() {
        init("invalid_cell_n", "INV_CELL_N", "Invalid cell number", 401);
    }

    public InvalidCellNumberException(String message) {
        super(message);
        init("invalid_cell_n", "INV_CELL_N", "", 401);
    }

    public InvalidCellNumberException(String message, Exception ex) {
        super(message,ex);
        init("invalid_cell_n", "INV_CELL_N", ex.getMessage(), 401);
    }

}
