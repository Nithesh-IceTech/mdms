package za.co.spsi.openmucrest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Slf4j
@Data
public class OpenMucRestClient {

    private String baseUrl;
    private String username;
    private String password;

    public OpenMucRestClient() {
        this.baseUrl = "http://devopenmuc.spsi.co.za/rest";
        this.username = "admin";
        this.password = "admin";
    }

    public Invocation.Builder getInvocationBuilder(String restPath) {

        log.info(String.format("Invocation Builder Rest Path: %s", this.baseUrl + "/" + restPath ));

        ClientConfig clientConfig = new ClientConfig();

        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(this.username, this.password);
        clientConfig.register(feature);
        clientConfig.register(JacksonFeature.class);

        Client client = ClientBuilder.newClient(clientConfig);
        WebTarget webTarget = client.target(this.baseUrl).path(restPath);

        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        return invocationBuilder;
    }

    public Optional<JsonNode> httpGet(String restPath) throws JsonProcessingException {

        Invocation.Builder invocationBuilder = getInvocationBuilder(restPath);

        Response response = invocationBuilder.get();

        System.out.println(response.getStatus());
        System.out.println(response.getStatusInfo());

        ObjectMapper objectMapper = new ObjectMapper();

        Optional<JsonNode> responseJsonNode = null;

        if(response.getStatus() == 200) {
            String responseJsonStr = response.readEntity(String.class);
            responseJsonNode = Optional.of(objectMapper.readTree(responseJsonStr));
        }

        return responseJsonNode;
    }

    public Response httpPost(String restPath, ObjectMapper objectMapper, ObjectNode bodyNode) throws JsonProcessingException {

        Invocation.Builder invocationBuilder = getInvocationBuilder(restPath);

        Response response = invocationBuilder.post(Entity.json(objectMapper.writeValueAsString( bodyNode )));

        System.out.println(response.getStatus());
        System.out.println(response.getStatusInfo());

        if(response.getStatus() == 200) {
            log.info( String.format("Object successfully created.") );
        } else {
            log.warn( String.format("Creating object has failed.") );
        }

        return response;
    }

    public Response httpDelete(String restPath) {

        Invocation.Builder invocationBuilder = getInvocationBuilder(restPath);

        Response response = invocationBuilder.delete();

        System.out.println(response.getStatus());
        System.out.println(response.getStatusInfo());

        if(response.getStatus() == 200) {
            log.info( String.format("Object successfully deleted.") );
        } else {
            log.warn( String.format("Deleting object has failed.") );
        }

        return response;
    }

}
