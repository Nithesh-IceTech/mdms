package za.co.spsi.openmucdoa.entities.dbviews;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ied_channel_view")
public class IedChannelViewEntity {

    @Id
    @Column(name = "channel_id")
    private Long channel_id;

    @Column(name = "ied_id")
    private Long ied_id;

    @Column(name = "ied_name")
    private String ied_name;

    @Column(name = "channel_name")
    private String channel_name;

    @Column(name = "description")
    private String description;

}
