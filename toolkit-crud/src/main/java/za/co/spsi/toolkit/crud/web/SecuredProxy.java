package za.co.spsi.toolkit.crud.web;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import za.co.spsi.toolkit.crud.login.LoginView;
import za.co.spsi.toolkit.crud.login.LogoutEventProcessor;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.ExpiringCacheMap;
import za.co.spsi.uaa.util.error.AuthorisationException;

import javax.enterprise.event.Observes;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public abstract class SecuredProxy extends ProxyServlet {

    private static ExpiringCacheMap<String,String> whiteList = new ExpiringCacheMap<>(TimeUnit.HOURS.toMillis(5));

    public abstract String getTargetUri();

    public void handleLogin(@Observes LoginView.LoginEventResponse loginEventResponse) {
        if (loginEventResponse.isLoginOk())
            whiteList.put(loginEventResponse.getLoginEventRequest().getIpAddress(),
                    loginEventResponse.getLoginEventRequest().getIpAddress());
        System.out.println("Handle Login event");
    }

    public void handleLogout(@Observes LogoutEventProcessor.LogoutEvent logoutEvent) {
        whiteList.remove(logoutEvent.getIpAddress());
    }

    @Override
    protected String getConfigParam(String key) {
        if (P_TARGET_URI.equals(key)) {
            return getTargetUri();
        }
        return this.getServletConfig().getInitParameter(key);
    }

    @Override
    protected void service(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        // Validate the Authorization header
        Assert.isTrue(AuthorisationException.class,whiteList.containsKey(servletRequest.getRemoteAddr()),"Not white listed");
        super.service(servletRequest,servletResponse);
    }
}
