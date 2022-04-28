package za.co.spsi.mdms.web.auth.view;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.mdms.web.gui.layout.NesMeterLayout;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*
 */
@CDIView("nes_meter")
@ViewMenuItem(value = "Echelon",icon = FontAwesome.PICTURE_O,order = 5,groupName = "Smart Meters")
@UIScoped
@Qualifier(roles = {@Role(value = "PecMeterVer")})
public class NesMeterView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(NesMeterLayout.class);
    }

    @Override
    protected javax.sql.DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return "Echelon Meters";
    }
}
