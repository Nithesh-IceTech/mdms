package za.co.spsi.mdms.common.services;


import org.joda.time.DateTime;
import org.joda.time.Minutes;
import za.co.spsi.toolkit.crud.login.LoginView;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.event.Observes;
import javax.sql.DataSource;
import java.util.Date;

@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class MDMSUserProcessor extends za.co.spsi.toolkit.crud.util.UserProcessor {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;


    @Asynchronous
    public void handleLogin(@Observes LoginView.LoginEventResponse loginEventResponse) {

        boolean shouldRun =
                lastRun == null || Minutes.minutesBetween(new DateTime(lastRun), new DateTime()).isGreaterThan(Minutes.minutes(60))
                        ? true : false;

        if (loginEventResponse.isLoginOk() && shouldRun) {
            lastRun = new Date();
            process(loginEventResponse, dataSource, "MDMS_ROLE");
        }
    }
}
