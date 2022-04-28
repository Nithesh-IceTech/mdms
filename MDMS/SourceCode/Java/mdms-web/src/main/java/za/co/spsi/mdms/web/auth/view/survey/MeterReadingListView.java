package za.co.spsi.mdms.web.auth.view.survey;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.web.gui.layout.surveys.PecMeterReadingListLayout;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*
 */
@CDIView("meter_reading_list")
@ViewMenuItem(icon = FontAwesome.MAP_MARKER,order = 4,value = MdmsLocaleId.MENU_METER_READING_LIST,groupName = MdmsLocaleId.MENU_SURVEYS)
@UIScoped
@Qualifier(roles = {@Role(value = "PecMeterVer")})
public class MeterReadingListView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(PecMeterReadingListLayout.class);
    }

    @Override
    protected javax.sql.DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return MdmsLocaleId.METER_READING_LIST_CAPTION;
    }


}
