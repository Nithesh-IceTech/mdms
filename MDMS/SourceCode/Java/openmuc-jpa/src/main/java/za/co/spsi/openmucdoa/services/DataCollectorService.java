package za.co.spsi.openmucdoa.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import za.co.spsi.openmucdoa.entities.DataCollectorConfigEntity;
import za.co.spsi.openmucdoa.repositories.DataCollectorConfigRepository;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
public class DataCollectorService {

    @Autowired
    private DataCollectorConfigRepository dataCollectorConfigRepository;

    public List<DataCollectorConfigEntity> getAllDataCollectors() {

        List<DataCollectorConfigEntity> dataCollectorConfigEntityList = dataCollectorConfigRepository.findAll();
        List<DataCollectorConfigEntity> filteredDataCollectorConfigList = new ArrayList<>();

        if(!CollectionUtils.isEmpty(dataCollectorConfigEntityList)) {
            Comparator<DataCollectorConfigEntity> portNumberComparator = Comparator.comparing(DataCollectorConfigEntity::getPortNumber);
            Collections.sort( dataCollectorConfigEntityList, portNumberComparator );
            filteredDataCollectorConfigList.addAll(dataCollectorConfigEntityList.stream()
                    .filter(dc -> dc.getNameId().matches("^dc[0-9]") )
                    .collect(Collectors.toList()));
        }

        return filteredDataCollectorConfigList;
    }

    public List<String> updateDataCollectorSelectBox() {

        List<DataCollectorConfigEntity> dataCollectorConfigEntityList = this.getAllDataCollectors();

        return dataCollectorConfigEntityList.stream()
                .filter(dc -> dc.getNameId().matches("^dc[0-9]"))
                .map(DataCollectorConfigEntity::getNameId)
                .collect(Collectors.toList());
    }

    public Integer updateDataCollectorSelectBoxCount() {

        List<String> dataCollectorConfigStringList = this.updateDataCollectorSelectBox();

        return dataCollectorConfigStringList.size();
    }

    public Optional<DataCollectorConfigEntity> getDataCollectorByNameId(String nameId) {

        return dataCollectorConfigRepository.findByNameId(nameId);
    }

    public Optional<DataCollectorConfigEntity> getFirstDataCollector() {

        List<DataCollectorConfigEntity> dataCollectorConfigEntityList = getAllDataCollectors();

        return Optional.of(dataCollectorConfigEntityList.get(0));
    }

    public void saveDataCollectorConfig(DataCollectorConfigEntity dataCollectorConfig) {
        dataCollectorConfigRepository.save(dataCollectorConfig);
    }

}
