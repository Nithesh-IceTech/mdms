package za.co.spsi.mdms.web;

import com.vaadin.annotations.VaadinServletConfiguration;
import za.co.spsi.mdms.web.ui.AppUI;
import za.co.spsi.toolkit.crud.web.GoogleMapServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Normally with Vaadin CDI, the servlet is automatically introduced. If you
 * need to customize stuff in the servlet or host page generation, you can still
 * do that. In this example we use a servlet implementation that adds a viewport
 * meta tag to the host page. It is essential essential for applications that
 * have designed the content to be suitable for smaller screens as well.
 */
@WebServlet(urlPatterns = {"/UI/*","/UI*","/VAADIN/*", "/UIDL"}, asyncSupported = true)
@VaadinServletConfiguration(ui = AppUI.class,productionMode = false)
public class MdmsServlet extends GoogleMapServlet {

}
