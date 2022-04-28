package za.co.spsi.mdms.web.services;

import za.co.spsi.lookup.dao.LookupDefinitionModule;
import za.co.spsi.lookup.db.LookupImporter;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.Map;
import java.util.Properties;


/**
 * Created by jaspervdb on 2/3/16.
 */
@Startup
@Singleton
@DependsOn({"MDMSUpgradeService"})
public class LookupImporterService {

    @Inject
    protected ToolkitLookupServiceHelper lookupServiceHelper;

    protected LookupImporter lookupImporter = new LookupImporter();

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    @ConfValue("agency_map")
    private Map<String, Properties> agencyMap;

    @Inject
    @ConfValue("importProcessorEnabled")
    private boolean processEnabled;

    // daily at 11 PM
    @Schedule(second = "0", minute = "0", hour = "11", dayOfMonth = "*", persistent = false)
    public void importData() {
        lookupImporter.clearAllConfiguredDefinitions();
        initAgencyLocale();

        for (LookupDefinitionModule lookupDefinitionModuleEntity : lookupServiceHelper.getAllLookupDefinitionModuleByModuleCd("PEC").
                removeGroups()) {
            if (!"MESSAGE".equals(lookupDefinitionModuleEntity.getLookupDefinitionModuleEntityPK().getLookupDefinitionId())) {
                lookupImporter.addLookups(lookupDefinitionModuleEntity.getLookupDefinitionModuleEntityPK().getLookupDefinitionId());
            }
        }

        lookupImporter.importLookupDefinitions(dataSource, lookupServiceHelper.getHelper());
    }


    @PostConstruct
    public void importDataAtStart() {
        if (processEnabled) {
            new Thread(() -> {
                importData();
            }).start();
        }
    }

    /**
     * configure the agency's and their locales
     */
    public void initAgencyLocale() {
        for (String agency : agencyMap.keySet()) {
            lookupImporter.addAgencyLocale(Integer.parseInt(agency), agencyMap.get(agency).getProperty("locale").split(";"));
        }

    }

}
