package za.co.spsi.openmucdoa.entities.iec104;

import lombok.Data;
import za.co.spsi.openmucdoa.interfaces.IDeviceConfig;

import javax.persistence.*;

@Data
@Entity
@Table(name = "iec104_devices")
public class Iec104DeviceConfigEntity implements IDeviceConfig {

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

    @Column(name = "communication_status")
    private String communicationStatus;

    @Column(name = "common_address")
    private String commonAddress;

    @Column(name = "msg_frag_timeout")
    private String msgFragTimeout;

    @Column(name = "cot_field_length")
    private String cotFieldLength;

    @Column(name = "common_address_field_length")
    private String commonAddressFieldLength;

    @Column(name = "ioa_field_length")
    private String ioaFieldLength;

    @Column(name = "max_time_no_ack_received")
    private String maxTimeNoAckReceived;

    @Column(name = "max_time_no_ack_sent")
    private String maxTimeNoAckSent;

    @Column(name = "max_idle_time")
    private String maxIdleTime;

    @Column(name = "max_unconfirmed_ipdus_received")
    private String maxUnconfirmedIpdusReceived;

    @Column(name = "startdt_connection_timeout")
    private String startdtConnectionTimeout;

    @Column(name = "read_timeout")
    private String readTimeout;

    @Column(name = "disabled")
    private Boolean disabled;

    public Iec104DeviceConfigEntity() {

    }

    @Override
    public Long getIedDeviceId() {
        return null;
    }

    public void setCustomDeviceAddress(Integer commonAddress, Integer portNumber, String hostAddress) {
        this.ipAddress =    "ca=" + commonAddress.toString() + ";" +
                             "p=" + portNumber.toString() + ";" +
                             "h=" + hostAddress;
    }

    public String setCustomSettings(Long messageFragmentTimeout, Integer cotFieldLength, Integer commonAddressFieldLength,
                             Integer ioaFieldLength, Long maxTimeNoAckReceived, Long maxTimeNoAckSent,
                             Long maxIdleTime, Long maxUnconfirmedIpdusReceived, Long stardtConTimeout) {

        String settings = "mft="   + messageFragmentTimeout.toString() + ";" +
                        "cfl="   + cotFieldLength.toString() + ";" +
                        "cafl="  + commonAddressFieldLength.toString() + ";" +
                        "ifl="   + ioaFieldLength.toString() + ";" +
                        "mtnar=" + maxTimeNoAckReceived.toString() + ";" +
                        "mtnas=" + maxTimeNoAckSent.toString() + ";" +
                        "mit="   + maxIdleTime.toString() + ";" +
                        "mupr="  + maxUnconfirmedIpdusReceived.toString() + ";" +
                        "sct="   + stardtConTimeout.toString();

        return settings;

    }

    @Override
    public String getDeviceAddressField() {

        String ip_address_str = "ca=" + commonAddress.toString() + ";" +
                                "p=" + portNumber.toString() + ";" +
                                "h=" + ipAddress;

        return ip_address_str;
    }

    @Override
    public String getDefaultDeviceAddressField() {

        String ip_address_str = "ca=1;" +
                                "p=2404;" +
                                "h=127.0.0.1";

        return ip_address_str;
    }

    @Override
    public String getSettingsField() {

        String settings = "mft="   + msgFragTimeout + ";" +
                          "cfl="   + cotFieldLength + ";" +
                          "cafl="  + commonAddressFieldLength + ";" +
                          "ifl="   + ioaFieldLength + ";" +
                          "mtnar=" + maxTimeNoAckReceived + ";" +
                          "mtnas=" + maxTimeNoAckSent + ";" +
                          "mit="   + maxIdleTime + ";" +
                          "mupr="  + maxUnconfirmedIpdusReceived + ";" +
                          "sct="   + startdtConnectionTimeout;

        return settings;

    }

    @Override
    public String getDefaultSettings() {
        return "mft=1000;cfl=2;cafl=2;ifl=3;mtnar=3000;mtnas=3000;mit=5000;mupr=10;sct=5000;rt=5000";
    }



}
