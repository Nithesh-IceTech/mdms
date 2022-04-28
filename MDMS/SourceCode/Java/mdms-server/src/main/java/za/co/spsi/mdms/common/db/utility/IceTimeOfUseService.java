package za.co.spsi.mdms.common.db.utility;

import org.idempiere.webservice.client.exceptions.WebServiceException;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.utility.MDMSUtilityHelper;
import za.co.spsi.toolkit.util.ExpiringCacheMap;
import za.co.spsi.mdms.util.ExpiringCacheMapTOUComparison;
import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static java.time.DayOfWeek.*;

/**
 * Created by Arno Combrinck
 */
@Startup
@Singleton
@DependsOn({"PropertiesConfig"})
@TransactionManagement(value = TransactionManagementType.BEAN)
public class IceTimeOfUseService {

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    public MDMSUtilityHelper utilityHelper;

    public static String SYNC = UUID.randomUUID().toString();

    private ExpiringCacheMapTOUComparison<String,List<IceTOURow>> touComparisonMap = new ExpiringCacheMapTOUComparison<>(TimeUnit.HOURS.toMillis(3));

    private ExpiringCacheMap<String,List<IceTimeOfUseHistRow>> touMap = new ExpiringCacheMap<>(TimeUnit.HOURS.toMillis(3));

    public IceTimeOfUseService() {

    }

    @PostConstruct
    private void init() {

    }

    public List<IceTimeOfUseHistRow> getTouTimeSlotRows(Integer plvId, Integer plvVersionId) {
        String plvKey = String.format("%d:%d", plvId, plvVersionId);
        synchronized (SYNC) {
            if (!touMap.containsKey(plvKey)) {
                List<IceTimeOfUseHistRow> touRows = utilityHelper.sendGetTOUTimeSlotDetailRequest(plvId, plvVersionId);
                touMap.put(plvKey, touRows);
            }
            return touMap.get(plvKey);
        }
    }

    /**
     * IED-3483: MDMS & AMI Portal: TOU Comparison Profile
     * @param iceTarrifScheduleId
     * @param dayOfWeekName
     * @param entryTimestamp
     * @return
     * @throws WebServiceException
     */
    public List<IceTOURow> getTouComparisonTimeSlotRows(Integer iceTarrifScheduleId, String dayOfWeekName, Timestamp entryTimestamp) throws WebServiceException {

        String plvKey = String.format("%d:%s", iceTarrifScheduleId, dayOfWeekName);
        synchronized (SYNC) {
            if (!touComparisonMap.containsKey(plvKey)) {
                List<IceTOURow> touRows = utilityHelper.sendGetTOUComparrisonTimeSlotDetailRequest(iceTarrifScheduleId, dayOfWeekName);
                touComparisonMap.put(plvKey, touRows);
            }
            return touComparisonMap.get(plvKey);
        }
    }

    public String getTouTimeSlotType(Integer plvId, Integer plvVersionId, Timestamp entryTimestamp) {
        List<IceTimeOfUseHistRow> timeslotRows = getTouTimeSlotRows(plvId, plvVersionId);

        LocalDateTime entryDateTime = entryTimestamp.toLocalDateTime().plusMinutes( propertiesConfig.getMdms_global_timezone_offset() );
        for(IceTimeOfUseHistRow timeSlotRow : timeslotRows) {
            if(inDayOfWeek(timeSlotRow, entryDateTime) && inTimeslot(timeSlotRow, entryDateTime) ) {
                return filterPlvNameForTouType( timeSlotRow.getPlvName() );
            }
        }
        return null;
    }

