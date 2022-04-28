package za.co.spsi.toolkit.crud.login;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;

@Dependent
public class LogoutEventProcessor {

    public void handleLogin(@Observes LogoutEvent logoutEvent) {
        VaadinSession.getCurrent().close();
        UI.getCurrent().getPage().setLocation(logoutEvent.location);

    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class LogoutEvent {
        private String username,ipAddress,location;

        public LogoutEvent(String location) {
            this.location = location;
            this.username = ToolkitUI.getToolkitUI().getUsername();
            this.ipAddress = Page.getCurrent().getWebBrowser().getAddress();
        }
    }
}
