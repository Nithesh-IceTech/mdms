package za.co.spsi.toolkit.crud.gui;

/**
 * Created by jaspervdb on 2016/05/10.
 */

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.CDIViewProvider;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.login.LoginView;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.crud.util.broadcast.BroadcastListener;
import za.co.spsi.toolkit.crud.util.broadcast.BroadcastRegister;
import za.co.spsi.toolkit.crud.webframe.ee.menu.WiredWebFrame;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.entity.Entity;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;
import static za.co.spsi.toolkit.ee.util.BeanUtil.getBeans;
import static za.co.spsi.toolkit.ee.util.BeanUtil.getInstanceWithSignature;

/**
 * A helper class with basic main layout with ViewMenu and ViewMenuLayout,
 * configures Navigator automatically. This way you'll get professional looking
 * basic application structure for free.
 * <p>
 * In your own app, override this class and map it with CDIUI annotation.
 */
public class CrudUI extends ToolkitUI implements BroadcastListener {

    @Inject
    @ConfValue("theme")
    protected String theme;

    @Inject
    protected CDIViewProvider viewProvider;


    protected WiredWebFrame webFrame;

    private LoginView.LoginEventResponse loginEventResponse;

    public static final Logger TAG = Logger.getLogger(CrudUI.class.getName());


    @Inject
    BeanManager beanManager;

    protected void initTheme() {
        if (theme != null) {
            setTheme(theme);
        }
    }

    @Override
    protected void init(VaadinRequest request) {
        webFrame = getBean(beanManager, WiredWebFrame.class);
        webFrame.getHeader().setContextLabel(String.format("<p>%s</p>System Administrator", loginEventResponse.getLoginEventRequest().getUsername()));

        Navigator navigator = new Navigator(this, webFrame.getMain()) {

            @Override
            public void navigateTo(String navigationState) {
                try {
                    super.navigateTo(navigationState);
                } catch (Exception e) {
                    handleNavigationError(navigationState, e);
                }
            }

            @Override
            public boolean beforeViewChange(final ViewChangeListener.ViewChangeEvent event) {
                if (event.getParameters().indexOf("NW/") != -1) {
                    try {

                        // load the class from the name
                        CrudView view = getInstanceWithSignature(getBeans(beanManager, CrudView.class), CDIView.class,
                                "value", event.getViewName());
                        String params[] = event.getParameters().split("/");
                        openViewInWindow(view, params[params.length - 1]);

                        return false;
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    // process single signon
                } else {
                    return super.beforeViewChange(event);
                }
            }

        };
        navigator.addProvider(viewProvider);
        BroadcastRegister.register(this);
    }

    @Override
    public void detach() {
        BroadcastRegister.unregister(this);
        super.detach();
    }

    public Window openViewInWindow(Class<? extends CrudView> viewClass,String id) {
        return openViewInWindow(getBean(beanManager,viewClass),id);
    }

    public Window openViewInWindow(Class<? extends CrudView> viewClass,Entity entity) {
        return openViewInWindow(getBean(beanManager,viewClass),entity);
    }

    public Window openViewInWindow(CrudView view,String id) {
        view.clear();
        view.enter(null);

        Layout layout = (Layout) view.getLayouts().toArray()[0];
        layout.getMainEntity().getSingleId().set(id);
        DataSourceDB.loadFromId(layout.getDataSource(), layout.getMainEntity());
        return openViewInWindow(view,layout.getMainEntity());
    }

    private Window getCrudWindow(CrudViewable view) {
        Window window = new Window();
        window.setHeight("90%");
        window.setWidth("90%");
        MVerticalLayout root = new MVerticalLayout(view.getRoot()).
                withFullHeight().withStyleName("main");
        window.setContent(new MVerticalLayout(root).withFullHeight().withStyleName("webframe").withMargin(false));
        window.center();
        window.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent e) {
                // release all tx's
                view.releaseAllTx();
            }
        });
        return window;

    }
    public Window displayViewInWindow(CrudView view,boolean openSelected) {

        if (openSelected) {
            Layout layout = (Layout) view.getLayouts().toArray()[0];
            layout.getLayoutViewGrid().getCallback().selected(layout, layout.getLayoutViewGrid(),false, layout.getMainEntity());
//            layout.beforeOnScreenEvent();
        }

        Window window = getCrudWindow(view);
        UI.getCurrent().addWindow(window);
        return window;
    }

    public Window displayViewsInWindow(CrudView ... view) {
        CrudViewSheet sheet = new CrudViewSheet(view);
        sheet.enter(null);
        Window window = getCrudWindow(sheet);

        UI.getCurrent().addWindow(window);
        return window;
    }


    public Window openViewInWindow(CrudView view,Entity entity) {
        view.clear();
        view.enter(null);

        Layout layout = (Layout) view.getLayouts().toArray()[0];
        if (entity != layout.getMainEntity()) {
            layout.getMainEntity().copyStrict(entity);
        }
        return displayViewInWindow(view,true);
    }

    public static CrudUI getCrudUI() {
        return (CrudUI) UI.getCurrent();
    }


    /**
     * Workaround for issue 1, related to vaadin issues: 13566, 14884
     *
     * @param navigationState the view id that was requested
     * @param e               the exception thrown by Navigator
     */
    protected void handleNavigationError(String navigationState, Exception e) {
        VaadinNotification.show(
                "The requested view (" + navigationState + ") was not available, "
                        + "entering default screen.", Notification.Type.WARNING_MESSAGE);
        if (navigationState != null && !navigationState.isEmpty()) {
            getNavigator().navigateTo("");
        }
        getSession().getErrorHandler().error(new com.vaadin.server.ErrorEvent(e));
    }

    public void setLoginEventResponse(LoginView.LoginEventResponse loginEventResponse) {
        this.loginEventResponse = loginEventResponse;
    }

    @Override
    public void receiveBroadcast(String message) {
        access(() -> Notification.show(message, Notification.Type.TRAY_NOTIFICATION));
    }
}