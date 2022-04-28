package za.co.spsi.openmucdoa.entities.modbus;

import lombok.Data;
import za.co.spsi.openmucdoa.interfaces.IChannelConfig;

import javax.persistence.*;

@Data
@Entity
@Table(name = "modbus_shared_maps")
public class ModbusChannelConfigEntity implements IChannelConfig {

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

    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "primary_table")
    private String primaryTable;

    @Column(name = "register_address")
    private Integer registerAddress;

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

    @Override
    public String getChannelAddressField() {
//        Example -> 255:HOLDING_REGISTERS:0:SHORT
        return unitId + ":" +
               primaryTable + ":" +
               registerAddress + ":" +
               channelType;
    }

}
