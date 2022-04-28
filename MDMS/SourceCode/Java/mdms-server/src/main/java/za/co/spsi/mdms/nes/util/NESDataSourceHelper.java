package za.co.spsi.mdms.nes.util;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

@Dependent
@Log
public class NESDataSourceHelper {

    @Inject
    @ConfValue(value = "db.nes.url", folder = "server")
    private String nesUrl;

    @Inject
    @ConfValue(value = "db.nes.username", folder = "server")
    private String nesUsername;

    @Inject
    @ConfValue(value = "db.nes.password", folder = "server")
    private String nesPassword;

    @Inject
    @ConfValue(value = "db.nes.driver", folder = "server")
    private String nesDriver;

    Properties cProperties = new Properties();
    Driver driver;


    @PostConstruct
    @SneakyThrows
    public void init() {
        Class.forName(nesDriver);
        cProperties.setProperty("username",nesUsername);
        cProperties.setProperty("password",nesPassword);
        driver = DriverManager.getDriver(nesUrl);
    }

    @SneakyThrows
    public Connection getConnection() {
        return driver.connect(nesUrl,cProperties);
    }

}
