package za.co.spsi.openmucdoa.services.modbus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.spsi.openmucdoa.entities.modbus.ModbusChannelConfigEntity;
import za.co.spsi.openmucdoa.entities.modbus.ModbusDeviceConfigEntity;
import za.co.spsi.openmucdoa.repositories.modbus.ModbusChannelConfigRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
public class ModbusChannelConfigService {

    @Autowired
    private ModbusChannelConfigRepository modbusChannelConfigJpa;

    @Autowired
    private ModbusDeviceConfigService modbusDeviceConfigService;

    public ModbusChannelConfigService() {

    }

    @PostConstruct
    private void init() {

    }

    public List<ModbusChannelConfigEntity> getDeviceChannelsByIedName(Long dataCollectorId, Long driverId, String iedName) {

        ModbusDeviceConfigEntity deviceConfig = modbusDeviceConfigService.getDeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(dataCollectorId, driverId, iedName);

        List<ModbusChannelConfigEntity> filteredModbusChannelConfigList = new ArrayList<>();

        if( deviceConfig != null ) {

            Comparator<ModbusChannelConfigEntity> modbusChannelConfigEntityComparator =
                    Comparator.comparing(ModbusChannelConfigEntity::getChannelName);

            filteredModbusChannelConfigList =
                    modbusChannelConfigJpa.findModbusChannelConfigEntitiesByIedDeviceId(deviceConfig.getId());

            Collections.sort( filteredModbusChannelConfigList, modbusChannelConfigEntityComparator );

        }

        return filteredModbusChannelConfigList;
    }

    public Optional<ModbusChannelConfigEntity> getChannelConfigEntityByChannelName(String channelName) {
        return Optional.of( modbusChannelConfigJpa.findModbusChannelConfigEntityByChannelName(channelName) );
    }

    public Optional<ModbusChannelConfigEntity> getChannelConfigEntityByChannelId(Long channelId) {
        return Optional.of( modbusChannelConfigJpa.findModbusChannelConfigEntityById(channelId) );
    }

    public void saveChannelConfig(ModbusChannelConfigEntity channelConfig) {
        modbusChannelConfigJpa.save(channelConfig);
    }

    public void deleteChannelConfig(List<ModbusChannelConfigEntity> channelConfigList) {
        modbusChannelConfigJpa.deleteAll(channelConfigList);
    }

}
