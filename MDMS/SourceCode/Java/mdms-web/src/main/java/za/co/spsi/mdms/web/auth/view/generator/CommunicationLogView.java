package za.co.spsi.mdms.web.auth.view.generator;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.web.gui.layout.generator.CommunicationLogLayout;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*
 */
@CDIView("comms_log")
@ViewMenuItem(icon = FontAwesome.PHONE,order = 4,value = MdmsLocaleId.MENU_COMMUNICATION_LOGS,groupName = MdmsLocaleId.MENU_GENERATOR)
@Qualifier(roles = {@Role(value = "PecSurveyVer")})
@UIScoped
public class CommunicationLogView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(CommunicationLogLayout.class);
    }

    @Override
    protected javax.sql.DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return MdmsLocaleId.MENU_COMMUNICATION_LOGS;
    }


}
