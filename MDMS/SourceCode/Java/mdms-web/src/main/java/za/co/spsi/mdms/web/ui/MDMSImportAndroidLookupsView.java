package za.co.spsi.mdms.web.ui;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.entity.gui.ImportAndroidLookupsLayout;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

@CDIView("Import Android Lookups")
@ViewMenuItem(icon = FontAwesome.DATABASE, order = 4, value = "Import Android Lookups", groupName = ToolkitLocaleId.MENU_ADMIN)
@UIScoped
@Qualifier(roles = {@Role(value = "Supervisor")})
public class MDMSImportAndroidLookupsView extends CrudView {

    @PostConstruct
    void init() {
        addLayout(IceImportAndroidLookupsLayout.class);
    }

    @Override
    public String getViewCaption() {
        return "Import Android Lookups";
    }

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public static class IceImportAndroidLookupsLayout extends ImportAndroidLookupsLayout {
        @Resource(mappedName = "java:/jdbc/mdms")
        private DataSource dataSource;

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }
    }
}
