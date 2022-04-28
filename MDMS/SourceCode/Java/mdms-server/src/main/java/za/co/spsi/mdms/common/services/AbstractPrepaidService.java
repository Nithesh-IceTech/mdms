package za.co.spsi.mdms.common.services;

import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.Resource;

public class AbstractPrepaidService extends ProcessorService {

    @Resource(mappedName = "java:/jdbc/mdms")
    public javax.sql.DataSource dataSource;

    protected Processor p = getProcessor();

}
