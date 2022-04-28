package za.co.spsi.toolkit.crud.util;

import com.vaadin.server.VaadinServlet;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.ee.util.VersionUtil;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ettiennelr
 * Date: 2013/08/21
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class VaadinVersionUtil extends VersionUtil {

    public static String VERSION = null;

    public static String getVersion() {
        if (VERSION == null && VaadinServlet.getCurrent() != null) {
            VERSION = getVersion(VaadinServlet.getCurrent().getServletContext(), "/META-INF/");
            VERSION = VERSION == null?"NULL":VERSION;
        }
        return "NULL".equals(VERSION)? AbstractView.getLocaleValue(ToolkitLocaleId.UNKNOWN):VERSION;
    }


    public static void main(String[] str) {
        System.out.println(Long.toString(new Date().getTime()));
    }
}
