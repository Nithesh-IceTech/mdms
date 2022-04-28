package za.co.spsi.openmucdoa.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.spsi.openmucdoa.entities.DriverConfigEntity;
import za.co.spsi.openmucdoa.repositories.DriverConfigRepository;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class ProtoDriverService {

    @Autowired
    private DriverConfigRepository driverConfigRepository;

    public List<DriverConfigEntity> getDriversByDataCollector(Long dataCollectorId) {

        return driverConfigRepository.findDriverConfigEntitiesByDataCollectorId(dataCollectorId);
    }

    public List<String> updateDriverSelectBoxItems(Long dataCollectorId) {

        List<DriverConfigEntity> driverConfigEntityList = driverConfigRepository
                .findDriverConfigEntitiesByDataCollectorId(dataCollectorId);

        return driverConfigEntityList.stream().map(DriverConfigEntity::getProtocolDriver)
                .collect(Collectors.toList());
    }

    public Integer updateDriverSelectBoxItemsCount(Long dataCollectorId) {

        List<String> driverConfigEntityList = updateDriverSelectBoxItems(dataCollectorId);

        return driverConfigEntityList.size();
    }

    public DriverConfigEntity getDriverByDcAndDriverName(Long dataCollectorId, String driverName) {
        return driverConfigRepository.findDriverConfigEntityByDataCollectorIdAndProtocolDriver(dataCollectorId, driverName);
    }

    public DriverConfigEntity getDriverById(Long driver_id) {
        return driverConfigRepository.findById(driver_id).orElseThrow(null);
    }

    public DriverConfigEntity getFirstDriver() {
        return driverConfigRepository.findAll().get(0);
    }

    public void saveDriverConfig(DriverConfigEntity driverConfigEntity) {
        driverConfigRepository.save(driverConfigEntity);
    }

    public void deleteAllDriverConfigs(List<DriverConfigEntity> driverConfigEntities) {
        driverConfigRepository.deleteAll(driverConfigEntities);
    }

}
