package za.co.spsi.pjtk.error;

public class MessageException extends RuntimeException {

    public MessageException() {
    }

    public MessageException(String message) {
        super(message);
        setStackTrace(new StackTraceElement[]{});
    }

    public MessageException(String message,Object ... params) {
        this(String.format(message,params));
    }


}
