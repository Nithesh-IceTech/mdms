package za.co.spsi.openmucdoa.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "docker_server")
public class DockerServerConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port_number")
    private String portNumber;

}
