package academy.learningprogramming;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class AdjustTimestampService {

    public AdjustTimestampService() {

    }

    public Timestamp adjustTimestamp(Integer nearestMin, Timestamp unadjustedTS ) {

        String tmzOffset = "0";

        long adjustedTimeEpochMilli = 0;
        int adjustMin = 0;
        long milliSecondAdj = 0;
        int min = unadjustedTS.toLocalDateTime().getMinute();
        int mode = min % nearestMin;

        if( mode > ( nearestMin / 2 ) ) {
            adjustMin = nearestMin - mode;
        } else {
            adjustMin = 0 - mode;
        }

        milliSecondAdj = adjustMin * 60 * 1000;

        adjustedTimeEpochMilli = unadjustedTS.getTime() + milliSecondAdj;

        LocalDateTime adjustedTime = LocalDateTime.ofEpochSecond(adjustedTimeEpochMilli/1000, 0,
                ZoneOffset.ofHours(Integer.parseInt(tmzOffset)/60) )
                .truncatedTo(ChronoUnit.MINUTES);

        return Timestamp.valueOf( adjustedTime );
    }

}
