package za.co.spsi.openmucdoa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DriverConfigRepository extends JpaRepository<DriverConfigEntity, Long> {

    List<DriverConfigEntity> findDriverConfigEntitiesByDataCollectorId(Long dataCollectorId);

    DriverConfigEntity findDriverConfigEntityByDataCollectorIdAndProtocolDriver(Long dataCollectorId, String protocolDriver);

}
