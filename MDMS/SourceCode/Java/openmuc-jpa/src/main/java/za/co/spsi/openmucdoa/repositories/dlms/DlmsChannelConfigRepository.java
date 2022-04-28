package za.co.spsi.openmucdoa.repositories.dlms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.dlms.DlmsChannelConfigEntity;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface DlmsChannelConfigRepository extends JpaRepository<DlmsChannelConfigEntity, Long> {

    DlmsChannelConfigEntity findDlmsChannelConfigEntityById(Long channelId);

    List<DlmsChannelConfigEntity> findDlmsChannelConfigEntitiesByIedDeviceId(Long iedDeviceId);

    DlmsChannelConfigEntity findDlmsChannelConfigEntityByChannelName(String channelName);

}
