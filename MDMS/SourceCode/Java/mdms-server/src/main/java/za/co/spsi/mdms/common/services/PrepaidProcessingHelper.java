package za.co.spsi.mdms.common.services;

import org.springframework.util.CollectionUtils;
import za.co.spsi.mdms.common.dao.MeterResultData;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.dao.PrepaidBatchData;
import za.co.spsi.mdms.common.dao.PrepaidBatchTOUData;
import za.co.spsi.mdms.common.dao.ano.MeterRegister;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.PrepaidMeterMeterReadingsView;
import za.co.spsi.mdms.common.db.utility.IcePrepaidTimeOfUseView;
import za.co.spsi.mdms.common.db.utility.IcePrepaidTimeOfUseViewSyncService;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.pjtk.util.Assert;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.*;
import static za.co.spsi.mdms.common.db.MeterReadingEntity.METER_ENTRY_FIELDS;

@Singleton()
@AccessTimeout(value=1800000)
@DependsOn("MDMSUpgradeService")
@TransactionManagement(value = TransactionManagementType.BEAN)
public class PrepaidProcessingHelper extends AbstractPrepaidService {

    @Resource(mappedName = "java:/jdbc/mdms")
    public javax.sql.DataSource mdmsDataSource;

    @Inject
    public IcePrepaidTimeOfUseViewSyncService icePrepaidTimeOfUseViewSyncService;

    @Inject
    public MeterDataService meterDataService;

    @Inject
    public PropertiesConfig propertiesConfig;

    public static Logger TAG = Logger.getLogger(PrepaidProcessingHelper.class.getName());

    private DateTimeFormatter dtFormatter;

    public PrepaidProcessingHelper() {

    }

