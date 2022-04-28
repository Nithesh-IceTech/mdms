package za.co.spsi.mdms.web.gui;

import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.db.EntityDB;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by jaspervdbijl on 2017/01/11.
 */
public abstract class MdmsLayout<E extends EntityDB> extends Layout<E> {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    public MdmsLayout(String name) {
        super(name);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

}
