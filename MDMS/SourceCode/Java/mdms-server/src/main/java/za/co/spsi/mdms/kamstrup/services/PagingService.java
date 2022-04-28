package za.co.spsi.mdms.kamstrup.services;

//import org.apache.deltaspike.core.api.config.ConfigProperty;

import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by jaspervdb on 2016/10/12.
 */
@Dependent
public class PagingService<T extends Pageable> {

    public static final Logger TAG = Logger.getLogger(PagingService.class.getName());

    @Inject
    @ConfValue(folder = "server",value = "utilitydriver.uri")
    private String utilityDriverUri;

    private Client client = ClientBuilder.newClient( );

    public T getObject(String path,Class<T> type,Integer offset) {
        TAG.info(String.format("REST: %d: %s",offset,path));
        WebTarget webTarget = client.target(utilityDriverUri).path(path).path("/").queryParam("offset",offset);

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
        Response response = invocationBuilder.get();

        return response.readEntity(type);
    }

    public List<T> getObjects(String path,Class<T> type) {
        List<T> values = new ArrayList<>(Arrays.asList(getObject(path,type,0)));
        int offset = values.get(values.size()-1).getCount();
        while (offset < values.get(values.size()-1).getTotal()) {
            values.add(getObject(path,type,offset));
            offset += values.get(values.size()-1).getCount();
        }
        return values;
    }

    public Response get(String uri,boolean bufferEntity) {
        // reformat URI
        if (!uri.startsWith(utilityDriverUri)) {
            uri = utilityDriverUri + uri.substring(uri.indexOf("/api/") + "/api/".length());
        }
        TAG.info(String.format("REST: %s",uri));
        WebTarget webTarget = client.target(uri);

        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_XML);
        Response response = invocationBuilder.get();
        if (bufferEntity) {
            response.bufferEntity();
        }
        return response;
    }

    public <E> E get(Class<E> type,String uri) {
        return get(uri,false).readEntity(type);
    }
}
