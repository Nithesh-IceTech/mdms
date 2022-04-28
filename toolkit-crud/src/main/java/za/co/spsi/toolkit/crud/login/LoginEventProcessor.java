package za.co.spsi.toolkit.crud.login;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.toolkit.crud.gui.CrudUI;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.util.AgencyHelper;
import za.co.spsi.toolkit.crud.util.CrudUAHelper;
import za.co.spsi.toolkit.crud.util.LoginHelper;
import za.co.spsi.toolkit.crud.util.VaadinPropertiesAgency;
import za.co.spsi.toolkit.crud.util.broadcast.BroadcastRegister;
import za.co.spsi.toolkit.db.audit.AuditHelper;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.uaa.util.dto.AgencyRoleMap;
import za.co.spsi.uaa.util.dto.TokenResponseDao;
import za.co.spsi.uaa.util.error.*;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;
import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created by jaspervdb on 2015/08/17.
 */
public abstract class LoginEventProcessor implements Serializable {

    @Inject
    protected AgencyHelper agencyHelper;

    @Inject
    protected javax.enterprise.event.Event<LoginView.LoginEventRequest> loginEventRequestEvent;

    @Inject
    protected javax.enterprise.event.Event<LoginView.LoginEventResponse> loginEventResponseEvent;

    @Inject
    protected javax.enterprise.event.Event<AgencyHelper.AgencySetEvent> agencySetEventEvent;

    @Inject
    protected BeanManager beanManager;

    @Inject
    CrudUAHelper uaHelper;

    @Inject
    LoginHelper loginHelper;

    public static final Logger TAG = Logger.getLogger(LoginEventProcessor.class.getName());

    public abstract String getContext();

    protected abstract DataSource getDataSource();

    public void selectAgency(LoginView.LoginEventRequest request, String agency, List<String> roles) {
        BroadcastRegister.map((CrudUI) UI.getCurrent(), Integer.parseInt(agency));
        LookupResult lookup = agencyHelper.getLookupResults().getForCode(agency);
        ToolkitCrudConstants.setContext(getContext());

        if (agencyHelper.getAgencyMap().get(agency) == null || agencyHelper.getAgencyMap().get(agency).isEmpty()) {
            VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.USER_HAS_NO_AGENCIES), Notification.Type.ERROR_MESSAGE);
            loginEventResponseEvent.fire(new LoginView.LoginEventResponse(request, false));
            return;
        }

        UI.getCurrent().setTheme(String.valueOf(agencyHelper.getAgencyMap().get(agency).get("theme")));
        ToolkitCrudConstants.setAgencyId(agencyHelper.getLookupServiceHelper().getAgencyHierarchyUp(lookup.getLookupCode()));
        // set properties agency config
        VaadinPropertiesAgency.setAgency(agency);
        agencySetEventEvent.fire(new AgencyHelper.AgencySetEvent(agency));
        loginEventResponseEvent.fire(new LoginView.LoginEventResponse(request, agency, lookup.getDescription(),
                new StringList(roles), true));

        // config auditing
        AuditHelper.setUIDCallback(() -> ToolkitUI.getToolkitUI() != null ? ToolkitUI.getToolkitUI().getUsername() : null);
        AuditHelper.setUIDRoleCallback(() ->
                ToolkitUI.getToolkitUI() != null && ToolkitUI.getToolkitUI().getUserRoles() != null ?
                        ToolkitUI.getToolkitUI().getUserRoles().containsIgnoreCase(ToolkitCrudConstants.ROLE_SUPERVISOR) : false);
    }

    protected void selectAgency(LoginView.LoginEventRequest request, AgencyRoleMap agencyRoleMap) {
        if (agencyRoleMap.keySet().size() == 1) {
            String agency = agencyRoleMap.keySet().iterator().next();
            selectAgency(request, agency, agencyRoleMap.get(agency));
        } else {
            StringList agencies = new StringList(agencyRoleMap.keySet());
            // remove master
            agencies.remove("0");
            if (!agencies.isEmpty()) {
                AgencyComboBox agencyBox = getBean(beanManager, AgencyComboBox.class);
                MessageBox.createQuestion().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.SELECT_AGENCY_HEADING))
                        .withMessage(agencyBox)
                        .withOkButton(() -> {
                            String agency = agencyBox.getValue().toString();
                            selectAgency(request, agency, agencyRoleMap.get(agency));
                        }, ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()), ButtonOption.closeOnClick(true)).open();
            } else {
                VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.USER_HAS_NO_AGENCIES), Notification.Type.ERROR_MESSAGE);
            }
        }
    }

    protected void processLogin(LoginView.LoginEventRequest request, TokenResponseDao tokenResponseDao) {
        AgencyRoleMap agencyRoleMap = uaHelper.getUaHelper().getAgencyRoleMapFromToken(tokenResponseDao);
        ToolkitUI.getToolkitUI().setAgencyRoleMap(tokenResponseDao.getAccessToken(), agencyRoleMap);

        if (!agencyRoleMap.keySet().isEmpty()) {
            request.setToken(tokenResponseDao.getAccessToken());
            UserDetailEntity userDetail = UserDetailEntity.init(getDataSource(), uaHelper, request, tokenResponseDao);
            if (userDetail.ou.get() != null) {
                ToolkitCrudConstants.setChildrenAgencyIds(agencyHelper.getLookupServiceHelper().getAgencyHierarchyDown(userDetail.ou.get()));
            }

            // fix case
            request.setUsername(userDetail.username.get());
            selectAgency(request, agencyRoleMap);
        } else {
            VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.USER_HAS_NO_AGENCIES), Notification.Type.ERROR_MESSAGE);
        }
    }

    public void handleLoginEvent(@Observes LoginView.LoginEventRequest request) {
        try {
            TAG.info("Handle login event " + request.getUsername());
            TokenResponseDao tokenResponseDao = uaHelper.login(request.getUsername(), request.getPassword());

//            TAG.info("LOGIN TOKEN: " + tokenResponseDao.toString());
            processLogin(request, tokenResponseDao);
            return;
        } catch (InvalidCredentialsException ive) {
            VaadinNotification.show(!ive.getErrorDescription().isEmpty() ? ive.getErrorDescription() : ive.getErrorDetail(), Notification.Type.ERROR_MESSAGE);
        } catch (ChangePasswordRequiredException cpr) {
            loginHelper.changePassword(request.getUsername(), request.getPassword());
        } catch (AccountLockedException ale) {
            Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.ACCOUNT_LOCKED), Notification.Type.ERROR_MESSAGE);
        } catch (PasswordAboutToExpireException pate) {
            if (!request.isPasswordChangeDeclined()) {
                loginHelper.passwordAboutToExpire(request.getUsername(), request.getPassword(), pate.getDaysBeforeExpiry());
            } else {
                processLogin(request, pate.getTokenResponseDao());
            }
        } catch (UAException ue) {
            VaadinNotification.show(ue.getError(), Notification.Type.ERROR_MESSAGE);
        } catch (Exception ue) {
            TAG.log(Level.WARNING, ue.getMessage(), ue);
            Notification.show(ue.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
        loginEventResponseEvent.fire(new LoginView.LoginEventResponse(request, false));
    }

}
