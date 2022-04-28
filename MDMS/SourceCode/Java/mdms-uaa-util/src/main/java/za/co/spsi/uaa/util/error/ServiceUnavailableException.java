package za.co.spsi.uaa.util.error;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by jaspervdb on 2016/05/31.
 */
@Slf4j
public class ServiceUnavailableException extends UAException {

    public ServiceUnavailableException(String message, Exception ex) {
        super(message,ex);
        setErrorDetail(ex.getMessage());
        init();
    }

    public ServiceUnavailableException(String message) {
        super(message);
        init();
    }

    public ServiceUnavailableException() {
        init();
    }

    private void init() {
        init("service unavailable", "SERVICE UNAVAILABLE", "Service unavailable", 503);
    }

    public void log() {
        // will by default only log the message, and not the stack trace
        log.info(getMessage(),this);
    }
}
