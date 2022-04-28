package za.co.spsi.mdms.web.auth.view.generator;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.web.gui.layout.generator.GeneratorLayout;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/*
 */
@CDIView("generator")
@ViewMenuItem(icon = FontAwesome.BOLT,order = 4,value = MdmsLocaleId.MENU_GENERATOR,groupName = MdmsLocaleId.MENU_GENERATOR)
@Qualifier(roles = {@Role(value = "PecSurveyVer")})
@UIScoped
public class GeneratorView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(GeneratorLayout.class);
    }

    @Override
    protected javax.sql.DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return MdmsLocaleId.GENERATOR_CAPTION;
    }


}
