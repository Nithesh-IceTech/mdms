package za.co.spsi.uaa.util.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jaspervdb on 2016/07/14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class User {

    protected String distName;
    protected String commonName;
    protected String surname;
    protected String initials;
    protected String givenName;
    protected String mail;
    protected String uid;
    protected String userPassword;
    protected String organizationalUnit;
    protected String preferredLanguage;
    protected boolean accountDisabled = false;
    protected boolean passwordExpired = false;
    protected Date passwordChangedTime;
    protected Date modifiedTimestamp;
    protected Date passwordExpiryTime;
    protected Date passwordAccountLockedTime;

    @JsonProperty("authorities")
    protected List<Authority> roles = new ArrayList<>();

    protected Date createTime;
    protected Integer passwordExpiryWarningInterval;
    protected String passwordPolicy;

    public User(){}

    public User(String uid, String userPassword){
        this.uid = uid;
        this.userPassword = userPassword;
    }



}
