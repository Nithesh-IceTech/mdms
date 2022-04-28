package za.co.spsi.toolkit.crud.excel;

/**
 * Created by jaspervdb on 2016/08/18.
 */
public class XlsException extends RuntimeException {
    public XlsException() {
    }

    public XlsException(String message) {
        super(message);
    }

    public XlsException(String message,String ... params) {
        super(String.format(message,params));
    }

    public XlsException(String message, Throwable cause) {
        super(message, cause);
    }
}
