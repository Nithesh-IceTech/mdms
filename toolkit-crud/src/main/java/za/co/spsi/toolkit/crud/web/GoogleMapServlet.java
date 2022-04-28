package za.co.spsi.toolkit.crud.web;

import com.vaadin.cdi.server.VaadinCDIServlet;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.SessionInitListener;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;

/**
 * Normally with Vaadin CDI, the servlet is automatically introduced. If you
 * need to customize stuff in the servlet or host page generation, you can still
 * do that. In this example we use a servlet implementation that adds a viewport
 * meta tag to the host page. It is essential essential for applications that
 * have designed the content to be suitable for smaller screens as well.
 */
public class GoogleMapServlet extends VaadinCDIServlet {


    private static String key = "AIzaSyABlEc3ifYX0YDYNZYDx_dvqV7F3fTsvrc";

    @Override
    protected void servletInitialized() throws ServletException {
        super.servletInitialized();
        getService().addSessionInitListener((SessionInitListener) event -> event.getSession().addBootstrapListener(new BootstrapListener() {

            @Override
            public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
            }

            @Override
            public void modifyBootstrapPage(BootstrapPageResponse response) {
                Element script = response.getDocument().createElement("script");
                // FOR NON LOCAL TESTING, ADD API KEY!!
                // AIzaSyABlEc3ifYX0YDYNZYDx_dvqV7F3fTsvrc
                script.attr("src", String.format("https://maps.google.com/maps/api/js?key=%s",key));
                response.getDocument().getElementsByTag("head").get(0).appendChild(script);

                script = response.getDocument().createElement("script1");
                script.attr("src", "location.js");
                response.getDocument().getElementsByTag("head").get(0).appendChild(script);
            }
        }));
    }

}
