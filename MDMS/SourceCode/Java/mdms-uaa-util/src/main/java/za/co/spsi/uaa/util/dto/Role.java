package za.co.spsi.uaa.util.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class Role {


    private String distName,commonName;

    private List<String> uniqueMembers;
}
