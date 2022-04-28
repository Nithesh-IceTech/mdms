package za.co.spsi.toolkit.crud.webframe.ee.menu;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.themes.ValoTheme;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.locale.PopupTranslateButton;
import za.co.spsi.toolkit.crud.login.AgencyComboBox;
import za.co.spsi.toolkit.crud.login.LogoutEventProcessor;
import za.co.spsi.toolkit.crud.webframe.WebFrame;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 * Created by jaspervdb on 2016/06/09.
 */
@Dependent
public class WiredWebFrame extends WebFrame {

    @Inject
    @ConfValue("locale")
    private String locales;

    @Inject
    WiredViewMenu menu;

    @Inject
    private PopupTranslateButton translateButton;

    @Inject
    private AgencyComboBox agencyComboBox;

    @Inject
    private javax.enterprise.event.Event<LogoutEventProcessor.LogoutEvent> logoutEventEvent;


    @Inject
    @ConfValue(value = "crest_label_id",agency = true)
    private String crest_label;

    @PostConstruct
    private void init() {
        setMenu(menu);
        ToolkitCrudConstants.setLocales(locales.split(","));
        translateButton.init();
        getHeader().addLeftAction(translateButton);
        getHeader().addRightAction(agencyComboBox);
        getHeader().getRightBtnLayout().setComponentAlignment(agencyComboBox, Alignment.MIDDLE_LEFT);
        agencyComboBox.addStyleName(ValoTheme.COMBOBOX_TINY);
        agencyComboBox.addStyleName(ValoTheme.COMBOBOX_BORDERLESS);
        agencyComboBox.activate();

        Button logoutBtn = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.LOGOUT), FontAwesome.LOCK);
        getHeader().addRightAction(logoutBtn);
        logoutBtn.addClickListener((Button.ClickListener) clickEvent -> {
            logoutEventEvent.fire(new LogoutEventProcessor.LogoutEvent("UI"));
        });
        getMenuFrame().setCrestLabel(AbstractView.getLocaleValue(crest_label));
    }


}
