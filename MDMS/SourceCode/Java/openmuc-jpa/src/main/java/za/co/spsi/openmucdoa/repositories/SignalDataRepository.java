package za.co.spsi.openmucdoa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.SignalDataEntity;

@Repository
@Transactional(readOnly = true)
public interface SignalDataRepository extends JpaRepository<SignalDataEntity, Long> {

}
