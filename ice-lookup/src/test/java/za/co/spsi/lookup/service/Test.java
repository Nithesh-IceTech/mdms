package za.co.spsi.lookup.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import za.co.spsi.lookup.dao.LookupResult;

import java.util.List;

/**
 * Created by jaspervdb on 2016/05/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-lookup-config.xml")

public class Test {

    @Autowired
    private LookupServiceHelper lookupServiceHelper;

    @org.junit.Test
    public void findAgency() throws Exception {
        List<LookupResult> values = lookupServiceHelper.executeLookupRequest("AGENCY", "en", "2");
        Assert.isTrue(!values.isEmpty());
    }

}
