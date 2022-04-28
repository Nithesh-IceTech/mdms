package za.co.spsi.uaa.util.dto;

import lombok.*;

@Data @Builder @ToString @EqualsAndHashCode @NoArgsConstructor @AllArgsConstructor
public class CreateOrUpdateUser {
    private String uid,mail,commonName,givenName,surname,initials,preferredLanguage,organizationalUnit;
}
