package za.co.spsi.openmucdoa.services.modbus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import za.co.spsi.openmucdoa.entities.modbus.ModbusDeviceConfigEntity;
import za.co.spsi.openmucdoa.repositories.modbus.ModbusDeviceConfigRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class ModbusDeviceConfigService {

    @Autowired
    private ModbusDeviceConfigRepository modbusDeviceConfigJpa;

    public ModbusDeviceConfigService() {

    }

    @PostConstruct
    private void init() {

    }

    public List<ModbusDeviceConfigEntity> getAllDevices() {
        return modbusDeviceConfigJpa.findAll();
    }

    public List<ModbusDeviceConfigEntity> getDeviceConfigEntitiesByDataCollectorId(Long dataCollectorId) {
        return modbusDeviceConfigJpa.findModbusDeviceConfigEntitiesByDataCollectorId(dataCollectorId);
    }

    public List<ModbusDeviceConfigEntity> getDeviceConfigEntitiesByDataCollectorIdAndDriverId(Long dataCollectorId, Long driverId) {
        return modbusDeviceConfigJpa.findModbusDeviceConfigEntitiesByDataCollectorIdAndDriverId(dataCollectorId, driverId);
    }

    public ModbusDeviceConfigEntity getDeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(Long dataCollectorId, Long driverId, String iedName) {

        ModbusDeviceConfigEntity modbusDeviceConfigEntity = new ModbusDeviceConfigEntity();

        if(!StringUtils.isEmpty(iedName)) {
            modbusDeviceConfigEntity =
                    modbusDeviceConfigJpa.findModbusDeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(dataCollectorId, driverId, iedName);
        }

        return modbusDeviceConfigEntity;
    }

    public void saveDeviceConfig(ModbusDeviceConfigEntity modbusDeviceConfig) {
        modbusDeviceConfigJpa.save(modbusDeviceConfig);
    }

    public void deleteDeviceConfigs(List<ModbusDeviceConfigEntity> modbusDeviceConfigEntities) {
        modbusDeviceConfigJpa.deleteAll(modbusDeviceConfigEntities);
    }

}
