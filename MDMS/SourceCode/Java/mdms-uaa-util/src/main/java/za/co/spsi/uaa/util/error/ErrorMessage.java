package za.co.spsi.uaa.util.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import za.co.spsi.pjtk.util.ObjectUtils;

/**
 * Created by jaspervdb on 2016/05/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessage {

    private String error, errorCode, errorDetail, errorDescription;
    private Integer httpStatus;

    public ErrorMessage() {
    }

    public ErrorMessage(UAException ua) {
        ObjectUtils.copy(this, ua);
    }


    @JsonProperty("error")
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @JsonProperty("error_code")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty("error_detail")
    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    @JsonProperty("error_description")
    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @JsonProperty("http_status")
    public Integer getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(Integer httpStatus) {
        this.httpStatus = httpStatus;
    }

    @JsonIgnore
    public UAException getAsException() {
        return ObjectUtils.copy(new UAException(), this);
    }
}
