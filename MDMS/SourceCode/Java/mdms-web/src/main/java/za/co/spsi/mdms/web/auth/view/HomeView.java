package za.co.spsi.mdms.web.auth.view;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.VerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;

/*
 */
@CDIView("home")
@ViewMenuItem(icon = FontAwesome.HOME,order = 0,value = ToolkitLocaleId.MENU_HOME)
public class HomeView extends VerticalLayout implements View {

    @PostConstruct
    private void init() {
        addStyleName("login-bg");
        setSizeFull();
//        addComponents(crest);

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }


}
