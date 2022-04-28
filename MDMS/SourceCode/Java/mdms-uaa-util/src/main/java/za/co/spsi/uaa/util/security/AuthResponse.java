package za.co.spsi.uaa.util.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import za.co.spsi.pjtk.reflect.Reflect;
import za.co.spsi.uaa.util.dto.Attributes;

import java.lang.reflect.Field;
import java.util.*;

@ToString
@Data
@Builder
@AllArgsConstructor
public class AuthResponse {

    @JsonProperty("org_unit")
    private String orgUnit;

    @JsonProperty("preferred_language")
    private String preferredLanguage;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("first_name")
    private String firstName;


    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("client_id")
    private String clientId;

    private Long exp;

    @JsonProperty("password_expiry_time")
    private Long passwordExpiryTime;

    @JsonProperty("password_expiry_warning_time")
    private Long passwordExpiryWarningTime;
    
    @JsonProperty("email_address")
    private String emailAddress;

    private List<String> authorities = new ArrayList<>(),
            scope = Arrays.asList("trust", "read", "write"),
            aud = Arrays.asList("rest_api");

    private Attributes attributes;

    private String jti;

    public AuthResponse() {

    }

    @SneakyThrows
    private static Object getClaimValue(Object bean, Field field) {
        Object value = field.get(bean);
        if (value != null && !value.getClass().getName().startsWith("java.")) {
            return toClaims(value);
        } else {
            return value;
        }
    }

    public static Map<String, Object> toClaims(Object bean) {
        final Map claims = new HashMap();
        for (Field field : Reflect.getFields(bean.getClass())) {
            claims.put(field.getName(), getClaimValue(bean, field));
        }
        return claims;
    }

    @JsonIgnore
    public Map<String, Object> toClaims() {
        final Map claims = toClaims(this);
        // TODO = not sure why  this has to be caps, possibly some legacy issue
        claims.put("user_name", userName);
        return claims;
    }

}
