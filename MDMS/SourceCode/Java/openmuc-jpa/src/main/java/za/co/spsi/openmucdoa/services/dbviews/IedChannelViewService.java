package za.co.spsi.openmucdoa.services.dbviews;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import za.co.spsi.openmucdoa.entities.dbviews.IedChannelViewEntity;
import za.co.spsi.openmucdoa.repositories.dbviews.IedChannelViewRepository;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Service
public class IedChannelViewService {

    @Autowired
    private IedChannelViewRepository iedChannelViewJpa;

    private Integer iedDeviceCount;
    private Integer iedChannelCount;

    public List<IedChannelViewEntity> getAllIedDevices() {
        List<IedChannelViewEntity> iedChannelViewList = iedChannelViewJpa.findAll();
        if(CollectionUtils.isEmpty(iedChannelViewList)) {
            iedChannelViewList = new ArrayList<>();
        }
        return iedChannelViewList;
    }

    public List<String> updateIedDeviceSelectBox() {
        List<String> iedDeviceList = getAllIedDevices().stream().map(IedChannelViewEntity::getIed_name).distinct().collect(Collectors.toList());
        Collections.sort(iedDeviceList);
        iedDeviceCount = iedDeviceList.size();
        return iedDeviceList;
    }

    public List<String> updateIedChannelSelectBox(String iedName) {
        List<String> iedChannelList = getAllIedDevices().stream()
                .filter(ied -> ied.getIed_name().equalsIgnoreCase(iedName))
                .map(IedChannelViewEntity::getChannel_name).distinct().collect(Collectors.toList());
        Collections.sort(iedChannelList);
        iedChannelCount = iedChannelList.size();
        return iedChannelList;
    }

    public String getIedChannelDescription(String iedName, String channelName) {

        String channelDescription = "";
        List<IedChannelViewEntity> iedChannelViewList = getAllIedDevices();

        if(!CollectionUtils.isEmpty(iedChannelViewList)) {
            List<IedChannelViewEntity> filteredIedChannelViewEntity =
                    iedChannelViewList.stream()
                    .filter(chv -> chv.getIed_name().equalsIgnoreCase(iedName))
                    .filter(chv -> chv.getChannel_name().equalsIgnoreCase(channelName))
                    .collect(Collectors.toList());

            if(!CollectionUtils.isEmpty(filteredIedChannelViewEntity)) {
                IedChannelViewEntity selectedIedChannelViewEntity =
                        filteredIedChannelViewEntity.get(0);
                channelDescription = selectedIedChannelViewEntity.getDescription();
            }

        }

        return channelDescription;
    }

}
