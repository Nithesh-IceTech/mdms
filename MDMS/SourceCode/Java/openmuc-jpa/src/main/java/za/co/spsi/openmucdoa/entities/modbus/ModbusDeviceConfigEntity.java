package za.co.spsi.openmucdoa.entities.modbus;

import lombok.Data;
import za.co.spsi.openmucdoa.interfaces.IDeviceConfig;

import javax.persistence.*;

@Data
@Entity
@Table(name = "modbus_devices")
public class ModbusDeviceConfigEntity implements IDeviceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "data_collector_id")
    private Long dataCollectorId;

    @Column(name = "ied_name")
    private String iedName;

    @Column(name = "description")
    private String description;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port_number")
    private String portNumber;

    @Column(name = "tcp_timeout")
    private String tcpTimeout;

    @Column(name = "communication_status")
    private String communicationStatus;

    @Column(name = "disabled")
    private Boolean disabled;

    public ModbusDeviceConfigEntity() {

    }

    @Override
    public Long getIedDeviceId() {
        return null;
    }

    @Override
    public String getDeviceAddressField() {
        return ipAddress + ":" + portNumber;
    }

    @Override
    public String getDefaultDeviceAddressField() {
        return "127.0.0.1:502";
    }

    @Override
    public String getSettingsField() {
        return String.format("TCP:timeout=%s", tcpTimeout);
    }

    @Override
    public String getDefaultSettings() {
        return "TCP:timeout=3000";
    }

}