    // IED-3483: MDMS & AMI Portal: TOU Comparison Profile
    public String getTouComparisonTimeSlotType(Integer iceTarrifScheduleId, String dayOfWeekName , Timestamp entryTimestamp) throws WebServiceException {
        List<IceTOURow> timeslotRowsTouComparison = getTouComparisonTimeSlotRows(iceTarrifScheduleId, dayOfWeekName, entryTimestamp);

        //* Filter the returned set of records to a single record. */
        LocalDateTime entryDateTime = entryTimestamp.toLocalDateTime().plusMinutes(propertiesConfig.getMdms_global_timezone_offset());

        for(IceTOURow timeslotRowTouComparison : timeslotRowsTouComparison) {
            if(inDayOfWeekTouc(timeslotRowTouComparison, entryDateTime) && inTimeslotTouc(timeslotRowTouComparison, entryDateTime) ) {
                return filterPlvNameForTouType( timeslotRowTouComparison.getTIMEOFUSE() );
            }
        }
        return null;
    }

    public List<DayOfWeek> getTouDayOfWeek(String touDayOfWeek) {
        return "Weekdays".equalsIgnoreCase( touDayOfWeek ) ?
                Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)
                : touDayOfWeek != null
                ? Arrays.asList(DayOfWeek.valueOf( touDayOfWeek.toUpperCase() ))
                : null;
    }

    public Boolean inDayOfWeek(IceTimeOfUseHistRow timeSlotRow, LocalDateTime entryDateTime) {
        DayOfWeek entryTimeDow = entryDateTime.getDayOfWeek();
        List<DayOfWeek> dayOfWeeks = getTouDayOfWeek(timeSlotRow.getDowName());
        return dayOfWeeks.stream().anyMatch( dow -> dow.equals(entryTimeDow) );
    }

    /**
     * IED-3483: MDMS & AMI Portal: TOU Comparison Profile
     * @param timeSlotRow
     * @param entryDateTime
     * @return
     */
    public Boolean inDayOfWeekTouc(IceTOURow timeSlotRow, LocalDateTime entryDateTime) {
        DayOfWeek entryTimeDow = entryDateTime.getDayOfWeek();
        List<DayOfWeek> dayOfWeeks = getTouDayOfWeek(timeSlotRow.getDAYOFWEEK());
        return dayOfWeeks.stream().anyMatch( dow -> dow.equals(entryTimeDow) );
    }

    public Boolean inTimeslot(IceTimeOfUseHistRow timeSlotRow, LocalDateTime entryDateTime) {

        LocalTime entryTime = entryDateTime.toLocalTime();

        LocalDateTime startDateTime = LocalDateTime.ofInstant(timeSlotRow.getStartTime().toInstant(), ZoneId.systemDefault() );
        LocalTime startTime = startDateTime.toLocalTime();

        LocalDateTime endDateTime = LocalDateTime.ofInstant(timeSlotRow.getEndTime().toInstant(), ZoneId.systemDefault() );
        LocalTime endTime = endDateTime.toLocalTime();

        Boolean startResult = entryTime.equals(startTime) | entryTime.isAfter(startTime);
        Boolean endResult = entryTime.isBefore(endTime);

        return startResult & endResult;
    }

    /**
     * IED-3483: MDMS & AMI Portal: TOU Comparison Profile
     * @param timeSlotRow
     * @param entryDateTime
     * @return
     */
    public Boolean inTimeslotTouc(IceTOURow timeSlotRow, LocalDateTime entryDateTime) {

        LocalTime entryTime = entryDateTime.toLocalTime();

        LocalDateTime startDateTime = LocalDateTime.ofInstant(timeSlotRow.getSTARTTIME().toInstant(), ZoneId.systemDefault() );
        LocalTime startTime = startDateTime.toLocalTime();

        LocalDateTime endDateTime = LocalDateTime.ofInstant(timeSlotRow.getENDTIME().toInstant(), ZoneId.systemDefault() );
        LocalTime endTime = endDateTime.toLocalTime();

        Boolean startResult = entryTime.equals(startTime) | entryTime.isAfter(startTime);
        Boolean endResult = entryTime.isBefore(endTime);

        return startResult & endResult;
    }

    public String filterPlvNameForTouType(String plvName) {
        if(plvName.toLowerCase().contains("off")) {
            return "OP";
        } else if(plvName.toLowerCase().contains("peak") ) {
            return "P";
        } else if(plvName.toLowerCase().contains("standard")) {
            return "STD";
        } else {
            return null;
        }
    }

}
