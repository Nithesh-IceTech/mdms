package za.co.spsi.mdms;

import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@Startup
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class MdmsDatasource {

    private final static Logger log = Logger.getLogger(MdmsDatasource.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    public MdmsDatasource() {

    }

    @PostConstruct
    private void init() {

        log.info("MDMS Data Source Initialization -> ");

        DriverFactory.setDataSource(dataSource);

        try(Connection connection = DriverFactory.getDataSource().getConnection()) {
            DriverFactory.getHelper(connection);
            log.info(String.format("Database Product Name: %s", connection.getMetaData().getDatabaseProductName() ));
            Driver testDriver = DriverFactory.getDriver();
            log.info(String.format("Driver Class Name: %s", testDriver.getClass().getName() ));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

}
