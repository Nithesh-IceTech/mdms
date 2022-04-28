package za.co.spsi.openmucdoa.entities.dlms;

import lombok.Data;
import za.co.spsi.openmucdoa.interfaces.IChannelConfig;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dlms_shared_maps")
public class DlmsChannelConfigEntity implements IChannelConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "ied_device_id")
    private Long iedDeviceId;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "description")
    private String  description;

    @Column(name = "channel_type")
    private String channelType;

    @Column(name = "class_id")
    private String classId;

    @Column(name = "obis_code")
    private String obisCode;

    @Column(name = "channel_attribute_id")
    private String attributeId;

    @Column(name = "data_object_type")
    private String dataObjectType;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "scaling_factor")
    private Double scalingFactor;

    @Column(name = "channel_status")
    private String channelStatus;

    @Column(name = "listening")
    private Boolean listening;

    @Column(name = "logging_interval")
    private String loggingInterval;

    @Column(name = "sampling_interval")
    private String samplingInterval;

    @Column(name = "disabled")
    private Boolean disabled;

    public DlmsChannelConfigEntity() {

    }

    @Override
    public String getChannelAddressField() {
        return String.format("a=%s/%s/%s;t=%s",
                classId,
                obisCode,
                attributeId,
                dataObjectType);
    }

    @Override
    public Boolean getListening() {
        return false;
    }

    @Override
    public Boolean getDisabled() {
        return false;
    }

}
