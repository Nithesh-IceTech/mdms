package za.co.spsi.lookup.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by jaspervdb on 1/21/16.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LookupCodeResult implements Serializable {

    public static final long serialVersionUID = 42L;

    private String agencyId;
    private Date datecreatedD,lastChangeD;
    private Boolean active;
    private String description;

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public Date getDatecreatedD() {
        return datecreatedD;
    }

    public void setDatecreatedD(Date datecreatedD) {
        this.datecreatedD = datecreatedD;
    }

    public Date getLastChangeD() {
        return lastChangeD;
    }

    public void setLastChangeD(Date lastChangeD) {
        this.lastChangeD = lastChangeD;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
