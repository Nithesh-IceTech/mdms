package za.co.spsi.openmucdoa.repositories.dbviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import za.co.spsi.openmucdoa.entities.dbviews.IedChannelViewEntity;

@Repository
public interface IedChannelViewRepository extends JpaRepository<IedChannelViewEntity, Long> {


}
