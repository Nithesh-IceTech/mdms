package za.co.spsi.mdms.web.auth.view;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.mdms.web.gui.layout.NesMeterResultLayout;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*
 */
@CDIView("nes_meter_result")
@ViewMenuItem(value = "Echelon",icon = FontAwesome.PICTURE_O,order = 6,groupName = "Smart Meter Orders")
@UIScoped
@Qualifier(roles = {@Role(value = "PecMeterVer")})
public class NesMeterResultView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(NesMeterResultLayout.class);
    }

    @Override
    protected javax.sql.DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return "Echelon Results";
    }
}
