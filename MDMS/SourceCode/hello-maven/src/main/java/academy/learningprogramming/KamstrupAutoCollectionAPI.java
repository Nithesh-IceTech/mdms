package academy.learningprogramming;

//import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
//import java.util.logging.Logger;

public class KamstrupAutoCollectionAPI {

//    public static final Logger TAG = Logger.getLogger(RestHelper.class.getName());

//    @Inject
//    @ConfValue(folder = "server",value = "utilitydriver.uri")
    private static String utilityDriverUri = "http://192.1.0.181/UtiliDriver/api";

    public KamstrupAutoCollectionAPI() {

    }

    public String formatUtiliDriver(String uri) {
        if (!uri.startsWith(utilityDriverUri)) {
            uri = utilityDriverUri + uri.substring(uri.indexOf("/api/") + "/api/".length());
        }
        return uri;
    }

    public Invocation.Builder getInvocationBuilder(String driverUri, String ...paths) {
        System.out.println( String.format( driverUri+"/"+paths != null?String.join(",", paths):"" ) );
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
