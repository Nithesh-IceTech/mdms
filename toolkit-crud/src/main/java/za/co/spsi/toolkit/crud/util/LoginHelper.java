package za.co.spsi.toolkit.crud.util;

import com.vaadin.server.Page;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.login.LoginView;
import za.co.spsi.toolkit.util.StringUtils;
import za.co.spsi.uaa.util.error.AuthorisationException;
import za.co.spsi.uaa.util.error.PasswordPreviouslyUsedException;
import za.co.spsi.uaa.util.error.UAException;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

/**
 * Created by jaspervdb on 2016/08/02.
 */
@Dependent
public class LoginHelper {

    @Inject
    private CrudUAHelper uaHelper;

    @Inject
    private javax.enterprise.event.Event<LoginView.LoginEventRequest> loginEventRequestEvent;


    private boolean isEmpty(AbstractTextField... fields) {
        for (AbstractTextField textField : fields) {
            if (StringUtils.isEmpty(textField.getValue())) {
                return true;
            }
        }
        return false;
    }

    public void passwordAboutToExpire(String username, String password,int days) {
        MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.CHANGE_PASSWORD))
                .withMessage(String.format(AbstractView.getLocaleValue(ToolkitLocaleId.PASSWORD_ABOUT_TO_EXPIRE_IN),days))
                .withOkButton(() -> {
                    changePassword(username,password);
                },ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()), ButtonOption.closeOnClick(true)).
                withCancelButton(() -> loginEventRequestEvent.fire(new LoginView.LoginEventRequest(
                        username, password,true,null, Page.getCurrent().getWebBrowser().getAddress())),
                        ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true)).open();

    }
    public void changePassword(String username, String password) {
        TextField usernameField = new TextField(AbstractView.getLocaleValue(ToolkitLocaleId.USERNAME));
        PasswordField currPasswordField = new PasswordField(AbstractView.getLocaleValue(ToolkitLocaleId.CURRENT_PASSWORD));
        PasswordField newPassword1Field = new PasswordField(AbstractView.getLocaleValue(ToolkitLocaleId.PASSWORD));
        PasswordField newPassword2Field = new PasswordField(AbstractView.getLocaleValue(ToolkitLocaleId.REPEAT_PASSWORD));
        usernameField.setValue(username);

        FormLayout root = new FormLayout(usernameField,currPasswordField,newPassword1Field,newPassword2Field);
        root.setSizeUndefined();

        MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.CHANGE_PASSWORD))
                .withMessage(new MVerticalLayout(root))
                .withOkButton(() -> {
                    if (isEmpty(usernameField)) {
                        VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.USERNAME_REQUIRED),
                                Notification.Type.ERROR_MESSAGE);
                    } else if (isEmpty(currPasswordField,newPassword1Field,newPassword2Field)) {
                        VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.PASSWORD_REQUIRED),
                                Notification.Type.ERROR_MESSAGE);
                    } else if (!newPassword1Field.getValue().equals(newPassword2Field.getValue())) {
                        VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.PASSWORD_DO_NOT_MATCH),
                                Notification.Type.ERROR_MESSAGE);
                    } else {
                        try {
                            uaHelper.changePassword(usernameField.getValue(), currPasswordField.getValue(), newPassword1Field.getValue());
                            loginEventRequestEvent.fire(new LoginView.LoginEventRequest(usernameField.getValue(), newPassword1Field.getValue()));
                            return;
                        } catch (AuthorisationException aue) {
                            // incorrect password
                            VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.WRONG_USERNAME_OR_PASSWORD),
                                    Notification.Type.ERROR_MESSAGE);
                            changePassword(username, password);
                        } catch (PasswordPreviouslyUsedException ppe) {
                            VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.PASSWORD_PREVIOUSLY_USER),
                                    Notification.Type.ERROR_MESSAGE);
                            changePassword(username, password);
                        } catch (UAException ua) {
                            VaadinNotification.show(AbstractView.getLocaleValue(ua.getErrorDescription()), Notification.Type.ERROR_MESSAGE);
                            changePassword(username, password);
                        }
                    }
                }, ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()), ButtonOption.closeOnClick(true)).
                withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true)).open();

    }

}
