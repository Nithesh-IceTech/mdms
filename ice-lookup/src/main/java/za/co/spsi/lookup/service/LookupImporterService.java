package za.co.spsi.lookup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import za.co.spsi.lookup.dao.LookupDefinitionModule;
import za.co.spsi.lookup.db.LookupImporter;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2/3/16.
 */
public abstract class LookupImporterService {

    @Autowired
    protected LookupServiceHelper lookupServiceHelper;

    @Autowired
    protected LookupImporter lookupImporter;

    @Autowired
    protected DataSource dataSource;

    private String modules[] = new String[]{"ICEFieldService"};


    public void setModules(String[] modules) {
        this.modules = modules;
    }

    // daily at 11 PM
    @Scheduled(cron = "0 0 23 * * *")
    public void importData() {

        lookupImporter.clearAllConfiguredDefinitions();
        initAgencyLocale();

        for (String module : modules) {
            for (LookupDefinitionModule lookupDefinitionModuleEntity : lookupServiceHelper.getAllLookupDefinitionModuleByModuleCd(module).
                    removeGroups()) {
                if (!"MESSAGE".equals(lookupDefinitionModuleEntity.getLookupDefinitionModuleEntityPK().getLookupDefinitionId())) {
                    lookupImporter.addLookups(lookupDefinitionModuleEntity.getLookupDefinitionModuleEntityPK().getLookupDefinitionId());
                }
            }
        }

        lookupImporter.importLookupDefinitions(dataSource,lookupServiceHelper);
    }

    @PostConstruct
    public void importDataAtStart() {
        importData();
    }

    /**
     * configure the agency's and their locales
     */
    public abstract void initAgencyLocale();

}
