package za.co.spsi.toolkit.crud.gui.menu;

import com.vaadin.cdi.UIScoped;
import com.vaadin.server.Responsive;
import com.vaadin.ui.CssLayout;
import org.vaadin.cdiviewmenu.ViewMenu;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

/**
 * Created by jaspervdb on 2016/05/13.
 */
@Dependent
public class TestInject extends MHorizontalLayout {

//    @Inject
//    WiredViewMenu viewMenu;

    CssLayout content = new CssLayout();

    /**
     * @return the layout to be used for the main content.
     */
    public CssLayout getMainContent() {
        return content;
    }

    //    @PostConstruct
    void init() {
        setSpacing(false);
        setSizeFull();
        /* We are using some CSS magic built into Valo theme
         * for reponsive menu. This adds hints necessary for some
         * supported browsers.
         */
        content.setPrimaryStyleName("valo-content");
        content.addStyleName("v-scrollable");
        content.setSizeFull();
//        addComponents(viewMenu, content);
        expand(content);
        addAttachListener(new AttachListener() {
            @Override
            public void attach(AttachEvent event) {
                Responsive.makeResponsive(getUI());
            }
        });
    }

    public TViewMenu getViewMenu() {
//        return viewMenu;
        return null;
    }
}
