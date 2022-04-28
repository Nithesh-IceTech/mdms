package za.co.spsi.toolkit.crud.util;

import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.uaa.ee.util.UAHelper;
import za.co.spsi.uaa.util.dto.TokenResponseDao;
import za.co.spsi.uaa.util.dto.User;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/08/02.
 */
@Dependent
public class CrudUAHelper {

    public static final Logger TAG = Logger.getLogger(CrudUAHelper.class.getName());

    @Inject
    @ConfValue("oauth.realm")
    private String oAuthRealm;

    @Inject
    @ConfValue("oauth.url")
    private String oAuthurl;

    private UAHelper uaHelper = new UAHelper();

    public TokenResponseDao login(String username, String password) {
        TAG.info(String.format("Handle Login event. Realm [%s] Url [%s]",oAuthRealm,oAuthurl));
        return uaHelper.login(oAuthRealm,oAuthurl,username,password);
    }

    public void changePassword(String uid, String curPassword, String newPassword) {
        uaHelper.changePassword(oAuthRealm,oAuthurl,uid,curPassword,newPassword, ToolkitCrudConstants.getLocale());
    }

    public User getUserDetail(String username,String token) {
        return uaHelper.getUserDetail(oAuthRealm,oAuthurl,username, token, ToolkitCrudConstants.getLocale());
    }

    public UAHelper getUaHelper() {
        return uaHelper;
    }
}
