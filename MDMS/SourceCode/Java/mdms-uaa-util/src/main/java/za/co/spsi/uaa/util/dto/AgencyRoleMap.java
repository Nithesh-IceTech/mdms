package za.co.spsi.uaa.util.dto;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jaspervdb on 2016/05/29.
 */
public class AgencyRoleMap extends HashMap<String,ArrayList<String>> implements Serializable {

    public static final long serialVersionUID = 42L;

    public AgencyRoleMap add(String agencyId,String role) {
        if (!containsKey(agencyId)) {
            put(agencyId,new ArrayList());
        }
        get(agencyId).add(role);
        return this;
    }

    public AgencyRoleMap filter(List<String> activeAgencies) {
        for (int i = 0;i < size();i++) {

        }
        return this;
    }

    public static AgencyRoleMap getFrom(final String delimiter, String ... roles) {
        AgencyRoleMap map = new AgencyRoleMap();
        Arrays.asList(roles).stream().forEach(r -> map.add(r.split(delimiter)[0],r.split(delimiter)[1]));
        return map;
    }

    /**
     * determine if a role is present in any of the agencies
     * @param role
     * @return
     */
    public boolean hasRole(String role) {
        for (String agency : keySet()) {
            if (get(agency).stream().filter(s -> s != null && s.equalsIgnoreCase(role)).findAny().isPresent()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hasAgency(Integer agencyId) {
        if (agencyId == null) return false;
        return keySet().contains(agencyId.toString());
    }

}
