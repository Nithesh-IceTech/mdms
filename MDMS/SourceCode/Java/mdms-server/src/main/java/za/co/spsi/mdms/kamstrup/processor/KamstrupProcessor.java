package za.co.spsi.mdms.kamstrup.processor;

import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.kamstrup.db.*;
import za.co.spsi.mdms.kamstrup.services.order.domain.MeterResult;
import za.co.spsi.mdms.kamstrup.services.order.domain.Register;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.LoggerCommandResult;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.ExpiringCacheMap;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.lang.Math;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static za.co.spsi.mdms.common.services.MeterDataService.Interval.HALF_HOURLY;
import static za.co.spsi.mdms.common.services.MeterDataService.Interval.HOURLY;

/**
 * Created by jaspervdb on 2016/11/24.
 */
@Dependent
public class KamstrupProcessor {

    public static Logger TAG = Logger.getLogger(KamstrupProcessor.class.getName());

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    private PrepaidMeterFilterService prepaidFilter;

    private ExpiringCacheMap<String, Optional<Timestamp>> groupCreateTime = new ExpiringCacheMap<>(TimeUnit.MINUTES.toMillis(5));

    public static SimpleDateFormat dayFormat = new SimpleDateFormat(MeterReadingEntity.ENTRY_DAY_FORMAT);

    private static void logReadings(Connection connection, KamstrupMeterOrderEntity order, KamstrupMeterEntity meter, MeterResult result, LoggerCommandResult.Entry entry) {
        asList(entry.registers.registers).stream().forEach(r -> {
            DataSourceDB.set(connection, new KamstrupMeterReadingEntity().init(order, meter, result, entry, r));
        });
    }

    private Optional<Timestamp> getOrderCreatedTime(Connection connection, KamstrupMeterOrderEntity order) {
        if (groupCreateTime.containsKey(order.meterOrderId.get())) {
            return groupCreateTime.get(order.meterOrderId.get());
        } else {
            if (order.groupId.get() != null) {
                KamstrupGroupEntity.LogEntity log = KamstrupGroupEntity.LogEntity.findBy(connection, order.groupId.get(), order.meterOrderId.get());
                groupCreateTime.put(order.meterOrderId.get(), Optional.ofNullable(log != null?timestampToUtc(log.entryTime.get()):null));
            } else {
                groupCreateTime.put(order.meterOrderId.get(), Optional.empty());
            }
            return getOrderCreatedTime(connection, order);
        }
    }

    public static Timestamp timestampToUtc(Timestamp tsLocal) {
        LocalDateTime tsUtc = LocalDateTime.ofEpochSecond(tsLocal.getTime()/1000, 0,
                ZoneOffset.ofHours(0) )
                .truncatedTo(ChronoUnit.MINUTES);
        return Timestamp.valueOf(tsUtc);
    }

    public static Timestamp truncateToMins(Timestamp timestamp) {
        return Timestamp.valueOf(timestamp.toLocalDateTime().truncatedTo(ChronoUnit.MINUTES));
    }

    public Timestamp roundToHour(Connection connection, KamstrupMeterOrderEntity order, MeterReadingEntity meterReadingEntity, Timestamp entryTime) {
        Optional<Timestamp> created = getOrderCreatedTime(connection, order);
        if (created.isPresent()) {
            return truncateToMins(created.get());
        } else {
            // round to hour
            return meterReadingEntity.adjustTimestamp(60, entryTime);
        }
    }

    private boolean shouldScheduleGapProcessor(Timestamp lastCommsD,Timestamp entryTime,boolean isWater) {
        return (lastCommsD != null
                && Duration.between(lastCommsD.toLocalDateTime(),entryTime.toLocalDateTime()).toMinutes() >=
                (isWater? propertiesConfig.getKamstrup_gap_processing_water_minutes() : propertiesConfig.getKamstrup_gap_processing_minutes()));
    }

    public static Timestamp roundToHalfHour(MeterReadingEntity meterReadingEntity, Timestamp entryTime) {
        return meterReadingEntity.adjustTimestamp(30, entryTime);
    }

