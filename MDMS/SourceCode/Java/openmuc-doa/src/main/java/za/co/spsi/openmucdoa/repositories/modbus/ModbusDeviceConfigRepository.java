package za.co.spsi.openmucdoa.repositories.modbus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.modbus.ModbusDeviceConfigEntity;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ModbusDeviceConfigRepository extends JpaRepository<ModbusDeviceConfigEntity, Long> {

    List<ModbusDeviceConfigEntity> findModbusDeviceConfigEntitiesByDataCollectorId(Long dataCollectorId);

    List<ModbusDeviceConfigEntity> findModbusDeviceConfigEntitiesByDataCollectorIdAndDriverId(Long dataCollectorId, Long driverId);

    ModbusDeviceConfigEntity findModbusDeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(Long dataCollectorId, Long driverId, String iedName);

}
