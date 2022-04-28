package za.co.spsi.openmucdoa.repositories.dbviews;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import za.co.spsi.openmucdoa.entities.dbviews.SignalDataViewEntity;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface SignalDataViewRepository extends JpaRepository<SignalDataViewEntity, Long> {

    @Query(value = "select ied_name, channel_name, description," +
            " application.time_bucket(cast(:interval as interval), time) as entrytime," +
            " avg(double_value) as double_value " +
            " from application.signal_data_view " +
            " where ied_name = :iedName and channel_name = :channelName " +
            " and time between :startDate and :endDate " +
            " group by ied_name, channel_name, description, entrytime " +
            " order by entrytime desc " +
            " offset :offset rows fetch next :numberOfRows rows only", nativeQuery = true)
    List<SignalDataViewEntity> findSignalDataViewsByIedNameAndChannelName(@Param("interval") String interval,
                                                                          @Param("iedName") String iedName,
                                                                          @Param("channelName") String channelName,
                                                                          @Param("startDate") Timestamp startDate,
                                                                          @Param("endDate") Timestamp endDate,
                                                                          @Param("offset") Integer offset,
                                                                          @Param("numberOfRows") Integer numberOfRows);

    @Query(value = "select ied_name, channel_name, description," +
            " application.time_bucket_gapfill(cast(:interval as interval), time, :startDate, :endDate) as entrytime," +
            " application.interpolate(avg(double_value)) as double_value " +
            " from application.signal_data_view " +
            " where ied_name = :iedName and channel_name = :channelName " +
            " and time between :startDate and :endDate " +
            " group by ied_name, channel_name, description, entrytime " +
            " order by entrytime desc ", nativeQuery = true)
    List<SignalDataViewEntity> findSignalDataViewsByIedNameAndChannelNameWithNoOffset(@Param("interval") String interval,
                                                                                      @Param("iedName") String iedName,
                                                                                      @Param("channelName") String channelName,
                                                                                      @Param("startDate") Timestamp startDate,
                                                                                      @Param("endDate") Timestamp endDate);

    @Query(value = "select ied_name, channel_name, description," +
            " application.time_bucket_gapfill(cast('5 sec' as interval), time) as entrytime," +
            " application.interpolate(avg(double_value)) as double_value " +
            " from application.signal_data_view " +
            " where ied_name = :iedName and channel_name = :channelName " +
            " and time >= now() - interval '1 min' and time < now() " +
            " group by ied_name, channel_name, description, entrytime " +
            " order by entrytime desc ", nativeQuery = true)
    List<SignalDataViewEntity> findSignalDataViewsByIedNameAndChannelNameLastMinute(@Param("iedName") String iedName,
                                                                                    @Param("channelName") String channelName);

    @Query(value = "select distinct ied_name from application.signal_data_view order by ied_name asc",nativeQuery = true)
    List<String> findDistinctIedNames();

    @Query(value = "select distinct channel_name from application.signal_data_view where ied_name = :iedName order by channel_name asc",nativeQuery = true)
    List<String> findDistinctChannelNamesByIedName(@Param("iedName") String iedName);

}
