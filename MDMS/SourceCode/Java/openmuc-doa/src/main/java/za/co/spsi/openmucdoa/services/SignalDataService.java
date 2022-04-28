package za.co.spsi.openmucdoa.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import za.co.spsi.openmucdoa.entities.SignalDataEntity;
import za.co.spsi.openmucdoa.repositories.SignalDataRepository;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
public class SignalDataService {

    @Autowired
    private SignalDataRepository signalDataJpa;

    public SignalDataService() {

    }

    @PostConstruct
    private void init() {

    }

    public void saveAllSignalData(List<SignalDataEntity> signalDataList) {
        signalDataJpa.saveAll(signalDataList);
    }

    public void saveSignalData(SignalDataEntity signalData) {
        signalDataJpa.save(signalData);
    }

}
