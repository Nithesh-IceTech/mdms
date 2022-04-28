package za.co.spsi.openmucdoa.entities.iec104;

import lombok.Data;
import za.co.spsi.openmucdoa.interfaces.IChannelConfig;

import javax.persistence.*;

@Data
@Entity
@Table(name = "iec104_shared_maps")
public class Iec104ChannelConfigEntity implements IChannelConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "ied_device_id")
    private Long iedDeviceId;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "description")
    private String  description;

    @Column(name = "common_address")
    private String commonAddress;

    @Column(name = "type_id")
    private String typeId;

    @Column(name = "information_object_address")
    private String informationObjectAddress;

    @Column(name = "data_type")
    private String dataType;

    @Column(name = "channel_status")
    private String channelStatus;

    @Column(name = "value_index")
    private String valueIndex;

    @Column(name = "value_multiple")
    private String valueMultiple;

    @Column(name = "value_select")
    private String valueSelect;

    @Column(name = "listening")
    private Boolean listening;

    @Column(name = "logging_interval")
    private String loggingInterval;

    @Column(name = "sampling_interval")
    private String samplingInterval;

    @Column(name = "disabled")
    private Boolean disabled;

    @Override
    public String getChannelAddressField() {

        return "ca="  + commonAddress + ";" +
               "t="   + typeId + ";" +
               "ioa=" + informationObjectAddress + ";" +
               "dt="  + "v" + ";" +
               "i="   + valueIndex;
    }

    @Override
    public String getChannelType() {
        return null;
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
