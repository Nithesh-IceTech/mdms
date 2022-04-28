package za.co.spsi.openmucdoa.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "data_collectors")
public class DataCollectorConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "docker_server_id", nullable = false)
    private Long dockerServerId;

    @Column(name = "name_id", nullable = false)
    private String nameId;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "port_number", nullable = false)
    private String portNumber;

    @Column(name = "username", nullable = true)
    private String username;

    @Column(name = "password", nullable = true)
    private String password;

    @Column(name = "status", nullable = true)
    private String  status;

    @Column(name = "disabled", nullable = true)
    private Boolean disabled;

}


//    CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                     PORTS                    NAMES
//    75c11d41f29c        openmuc-image       "./openmuc start -fg…"   2 weeks ago         Up 29 hours                0.0.0.0:1987->8888/tcp   openmuc-dc1