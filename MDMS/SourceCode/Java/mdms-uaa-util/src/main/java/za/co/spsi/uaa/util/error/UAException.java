package za.co.spsi.uaa.util.error;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by jaspervdb on 2016/05/30.
 */
@Data
@Slf4j
public class UAException extends RuntimeException {

    private String error,errorCode,errorDetail="",errorDescription;
    private Integer httpStatus;

    public UAException() {}

    public UAException(String message) {
        super(message);
        setErrorDetail(message);
    }

    public UAException(String message,String errorDetail,Exception ex) {
        super(message,ex);
        this.errorDetail = errorDetail;
    }

    public UAException setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
        return this;
    }

    public UAException(String message, Exception ex) {
        super(message,ex);
    }

    public UAException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }



    public UAException init(String error, String errorCode, String errorDescription, Integer httpStatus) {
        this.error = error;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.httpStatus = httpStatus;
        return this;
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public <T extends UAException> T setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
        return (T) this;
    }

    public void log() {
        // will by default only log the message, and not the stack trace
        if (logFullStackTrace()) {
            log.info(String.format("%s: %s", getClass().getSimpleName(), getMessage()),this);
        } else {
            log.info(String.format("%s: %s", getClass().getSimpleName(), getMessage()));
        }
    }

    public boolean logFullStackTrace() {
        return false;
    }
}
