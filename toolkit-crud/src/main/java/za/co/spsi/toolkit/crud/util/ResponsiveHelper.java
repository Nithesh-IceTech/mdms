package za.co.spsi.toolkit.crud.util;

import com.vaadin.server.ClientConnector;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.gui.ano.UILayout;

/**
 * Created by jaspervdb on 2016/09/08.
 */
public class ResponsiveHelper {

    private Component component;
    private UILayout uiLayout;
    private int lastCols = 0;
    private ResponseCallback responseCallback;

    public ResponsiveHelper(Component component, UILayout uiLayout,ResponseCallback responseCallback) {
        this.component = component;
        this.uiLayout = uiLayout;
        this.responseCallback = responseCallback;
        if (uiLayout != null) {
            init();
        }
    }

    public int getCols() {
        return uiLayout != null && Page.getCurrent().getBrowserWindowWidth() > uiLayout.minWidth()?uiLayout.column():1;
    }

    final Page.BrowserWindowResizeListener browserWindowResizeListener = new Page.BrowserWindowResizeListener() {
        @Override
        public void browserWindowResized(Page.BrowserWindowResizeEvent event) {
            if (lastCols != getCols()) {
                lastCols = getCols();
                responseCallback.build(lastCols);
            }
        }
    };

    private void init() {
        lastCols = getCols();
        attachListener();
        component.addDetachListener((ClientConnector.DetachListener) event -> detachListener());
    }

    public void attachListener() {
        Page.getCurrent().addBrowserWindowResizeListener(browserWindowResizeListener);
    }


    public void detachListener() {
        Page.getCurrent().removeBrowserWindowResizeListener(browserWindowResizeListener);
    }

    public static interface ResponseCallback<T extends Component> {
        T build(int cols);
    }
}
