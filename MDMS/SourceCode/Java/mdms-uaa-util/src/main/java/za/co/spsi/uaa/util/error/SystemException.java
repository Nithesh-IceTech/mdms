package za.co.spsi.uaa.util.error;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by jaspervdb on 2016/05/31.
 */
@Slf4j
public class SystemException extends UAException {

    public SystemException(String message,Exception ex) {
        super(message,ex);
        setErrorDetail(ex.getMessage());
        init();
    }

    public SystemException(String message) {
        super(message);
        init();
    }

    public SystemException() {
        init();
    }

    private void init() {
        init("system", "SYSTEM", "System Error", 500);
    }

    public void log() {
        // will by default only log the message, and not the stack trace
        log.info(getMessage(),this);
    }

    public boolean logFullStackTrace() {
        return true;
    }



}
