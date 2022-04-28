package za.co.spsi.openmucdoa.repositories.dlms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.dlms.DlmsDeviceConfigEntity;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DlmsDeviceConfigRepository extends JpaRepository<DlmsDeviceConfigEntity, Long> {

    List<DlmsDeviceConfigEntity> findDlmsDeviceConfigEntitiesByDataCollectorId(Long dataCollectorId);

    List<DlmsDeviceConfigEntity> findDlmsDeviceConfigEntitiesByDataCollectorIdAndDriverId(Long dataCollectorId, Long driverId);

    DlmsDeviceConfigEntity findDlmsDeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(Long dataCollectorId, Long driverId, String iedName);

}
