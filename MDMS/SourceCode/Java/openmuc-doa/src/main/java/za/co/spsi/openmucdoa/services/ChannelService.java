package za.co.spsi.openmucdoa.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;
import za.co.spsi.openmucdoa.entities.iec104.Iec104ChannelConfigEntity;
import za.co.spsi.openmucdoa.entities.modbus.ModbusChannelConfigEntity;
import za.co.spsi.openmucdoa.interfaces.IChannelConfig;
import za.co.spsi.openmucdoa.services.iec104.Iec104ChannelConfigService;
import za.co.spsi.openmucdoa.services.modbus.ModbusChannelConfigService;

import javax.annotation.PostConstruct;

@Slf4j
@Service
public class ChannelService {

    @Autowired
    private Iec104ChannelConfigService iec104ChannelConfigService;

    @Autowired
    private ModbusChannelConfigService modbusChannelConfigService;

    public ChannelService() {

    }

    @PostConstruct
    private void init() {
        log.info(String.format("Class Constructed: %s", ChannelService.log.getName() ));
    }

    public void saveChannelConfig(String driver, IChannelConfig channelConfig) {

        if(driver.equalsIgnoreCase(DriverConfigEntity.MODBUS)) {

            modbusChannelConfigService.saveChannelConfig( (ModbusChannelConfigEntity) channelConfig );

        } else if(driver.equalsIgnoreCase(DriverConfigEntity.IEC60870)) {

            iec104ChannelConfigService.saveChannelConfig( (Iec104ChannelConfigEntity) channelConfig );

        } else if(driver.equalsIgnoreCase(DriverConfigEntity.IEC61850)) {

            log.info(String.format("Driver %s not yet implemented !", DriverConfigEntity.IEC61850));

        } else if(driver.equalsIgnoreCase(DriverConfigEntity.REST)) {

            log.info(String.format("Driver %s not yet implemented !", DriverConfigEntity.REST));

        } else if(driver.equalsIgnoreCase(DriverConfigEntity.SNMP)) {

            log.info(String.format("Driver %s not yet implemented !", DriverConfigEntity.SNMP));

        } else if(driver.equalsIgnoreCase(DriverConfigEntity.CSV)) {

            log.info(String.format("Driver %s not yet implemented !", DriverConfigEntity.CSV));

        }

    }

}
