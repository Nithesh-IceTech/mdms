package za.co.spsi.toolkit.crud.login;

import com.vaadin.cdi.CDIView;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import lombok.Synchronized;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.CrudUI;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.locale.FormattingLocale;
import za.co.spsi.toolkit.crud.util.CrudUAHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.uaa.util.dto.AgencyRoleMap;
import za.co.spsi.uaa.util.dto.JWTokenClaimDao;

import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.ee.util.BeanUtil.*;

/**
 * Created by jaspervdbijl on 2017/02/17.
 */
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class SSOProcessor {

    private static AtomicInteger REQUEST_N = new AtomicInteger(0);
    private static List<String> REQUEST_L = new ArrayList<>();

    @Synchronized("REQUEST_N")
    public static int getNewRequest() {
        int requestN = REQUEST_N.incrementAndGet();
        REQUEST_L.add(""+requestN);
        return requestN;
    }

    @Synchronized("REQUEST_N")
    public static void requestCompleted(String value) {
        REQUEST_L.remove(value);
    }

    @Synchronized("REQUEST_N")
    public static boolean isValidRequest(String value) {
        return REQUEST_L.contains(value);
    }

    public static boolean isSSORequest(VaadinRequest request) {
        return request.getParameter("token") != null && request.getParameter("requestN") != null &&
                SSOProcessor.isValidRequest(request.getParameter("requestN"));
    }

    public static void requestCompleted(VaadinRequest request) {
        requestCompleted(request.getParameter("requestN"));
    }


    public static Logger TAG = Logger.getLogger(SSOProcessor.class.getName());

    @Inject
    CrudUAHelper uaHelper;

    @Inject
    private LoginEventProcessor loginEventProcessor;

    @Inject
    private BeanManager beanManager;

    @Inject
    private javax.enterprise.event.Event<LoginView.LoginEventResponse> loginEventResponseEvent;

    @Inject
    private javax.enterprise.event.Event<LogoutEventProcessor.LogoutEvent> logoutEventEvent;



    public void handleLoginEvent(@Observes SSORequest request) {
        try {
            JWTokenClaimDao claims = uaHelper.getUaHelper().getJwTokenClaimDao(request.token);
            AgencyRoleMap agencyRoleMap = uaHelper.getUaHelper().getAgencyRoleMapFromToken(request.token);
            ToolkitUI.getToolkitUI().setAgencyRoleMap(request.token, agencyRoleMap);
            loginEventProcessor.selectAgency(new LoginView.LoginEventRequest(claims.getUser_name(), ""), request.agency, agencyRoleMap.get(request.agency));


            // Init locale formatting after login
            getBean(beanManager, FormattingLocale.class);
            ((CrudUI) UI.getCurrent()).setUsername(claims.getUser_name());
            ((CrudUI) UI.getCurrent()).setUserRoles(new StringList(agencyRoleMap.get(request.agency)));

            // render the UI
            if (request.embed) {
                CrudView view = getInstanceWithSignature(getBeans(beanManager, CrudView.class), CDIView.class, "value", request.txName);

                MVerticalLayout root = new MVerticalLayout(view).withFullWidth().withFullHeight().withMargin(false);
                view.setSizeFull();
                root.addStyleName("webframe");
                view.addStyleName("main");

                view.clear();
                view.enter(null);

                // open the tx if the id is set
                if (request.txId != null) {
                    Layout layout = (Layout) view.getLayouts().toArray()[0];
                    layout.getMainEntity().getSingleId().set(request.txId);
                    DataSourceDB.loadFromId(layout.getDataSource(), layout.getMainEntity());

                    layout.getLayoutViewGrid().getCallback().selected(layout, layout.getLayoutViewGrid(), false, layout.getMainEntity());
                }

                UI.getCurrent().setContent(root);
            } else {
                try {
                    UI.getCurrent().getNavigator().navigateTo(request.txName);
                } catch (Exception ex) {
                    TAG.log(Level.INFO, ex.getMessage(), ex);
                    Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.SS_SWITCH_VIEW_NOT_AVAILABLE_TITLE),
                            AbstractView.getLocaleValue(ToolkitLocaleId.SS_SWITCH_VIEW_NOT_AVAILABLE_TITLE), Notification.Type.TRAY_NOTIFICATION);
                }
            }
        } catch (Exception ex) {
            TAG.log(Level.WARNING,ex.getMessage(),ex);
            logoutEventEvent.fire(new LogoutEventProcessor.LogoutEvent(""));
        }

    }

    public static class SSORequest {
        protected String token;
        protected String agency;
        protected String txName,txId;
        protected boolean embed = false;

        public SSORequest(VaadinRequest request) {
            this(request.getParameter("token"),request.getParameter("agency"),request.getParameter("txName"),request.getParameter("txId"),
                    request.getParameter("embed") != null?Boolean.valueOf(request.getParameter("embed")):false);
        }

        public SSORequest(String token, String agency, String txName, String txId,boolean embed) {
            this.token = token;
            this.agency = agency;
            this.txName = txName;
            this.txId = txId;
            this.embed = embed;
        }
    }
}
