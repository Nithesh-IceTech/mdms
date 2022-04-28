package za.co.spsi.lookup.service;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import za.co.spsi.lookup.dao.LookupResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 1/21/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-lookup-config.xml")
public class LookupServiceHelperTest {

    @Autowired
    private LookupServiceHelper lookupServiceHelper;

    @org.junit.Test
    public void testExecuteLookupRequest() throws Exception {
        List<LookupResult> values = lookupServiceHelper.executeLookupRequest("TAXTYPE", "en", "3");
        Assert.isTrue(!values.isEmpty());
    }

    @org.junit.Test
    public void getAgencyHierarchyUp() throws Exception {
        String values = lookupServiceHelper.getAgencyHierarchyUp("3");
        Assert.isTrue(values!=null);
    }

    @org.junit.Test
    public void getAgencyHierarchyDown() throws Exception {
        List<String> values = lookupServiceHelper.getAgencyHierarchyDown("3");
        Assert.isTrue(values!=null);
    }

    @org.junit.Test
    public void testExecuteLookupMappingRequest() throws Exception {

        Long startTime = System.currentTimeMillis();
        System.out.println("Start time " + startTime);

        int threadCount = 1;
        List<Thread> threads = new ArrayList<Thread>();

        for (int threadLoop = 0; threadLoop < threadCount; threadLoop++) {
            Thread thread = new Thread(new Runnable() {
                public void run() {

                    for (int i = 0; i < 1; i++) {

                        List<LookupResult> values = lookupServiceHelper.executeLookupRequest("DISTRICT", "en", "1,3");
                        Assert.isTrue(!values.isEmpty());
                        List<LookupResult> mapping = lookupServiceHelper.executeLookupMappingRequest("DISTRICTNEIGHBORHOOD", "en","3",
                                values.get(0).getLookupCode());

                        Assert.isTrue(!mapping.isEmpty());
                    }
                }
            });

            thread.start();
            thread.join();

            // Add the thread to our thread list
            threads.add(thread);
        }


        while (threads.size() > 0) {
            for (Thread thread: threads) {
                if (!thread.isAlive()) {
                    threads.remove(thread);
                    break;
                }
            }
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("End Time " + endTime);
        System.out.println("Duration " + ((endTime - startTime) * 1.0) / 1000);
    }
}
