package za.co.spsi.mdms.web.ui;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.Widgetset;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.web.locale.MDMSLocaleService;
import za.co.spsi.toolkit.crud.gui.CrudUI;
import za.co.spsi.toolkit.crud.gui.custom.ErrorHandlerDialog;
import za.co.spsi.toolkit.crud.gui.fields.FieldProperty;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.locale.FormattingLocale;
import za.co.spsi.toolkit.crud.locale.VaadinLocaleHelper;
import za.co.spsi.toolkit.crud.login.LoginView;
import za.co.spsi.toolkit.crud.login.SSOProcessor;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.Map;
import java.util.Properties;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;


/**
 * UI class and its init method  is the "main method" for Vaadin apps.
 * But as we are using Vaadin CDI, Navigator and Views, we'll just
 * extend the helper class ViewMenuUI that provides us a top level layout,
 * automatically generated top level navigation and Vaadin Navigator usage.
 * <p>
 * We also configure the theme, host page title and the widgetset used
 * by the application.
 * </p>
 * <p>
 * The real meat of this example is in CustomerView and CustomerForm classes.
 * </p>
 */
@CDIUI("")
@Theme("mdms")
@Title("MDMS")
@Widgetset("AppWidgetset")
@Push()
public class AppUI extends CrudUI {

    static {
        MDMSLocaleService.init();
        FieldProperty.DEFAULT_UPPERCASE = true;
    }

    @Inject
    private BeanManager beanManager;

    @Inject
    @ConfValue("close_tx_on_fatal")
    private boolean closeTxOnFatal;

    @Inject
    @ConfValue("parent_locales")
    private String locales;

    @Inject
    @ConfValue("agency_map")
    private Map<String,Properties> agencyMap;

    @Inject
    @ConfValue("parent_agency_map")
    private String parentAgencyMap;

    @Inject
    private javax.enterprise.event.Event<SSOProcessor.SSORequest> ssRequestEvent;

    /**
     * @return the currently active UI instance with correct type.
     */
    public static AppUI get() {
        return (AppUI) UI.getCurrent();
    }

    @Override
    protected void init(VaadinRequest request) {
        initTheme();
        MDMSLocaleService.init();

        UI.getCurrent().setErrorHandler(new ErrorHandlerDialog(request.getRemoteHost(), new ErrorHandlerDialog.ErrorListener() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                if (closeTxOnFatal && UI.getCurrent().getNavigator() != null) {
                    UI.getCurrent().getNavigator().navigateTo("home");
                }
            }
        }));

        initLocales();

        getPage().setTitle(VaadinLocaleHelper.getValue(MdmsLocaleId.PAGE_TITLE, ToolkitCrudConstants.getLocale()));

        // show login screen
        if (request.getHeader("token") == null) {
            MdmsLoginView loginView = getBean(beanManager, MdmsLoginView.class);
            loginView.buildUI();
            setContent(loginView);
        } else {
            // perform login
            ssRequestEvent.fire(new SSOProcessor.SSORequest(request));
        }
    }


    public void initLocales() {
        ToolkitCrudConstants.setContext("mdms");
        ToolkitCrudConstants.setLocales(locales.split(","));
        ToolkitCrudConstants.setLocale(ToolkitCrudConstants.loadLocale("en"));
        ToolkitCrudConstants.getAsLocale();
    }

    public void handleLogin(@Observes LoginView.LoginEventResponse loginEventResponse) {
        if (loginEventResponse.isLoginOk()) {
            setLoginEventResponse(loginEventResponse);
            // Init locale formatting after login
            getBean(beanManager,FormattingLocale.class);
            setUsername(loginEventResponse.getLoginEventRequest().getUsername());
            setUserRoles(loginEventResponse.getRoles());
            super.init(null);
            setContent(webFrame);
            getUI().getNavigator().navigateTo("home");
        }
    }



}
