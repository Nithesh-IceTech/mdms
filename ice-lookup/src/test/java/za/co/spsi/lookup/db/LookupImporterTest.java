package za.co.spsi.lookup.db;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import za.co.spsi.lookup.dao.LookupDefinitionModule;
import za.co.spsi.lookup.dao.LookupDefinitionModulePK;
import za.co.spsi.lookup.service.LookupServiceHelper;

import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2/3/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-lookup-config.xml","classpath:spring-lookup-db-test-config.xml"})

public class LookupImporterTest {

    @Autowired
    private LookupServiceHelper lookupServiceHelper;

    @Autowired
    private DataSource dataSource;

    @Test
    public void testImportLookups() throws Exception {
        LookupImporter lookupImporter = new LookupImporter();//"2000001","en");
        lookupImporter.addAgencyLocale(2000001,new String[]{"en"});

        for (LookupDefinitionModule lookupDefinitionModuleEntity : lookupServiceHelper.getAllLookupDefinitionModuleByModuleCd("ICEFieldService")
                .removeGroups()) {
            if (!"MESSAGE".equals(lookupDefinitionModuleEntity.getLookupDefinitionModuleEntityPK().getLookupDefinitionId())) {
                lookupImporter.addLookups(lookupDefinitionModuleEntity.getLookupDefinitionModuleEntityPK().getLookupDefinitionId());
            }
        }

        lookupImporter.importLookupDefinitions(dataSource,lookupServiceHelper);
    }
}