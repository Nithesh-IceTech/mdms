package za.co.spsi.mdms.web.auth.view.survey;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.web.gui.layout.surveys.PecLocationSurveyLayout;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*
 */
@CDIView("location_survey")
@ViewMenuItem(icon = FontAwesome.MAP_MARKER,order = 4,value = MdmsLocaleId.MENU_LOCATION_SURVEY,groupName = MdmsLocaleId.MENU_SURVEYS)
@Qualifier(roles = {@Role(value = "PecSurveyVer")})
@UIScoped
public class LocationSurveyView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(PecLocationSurveyLayout.class);
    }

    @Override
    protected javax.sql.DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return MdmsLocaleId.LOCATION_SURVEY_CAPTION;
    }


}
