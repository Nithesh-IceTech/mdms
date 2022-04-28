package za.co.spsi.mdms.web.gui.layout.surveys;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gis.gui.DeviceTrackMap;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.inject.Inject;


/*
 */
@CDIView("device_tracking")
@ViewMenuItem(icon = FontAwesome.CROSSHAIRS, order = 3, value = ToolkitLocaleId.MENU_DEVICE_TRACKING, groupName = MdmsLocaleId.MENU_DASHBOARD)
@Qualifier(roles = {@Role(value = "Supervisor", write = false, create = false, delete = false)})
public class DeviceTrackView extends MVerticalLayout implements View {

    @Inject
    private DeviceTrackMap deviceTrackMap;

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        setSizeFull();
        setSpacing(false);
        setMargin(false);
        deviceTrackMap.setSizeFull();
        deviceTrackMap.init();
        add(deviceTrackMap);
    }
}
