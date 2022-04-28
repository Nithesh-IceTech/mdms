package za.co.spsi.mdms.common.generator;

public class WinSmsException extends RuntimeException {

    public WinSmsException() {
    }

    public WinSmsException(String message) {
        super(message);
    }

    public WinSmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public WinSmsException(Throwable cause) {
        super(cause);
    }

    public WinSmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
