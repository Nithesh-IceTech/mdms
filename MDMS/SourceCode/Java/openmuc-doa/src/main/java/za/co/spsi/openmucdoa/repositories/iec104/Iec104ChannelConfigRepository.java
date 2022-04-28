package za.co.spsi.openmucdoa.repositories.iec104;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.iec104.Iec104ChannelConfigEntity;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface Iec104ChannelConfigRepository extends JpaRepository<Iec104ChannelConfigEntity, Long> {

    List<Iec104ChannelConfigEntity> findIec104ChannelConfigEntitiesByIedDeviceId(Long iedDeviceId);

}
