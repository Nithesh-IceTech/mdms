package za.co.spsi.uaa.util.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 2016/05/29.
 */
public class UserAgencyRoleMap implements Serializable {

    private AgencyRoleMap agencyRoleMap = new AgencyRoleMap();
    private List<String> agencies = new ArrayList<>();
    private User user;

    public UserAgencyRoleMap() {}

    public UserAgencyRoleMap(User user, List<String> agencies, AgencyRoleMap agencyRoleMap) {
        this.agencyRoleMap = agencyRoleMap;
        this.agencies = agencies;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AgencyRoleMap getAgencyRoleMap() {
        return agencyRoleMap;
    }

    public void setAgencyRoleMap(AgencyRoleMap agencyRoleMap) {
        this.agencyRoleMap = agencyRoleMap;
    }

    public List<String> getAgencies() {
        return agencies;
    }

    public void setAgencies(List<String> agencies) {
        this.agencies = agencies;
    }

    public List<String> filerRoles(List<String> agencies) {
        return agencyRoleMap.keySet().stream().filter(agencies::contains).map(a -> agencyRoleMap.get(a))
                .flatMap(l -> l.stream()).collect(Collectors.toList());
    }
}
