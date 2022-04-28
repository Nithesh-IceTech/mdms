package za.co.spsi.toolkit.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DeviceErrors implements Serializable {
    public Timestamp timestamp;
    public String imei;
    public String apkVersion;
    public String error;
    public String msg;
}