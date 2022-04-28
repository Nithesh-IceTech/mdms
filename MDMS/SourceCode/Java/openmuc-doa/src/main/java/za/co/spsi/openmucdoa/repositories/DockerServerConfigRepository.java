package za.co.spsi.openmucdoa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.DockerServerConfigEntity;

@Repository
@Transactional(readOnly = true)
public interface DockerServerConfigRepository extends JpaRepository<DockerServerConfigEntity, Long> {


}
