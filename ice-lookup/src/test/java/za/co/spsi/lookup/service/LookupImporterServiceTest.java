package za.co.spsi.lookup.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by jaspervdb on 3/9/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-lookup-config.xml")
public class LookupImporterServiceTest {


    public static class LookupImporterServiceStub extends LookupImporterService {

        @Override
        public void initAgencyLocale() {
        }
    }
}