package za.co.spsi.lookup.db;

import org.springframework.stereotype.Component;
import za.co.spsi.lookup.dao.HierarchyDefinitionResult;
import za.co.spsi.lookup.dao.HierarchyDefinitionResultList;
import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.lookup.dao.LookupResultList;
import za.co.spsi.lookup.service.LookupServiceHelper;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2/3/16.
 */
@Component
public class LookupImporter {

    public static final Logger LOG = Logger.getLogger(LookupImporter.class.getName());

    private List<String> lookupDefList = new ArrayList<String>(),
            hierarchyDefList = new ArrayList<String>();
    private List<String[]> lookupMappings = new ArrayList<String[]>();

    private Map<Integer, String[]> agencyLocaleMap = new HashMap<Integer, String[]>();

    public LookupImporter() {
    }

    public void addLookups(String... lookupDefs) {
        Collections.addAll(lookupDefList, lookupDefs);
    }

    public void addLookup(String lookupDef) {
        lookupDefList.add(lookupDef);
    }

    public void clearAllConfiguredDefinitions() {
        lookupDefList.clear();
        hierarchyDefList.clear();
        lookupMappings.clear();
    }

    public void addLookupMapping(String lookupDefs, String parentLookupDefs) {
        lookupMappings.add(new String[]{lookupDefs, parentLookupDefs});
    }

    public void addMappings(String... hierarchyDefs) {
        Collections.addAll(hierarchyDefList, hierarchyDefs);
    }

    public void addAgencyLocale(Integer agencyId, String locales[]) {
        agencyLocaleMap.put(agencyId, locales);
    }

    public void importLookupDefinitions(Connection connection, String lookupDef, String parentLookup, String locale, Integer agencyId, LookupResultList lookupResults) throws SQLException {
        LookupEntity lookupEntity = new LookupEntity();
        DataSourceDB<LookupEntity> ds = new DataSourceDB<LookupEntity>(LookupEntity.class);
        PreparedStatement ps = ds.getInsertStatement(connection, lookupEntity);
        try {
            for (LookupResult lookupResult : lookupResults) {
                lookupEntity.init(lookupResult, lookupDef, parentLookup, agencyId);
                ds.setInsert(ps, lookupEntity);
            }
        } finally {
            ps.close();
        }
    }

    public void importHierarchyDefinition(Connection connection, String locale, Integer agencyId, HierarchyDefinitionResultList lookupResults) throws SQLException {
        LookupEntity lookupEntity = new LookupEntity();
        DataSourceDB<LookupEntity> ds = new DataSourceDB<LookupEntity>(LookupEntity.class);
        long time = System.currentTimeMillis();
        PreparedStatement ps = ds.getInsertStatement(connection, lookupEntity);
        try {
            for (HierarchyDefinitionResult hierarchyDefinition : lookupResults) {
                lookupEntity.init(hierarchyDefinition, locale, agencyId);
                ds.setInsert(ps, lookupEntity);
            }
        } finally {
            ps.close();
        }
        time = System.currentTimeMillis() - time;
        System.out.println("TOOK " + time);
    }


    private void importLookupOrMappings(Connection connection, LookupServiceHelper helper, String lookupDef, String parentLookupDef) throws SQLException {
        for (Integer agencyId : agencyLocaleMap.keySet()) {
            for (String locale : agencyLocaleMap.get(agencyId)) {
                if (parentLookupDef == null) {
                    importLookupDefinitions(connection, lookupDef, null, locale, agencyId, helper.executeLookupRequest(lookupDef, locale, agencyId.toString()));
                } else {
                    importLookupDefinitions(connection, lookupDef, parentLookupDef, locale, agencyId, helper.executeLookupMappingRequest(lookupDef, locale, agencyId.toString(), parentLookupDef));
                }
            }
        }
    }

    public void importLookupDefinitions(Connection connection, LookupServiceHelper helper, String lookupDef) throws SQLException {
        importLookupOrMappings(connection, helper, lookupDef, null);
    }

    public void importLookupMappings(Connection connection, LookupServiceHelper helper, String lookupDef, String parentLookupDef) throws SQLException {
        importLookupOrMappings(connection, helper, lookupDef, parentLookupDef);
    }

    public void importHierarchyDefinitions(Connection connection, LookupServiceHelper helper, String lookupDef) throws SQLException {
        for (Integer agencyId : agencyLocaleMap.keySet()) {
            for (String locale : agencyLocaleMap.get(agencyId)) {
                importHierarchyDefinition(connection, locale, agencyId, helper.findAllByHierarchyDefinitionId(lookupDef, locale, agencyId.toString()));
            }
        }
    }

    /**
     * clear all the lookups from the entity
     *
     * @param connection
     * @throws SQLException
     */
    public void clearLookups(Connection connection) throws SQLException {
        Statement smt = connection.createStatement();
        try {
            smt.execute("delete from lookups");
        } finally {

        }
    }

    /**
     * import all defined lookups, mappings and hierarchy definitions
     *
     * @param dataSource
     * @param helper
     */
    public void importLookupDefinitions(DataSource dataSource, LookupServiceHelper helper) {
        try {
            Connection connection = dataSource.getConnection();
            try {
                clearLookups(connection);
                for (String lookupDef : lookupDefList) {
                    importLookupDefinitions(connection, helper, lookupDef);
                }
                for (String subGroupLookupDef[] : lookupMappings) {
                    importLookupMappings(connection, helper, subGroupLookupDef[0], subGroupLookupDef[1]);
                }
                for (String hierarchy : hierarchyDefList) {
                    importHierarchyDefinitions(connection, helper, hierarchy);
                }
                LOG.info(String.format("Imported %s lookup definitions", lookupDefList.size()));
            } finally {
                connection.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }
}
