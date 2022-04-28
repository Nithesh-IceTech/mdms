package za.co.spsi.mdms.web.auth;

import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.login.LoginEventProcessor;
import za.co.spsi.toolkit.crud.login.LoginView;
import za.co.spsi.toolkit.crud.util.AgencyHelper;
import za.co.spsi.toolkit.crud.util.CrudUAHelper;
import za.co.spsi.toolkit.crud.util.LoginHelper;
import za.co.spsi.toolkit.crud.util.VaadinPropertiesAgency;
import za.co.spsi.toolkit.db.audit.AuditHelper;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.uaa.util.dto.AgencyRoleMap;
import za.co.spsi.uaa.util.dto.TokenResponseDao;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2015/08/17.
 */
@Singleton
public class MDMSLoginEventProcessor extends LoginEventProcessor {

    @Inject
    @ConfValue("pec_agency")
    protected String agency;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;


    @Inject
    CrudUAHelper crudUAHelper;

    @Inject
    LoginHelper loginHelper;

    public static final Logger TAG = Logger.getLogger(MDMSLoginEventProcessor.class.getName());

    @Override
    public String getContext() {
        return "mdms";
    }

    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }

    public void processLogin(LoginView.LoginEventRequest request, TokenResponseDao tokenResponseDao) {
        AgencyRoleMap agencyRoleMap = crudUAHelper.getUaHelper().getAgencyRoleMapFromToken(tokenResponseDao);
        ToolkitUI.getToolkitUI().setAgencyRoleMap(request.getToken(),agencyRoleMap);

        LookupResult lookup = agencyHelper.getLookupResults().getForCode(agency);
        VaadinPropertiesAgency.setAgency(agency);
        ToolkitCrudConstants.setAgencyId(agencyHelper.getLookupServiceHelper().getAgencyHierarchyUp(lookup.getLookupCode()));
        agencySetEventEvent.fire(new AgencyHelper.AgencySetEvent(agency));
        loginEventResponseEvent.fire(new LoginView.LoginEventResponse(request, agency, lookup.getDescription(),
                new StringList(agencyRoleMap.get(agency)), true));

        // config auditing
        AuditHelper.setUIDCallback(() -> ToolkitUI.getToolkitUI() != null?ToolkitUI.getToolkitUI().getUsername():null);
        AuditHelper.setUIDRoleCallback(() ->
                ToolkitUI.getToolkitUI() != null && ToolkitUI.getToolkitUI().getUserRoles() != null ?
                        ToolkitUI.getToolkitUI().getUserRoles().containsIgnoreCase(ToolkitCrudConstants.ROLE_SUPERVISOR):false);

    }

}
