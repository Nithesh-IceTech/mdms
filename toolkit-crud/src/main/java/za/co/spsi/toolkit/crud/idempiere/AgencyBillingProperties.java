package za.co.spsi.toolkit.crud.idempiere;

import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

/**
 * Created by jaspervdbijl on 2017/05/17.
 */
@Dependent
public class AgencyBillingProperties implements BillingProperties {


    @Inject
    @ConfValue(value = "billingEnabled")
    protected Boolean billingEnabled;

    @Inject
    protected BeanManager beanManager;

    @Inject
    @ConfValue(value = "iceUser")
    public String iceUser = "IceFieldServiceTestUser";

    @Inject
    @ConfValue(value = "icePassword")
    public String icePassword = "sHn6Zmcn6awOSeYp6K6z";

    @Inject
    @ConfValue(value = "iceClientId")
    public Integer iceClientId = 1000002;

    @Inject
    @ConfValue(value = "iceRoleId")
    public Integer iceRoleId = 1000011;

    @Inject
    @ConfValue(value = "iceOrgId")
    public Integer iceOrgId = 1000029;

    @Inject
    @ConfValue(value = "iceWarehouseId")
    public Integer iceWarehouseId = 1000014;

    @Inject
    @ConfValue(value = "iceBaseUrl")
    public String iceBaseUrl = "http://ice-util01:8081";

    public Boolean getBillingEnabled() {
        return billingEnabled;
    }

    public BeanManager getBeanManager() {
        return beanManager;
    }

    @Override
    public String getIceUser() {
        return iceUser;
    }

    @Override
    public String getIcePassword() {
        return icePassword;
    }

    @Override
    public Integer getIceClientId() {
        return iceClientId;
    }

    @Override
    public Integer getIceRoleId() {
        return iceRoleId;
    }

    @Override
    public Integer getIceOrgId() {
        return iceOrgId;
    }

    @Override
    public Integer getIceWarehouseId() {
        return iceWarehouseId;
    }

    @Override
    public String getIceBaseUrl() {
        return iceBaseUrl;
    }
}
