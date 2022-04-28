package za.co.spsi.toolkit.crud.gui.lookup;

import za.co.spsi.lookup.dao.LookupCodeResult;
import za.co.spsi.lookup.dao.LookupDefinitionModuleList;
import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.lookup.dao.LookupResultList;
import za.co.spsi.lookup.service.LookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.StringList;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.*;
import java.util.List;

/**
 * Created by jaspervdb on 4/19/16.
 */
@Dependent
public class ToolkitLookupServiceHelper {

    @Inject
    @ConfValue("ice_lookup_url")
    private String iceLookupUrl;

    @Inject
    @ConfValue(value = "mlcsCacheEnabled")
    private Boolean cacheEnabled;

    @Inject
    @ConfValue("purge_cache_on_restart")
    private boolean purgeCache;


    private LookupServiceHelper lookupServiceHelper = new LookupServiceHelper();

    @PostConstruct
    private void initVars() {
        lookupServiceHelper.setIceLookupUrl(iceLookupUrl);
    }

    private static File folder = new File("mlcsCache");

    static {
        folder.mkdir();
        purgeCache();
    }

    public static void purgeCache() {
        if (folder.listFiles() == null ||
                folder.listFiles().length == 0) return;

        for (File file : folder.listFiles()) {
            file.delete();
        }
    }

    private static String getFileName(Object... nameValues) {
        StringList names = new StringList();
        for (int i = 0; i < nameValues.length; i++) {
            names.add(nameValues[i] != null ? nameValues[i].toString() : "null");
        }
        return names.toString("_");
    }

    private static <T> T getObjectFromFile(File file) {
        try {
            try (FileInputStream fis = new FileInputStream(file)) {
                try (ObjectInputStream is = new ObjectInputStream(fis)) {
                    return (T) is.readObject();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void writeObjectToFile(File file, Object value) {
        try {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                    oos.writeObject(value);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    private Object getCachedResult(ExecuteCallback executeCallback, Object... nameValues) {
        if (cacheEnabled) {
            File file = new File(folder.getAbsoluteFile() + "/" + getFileName(nameValues));
            if (file.exists()) {
                return getObjectFromFile(file);
            } else {
                Object result = executeCallback.execute();
                writeObjectToFile(file, result);
                return result;
            }
        } else {
            return executeCallback.execute();
        }
    }

    public static interface ExecuteCallback {
        public Object execute();
    }

    public LookupServiceHelper getHelper() {
        return lookupServiceHelper;
    }

    @Schedule(second = "1", minute = "0", hour = "0", dayOfMonth = "*", persistent = false)
    private void purgeCacheSchedule() {
        purgeCache();
    }

    public LookupResultList getAllAgencies(String language, String agencyId) {
        return (LookupResultList) getCachedResult(new ExecuteCallback() {
            @Override
            public LookupResultList execute() {
                return lookupServiceHelper.executeLookupRequest("AGENCY", language, agencyId);
            }
        }, "executeLookupRequest", "AGENCY", language, agencyId);
    }

    public String getAgencyHierarchyUp(String agencyId) {
        return (String) getCachedResult(() -> lookupServiceHelper.getAgencyHierarchyUp(agencyId), "getAgencyHierarchyUp", agencyId);
    }

    public List<String> getAgencyHierarchyDown(String agencyId) {
        return (List<String>) getCachedResult(() -> lookupServiceHelper.getAgencyHierarchyDown(agencyId), "getAgencyHierarchyDown", agencyId);
    }

    public LookupCodeResult executeLookupCodeRequest(String lookupDefinitionId, String lookupCode, String language, Object agencyId) {
        return (LookupCodeResult) getCachedResult(() ->
                        lookupServiceHelper.executeLookupCodeRequest(lookupDefinitionId, lookupCode, language, agencyId),
                "executeLookupCodeRequest", lookupDefinitionId, lookupCode, language, agencyId);
    }

    public LookupResultList executeLookupRequest(String lookupDefinitionId, String language, String agencyId) {
        return (LookupResultList) getCachedResult(new ExecuteCallback() {
            @Override
            public LookupResultList execute() {
                return lookupServiceHelper.executeLookupRequest(lookupDefinitionId, language, agencyId);
            }
        }, "executeLookupRequest", lookupDefinitionId, language, agencyId);
    }

    public LookupResultList executeLookupMappingRequest(String hierarchyDefId, String language, String agencyId, Object parentId) {
        return (LookupResultList) getCachedResult(new ExecuteCallback() {
            @Override
            public LookupResultList execute() {
                return lookupServiceHelper.executeLookupMappingRequest(hierarchyDefId, language, agencyId, parentId);
            }
        }, "executeLookupMappingRequest", hierarchyDefId, language, agencyId, parentId);
    }

    public LookupDefinitionModuleList getAllLookupDefinitionModuleByModuleCd(String moduleCode) {
        return lookupServiceHelper.getAllLookupDefinitionModuleByModuleCd(moduleCode);
    }

    /**
     * replace _AGENCY_ID_, _LANG_,_LOOKUP_
     *
     * @param definition
     * @param description
     * @return
     */
    public String getLookupFromDescription(DataSource dataSource, String definition, String description) {
        List<List> values = DataSourceDB.executeQuery(dataSource, "select code from lookups " +
                        "where " +
                        "lookups.LANG = '_LANG_' AND lookups.agency_id = '_AGENCY_ID_' AND LOOKUPS.LOOKUP_DEF = '_LOOKUP_' AND DESCRIPTION = ?".
                                replace("_AGENCY_ID_", "" + ToolkitCrudConstants.getChildAgencyId()).replace("_LANG_", ToolkitCrudConstants.getLocale()).
                                replace("_LOOKUP_", definition),
                description);
        return !values.isEmpty() ? !values.get(0).isEmpty() ? (String) values.get(0).get(0) : null : null;
    }

    public static int getLookupSize(String lookupDefId, Layout layout) {
        List<LookupResult> lookupResults = layout.getLookupServiceHelper().executeLookupRequest(
                lookupDefId,
                ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getAgencyId());
        return lookupResults.size();
    }
}
