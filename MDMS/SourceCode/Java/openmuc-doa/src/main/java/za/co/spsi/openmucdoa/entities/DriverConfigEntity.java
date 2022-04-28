package za.co.spsi.openmucdoa.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "dc_drivers")
public class DriverConfigEntity {

    public enum DriverType {
        modbus, iec60870, iec61850, dlms, rest, csv, snmp
    }

    public static String MODBUS = DriverType.modbus.name();
    public static String IEC60870 = DriverType.iec60870.name();
    public static String IEC61850 = DriverType.iec61850.name();
    public static String DLMS = DriverType.dlms.name();
    public static String REST = DriverType.rest.name();
    public static String CSV = DriverType.csv.name();
    public static String SNMP = DriverType.snmp.name();

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "data_collector_id")
    private Long dataCollectorId;

    @Column(name = "protocol_driver")
    private String protocolDriver;

    @Column(name = "sampling_timeout")
    private String samplingTimeout;

    @Column(name = "connection_retry_interval")
    private String connectRetryInterval;

    @Column(name = "status")
    private String status;

    @Column(name = "disabled")
    private Boolean disabled;

}
