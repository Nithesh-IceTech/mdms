package za.co.spsi.uaa.util.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidatePasswordRequest {

    private String username,password,agencyId;

}
