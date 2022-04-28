package za.co.spsi.mdms.io.kamstrup;

import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * Created by jaspervdbijl on 2017/01/11.
 */
@Dependent
public class RestHelper {

    public static final Logger TAG = Logger.getLogger(RestHelper.class.getName());

    @Inject
    @ConfValue(folder = "server",value = "utilitydriver.uri")
    private String utilityDriverUri;

    public String formatUtiliDriver(String uri) {
        if (!uri.startsWith(utilityDriverUri)) {
            uri = utilityDriverUri + uri.substring(uri.indexOf("/api/") + "/api/".length());
        }
        return uri;
    }

    public Invocation.Builder getInvocationBuilder(String driverUri, String ...paths) {
        TAG.info(driverUri+"/"+paths != null?String.join(",", paths):"");
        driverUri = formatUtiliDriver(driverUri);
        Client client = ClientBuilder.newClient( );
        WebTarget webTarget = client.target(driverUri);
        for (String path : paths) {
            webTarget = webTarget.path(path);
        }

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
        return invocationBuilder;
    }

    public <T> T restGet(String driverUri,Class<T> type,String ...paths) {
        return getInvocationBuilder(driverUri, paths).get().readEntity(type);
    }

    public Response restPost(Object postRequestObject,String ...paths) {
        return getInvocationBuilder(utilityDriverUri, paths).post(
                javax.ws.rs.client.Entity.entity(postRequestObject, MediaType.APPLICATION_XML)
        );
    }

    public Response restDelete(String driverUri) {
        return getInvocationBuilder(driverUri).delete();
    }

}
