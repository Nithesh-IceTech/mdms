package za.co.spsi.openmucrest.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;
import za.co.spsi.openmucdoa.interfaces.IChannelConfig;
import za.co.spsi.openmucdoa.interfaces.IDeviceConfig;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
public class ChannelRestClient {

    @Inject
    public OpenMucRestClient openMucRestClient;

    public String channelsUrl;

    public String devicesUrl;

    public ChannelRestClient() {

    }

    @PostConstruct
    private void init() {
        this.devicesUrl = "devices";
        this.channelsUrl = "channels";
    }

    public JsonNode getAllChannels() throws JsonProcessingException {
        String restPath = this.channelsUrl;
        return openMucRestClient.httpGet(restPath).orElse(null);
    }

    public JsonNode getDeviceIdByChannelId(String channelName) throws JsonProcessingException {
        String restPath = String.format("%s/%s/deviceId", this.channelsUrl, channelName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public JsonNode getDriverIdByChannelId(String channelName) throws JsonProcessingException {
        String restPath = String.format("%s/%s/driverId", this.channelsUrl, channelName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public JsonNode getValueByChannelId(String channelName) throws JsonProcessingException {
        String restPath = String.format("%s/%s/value", this.channelsUrl, channelName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public JsonNode getFlagByChannelId(String channelName) throws JsonProcessingException {
        String restPath = String.format("%s/%s/flag", this.channelsUrl, channelName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public JsonNode getConfigByChannel(String channelName) throws JsonProcessingException {
        String restPath = String.format("%s/%s/configs", this.channelsUrl, channelName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public JsonNode getRecordByChannelId(String channelName) throws JsonProcessingException {
        String restPath = String.format("%s/%s", this.channelsUrl, channelName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public JsonNode getAllChannelRecordsByDeviceId(String deviceName) throws JsonProcessingException {
        String restPath = String.format("%s/%s", this.devicesUrl, deviceName);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<JsonNode> responseJson = openMucRestClient.httpGet(restPath);
        return responseJson != null ? responseJson.get() : objectMapper.nullNode();
    }

    public Response createNewChannel(String driverName, IDeviceConfig deviceConfig, IChannelConfig channelConfig) throws JsonProcessingException {

        String restPath = String.format("%s/%s", this.channelsUrl, channelConfig.getChannelName());

        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode bodyNode = objectMapper.createObjectNode();
        bodyNode.put("driver", driverName );
        bodyNode.put("device", deviceConfig.getIedName() );

        ObjectNode configsNode = objectMapper.createObjectNode();
        configsNode.put("description", channelConfig.getDescription() );
        configsNode.put("channelAddress", channelConfig.getChannelAddressField() );
        configsNode.put("listening", channelConfig.getListening().toString() );
        configsNode.put("disabled", channelConfig.getDisabled().toString() );

        if(driverName.equalsIgnoreCase(DriverConfigEntity.MODBUS)) {
            configsNode.put("valueType", channelConfig.getChannelType() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.IEC60870)) {
            configsNode.put("valueType", channelConfig.getDataType() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.IEC61850)) {
            configsNode.put("valueType", channelConfig.getDataType() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.DLMS)) {
            configsNode.put("valueType", channelConfig.getDataType() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.SNMP)) {
            configsNode.put("valueType", channelConfig.getDataType() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.REST)) {
            configsNode.put("valueType", channelConfig.getDataType() );
        } else if(driverName.equalsIgnoreCase(DriverConfigEntity.CSV)) {
            configsNode.put("valueType", channelConfig.getDataType() );
        }

        if(!channelConfig.getListening()) {
            configsNode.put("loggingInterval", channelConfig.getLoggingInterval() );
            configsNode.put("samplingInterval", channelConfig.getSamplingInterval() );
        }
        bodyNode.set("configs", configsNode);

        log.info(String.format("Channel Post Body Node: \n%s\n", bodyNode.toPrettyString()));

        return openMucRestClient.httpPost(restPath, objectMapper, bodyNode);
    }

    public Response deleteChannel(String channelName) {
        String restPath = String.format("%s/%s", this.channelsUrl, channelName);
        return openMucRestClient.httpDelete(restPath);
    }

    public void deleteChannels(List<String> channelNames) {
        for(String channelName: channelNames) {
            deleteChannel(channelName);
        }
    }

}
