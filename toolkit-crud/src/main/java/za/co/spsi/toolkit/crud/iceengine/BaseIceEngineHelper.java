package za.co.spsi.toolkit.crud.iceengine;

import za.co.spsi.toolkit.crud.util.CrudUAHelper;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.uaa.util.dto.TokenResponseDao;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 5/12/16.
 */
public abstract class BaseIceEngineHelper {

    private static final Logger LOG = Logger.getLogger(BaseIceEngineHelper.class.getName());

    @Inject
    CrudUAHelper uaHelper;

    @Inject
    @ConfValue(value = "systemUser")
    public String systemUser;

    @Inject
    @ConfValue("system_user_password")
    public String systemPassword;

    protected String getCustomUser() {
        return null;
    }

    protected String getCustomPassword() {
        return null;
    }

    private String getIceEngineUser() {
        return getCustomUser() == null ? systemUser : getCustomUser();
    }

    private String getIceEnginePassword() {
        return getCustomPassword() == null ? systemPassword : getCustomPassword();
    }

    public String makeRestRequest(String uri, String json) throws Exception {
        TokenResponseDao tokenResponseDao = uaHelper.login(getIceEngineUser(), getIceEnginePassword());
        Client client = ClientBuilder.newClient();
        Response response = null;
        try {
            LOG.info("uri " + uri);
            LOG.info("JSON :" + json);
            response = client.target(uri).request().header(HttpHeaders.AUTHORIZATION,
                    "Bearer " + tokenResponseDao.getAccessToken()).
                    post(Entity.entity(json, MediaType.APPLICATION_JSON));

            return response.readEntity(String.class);

        } finally {
            client.close();
            if (response != null && response.getStatus() == Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()) {
                throw new IceEngineException(response.readEntity(String.class));
            }
        }
    }
}
