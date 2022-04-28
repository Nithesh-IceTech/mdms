package za.co.spsi.openmucdoa.entities.dlms;

import lombok.Data;
import za.co.spsi.openmucdoa.interfaces.IDeviceConfig;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dlms_devices")
public class DlmsDeviceConfigEntity implements IDeviceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_collector_id")
    private Long dataCollectorId;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "ied_name")
    private String iedName;

    @Column(name = "description")
    private String description;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port_number")
    private String portNumber;

    @Column(name = "iec_physical_address")
    private String serialNumber;

    @Column(name = "hdlc_physical_address")
    private String hdlcAddress;

    @Column(name = "logical_device_address")
    private String logicalDeviceAddress;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "communication_status")
    private String communicationStatus;

    @Column(name = "disabled")
    private Boolean disabled;

    public DlmsDeviceConfigEntity() {

    }

    @Override
    public String getDeviceAddressField() {
        return String.format("t=tcp;h=%s;p=%s;hdlc=true;iec=%s;pd=%s",
                ipAddress,
                portNumber,
                serialNumber, // meter serial number
                hdlcAddress); // last 4 digits of serialNumber + 1000
    }

    @Override
    public String getSettingsField() {
        return String.format("Id=%s;cid=%s",
                logicalDeviceAddress, // 1
                clientId); // 16 for public access
    }

    @Override
    public Long getIedDeviceId() {
        return null;
    }

    @Override
    public String getDefaultDeviceAddressField() {
        return null;
    }

    @Override
    public String getDefaultSettings() {
        return null;
    }

}
