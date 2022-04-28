package za.co.spsi.mdms.actuator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.spsi.mdms.actuator.inspectors.JmxInspectorImpl;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/actuator/metrics")
public class MetricsResource {

    @Inject
    private JmxInspectorImpl jmxInspector;

    private String wfPort;

    @PostConstruct
    private void init() {
        this.wfPort = jmxInspector.getWildflyManagementPort();
    }

    @GET
    @Produces("application/json")
    public Response getInfo() {
        return Response.ok(getServerMetrics()).build();
    }

    private JsonNode getServerMetrics() {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode metricsJson = null;
        try {
            String url = String.format("http://localhost:%s/metrics", wfPort);
            Client client = ClientBuilder.newClient();
            WebTarget managementResource = client.target(url);
            Response response = managementResource.request(MediaType.APPLICATION_JSON_TYPE)
                    .header("Content-type", MediaType.APPLICATION_JSON)
                    .get();
            String responseStr = response.readEntity(String.class);
            metricsJson = objectMapper.readTree(responseStr);
        } catch(Exception ex) {
            metricsJson = objectMapper.valueToTree(ex.getMessage());
        }
        return metricsJson;
    }

}
