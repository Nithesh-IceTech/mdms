package za.co.spsi.uaa.util.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by jaspervdb on 2016/07/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsernamePassword {
    private String username, password;

    public UsernamePassword() {}

    public UsernamePassword(String username,String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
