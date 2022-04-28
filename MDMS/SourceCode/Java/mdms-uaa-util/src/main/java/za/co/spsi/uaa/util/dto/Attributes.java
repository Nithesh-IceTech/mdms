package za.co.spsi.uaa.util.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @NoArgsConstructor @AllArgsConstructor @Builder @ToString
public class Attributes implements Serializable {

    public static List<String> DEFAULT_SCOPE = Arrays.asList("read","trust","write");

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    @JsonIgnore
    private static final long serialVersionUID = 1L;

    @JsonProperty("org_unit")
    private String orgUnit;
    @JsonProperty("preferred_language")
    private String preferredLanguage;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("password_expiry_time")
    private Long passwordExpiryTime;
    @JsonProperty("password_expiry_warning_time")
    private Long passwordExpiryWarningTime;
    @JsonProperty("mail")
    private String emailAddress;

    public Attributes(String orgUnit, String preferredLanguage, String firstName, String lastName) {
        this.orgUnit = orgUnit;
        this.preferredLanguage = preferredLanguage;
        this.firstName = firstName;
        this.lastName = lastName;
    }


}
