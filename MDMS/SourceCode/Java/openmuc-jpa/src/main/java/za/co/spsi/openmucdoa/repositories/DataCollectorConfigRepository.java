package za.co.spsi.openmucdoa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.DataCollectorConfigEntity;

import java.util.Optional;

@Repository
@Transactional
public interface DataCollectorConfigRepository extends JpaRepository<DataCollectorConfigEntity, Long> {

    Optional<DataCollectorConfigEntity> findByNameId(String nameId);

}
