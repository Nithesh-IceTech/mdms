package za.co.spsi.toolkit.crud.gui.render;

import com.vaadin.server.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by francoism on 2016/08/24.
 */
public class VaadinNotification {

    private static final int DEFAULT_TIMEOUT = 1000;

    public static void show(String caption, String description, Integer delay, com.vaadin.ui.Notification.Type vaadinNotificationType){
        com.vaadin.ui.Notification notif = new com.vaadin.ui.Notification(caption, description, vaadinNotificationType);
        notif.setDelayMsec(delay == null?DEFAULT_TIMEOUT:delay);
        notif.show(Page.getCurrent());
    }

    public static void show(String caption, String description, com.vaadin.ui.Notification.Type vaadinNotificationType){
        show(caption,description,null,vaadinNotificationType);
    }

    public static void show(String description, Integer delay, com.vaadin.ui.Notification.Type vaadinNotificationType){
        com.vaadin.ui.Notification notif = new com.vaadin.ui.Notification(description, vaadinNotificationType);
        notif.setDelayMsec(delay == null?DEFAULT_TIMEOUT:delay);
        notif.show(Page.getCurrent());
    }

    public static void show(String description, com.vaadin.ui.Notification.Type vaadinNotificationType){
        show(description,(Integer)null,vaadinNotificationType);
    }

    public static void show(String description, Integer delay){
        com.vaadin.ui.Notification notif = new com.vaadin.ui.Notification(description);
        notif.setDelayMsec(delay == null?DEFAULT_TIMEOUT:delay);
        notif.show(Page.getCurrent());
    }

    public static void show(String description){
        show(description,(Integer)null);
    }

}
