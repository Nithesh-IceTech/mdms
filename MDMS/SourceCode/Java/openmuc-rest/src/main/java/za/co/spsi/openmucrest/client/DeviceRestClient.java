package za.co.spsi.openmucrest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;
import za.co.spsi.openmucdoa.interfaces.IDeviceConfig;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
public class DeviceRestClient {

    @Inject
    public OpenMucRestClient openMucRestClient;

    public String devicesUrl;

    public DeviceRestClient() {
    }

    @PostConstruct
    private void init() {
        this.devicesUrl = "devices";
    }

    public JsonNode getAllDevices() throws IOException {
        String restPath = this.devicesUrl;
        return openMucRestClient.httpGet(restPath).orElse(null);
    }

    public JsonNode getDeviceById(String deviceId) throws JsonProcessingException {
        String restPath = String.format("%s/%s", this.devicesUrl, deviceId);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public String getDeviceState(String deviceId) throws JsonProcessingException {
        String restPath = String.format("%s/%s", this.devicesUrl, deviceId);
        JsonNode jsonNodeResponse = openMucRestClient.httpGet(restPath).orElse(null);
        return jsonNodeResponse != null ? jsonNodeResponse.get("state").asText() : "UNKNOWN";
    }

    public JsonNode getChannelsByDeviceId(String deviceName) throws JsonProcessingException {
        String restPath = String.format("%s/%s/channels", this.devicesUrl, deviceName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public Response createNewDevice(IDeviceConfig deviceConfig, DriverConfigEntity driverConfigEntity) throws JsonProcessingException {

        String restPath = String.format("%s/%s", this.devicesUrl, deviceConfig.getIedName());

        String driverName = driverConfigEntity.getProtocolDriver();

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode bodyNode = objectMapper.createObjectNode();
        bodyNode.put("driver", driverName );

        ObjectNode configsNode = objectMapper.createObjectNode();
        configsNode.put("description", deviceConfig.getDescription() );
        configsNode.put("deviceAddress", deviceConfig.getDeviceAddressField() );
        configsNode.put("samplingTimeout", driverConfigEntity.getSamplingTimeout());
        configsNode.put("connectRetryInterval", driverConfigEntity.getConnectRetryInterval());
        configsNode.put("disabled", deviceConfig.getDisabled().toString());

        if(driverName.equalsIgnoreCase(DriverConfigEntity.MODBUS)) {
            configsNode.put("settings", deviceConfig.getDefaultSettings() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.IEC60870)) {
            configsNode.put("settings", deviceConfig.getDefaultSettings() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.IEC61850)) {
            configsNode.put("settings", deviceConfig.getDefaultSettings() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.DLMS)) {
            configsNode.put("settings", deviceConfig.getSettingsField() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.SNMP)) {
            configsNode.put("settings", deviceConfig.getDefaultSettings() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.REST)) {
            configsNode.put("settings", deviceConfig.getDefaultSettings() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.CSV)) {
            configsNode.put("settings", deviceConfig.getDefaultSettings() );
        }

        bodyNode.set("configs", configsNode);

//        log.info(String.format("Device Post Body Node: \n%s\n", bodyNode.toPrettyString()));

        return openMucRestClient.httpPost(restPath, objectMapper, bodyNode);
    }

    public Response deleteDevice(String deviceName) {
        String restPath = String.format("%s/%s", this.devicesUrl, deviceName);
        return openMucRestClient.httpDelete(restPath);
    }

    public Boolean deleteDevices(List<String> deviceNames) {
        Boolean status = true;
        for(String deviceName: deviceNames) {
            Response response = deleteDevice(deviceName);
            status &= response.getStatus() == 200;
        }
        return status;
    }

}
