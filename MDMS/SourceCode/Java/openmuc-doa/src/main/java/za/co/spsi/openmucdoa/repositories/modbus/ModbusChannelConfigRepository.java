package za.co.spsi.openmucdoa.repositories.modbus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.modbus.ModbusChannelConfigEntity;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ModbusChannelConfigRepository extends JpaRepository<ModbusChannelConfigEntity, Long> {

    ModbusChannelConfigEntity findModbusChannelConfigEntityById(Long channelId);

    List<ModbusChannelConfigEntity> findModbusChannelConfigEntitiesByIedDeviceId(Long iedDeviceId);

    ModbusChannelConfigEntity findModbusChannelConfigEntityByChannelName(String channelName);

}
