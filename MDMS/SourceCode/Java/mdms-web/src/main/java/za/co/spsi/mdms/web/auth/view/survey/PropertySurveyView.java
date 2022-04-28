package za.co.spsi.mdms.web.auth.view.survey;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.web.gui.layout.surveys.PecPropertyLayout;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*
 */
@CDIView("property")
@ViewMenuItem(icon = FontAwesome.HOME,order = 4,value = MdmsLocaleId.MENU_PROPERTY,groupName = MdmsLocaleId.MENU_SURVEYS)
@UIScoped
@Qualifier(roles = {@Role(value = "PecPropVer")})
public class PropertySurveyView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(PecPropertyLayout.class);
    }

    @Override
    protected javax.sql.DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return MdmsLocaleId.PROPERTY_CAPTION;
    }


}
