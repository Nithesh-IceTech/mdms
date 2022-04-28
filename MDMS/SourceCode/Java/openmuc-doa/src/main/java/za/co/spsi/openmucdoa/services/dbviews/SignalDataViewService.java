package za.co.spsi.openmucdoa.services.dbviews;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import za.co.spsi.openmucdoa.entities.dbviews.SignalDataViewEntity;
import za.co.spsi.openmucdoa.repositories.dbviews.SignalDataViewRepository;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Component
public class SignalDataViewService {

    @Autowired
    private SignalDataViewRepository signalDataViewJpa;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public SignalDataViewService() {

    }

    @PostConstruct
    private void init() {

    }

    public List<SignalDataViewEntity> getSignalDataByIedNameAndChannelNameLastMinute(String iedName, String channelName) {
        return signalDataViewJpa.findSignalDataViewsByIedNameAndChannelNameLastMinute(iedName, channelName);
    }

    public List<SignalDataViewEntity> getSignalDataByIedNameAndChannelName(String interval, String iedName, String channelName,
                                                                           LocalDate startDate, LocalDate endDate,
                                                                           Integer offset, Integer numberOfRows,
                                                                           Boolean ignoreOffSet) {

        List<SignalDataViewEntity> signalDataViewEntities = new ArrayList<>();

        if(!StringUtils.isEmpty(iedName) && !StringUtils.isEmpty(channelName) && startDate != null && endDate != null) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDT = startDate.atStartOfDay();
            LocalDateTime endDT = endDate.atTime(23,59,59);

            if(ignoreOffSet) {

                signalDataViewEntities =
                        signalDataViewJpa.findSignalDataViewsByIedNameAndChannelNameWithNoOffset(
                                interval,
                                iedName,
                                channelName,
                                Timestamp.valueOf(startDT),
                                Timestamp.valueOf(endDT));

            } else {

                signalDataViewEntities =
                        signalDataViewJpa.findSignalDataViewsByIedNameAndChannelName(
                                interval,
                                iedName,
                                channelName,
                                Timestamp.valueOf(startDT),
                                Timestamp.valueOf(endDT),
                                offset,
                                numberOfRows);

            }

        }

        return signalDataViewEntities;
    }

    public Integer getSignalDataByIedNameAndChannelNameCount(String interval, String iedName, String channelName, LocalDate startDate, LocalDate endDate, Integer offset, Integer numberOfRows) {
        Integer count = 0;
        if(!StringUtils.isEmpty(iedName) && !StringUtils.isEmpty(channelName)) {
            List<SignalDataViewEntity> signalDataViewEntities = getSignalDataByIedNameAndChannelName(interval, iedName, channelName, startDate, endDate, offset, numberOfRows, false);
            if(!CollectionUtils.isEmpty(signalDataViewEntities)) {
                count = signalDataViewEntities.size();
            }
        }
        return count;
    }

    public List<String> getDistinctDevices() {
        List<String> deviceList = signalDataViewJpa.findDistinctIedNames();
        return CollectionUtils.isEmpty(deviceList) ? new ArrayList<>() : deviceList;
    }

    public Integer getDistinctDevicesCount() {
        List<String> deviceList = getDistinctDevices();
        return CollectionUtils.isEmpty(deviceList) ? 0 : deviceList.size();
    }

    public List<String> getDistinctChannels(String iedName) {
        List<String> channelList = new ArrayList<>();
        if(!StringUtils.isEmpty(iedName)) {
            channelList = signalDataViewJpa.findDistinctChannelNamesByIedName(iedName);
        }
        return CollectionUtils.isEmpty(channelList) ? new ArrayList<>() : channelList;
    }

    public Integer getDistinctChannelsCount(String iedName) {
        List<String> channelList = getDistinctChannels(iedName);
        return CollectionUtils.isEmpty(channelList) ? 0 : channelList.size();
    }


}