    public Register[] scaleWaterVolume( Register registers[] ) {

        // IED-2861 Kamstrup Water Meter Volume Scale Rule
        // Convert to litre:
        // Warm water register 9.1.1.0.0.255
        // Cold water register 8.1.1.0.0.255

        asList(registers).stream().forEach( r -> {
            if( r.id.equals("8.1.1.0.0.255") || r.id.equals("9.1.1.0.0.255")  ) {
                r.value = ( r.value * Math.pow(10, r.scale ) );
            }
        });

        return registers;

    }

    public void individualKamstrupRegisterGapIdentifier(Connection connection, Timestamp entryTime, KamstrupMeterEntity meter, Register registers[] ) {

        // IED-3301 & IED-3300
        // A meter could belong to total energy, t1 energy, t2 energy, voltage and current kamstrup groups at the same time.
        // The primary problem is that each group order's data arrives at different times, and sometimes one specific group could fail
        // where the other group orders were successfully collected.
        // The shouldScheduleGapProcessor only detects gaps if all group orders have failed and no records were inserted into the DB.
        // To improve the shouldScheduleGapProcessor, each group order update times must be logged and tested individually to
        // schedule kamstrup group specific gap filling jobs, and this is the purpose of this method.

        int thirtyMinutesInMilli = (int) TimeUnit.MINUTES.toMillis( 30 );

        asList(registers).stream().forEach( r -> {

            if( r.id.equals("1.1.1.8.0.255") ) { // -> TOTAL_KWHP (Total Energy Update)

                try {

                    if ( ( entryTime.getTime() - meter.lastTotalEnergyUpdateTimestamp.get().getTime() ) > thirtyMinutesInMilli ) {

                        KamstrupGapProcessorJobEntity.create(connection, meter
                                , KamstrupGapProcessorJobEntity.JobTypes.TOTAL_ENERGY.name()
                                , meter.groupId.get()
                                , Timestamp.valueOf(meter.lastTotalEnergyUpdateTimestamp.get().toLocalDateTime().minusMinutes(120))
                                , Timestamp.valueOf(entryTime.toLocalDateTime().plusMinutes(120))
                                , Timestamp.valueOf(LocalDateTime.now().plusMinutes(propertiesConfig.getKamstrup_gap_processing_delay()))
                                , HALF_HOURLY.minutes);
                    }

                    meter.lastTotalEnergyUpdateTimestamp.set( entryTime );

                } catch (NullPointerException npe) {

                    meter.lastTotalEnergyUpdateTimestamp.set( entryTime );

                }

            } else if( r.id.equals("1.1.1.8.1.255") ) { // -> T1_KWHP (T1 Energy Update)

                try {

                    if ( ( entryTime.getTime() - meter.lastT1EnergyUpdateTimestamp.get().getTime() ) > thirtyMinutesInMilli ) {

                        KamstrupGapProcessorJobEntity.create(connection, meter
                                , KamstrupGapProcessorJobEntity.JobTypes.T1_ENERGY.name()
                                , meter.groupId.get()
                                , Timestamp.valueOf(meter.lastT1EnergyUpdateTimestamp.get().toLocalDateTime().minusMinutes(120))
                                , Timestamp.valueOf(entryTime.toLocalDateTime().plusMinutes(120))
                                , Timestamp.valueOf(LocalDateTime.now().plusMinutes(propertiesConfig.getKamstrup_gap_processing_delay()))
                                , HALF_HOURLY.minutes);
                    }

                    meter.lastT1EnergyUpdateTimestamp.set( entryTime );

                } catch (NullPointerException npe) {

                    meter.lastT1EnergyUpdateTimestamp.set( entryTime );

                }

            } else if( r.id.equals("1.1.1.8.2.255") ) { // -> T2_KWHP (T2 Energy Update)

                try {

                    if ( ( entryTime.getTime() - meter.lastT2EnergyUpdateTimestamp.get().getTime() ) > thirtyMinutesInMilli ) {

                        KamstrupGapProcessorJobEntity.create(connection, meter
                                , KamstrupGapProcessorJobEntity.JobTypes.T2_ENERGY.name()
                                , meter.groupId.get()
                                , Timestamp.valueOf(meter.lastT2EnergyUpdateTimestamp.get().toLocalDateTime().minusMinutes(120))
                                , Timestamp.valueOf(entryTime.toLocalDateTime().plusMinutes(120))
                                , Timestamp.valueOf(LocalDateTime.now().plusMinutes(propertiesConfig.getKamstrup_gap_processing_delay()))
                                , HALF_HOURLY.minutes);
                    }

                    meter.lastT2EnergyUpdateTimestamp.set( entryTime );

                } catch (NullPointerException npe) {

                    meter.lastT2EnergyUpdateTimestamp.set( entryTime );

                }

            }  else if( r.id.equals("1.1.31.25.0.255") ) { // -> RMS_L1_I (Current Update)

                try {

                    if ( ( entryTime.getTime() - meter.lastCurrentUpdateTimestamp.get().getTime() ) > thirtyMinutesInMilli ) {

                        KamstrupGapProcessorJobEntity.create(connection, meter
                                , KamstrupGapProcessorJobEntity.JobTypes.CURRENT.name()
                                , meter.groupId.get()
                                , Timestamp.valueOf(meter.lastCurrentUpdateTimestamp.get().toLocalDateTime().minusMinutes(120))
                                , Timestamp.valueOf(entryTime.toLocalDateTime().plusMinutes(120))
                                , Timestamp.valueOf(LocalDateTime.now().plusMinutes(propertiesConfig.getKamstrup_gap_processing_delay()))
                                , HALF_HOURLY.minutes);
                    }

                    meter.lastCurrentUpdateTimestamp.set( entryTime );

                } catch (NullPointerException npe) {

                    meter.lastCurrentUpdateTimestamp.set( entryTime );

                }

            } else if( r.id.equals("1.1.32.25.0.255") ) { // -> RMS_L1_V (Voltage Update)

                try {

                    if ( ( entryTime.getTime() - meter.lastVoltageUpdateTimestamp.get().getTime() ) > thirtyMinutesInMilli ) {

                        KamstrupGapProcessorJobEntity.create(connection, meter
                                , KamstrupGapProcessorJobEntity.JobTypes.VOLTAGE.name()
                                , meter.groupId.get()
                                , Timestamp.valueOf(meter.lastVoltageUpdateTimestamp.get().toLocalDateTime().minusMinutes(120))
                                , Timestamp.valueOf(entryTime.toLocalDateTime().plusMinutes(120))
                                , Timestamp.valueOf(LocalDateTime.now().plusMinutes(propertiesConfig.getKamstrup_gap_processing_delay()))
                                , HALF_HOURLY.minutes);
                    }

                    meter.lastVoltageUpdateTimestamp.set( entryTime );

                } catch (NullPointerException npe) {

                    meter.lastVoltageUpdateTimestamp.set( entryTime );

                }

            }

        });

    }

