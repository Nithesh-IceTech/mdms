package za.co.spsi.uaa.util.error;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by jaspervdb on 2016/05/31.
 */
@Slf4j
public class MailSystemException extends UAException {

    public MailSystemException(String message, Exception ex) {
        super(message,ex);
        setErrorDetail(ex.getMessage());
        init();
    }

    public MailSystemException(String message) {
        super(message);
        init();
    }

    public MailSystemException() {
        init();
    }

    private void init() {
        init("system", "MAIL ERROR", "Mail System Error", 500);
    }

    public void log() {
        // will by default only log the message, and not the stack trace
        log.info(getMessage(),this);
    }

    public boolean logFullStackTrace() {
        return true;
    }



}
