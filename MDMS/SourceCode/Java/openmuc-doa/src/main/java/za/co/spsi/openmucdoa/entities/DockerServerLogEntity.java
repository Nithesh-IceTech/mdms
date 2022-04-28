package za.co.spsi.openmucdoa.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "docker_server_log")
public class DockerServerLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "docker_server_id")
    private Long dockerServerId;

    @Column(name = "status")
    private String status;

    @Column(name = "error")
    private String error;

    @Column(name = "date_time")
    private Timestamp dateTime;

    public DockerServerLogEntity() {

    }

    public DockerServerLogEntity(Long dockerServerId, String status, String error, Timestamp dateTime) {
        this.dockerServerId = dockerServerId;
        this.status = status;
        this.error = error;
        this.dateTime = dateTime;
    }

}