    private void process(Connection connection, KamstrupMeterOrderEntity order, KamstrupMeterEntity meter, MeterResult result, Timestamp entryTime, Register registers[]) {

        MeterReadingEntity meterReading = new MeterReadingEntity();

        Timestamp correctedTime = truncateToMins(entryTime);

        // Water meters hard coded to to instant readings once every hour
        boolean isWater = meter.isWater();
        if (isWater) {
            correctedTime = roundToHour(connection,order,meterReading,correctedTime);
        } else {
            correctedTime = roundToHalfHour(meterReading, correctedTime);
        }

        meterReading.kamMeterId.set(meter.meterId.get());
        meterReading.entryTime.set(correctedTime);
        meterReading.entryDay.set(Integer.parseInt(dayFormat.format(correctedTime)));
        meterReading = DataSourceDB.getFromSet(connection, meterReading); // check if its a duplicate

        boolean meterReadingExists = meterReading != null;

        // process Meter Reading
        meterReading = meterReadingExists ? meterReading : new MeterReadingEntity();
        meterReading.kamMeterId.set(meter.meterId.get());
        meterReading.entryTime.set(correctedTime);
        meterReading.entryDay.set(Integer.parseInt(dayFormat.format(correctedTime)));

        if(!meterReadingExists) {
            meterReading.kamMeterOrderId.set(order.meterOrderId.get());
        }

        if( isWater ) {
            registers = scaleWaterVolume( registers );
            if (shouldScheduleGapProcessor(meter.lastCommsD.get(),correctedTime,true)) {
                // If its water, then fill gap immediately as we won't be trying to re process failures
                KamstrupGapProcessorJobEntity.create(connection,meter
                        ,KamstrupGapProcessorJobEntity.JobTypes.WATER.name()
                        ,meter.groupId.get()
                        ,Timestamp.valueOf(meter.lastCommsD.get().toLocalDateTime().minusMinutes(120))
                        ,Timestamp.valueOf(correctedTime.toLocalDateTime().plusMinutes(120))
                        ,Timestamp.valueOf(LocalDateTime.now().plusMinutes(meter.isWater() ?
                                propertiesConfig.getKamstrup_gap_processing_water_delay() :
                                propertiesConfig.getKamstrup_gap_processing_delay()))
                        ,isWater?HOURLY.minutes:HALF_HOURLY.minutes);
            }
        } else {
            individualKamstrupRegisterGapIdentifier( connection, correctedTime, meter, registers );
        }

        meterReading.update(Arrays.stream(registers).filter(r -> !r.id.matches("1.1.9.6.[0,1,2].255")).toArray(Register[]::new));

        meterReading.prepaidMeter.set(meterReading.prepaidMeter.get() == null ? prepaidFilter.isPrepaid(meter.serialN.get()) : meterReading.prepaidMeter.get());

        GeneratorTransactionEntity.updateForTx(connection, meterReading);

        meter.lastCommsD.set(entryTime);
        meter.failedNumber.set(0);
        DataSourceDB.set(connection, meter);
        DataSourceDB.set(connection, meterReading);

    }

