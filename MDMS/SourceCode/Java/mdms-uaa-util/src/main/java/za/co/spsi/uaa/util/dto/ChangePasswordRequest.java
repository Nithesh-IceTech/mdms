package za.co.spsi.uaa.util.dto;

/**
 * Created by jaspervdb on 2016/07/11.
 */
public class ChangePasswordRequest {
    private String currPassword, newPassword;

    public ChangePasswordRequest() {}

    public ChangePasswordRequest(String currPassword,String newPassword) {
        this.currPassword = currPassword;
        this.newPassword = newPassword;
    }

    public String getCurrPassword() {
        return currPassword;
    }

    public void setCurrPassword(String currPassword) {
        this.currPassword = currPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
