package za.co.spsi.openmucdoa.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "signal_data")
public class SignalDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "signal_device_id")
    private Long deviceId;

    @Column(name = "signal_channel_id")
    private Long channelId;

    @Column(name = "time")
    private Timestamp timestamp;

    @Column(name = "value_type")
    private String valueType;

    @Column(name = "flag")
    private String  flag;

    @Column(name = "double_value")
    private Double doubleValue;

}
