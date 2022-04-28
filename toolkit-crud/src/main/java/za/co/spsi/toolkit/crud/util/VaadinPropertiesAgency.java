package za.co.spsi.toolkit.crud.util;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import za.co.spsi.toolkit.crud.gui.render.AgencyThreadLocal;
import za.co.spsi.toolkit.ee.properties.PropertiesAgency;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Default;
import javax.inject.Singleton;

/**
 * Created by jaspervdb on 2016/10/25.
 */
@Dependent
public class VaadinPropertiesAgency implements PropertiesAgency {

    @Override
    public String getAgency() {
        return AgencyThreadLocal.getAgency() != null?
                AgencyThreadLocal.getAgency():
                VaadinSession.getCurrent() != null?
                (String) VaadinSession.getCurrent().getAttribute(VaadinPropertiesAgency.class.getName()):null;
    }

    public static void setAgency(String agency) {
        VaadinSession.getCurrent().setAttribute(VaadinPropertiesAgency.class.getName(),agency);
    }
}
