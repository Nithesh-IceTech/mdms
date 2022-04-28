package za.co.spsi.toolkit.crud.util;


import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.lookup.dao.LookupResultList;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
import za.co.spsi.toolkit.crud.login.LoginView;
import za.co.spsi.toolkit.crud.login.UserDetailEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.uaa.ee.util.UAHelper;
import za.co.spsi.uaa.util.dto.User;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class UserProcessor {

    protected static Date lastRun = null;

    public static final Logger TAG = Logger.getLogger(UserProcessor.class.getName());

    @Inject
    @ConfValue("oauth.realm")
    private String oAuthRealm;

    @Inject
    @ConfValue("oauth.url")
    private String oAuthurl;

    private UAHelper uaHelper = new UAHelper();

    @Inject
    @ConfValue("oauth.url")
    private String authUrl;

    @Inject
    @ConfValue("parent_agency")
    private String parentAgency;

    @Inject
    private ToolkitLookupServiceHelper lookupServiceHelper;

    /**
     * step through the batch data and process
     */
    public void process(LoginView.LoginEventResponse loginEventResponse, Connection connection, String lookupDef) throws Exception {

        ArrayList<String> roles = new ArrayList<>();

        for (String agency : parentAgency.split(",")) {
            LookupResultList lookupResultList =
                    lookupServiceHelper.executeLookupRequest(lookupDef, "en", agency);

            for (LookupResult lookupResults : lookupResultList) {
                if (!roles.contains(lookupResults.getLookupCode())) {
                    roles.add(lookupResults.getLookupCode());
                }
            }
        }

        if (roles.isEmpty()) {
            return;
        }
        // Query useradmin
        List<User> userList =
                uaHelper.getUserWithRoles(oAuthRealm, authUrl, loginEventResponse.getLoginEventRequest().getToken(), roles, "en");

        if (userList != null && !userList.isEmpty()) {
            // fix case BUG - TODO remove later

            Optional<String> upper = userList.stream().map(u -> "'" + u.getUid().toUpperCase() + "'").reduce((n1, n2) -> n1 + "," + n2);
            Optional<String> normal = userList.stream().map(u -> "'" + u.getUid() + "'").reduce((n1, n2) -> n1 + "," + n2);
            List<List> users = DataSourceDB.executeQuery(connection, new Class[]{String.class},
                    String.format("select user_name from user_detail where upper(user_name) in (%s) and user_name not in (%s)", upper.get(), normal.get()));

            if (!users.isEmpty()) {
                DataSourceDB.execute(connection, String.format("delete from user_detail where user_name in (%s)",
                        users.stream().map(u -> "'" + u.get(0) + "'").reduce((n1, n2) -> n1 + "," + n2).get()));
            }

            // fix audit logs
            DataSourceDB.execute(connection, "update AUDIT_LOG set USER_ID = (" +
                    "    select user_name from USER_DETAIL where upper(user_name)  = upper(user_id) and not user_name = user_id)");


            for (User user : userList) {

                user.setUid(user.getUid().toUpperCase());

                UserDetailEntity userDetailEntity =
                        (UserDetailEntity) DataSourceDB.getFromSet(connection, (EntityDB) new UserDetailEntity().username.set(user.getUid()));

                userDetailEntity = userDetailEntity == null ? new UserDetailEntity().init(user) : userDetailEntity.init(user);
                DataSourceDB.setIfChanged(connection, userDetailEntity);
            }
            connection.commit();
        }
    }

    /**
     * step through the batch data and process
     */
    public void process(LoginView.LoginEventResponse loginEventResponse, DataSource dataSource, String lookupDef) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                process(loginEventResponse, connection, lookupDef);
            }
        } catch (Throwable e) {
            TAG.severe(e.getMessage());
        }
    }
}
