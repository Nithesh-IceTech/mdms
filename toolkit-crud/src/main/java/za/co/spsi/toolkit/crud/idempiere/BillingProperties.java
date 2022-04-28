package za.co.spsi.toolkit.crud.idempiere;

/**
 * Created by jaspervdbijl on 2017/05/17.
 */
public interface BillingProperties {

    public String getIceUser();

    public String getIcePassword() ;

    public Integer getIceClientId();

    public Integer getIceRoleId();

    public Integer getIceOrgId();

    public Integer getIceWarehouseId();

    public String getIceBaseUrl();

    public Boolean getBillingEnabled();

}
