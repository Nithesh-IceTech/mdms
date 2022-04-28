package za.co.spsi.uaa.util.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import za.co.spsi.uaa.util.Constants;

import java.io.Serializable;

/**
 * Created by jaspervdb on 2016/05/30.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class TokenResponseDao implements Serializable {

    @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("refresh_token")
    private String refreshToken;
    private String scope;
    private String jti;

    @JsonProperty(Constants.COMPRESS_TOKEN)
    private String compressedToken;

    @JsonProperty("expires_in")
    private Long expiresIn;
    private Attributes attributes;


}
