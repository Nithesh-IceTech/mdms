package za.co.spsi.openmucdoa.services.iec104;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import za.co.spsi.openmucdoa.entities.iec104.Iec104DeviceConfigEntity;
import za.co.spsi.openmucdoa.repositories.iec104.Iec104DeviceConfigRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class Iec104DeviceConfigService {

    @Autowired
    private Iec104DeviceConfigRepository iec104DeviceConfigJpa;

    public Iec104DeviceConfigService() {

    }

    @PostConstruct
    private void init() {

    }

    public List<Iec104DeviceConfigEntity> getAllDevices() {
        return iec104DeviceConfigJpa.findAll();
    }

    public List<Iec104DeviceConfigEntity> getDeviceConfigEntitiesByDataCollectorIdAndDriverId(Long dataCollectorId, Long driverId) {
        return iec104DeviceConfigJpa.findIec104DeviceConfigEntitiesByDataCollectorIdAndDriverId (dataCollectorId, driverId);
    }

    public Iec104DeviceConfigEntity getDeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(Long dataCollectorId, Long driverId, String iedName) {

        Iec104DeviceConfigEntity iec104DeviceConfigEntity = new Iec104DeviceConfigEntity();

        if(!StringUtils.isEmpty(iedName)) {
            iec104DeviceConfigEntity =
                    iec104DeviceConfigJpa.findIec104DeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(dataCollectorId,driverId,iedName);
        }

        return iec104DeviceConfigEntity;
    }

    public void saveDeviceConfig(Iec104DeviceConfigEntity deviceConfig) {
        iec104DeviceConfigJpa.save(deviceConfig);
    }

    public void deleteDeviceConfig(List<Iec104DeviceConfigEntity> iec104DeviceConfigEntities) {
        iec104DeviceConfigJpa.deleteAll(iec104DeviceConfigEntities);
    }

}