    public void process(Connection connection, KamstrupMeterOrderEntity order, KamstrupMeterEntity meter, MeterResult result) {

        if (result.commandResults.loggerCommandResult != null) {

            if (result.commandResults.loggerCommandResult.logger.entries.entries != null) {
                KamstrupMeterReadFailedLog.cancel(connection, meter.meterId.get(),
                        result.commandResults.loggerCommandResult.logger.fromDate, result.commandResults.loggerCommandResult.logger.toDate);
                // load the last meter reading
                for (LoggerCommandResult.Entry entry : result.commandResults.loggerCommandResult.logger.entries.entries) {
                    process(connection, order, meter, result, entry.timestamp, entry.registers.registers);
                }
            }

        } else if (result.commandResults.faultedCommandResult != null
                && order.groupId.get() != null && !meter.isWater()) {

            // re issue on next run
            meter.failedNumber.set(meter.failedNumber.getNonNull() + 1);
            if (meter.failedNumber.getNonNull() < propertiesConfig.getKamstrup_processing_failed_orders_retry()) {
                TAG.log(Level.INFO, String.format("Reschedule read failure: %s", result.commandResults.faultedCommandResult.command.ref));
                DataSourceDB.set(connection, new KamstrupMeterReadFailedLog(meter, order, result.commandResults.faultedCommandResult));
            } else {
                TAG.log(Level.INFO, String.format("Reschedule read failure: Exceeded failure count %d", meter.failedNumber.getNonNull()));
                //Reset to lower failure number to avoid DB precision failure
                meter.failedNumber.set(100);
            }

        } else if (result.commandResults.registerCommandResult != null) {

            Register [] registers = new Register[]{result.commandResults.registerCommandResult.register};
            process(connection, order, meter, result, result.commandResults.registerCommandResult.readOutTime, registers);

        } else if (result.commandResults.breakerCommandResult != null) {

            meter.statusOn.set(result.commandResults.breakerCommandResult.outputState);

        } else {

            TAG.log(Level.INFO, String.format("Meter %s failed to connect. Order %s", meter.meterId.get(), order.meterOrderId.get()));

        }

        if (meter.requestStatusOff.get() == null && meter.requestStatusOn.get() == null) {
            meter.statusOn.set(true);
            meter.statusOff.set(false);
        }
        DataSourceDB.set(connection, meter);

    }

    public static void main(String[] args) {
        Timestamp lastCommsD = Timestamp.valueOf(LocalDateTime.now().minusMinutes(30));
        Timestamp entryTime = Timestamp.valueOf(LocalDateTime.now().minusMinutes(0));
        System.out.println(Duration.between(lastCommsD.toLocalDateTime(),entryTime.toLocalDateTime()).toMinutes());
    }
}
