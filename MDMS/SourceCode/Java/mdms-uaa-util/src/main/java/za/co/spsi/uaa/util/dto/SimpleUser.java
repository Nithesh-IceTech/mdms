package za.co.spsi.uaa.util.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.spsi.pjtk.util.StringUtils;

import java.util.Arrays;

/**
 * Created by jaspervdb on 2016/11/24.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data @NoArgsConstructor @AllArgsConstructor
public class SimpleUser {

    private String username,initials,firstName,lastName;

    @JsonIgnore
    public String getDisplayFirstName() {
        return getFirstName() != null?getFirstName():getLastName() != null?getLastName():getUsername();
    }

    @JsonIgnore
    public String getDisplayName() {
        // ensure that you remove duplicates
        return Arrays.stream(String.format("%s %s",
                !StringUtils.isEmpty(getFirstName()) ? getFirstName() : getUsername(),
                !StringUtils.isEmpty(getLastName()) ? getLastName() : "").split(" "))
                .filter(s -> !StringUtils.isEmpty(s.trim())).distinct().reduce((s1,s2) -> s1+" " + s2)
                .get();
    }

}
