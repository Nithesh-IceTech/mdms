package za.co.spsi.mdms.web.ui;

import com.vaadin.annotations.Push;
import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.shared.ui.ui.Transport;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.web.gui.gis.MdmsDeviceLayout;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@CDIView("devices")
@ViewMenuItem(icon = FontAwesome.CROSSHAIRS, order = 4, value = ToolkitLocaleId.DEVICES, groupName = MdmsLocaleId.MENU_DASHBOARD)
@UIScoped
@Qualifier(roles = {@Role(value = "Supervisor", write = true, create = false, delete = false)})
@Push(value = PushMode.AUTOMATIC, transport = Transport.LONG_POLLING)
public class DeviceView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(MdmsDeviceLayout.class);
    }

    @Override
    protected javax.sql.DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return ToolkitLocaleId.DEVICES;
    }
}
