package za.co.spsi.uaa.util.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;
import za.co.spsi.pjtk.reflect.RefFields;
import za.co.spsi.pjtk.reflect.Reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import static za.co.spsi.uaa.util.BaseUAHelper.decompressRoles;
import static za.co.spsi.uaa.util.Constants.COMPRESSED_PREFIX;

/**
 * Created by jaspervdb on 2016/05/31.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class JWTokenClaimDao {

    private List<String> aud = new ArrayList<>(), scope= new ArrayList<>(), authorities= new ArrayList<>();
    private String user_name, client_id, jti;
    @Getter @Setter
    private String firstName, lastName;
    private Integer exp;
    private Map<String, Object> attributes;

    // specific attributes
    private String orgUnit;
    private Date passwordExpiryTime, passwordExpiryWarningTime;

    public JWTokenClaimDao() {
    }

    @SneakyThrows
    public static void convertType(Object setInstance, Field setField, Object getValue) {
        if (getValue != null) {
            if (setField.getType().equals(getValue.getClass())) {
                setField.set(setInstance, getValue);
            } else if (Date.class.equals(setField.getType()) && Long.class.equals(getValue.getClass())) {
                setField.set(setInstance, new Date((long) getValue));
            } else if (List.class.isAssignableFrom(setField.getType()) && Long.class.equals(getValue.getClass())) {
            } else {
                Optional<Method> valueOf = Reflect.getMethods(setField.getType()).getByName("valueOf",false);
                if (valueOf.isPresent()) {
                    setField.set(setInstance, valueOf.get().invoke(null, getValue.toString()));
                }
            }
        } else {
            setField.set(setInstance, null);
        }
    }

    @SneakyThrows
    public JWTokenClaimDao(Claims claims) {
        init(claims);
    }

    @SneakyThrows
    private void initField(Field field,Object value) {
        if (value != null) {
            if (field.getType().equals(value.getClass())) {
                field.set(this, value);
            } else if (Date.class.equals(field.getType()) && Long.class.equals( value.getClass())) {
                field.set(this, new Date((Long) value));
            } else if (List.class.isAssignableFrom(field.getType()) && List.class.isAssignableFrom(value.getClass())) {
                field.set(this,value);
            } else if (Map.class.equals(field.getType()) && Map.class.isAssignableFrom(value.getClass())) {
                field.set(this,new HashMap<>((Map)value));
            } else {
                Optional<Method> valueOf = Reflect.getMethods(field.getType()).filterParams(String.class).getByName("valueOf",false);
                if (valueOf.isPresent()) {
                    field.set(this, valueOf.get().invoke(null, value.toString()));
                }
            }
        }
    }

    private void init(Map<String,Object> map) {
        RefFields fields = Reflect.getFields(getClass());
        for (Object key : map.keySet()) {
            if (map.get(key) instanceof Map) {
                init((Map) map.get(key));
            }
            if (fields.get((String)key) != null) {
                initField(fields.get((String)key),map.get(key));
            }
        }
    }

    private void initAttributes() {
        if (attributes != null) {
            orgUnit = attributes.get("orgUnit") != null ? attributes.get("orgUnit").toString() : orgUnit;
            passwordExpiryTime = attributes.get("passwordExpiryTime") != null ?
                    new Date((Long) attributes.get("passwordExpiryTime")) : passwordExpiryTime;
            passwordExpiryWarningTime = attributes.get("passwordExpiryWarningTime") != null ?
                    new Date((Long) attributes.get("passwordExpiryWarningTime")) : passwordExpiryWarningTime;
        }
    }

    public String getOrgUnit() {
        return orgUnit;
    }

    public Date getPasswordExpiryTime() {
        return passwordExpiryTime;
    }

    public Date getPasswordExpiryWarningTime() {
        return passwordExpiryWarningTime;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<String> getAud() {
        return aud;
    }

    public void setAud(List<String> aud) {
        this.aud = aud;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        if (authorities.size() == 1 && authorities.get(0).startsWith(COMPRESSED_PREFIX)) {
            this.authorities = decompressRoles(authorities.get(0).substring(COMPRESSED_PREFIX.length()));
        } else {
            this.authorities = authorities;
        }
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Integer getExp() {
        return exp;
    }

    public void setExp(Integer exp) {
        this.exp = exp;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Arrays.asList(1,2,3,4).equals(Arrays.asList(1,2,3,4)));
        DefaultClaims claims = new DefaultClaims();
        claims.setAudience("");
        claims.setExpiration(new Date());
        claims.setId("123");
        claims.setIssuer("");
        claims.setSubject("1323");
        JWTokenClaimDao token = new JWTokenClaimDao(claims);
        Map map = new HashMap();
        map.put("he","1321");
        token.setAttributes(map);
        System.out.println(token);
    }


}
