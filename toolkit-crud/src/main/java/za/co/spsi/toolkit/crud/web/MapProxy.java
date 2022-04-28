package za.co.spsi.toolkit.crud.web;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = {"/map/*"}, asyncSupported = true)
public class MapProxy extends SecuredProxy {

    public static final String TARGET_URI = "env.global.map.proxy.targetUri";
    @Override
    public String getTargetUri() {
        return System.getProperty(TARGET_URI);
    }
}
