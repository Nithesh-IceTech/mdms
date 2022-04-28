package za.co.spsi.openmucdoa.repositories.iec104;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.iec104.Iec104DeviceConfigEntity;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface Iec104DeviceConfigRepository extends JpaRepository<Iec104DeviceConfigEntity, Long> {

    List<Iec104DeviceConfigEntity> findIec104DeviceConfigEntitiesByDataCollectorIdAndDriverId(Long dataCollectorId, Long driverId);

    Iec104DeviceConfigEntity findIec104DeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(Long dataCollectorId, Long driverId, String iedName);

}
