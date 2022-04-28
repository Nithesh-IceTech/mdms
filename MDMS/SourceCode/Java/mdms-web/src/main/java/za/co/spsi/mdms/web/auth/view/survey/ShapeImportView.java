package za.co.spsi.mdms.web.auth.view.survey;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.server.FontAwesome;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.entity.gui.ShapeImportLayout;
import za.co.spsi.toolkit.crud.entity.gui.ShapeLayout;
import za.co.spsi.toolkit.crud.gui.CrudView;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

/*
 * A very simple view that just displays an "about text". The view also has 
 * a button to reset the demo date in the database.
 */
@CDIView("shape_import")
@ViewMenuItem(icon = FontAwesome.DATABASE,order = 4,value =  ToolkitLocaleId.SHAPE_IMPORT,groupName = ToolkitLocaleId.MENU_ADMIN)
@UIScoped
@Qualifier(roles = {@Role(value = "Supervisor")})
public class ShapeImportView extends CrudView {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @PostConstruct
    void init() {
        addLayout(IceShapeImportLayout.class);
    }

    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getViewCaption() {
        return ToolkitLocaleId.SHAPE_IMPORT;
    }


    public static class IceShapeImportLayout extends ShapeImportLayout {
        @Resource(mappedName = "java:/jdbc/mdms")
        private DataSource dataSource;

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }

        @Override
        public Class<? extends ShapeLayout> getShapeLayoutClass() {
            return IceShapeLayout.class;
        }

    }

    public static class IceShapeLayout extends ShapeLayout {
        @Resource(mappedName = "java:/jdbc/mdms")
        private DataSource dataSource;

        @Override
        public DataSource getDataSource() {
            return dataSource;
        }
    }

}
