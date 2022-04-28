package za.co.spsi.toolkit.crud.login;


import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.locale.PopupTranslateButton;
import za.co.spsi.toolkit.crud.util.LoginHelper;
import za.co.spsi.toolkit.crud.util.VaadinVersionUtil;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.Map;
import java.util.Properties;

/**
 * UI content when the user is not logged in yet.
 */
public abstract class LoginView extends CssLayout {


    private TextField username;
    private PasswordField password;
    private Button login, changePassword;

    @Inject
    private PopupTranslateButton translateButton;

    @Inject
    private javax.enterprise.event.Event<LoginEventRequest> loginEventRequestEvent;

    private String htmlHeader, logoImage;

    @Inject
    private ToolkitLookupServiceHelper lookupServiceHelper;

    @Inject
    @ConfValue("agency_map")
    private Map<String, Properties> agencyMap;

    @Inject
    LoginHelper loginHelper;

    public LoginView() {
    }

    public LoginView(String htmlHeader, String logoImage) {
        setHtmlHeader(htmlHeader);
        setLogoImage(logoImage);
    }

    @PostConstruct
    private void init() {
        translateButton.init();
    }

    public void setHtmlHeader(String htmlHeader) {
        this.htmlHeader = htmlHeader;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public abstract String getParentAgency();

    public void focus() {
        username.focus();
    }

    public PopupTranslateButton getTranslateButton() {
        return translateButton;
    }

    public void buildUI() {
        addStyleName("login-screen");

        // login form, centered in the available part of the screen
        Component loginForm = buildLoginForm();

        VerticalLayout centeringLayout = new VerticalLayout();
        centeringLayout.setStyleName("centering-layout");
        centeringLayout.addComponent(loginForm);
        centeringLayout.setComponentAlignment(loginForm,
                Alignment.MIDDLE_CENTER);

        // information text about logging in
        CssLayout loginInformation = buildLoginInformation(htmlHeader, logoImage);

        addComponent(centeringLayout);
        addComponent(loginInformation);
    }


    private Component buildLoginForm() {
        final FormLayout loginForm = new FormLayout();

        loginForm.addStyleName("login-form");
        loginForm.setSizeUndefined();
        loginForm.setMargin(false);

        loginForm.addComponent(username = new TextField(AbstractView.getLocaleValue(ToolkitLocaleId.USERNAME)));
        username.setWidth(15, Unit.EM);
        loginForm.addComponent(password = new PasswordField(AbstractView.getLocaleValue(ToolkitLocaleId.PASSWORD)));
        password.setWidth(15, Unit.EM);
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setStyleName("buttons");
        buttons.setSpacing(true);
        buttons.setWidth("100%");
        loginForm.addComponent(buttons);

        buttons.addComponent(login = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.LOGIN)));
        login.setDisableOnClick(true);
        login.addClickListener((Button.ClickListener) event ->
                loginEventRequestEvent.fire(new LoginEventRequest(username.getValue(), password.getValue())));
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        login.addStyleName(ValoTheme.BUTTON_PRIMARY);

        buttons.addComponent(changePassword = new Button(FontAwesome.KEY));
        changePassword.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.CHANGE_PASSWORD));
        changePassword.addClickListener((Button.ClickListener) event ->
                loginHelper.changePassword(username.getValue(), password.getValue()));
        changePassword.addStyleName(ValoTheme.BUTTON_PRIMARY);

        buttons.addComponent(translateButton);
        buttons.setComponentAlignment(translateButton, Alignment.BOTTOM_RIGHT);
        translateButton.addStyleName(ValoTheme.BUTTON_LINK);

        translateButton.addClickListener((Button.ClickListener) event -> login.focus());
        return loginForm;
    }

    public static CssLayout buildLoginInformation(String htmlHeader, String logoImage) {
        CssLayout loginInformation = new CssLayout();
        loginInformation.setStyleName("login-information");
        ThemeResource logo = new ThemeResource(logoImage);
        loginInformation.addComponent(new Embedded("", logo));
        Label loginInfoText = new Label(htmlHeader, ContentMode.HTML);
        loginInformation.addComponent(loginInfoText);
        HorizontalLayout buildInfo = new HorizontalLayout(new Label(AbstractView.getLocaleValue(ToolkitLocaleId.VERSION), ContentMode.HTML),
                new Label(VaadinVersionUtil.getVersion()));
        buildInfo.setSpacing(true);
        loginInformation.addComponent(buildInfo);
        return loginInformation;
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDelayMsec(2000);
        notification.show(Page.getCurrent());
    }


    /**
     * codes are mapping to three letter string names
     *
     * @param agencyId
     * @return
     */
    private String getAgencyMapName(String agencyId) {
        return agencyMap.get(agencyId).getProperty("theme");
    }

    public void handleLoginEvent(@Observes LoginEventResponse loginEventResponse) {
        if (!loginEventResponse.isLoginOk()) {
            login.setEnabled(true);
            changePassword.setEnabled(true);
        }
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    public static class LoginEventRequest {

        private String username, password;
        private boolean passwordChangeDeclined = false;
        private String token,ipAddress;

        public LoginEventRequest(String username, String password) {
            this.username = username;
            this.password = password;
            this.ipAddress = UI.getCurrent().getPage().getWebBrowser().getAddress();
        }

    }

    /*
    request, agency, lookup.getDescription(),
                new StringList(agencyRoleMap.get(agency)), true));
     */
    @Data @AllArgsConstructor @NoArgsConstructor
    public static class LoginEventResponse {
        private LoginEventRequest loginEventRequest;
        private String agencyId, agencyName;
        private StringList roles;
        private boolean loginOk = false;

        public LoginEventResponse(LoginEventRequest loginEventRequest, boolean loginOk) {
            this.loginEventRequest = loginEventRequest;
            this.loginOk = loginOk;
            Assert.isTrue(!loginOk, "Can only use this constructor for failed logins");
        }
    }

}
