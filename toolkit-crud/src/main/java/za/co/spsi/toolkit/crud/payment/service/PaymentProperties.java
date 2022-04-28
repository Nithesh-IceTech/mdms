package za.co.spsi.toolkit.crud.payment.service;

import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.logging.Logger;

@Dependent
public class PaymentProperties {

    public static final Logger TAG = Logger.getLogger(PaymentProperties.class.getName());

    @Inject
    @ConfValue("oauth.realm")
    private String realm;

    @Inject
    @ConfValue("eco_cash.url")
    private String url;

    @Inject
    @ConfValue("eco_cash.retries")
    private Integer retries;


    @PostConstruct
    public void init() {
        ToolkitCrudConstants.setRealm(realm);
        ToolkitCrudConstants.setEcoCashUrl(url);
        ToolkitCrudConstants.setEcoCashPollingRetry(retries);
    }
}
