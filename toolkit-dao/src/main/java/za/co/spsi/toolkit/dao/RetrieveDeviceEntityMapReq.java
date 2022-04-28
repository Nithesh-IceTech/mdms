package za.co.spsi.toolkit.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrieveDeviceEntityMapReq implements Serializable {

    private Integer deviceEntitySyncMapId;
    private String deviceId;
    private String entityId;
    private String forceUpdate;
    private Boolean recall;
    private Integer index;
    private String entity;

    public RetrieveDeviceEntityMapReq() {
    }

    public RetrieveDeviceEntityMapReq(Integer deviceEntitySyncMapId, String deviceId, String entityId, String forceUpdate, Integer index) {
        this.deviceEntitySyncMapId = deviceEntitySyncMapId;
        this.deviceId = deviceId;
        this.entityId = entityId;
        this.forceUpdate = forceUpdate;
        this.index = index;
    }

    public Integer getDeviceEntitySyncMapId() {
        return deviceEntitySyncMapId;
    }

    public void setDeviceEntitySyncMapId(Integer deviceEntitySyncMapId) {
        this.deviceEntitySyncMapId = deviceEntitySyncMapId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(String forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }


    public String getEntity() {
        return entity;
    }

    public RetrieveDeviceEntityMapReq setEntity(String entity) {
        this.entity = entity;
        return this;
    }

    public Boolean getRecall() {
        return recall;
    }

    public void setRecall(Boolean recall) {
        this.recall = recall;
    }
}