    public PrepaidBatchData processPrepaidRegisters(MeterResultDataArray dataSet,
                                                    Timestamp ppBatchEntryTime,
                                                    String mdmsMeterRegisterId,
                                                    Boolean latestReadingOnlyFlag) {

        dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        PrepaidBatchData prepaidBatchData = new PrepaidBatchData();

        try {

            MeterResultData resultData = this.getMeterReading(dataSet, ppBatchEntryTime, latestReadingOnlyFlag);
            Assert.notNull(resultData.getEntryTime(), String.format("Meter reading could not be found for EntryTime: %s", dtFormatter.format(ppBatchEntryTime.toLocalDateTime()) ));
            prepaidBatchData.setEntryTime(resultData.getEntryTime());

            // Total Energy Registers
            if( mdmsMeterRegisterId.matches("1.1.[1,2,3,4].8.0.255") ) {
                if( mdmsMeterRegisterId.matches("1.1.1.8.0.255") ) { // Total kWh Import
                    if(resultData.getTotalKwhP() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getTotalKwhP());
                        prepaidBatchData.setRegisterConsumption(resultData.getTotalKwhPUsage() != null ? resultData.getTotalKwhPUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.2.8.0.255") ) { // Total kWh Export
                    if(resultData.getTotalKwhN() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getTotalKwhN());
                        prepaidBatchData.setRegisterConsumption(resultData.getTotalKwhNUsage() != null ? resultData.getTotalKwhNUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.3.8.0.255") ) { // Total kvarh Import
                    if(resultData.getTotalKVarP() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getTotalKVarP());
                        prepaidBatchData.setRegisterConsumption(resultData.getTotalKVarPUsage() != null ? resultData.getTotalKVarPUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.4.8.0.255") ) { // Total kvarh Export
                    if(resultData.getTotalKVarN() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getTotalKVarN());
                        prepaidBatchData.setRegisterConsumption(resultData.getTotalKVarNUsage() != null ? resultData.getTotalKVarNUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

            }

            // T1 Energy Registers
            if( mdmsMeterRegisterId.matches("1.1.[1,2,3,4].8.1.255") ) {

                if( mdmsMeterRegisterId.matches("1.1.1.8.1.255") ) { // T1 kWh Import
                    if(resultData.getT1KwhP() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getT1KwhP());
                        prepaidBatchData.setRegisterConsumption(resultData.getT1KwhPUsage() != null ? resultData.getT1KwhPUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.2.8.1.255") ) { // T1 kWh Export
                    if(resultData.getT1KwhN() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getT1KwhN());
                        prepaidBatchData.setRegisterConsumption(resultData.getT1KwhNUsage() != null ? resultData.getT1KwhNUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.3.8.1.255") ) { // T1 kvarh Import
                    if(resultData.getT1KVarP() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getT1KVarP());
                        prepaidBatchData.setRegisterConsumption(resultData.getT1KVarPUsage() != null ? resultData.getT1KVarPUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.4.8.1.255") ) { // T1 kvarh Export
                    if(resultData.getT1KVarN() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getT1KVarN());
                        prepaidBatchData.setRegisterConsumption(resultData.getT1KVarNUsage() != null ? resultData.getT1KVarNUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

            }

            // T2 Energy Registers
            if( mdmsMeterRegisterId.matches("1.1.[1,2,3,4].8.2.255") ) {

                if( mdmsMeterRegisterId.matches("1.1.1.8.2.255") ) { // T2 kWh Import
                    if(resultData.getT2KwhP() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getT2KwhP());
                        prepaidBatchData.setRegisterConsumption(resultData.getT2KwhPUsage() != null ? resultData.getT2KwhPUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.2.8.2.255") ) { // T2 kWh Export
                    if(resultData.getT2KwhN() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getT2KwhN());
                        prepaidBatchData.setRegisterConsumption(resultData.getT2KwhNUsage() != null ? resultData.getT2KwhNUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.3.8.2.255") ) { // T2 kvarh Import
                    if(resultData.getT2KVarP() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getT2KVarP());
                        prepaidBatchData.setRegisterConsumption(resultData.getT2KVarPUsage() != null ? resultData.getT2KVarPUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.4.8.2.255") ) { // T2 kvarh Export
                    if(resultData.getT2KVarN() > 0.0) {
                        prepaidBatchData.setRegisterReading(resultData.getT2KVarN());
                        prepaidBatchData.setRegisterConsumption(resultData.getT2KVarNUsage() != null ? resultData.getT2KVarNUsage() : 0.0);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

            }

            // Demand (kVA) Registers
            if( mdmsMeterRegisterId.matches("1.1.9.6.[0,1,2].255") ) {

                if( mdmsMeterRegisterId.matches("1.1.9.6.0.255") ) { // Total kVA
                    OptionalDouble maxTotalkVA = dataSet.getMaxValue("totalKVA");
                    Double maxTotalkVADouble   = maxTotalkVA == null ? 0.0 : maxTotalkVA.getAsDouble();
                    if(maxTotalkVADouble > 0.0) {
                        prepaidBatchData.setRegisterReading(maxTotalkVADouble);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.9.6.1.255") ) { // T1 kVA
                    OptionalDouble maxT1kVA    = dataSet.getMaxValue("t1KVA");
                    Double maxT1kVADouble      = maxT1kVA == null ? 0.0 : maxT1kVA.getAsDouble();
                    if(maxT1kVADouble > 0.0) {
                        prepaidBatchData.setRegisterReading(maxT1kVADouble);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

                if( mdmsMeterRegisterId.matches("1.1.9.6.2.255") ) { // T2 kVA
                    OptionalDouble maxT2kVA    = dataSet.getMaxValue("t2KVA");
                    Double maxT2kVADouble      = maxT2kVA == null ? 0.0 : maxT2kVA.getAsDouble();
                    if(maxT2kVADouble > 0.0) {
                        prepaidBatchData.setRegisterReading(maxT2kVADouble);
                    } else {
                        prepaidBatchData.setRegisterReading(null);
                    }
                }

            }

            // Water Registers Cold/Hot
            if( mdmsMeterRegisterId.matches("[8,9].1.1.0.0.255") ) {

                if(resultData.getVolume1() > 0.0) {
                    prepaidBatchData.setRegisterReading(resultData.getVolume1());
                    prepaidBatchData.setRegisterConsumption(resultData.getVolume1Usage() != null ? resultData.getVolume1Usage() : 0.0);
                } else {
                    prepaidBatchData.setRegisterReading(null);
                }

            }

        } catch(Exception ex) {
            prepaidBatchData.setRegisterReading(null);
            prepaidBatchData.setEntryTime(null);
            if( ex.getClass().equals(NoSuchElementException.class) ) {
                TAG.severe(String.format("Register: %s, no meter reading could be found for EntryTime: %s",
                        mdmsMeterRegisterId,
                        dtFormatter.format(ppBatchEntryTime.toLocalDateTime()))
                );
            } else {
                ex.printStackTrace();
            }
        }

        return prepaidBatchData;
    }

    // TODO IED-4286: Develop Prepaid TOU Service
    public PrepaidBatchTOUData processPrepaidTOURegisters(MeterResultDataArray dataSet,
                                                          Map<String,PrepaidBatchTOUData> ppBatchTouRegisterCacheMap,
                                                          PrepaidBatchData prepaidBatchData,
                                                          String iceMeterNumber,
                                                          String iceMeterRegisterId,
                                                          String mdmsMeterRegisterId,
                                                          Boolean latestReadingOnlyFlag) {

        Boolean isPPTOUMeterAndReg = icePrepaidTimeOfUseViewSyncService.isPPTimeOfUseMeterAndRegister(iceMeterNumber,iceMeterRegisterId,mdmsMeterRegisterId);
        dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TAG.info(String.format("IceMeterNumber: %s, IceMeterRegister: %s, MdmsMeterRegisterId: %s -> isPPTimeOfUseMeterAndRegister ? %s",
                iceMeterNumber, iceMeterRegisterId, mdmsMeterRegisterId, isPPTOUMeterAndReg));

        String cacheMapKey = iceMeterNumber + ":" + iceMeterRegisterId + ":" + mdmsMeterRegisterId;
        PrepaidBatchTOUData prepaidBatchTOUData = ppBatchTouRegisterCacheMap.get(cacheMapKey);
        prepaidBatchTOUData = prepaidBatchTOUData != null ? prepaidBatchTOUData : new PrepaidBatchTOUData();
        prepaidBatchTOUData.setIsPPTOUMeterAndReg( isPPTOUMeterAndReg );

        Assert.notNull(prepaidBatchData.getEntryTime(),
                String.format("Prepaid batch entry time is null for ICE Meter Number: %s, ICE Meter Register ID: %s, MDMS Meter Register ID: %s,",
                        iceMeterNumber, iceMeterRegisterId, mdmsMeterRegisterId) );

        if (isPPTOUMeterAndReg) {

            if( prepaidBatchTOUData.getNextReading() != null )
                prepaidBatchTOUData.setPrevReading( prepaidBatchTOUData.getNextReading() );

            prepaidBatchTOUData.setPrevEntryTime( prepaidBatchTOUData.getNextEntryTime() );
            prepaidBatchTOUData.setPrevEntryTimeLocal( prepaidBatchTOUData.getNextEntryTimeLocal() );
            prepaidBatchTOUData.setNextEntryTime( Timestamp.valueOf( prepaidBatchData.getEntryTime().toLocalDateTime() ) );
            prepaidBatchTOUData.setNextEntryTimeLocal( Timestamp.valueOf( prepaidBatchData.getEntryTime().toLocalDateTime()
                    .plusMinutes( propertiesConfig.getMdms_global_timezone_offset() ) ) );

            prepaidBatchTOUData.setAccumulatedConsumption(0.0);

            // This loop iterates over all TOU timeslots for this particular iceMeterNumber and iceMeterRegisterId
            for (IcePrepaidTimeOfUseView touView : icePrepaidTimeOfUseViewSyncService.filterByIceMeterNumberAndRegisterId(iceMeterNumber, iceMeterRegisterId, mdmsMeterRegisterId)) {

                Timestamp startTS = touView.startTime.get();
                Timestamp endTS = touView.endTime.get();
                String dayOfWeek = touView.dowName.get();
                Double prevTouViewReading = touView.iceMeterReading.get();

                if(prepaidBatchTOUData.getPrevReading() == null && prepaidBatchTOUData.getNextReading() == null) {
                    prepaidBatchTOUData.setPrevReading( prevTouViewReading );
                    prepaidBatchTOUData.setNextReading( prevTouViewReading );
                }

                double consumption = 0.0;

                if(latestReadingOnlyFlag) {
                    MeterResultDataArray subSetArray = this.getSubSetMeterDataArray(dataSet,startTS,endTS,dayOfWeek,propertiesConfig.getMdms_global_timezone_offset());
                    if(subSetArray.size() > 0) {
                        consumption = subSetArray.calculateConsumption(getMappedName(mdmsMeterRegisterId) + "Usage");
                    }
                } else {
                    Boolean isInTimeslot = this.isInTimeSlot(prepaidBatchData.getEntryTime(), startTS, endTS, dayOfWeek,propertiesConfig.getMdms_global_timezone_offset());
                    if (isInTimeslot) {
                        consumption = prepaidBatchData.getRegisterConsumption() != null ? prepaidBatchData.getRegisterConsumption() : 0.0;
                    }
                }

                prepaidBatchTOUData.setAccumulatedConsumption( prepaidBatchTOUData.getAccumulatedConsumption() + consumption );

                TAG.finest(String.format("ICE Meter Number: %s, ICE Meter Register: %s, PP Batch EntryTime: %s, DayOfWeek: %s, Timeslot Start: %s, Timeslot End: %s, Accumulated Consumption: -> %.5f",
                        iceMeterNumber,
                        iceMeterRegisterId,
                        dtFormatter.format(  prepaidBatchData.getEntryTime().toLocalDateTime().plusMinutes( propertiesConfig.getMdms_global_timezone_offset() ) ),
                        dayOfWeek,
                        dtFormatter.format( startTS.toLocalDateTime() ),
                        dtFormatter.format( endTS.toLocalDateTime() ),
                        prepaidBatchTOUData.getAccumulatedConsumption() ));
            }

            prepaidBatchTOUData.setNextReading( prepaidBatchTOUData.getPrevReading() + prepaidBatchTOUData.getAccumulatedConsumption() );

            ppBatchTouRegisterCacheMap.put(cacheMapKey, prepaidBatchTOUData);

            TAG.info(String.format("PP TOU Result: %s", prepaidBatchTOUData.toString()));
        }

        return prepaidBatchTOUData;
    }

    public void updateMeterReadingsViewPrepaidRegisterValue(PrepaidMeterMeterReadingsView readingsView, PrepaidBatchData batchData, PrepaidBatchTOUData batchTOUData, String mdmsMeterRegisterId) {

        Assert.notNull(batchData.getEntryTime(), String.format("Prepaid batch entry time is null !") );

        readingsView.meterReadingEntity.entryTime.set(batchData.getEntryTime());

        if(batchTOUData != null) {
            if(batchTOUData.getIsPPTOUMeterAndReg()) {
                readingsView.meterReadingEntity.getRegField(mdmsMeterRegisterId).set(batchTOUData.getNextReading());
            } else {
                readingsView.meterReadingEntity.getRegField(mdmsMeterRegisterId).set(batchData.getRegisterReading());
            }
        }

    }

    public MeterResultData getMeterReading(MeterResultDataArray dataSet, Timestamp entryTime, Boolean latestOnly) {
        // TODO IED-4581: PP-MDMS Meter Reading Times differs from ICE Utilities Meter Reading times
        // If the getValueByTimestamp method returns 0.0, then the value must be excluded from the dataRow sent to ICE UTIL
        return latestOnly ? dataSet.getLastValue() : dataSet.getValueByTimestamp(entryTime);
    }

    public MeterResultDataArray getPrepaidBatchMeterReadings(String prepaidBatchId, String meterSerialN) {

        List<MeterReadingEntity> prepaidBatchMeterReadings =
                DataSourceDB.getAllAsList(MeterReadingEntity.class, mdmsDataSource,
                        "select * from METER_READING where PREPAID_METER_READING_BATCH_ID = ? order by ENTRY_TIME asc", prepaidBatchId);

        Timestamp to;
        Timestamp toTmp;
        Timestamp from;
        Timestamp fromTmp;

        MeterResultDataArray dataSet = null;

        // Get actual time window from prepaid batch readings
        if(!CollectionUtils.isEmpty(prepaidBatchMeterReadings)) {
            Integer listLen = prepaidBatchMeterReadings.size();
            if(listLen > 1) {

                from = prepaidBatchMeterReadings.get(0).entryTime.get();
                Assert.notNull(from, String.format("Prepaid batch: %s, From Date is null.", prepaidBatchId));
                from = Timestamp.from(from.toInstant().minusSeconds(TimeUnit.MINUTES.toSeconds(30)));

                to = prepaidBatchMeterReadings.get(listLen - 1).entryTime.get();
                Assert.notNull(to, String.format("Prepaid batch: %s, To Date is null.", prepaidBatchId));
                to = Timestamp.from(to.toInstant().plusSeconds(TimeUnit.MINUTES.toSeconds(30)));

                Boolean shouldSwap = ( to.getTime() - from.getTime() ) < 0;

                fromTmp = shouldSwap ? to : from;
                toTmp = shouldSwap ? from : to;
                from = fromTmp;
                to = toTmp;

                // Get calculated/gap filled meter readings over time window
                dataSet = meterDataService.getDetailData(meterSerialN, from, to, 0
                        , MeterDataService.Interval.HALF_HOURLY, "series1", null ,null, false);

            }
        }

        return dataSet;
    }

    public List<DayOfWeek> getTouDayOfWeek(String touDayOfWeek) {
        return "Weekdays".equalsIgnoreCase( touDayOfWeek ) ?
                Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)
                : touDayOfWeek != null
                ? Arrays.asList(DayOfWeek.valueOf( touDayOfWeek.toUpperCase() ))
                : null;
    }

    public static String getMappedName(String registerId) {
        List<String> fields =
                METER_ENTRY_FIELDS.stream()
                        .filter(f -> f.getAnnotation(MeterRegister.class) != null
                                && Arrays.stream(f.getAnnotation(MeterRegister.class).value()).anyMatch(s -> s.equals(registerId)))
                        .map(f -> f.getName())
                        .collect(Collectors.toCollection(ArrayList::new));
        Assert.isTrue(!fields.isEmpty(), "Could not find mapping for register %s", registerId);
        return fields.get(0);
    }

    public Boolean isInTimeSlot(Timestamp entryTimeUTC, Timestamp startTS, Timestamp endTS, String dow, long tmzOffset) {

        LocalDateTime startDateTime = startTS.toLocalDateTime();
        LocalTime startTime = startDateTime.toLocalTime();

        LocalDateTime endDateTime = endTS.toLocalDateTime();
        LocalTime endTime = endDateTime.toLocalTime();

        LocalDateTime entryDateTime = entryTimeUTC.toLocalDateTime().plusMinutes(tmzOffset);
        LocalTime entryTime = entryDateTime.toLocalTime();
        DayOfWeek entryTimeDow = entryDateTime.getDayOfWeek();

        Boolean startResult = entryTime.equals(startTime) | entryTime.isAfter(startTime);
        Boolean endResult = entryTime.isBefore(endTime);
        Boolean dowResult = getTouDayOfWeek(dow).stream().anyMatch( dayOfWeek -> dayOfWeek.equals( entryTimeDow ) );

        return startResult & endResult & dowResult;
    }

    public MeterResultDataArray getSubSetMeterDataArray(MeterResultDataArray resultDataArray,
                                                        Timestamp startTS, Timestamp endTS,
                                                        String dayOfWeek, long tmzOffset) {

        MeterResultDataArray subSet = new MeterResultDataArray();

        for(MeterResultData resultData: resultDataArray) {
            Boolean isInTimeslot = this.isInTimeSlot(resultData.getEntryTime(), startTS, endTS, dayOfWeek, tmzOffset);
            if(isInTimeslot) subSet.add(resultData);
        }

        return subSet;
    }

}
