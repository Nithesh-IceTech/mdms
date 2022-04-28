package za.co.spsi.openmucdoa.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import za.co.spsi.openmucdoa.entities.DockerServerConfigEntity;
import za.co.spsi.openmucdoa.repositories.DockerServerConfigRepository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DockerServerService {

    @Autowired
    private DockerServerConfigRepository dockerServerConfigJpa;

    public DockerServerService() {

    }

    @PostConstruct
    private void init() {

    }

    public Optional<DockerServerConfigEntity> getFirstDockerServerConfig() {
        List<DockerServerConfigEntity> dockerServerConfigList = getAllDockerServerConfigs();
        Optional<DockerServerConfigEntity> firstDockerServerConfig = Optional.of(new DockerServerConfigEntity());
        if(!CollectionUtils.isEmpty(dockerServerConfigList)) {
            firstDockerServerConfig = Optional.of(dockerServerConfigList.get(0));
        }
        return firstDockerServerConfig;
    }

    public Optional<DockerServerConfigEntity> getDockerServerConfigById(Long dockerServerId) {
        return dockerServerConfigJpa.findById(dockerServerId);
    }

    public List<DockerServerConfigEntity> getAllDockerServerConfigs() {
        return dockerServerConfigJpa.findAll();
    }

    public void saveDockerServerConfig(DockerServerConfigEntity dockerServerConfig) {
        dockerServerConfigJpa.save(dockerServerConfig);
    }

}
