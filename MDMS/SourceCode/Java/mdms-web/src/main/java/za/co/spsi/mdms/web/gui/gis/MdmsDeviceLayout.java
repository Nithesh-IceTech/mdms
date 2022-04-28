package za.co.spsi.mdms.web.gui.gis;

import za.co.spsi.toolkit.crud.gis.gui.DeviceLayout;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.sql.DataSource;

/**
 * Created by jaspervdbijl on 2017/04/25.
 */
@Dependent
public class MdmsDeviceLayout extends DeviceLayout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
    }

}
