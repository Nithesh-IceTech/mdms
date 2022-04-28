package za.co.spsi.toolkit.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceEntityMapReq implements Serializable {

    private Integer deviceEntitySyncMapId;
    private String delivered;
    private String error;
    private String onTablet;

    public DeviceEntityMapReq() {}

    public DeviceEntityMapReq(Integer deviceEntitySyncMapId, String delivered, String error) {
        this.deviceEntitySyncMapId = deviceEntitySyncMapId;
        this.delivered = delivered;
        this.error = error;
    }

    public DeviceEntityMapReq(Integer deviceEntitySyncMapId, String delivered, String error, String onTablet) {
        this.deviceEntitySyncMapId = deviceEntitySyncMapId;
        this.delivered = delivered;
        this.error = error;
        this.onTablet = onTablet;
    }

    public Integer getDeviceEntitySyncMapId() {
        return deviceEntitySyncMapId;
    }

    public void setDeviceEntitySyncMapId(Integer deviceEntitySyncMapId) {
        this.deviceEntitySyncMapId = deviceEntitySyncMapId;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getOnTablet() {
        return onTablet;
    }

    public void setOnTablet(String onTablet) {
        this.onTablet = onTablet;
    }
}
