package za.co.spsi.uaa.util.dto;

import lombok.Data;

@Data
public class ValidatePasswordResponse {

    public enum Status {
        Success,Failed
    }

    private String username,message,agencyId;
    private Status status;

    public ValidatePasswordResponse() {}

    public ValidatePasswordResponse(ValidatePasswordRequest request) {
        this.username = request.getUsername();
        this.agencyId = request.getAgencyId();
    }

    public static ValidatePasswordResponse failed(ValidatePasswordRequest request,String message) {
        ValidatePasswordResponse validation = new ValidatePasswordResponse(request);
        validation.message = message;
        validation.status = Status.Failed;
        return validation;
    }

    public static ValidatePasswordResponse success(ValidatePasswordRequest request) {
        ValidatePasswordResponse validation = new ValidatePasswordResponse(request);
        validation.status = Status.Success;
        return validation;
    }
}
