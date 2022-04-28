package za.co.spsi.mdms.web.gui.audit;

import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.audit.gui.AuditDetailLayout;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2016/11/01.
 */
@Qualifier(roles = {@Role(value = "Supervisor",write = false,create = false)})
public class MDMSAuditDetailLayout extends AuditDetailLayout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

}
