package za.co.spsi.openmucrest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
public class DriverRestClient {

    @Inject
    public OpenMucRestClient openMucRestClient;

    public String driversUrl;

    public DriverRestClient() {

    }

    @PostConstruct
    private void init() {
        this.driversUrl = "drivers";
    }

    public JsonNode getAllDrivers() throws IOException {
        String restPath = this.driversUrl;
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public JsonNode getDriverById(String driverName) throws IOException {
        String restPath = String.format("%s/%s", this.driversUrl, driverName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }


    public Response createNewDriver(DriverConfigEntity driverConfig) throws JsonProcessingException {

        String restPath = String.format("%s/%s", this.driversUrl, driverConfig.getProtocolDriver());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyNode = objectMapper.createObjectNode();
        ObjectNode configsNode = objectMapper.createObjectNode();

        configsNode.put("samplingTimeout", driverConfig.getSamplingTimeout());
        configsNode.put("connectRetryInterval", driverConfig.getConnectRetryInterval());
        configsNode.put("disabled", driverConfig.getDisabled().toString());
        bodyNode.set("configs", configsNode);

        return openMucRestClient.httpPost(restPath, objectMapper, bodyNode);
    }

    public Response deleteDriver(DriverConfigEntity driverConfigEntity) {
        String restPath = String.format("%s/%s", this.driversUrl, driverConfigEntity.getProtocolDriver());
        return openMucRestClient.httpDelete(restPath);
    }

    public void deleteAllDrivers(List<DriverConfigEntity> driverConfigList) {
        for(DriverConfigEntity driverConfigEntity: driverConfigList) {
            deleteDriver(driverConfigEntity);
        }
    }

}
