package za.co.spsi.toolkit.dao;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by jaspervdb on 15/06/08.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonAutoDetect
public class RegisterDeviceResp implements Serializable {

}
