package za.co.spsi.openmucdoa.services.iec104;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.spsi.openmucdoa.entities.iec104.Iec104ChannelConfigEntity;
import za.co.spsi.openmucdoa.entities.iec104.Iec104DeviceConfigEntity;
import za.co.spsi.openmucdoa.repositories.iec104.Iec104ChannelConfigRepository;

import javax.annotation.PostConstruct;
import java.util.*;

@Slf4j
@Service
public class Iec104ChannelConfigService {

    @Autowired
    private Iec104ChannelConfigRepository iec104ChannelConfigJpa;

    @Autowired
    private Iec104DeviceConfigService iec104DeviceConfigService;

    public Iec104ChannelConfigService() {

    }

    @PostConstruct
    private void init() {

    }

    public List<Iec104ChannelConfigEntity> getAllChannels() {
        return iec104ChannelConfigJpa.findAll();
    }

    public List<Iec104ChannelConfigEntity> getDeviceChannelsByDeviceId(Long iedDeviceId) {

        List<Iec104ChannelConfigEntity> filteredIec104ChannelConfigList = new ArrayList<>();

        if( iedDeviceId != null ) {

            Comparator<Iec104ChannelConfigEntity> iec104ChannelConfigEntityComparator =
                    Comparator.comparing(Iec104ChannelConfigEntity::getChannelName);

            filteredIec104ChannelConfigList =
                    iec104ChannelConfigJpa.findIec104ChannelConfigEntitiesByIedDeviceId(iedDeviceId);

            Collections.sort( filteredIec104ChannelConfigList, iec104ChannelConfigEntityComparator );

        }

        return filteredIec104ChannelConfigList;
    }

    public List<Iec104ChannelConfigEntity> getDeviceChannelsByIedName(Long dataCollectorId, Long driverId, String iedName) {

        Iec104DeviceConfigEntity deviceConfig = iec104DeviceConfigService.getDeviceConfigEntityByDataCollectorIdAndDriverIdAndIedName(dataCollectorId,driverId,iedName);

        List<Iec104ChannelConfigEntity> filteredIec104ChannelConfigList = new ArrayList<>();

        if( deviceConfig != null ) {

            Comparator<Iec104ChannelConfigEntity> iec104ChannelConfigEntityComparator =
                    Comparator.comparing(Iec104ChannelConfigEntity::getChannelName);

            filteredIec104ChannelConfigList =
                    iec104ChannelConfigJpa.findIec104ChannelConfigEntitiesByIedDeviceId(deviceConfig.getId());

            Collections.sort( filteredIec104ChannelConfigList, iec104ChannelConfigEntityComparator );

        }

        return filteredIec104ChannelConfigList;
    }

    public void saveChannelConfig(Iec104ChannelConfigEntity channelConfig) {
        iec104ChannelConfigJpa.save(channelConfig);
    }

    public void deleteChannel(Iec104ChannelConfigEntity channelConfig) {
        iec104ChannelConfigJpa.delete(channelConfig);
    }

    public void deleteChannels(List<Iec104ChannelConfigEntity> channelConfigList) {
        iec104ChannelConfigJpa.deleteAll(channelConfigList);
    }

}
