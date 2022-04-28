package za.co.spsi.mdms.common.services;

import lombok.extern.java.Log;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.MeterReadingGapProcessorJobEntity;
import za.co.spsi.mdms.common.db.interfaces.MeterEntity;
import za.co.spsi.toolkit.db.DSDB;

import javax.ejb.*;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static za.co.spsi.mdms.common.db.MeterReadingGapProcessorJobEntity.JobTypes.TOTAL_ENERGY;
import static za.co.spsi.mdms.common.db.MeterReadingGapProcessorJobEntity.JobTypes.WATER;
import static za.co.spsi.mdms.common.services.MeterDataService.Interval.HALF_HOURLY;
import static za.co.spsi.mdms.common.services.MeterDataService.Interval.HOURLY;

@Log
@Startup
@Singleton
@AccessTimeout(value=300000)
@DependsOn({"MDMSUpgradeService"})
@TransactionManagement(value = TransactionManagementType.BEAN)
public class ReadingGenericGapIdentifier {

    private long getTimeDiff(MeterEntity meter, MeterReadingEntity reading, Timestamp remoteEntryTime, boolean isWater) {
        Integer gapInterval = isWater ? HOURLY.minutes : HALF_HOURLY.minutes;

        if( meter.getMeterType().equalsIgnoreCase("generic") ) {
            Timestamp genericTS = reading.adjustTimestamp( gapInterval, remoteEntryTime );
            return ( ( genericTS.getTime() - meter.getMaxEntryTime().getTime() )  / 1000 ) / 60;
        } else {
            return (( remoteEntryTime.getTime() - meter.getMaxEntryTime().getTime() )  / 1000 ) / 60;
        }
    }

    public void identifyMeterReadingGaps(Connection connection, MeterEntity meter, MeterReadingEntity reading, Timestamp remoteEntryTime, boolean isWater) {
        long timeDiff = meter.getMaxEntryTime() != null ? getTimeDiff(meter,reading,remoteEntryTime,isWater) : 0;
        Integer gapInterval = isWater ? HOURLY.minutes : HALF_HOURLY.minutes;

        if (timeDiff > gapInterval) {
            Timestamp gapFromDate = Timestamp.valueOf(meter.getMaxEntryTime().toLocalDateTime().minusHours(2));
            Timestamp gapToDate = Timestamp.valueOf(remoteEntryTime.toLocalDateTime().plusHours(2));

            String jobType = isWater ? WATER.name() : TOTAL_ENERGY.name();
            MeterReadingGapProcessorJobEntity newGapJob = MeterReadingGapProcessorJobEntity
                    .create(connection,meter,jobType,gapFromDate,gapToDate,
                    Timestamp.valueOf(LocalDateTime.now().plusHours(3)),gapInterval);

            DSDB.set(connection,newGapJob);
        }
    }

}
