package za.co.spsi.mdms.common.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import za.co.spsi.mdms.common.dao.ano.MeterRegister;
import za.co.spsi.mdms.common.dao.ano.Register;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseViewSyncService;
import za.co.spsi.mdms.processor.ano.SummaryField;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.reflect.RefFields;
import za.co.spsi.toolkit.reflect.Reflect;
import za.co.spsi.toolkit.util.ObjectUtils;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.IntStream;

import static za.co.spsi.mdms.kamstrup.processor.KamstrupProcessor.dayFormat;
import static za.co.spsi.toolkit.util.Util.*;

/**
 * Created by jaspervdbijl on 2017/01/06.
 * lf = kwh / (kva * hours )
 * <p>
 * Water meter no 63760613
 * Checkers 18452495
 */
@Data @EqualsAndHashCode
public class MeterResultData {

    public static class Helper {
        private static Calendar calendar = Calendar.getInstance();
        private static final String UUID = java.util.UUID.randomUUID().toString();

        private static int getDayOfWeek(Timestamp timestamp) {
            synchronized (Helper.UUID) {
                calendar.setTime(timestamp);
                return calendar.get(Calendar.DAY_OF_WEEK);
            }
        }

    }

    @JsonIgnore
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    public static RefFields refFields = new RefFields(MeterResultData.class);

    public static SimpleDateFormat dFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    private Timestamp entryTime, plotEntryTime;
    private String entryTimeTxt;

    private Integer monthOfYear;
    private Integer weekDay;
    private Integer entryDay;

    @SummaryField
    @Register
    private Double totalKwhP, totalKwhN, totalKVarP, totalKVarN, t1KwhP, t1KwhN, t1KVarP, t1KVarN, t2KwhP, t2KwhN, t2KVarP, t2KVarN;

    @SummaryField
    private Double totalKwh, totalKVar;

    @Register
    private Double totalMaxDemandPoKva, totalMaxDemandPoRtc, totalMaxDemandPrKva, totalMaxDemandPrRtc;

    @Register
    private Double t1MaxDemandPoKva, t1MaxDemandPrKva, t2MaxDemandPoKva, t2MaxDemandPrKva;

    // usages
    @SummaryField(tou = true)
    private Double totalKwhPUsage, totalKwhNUsage;
    @SummaryField
    private Double totalKVarPUsage, totalKVarNUsage;
    @SummaryField
    private Double t1KwhPUsage, t1KwhNUsage, t1KVarPUsage, t1KVarNUsage;
    @SummaryField
    private Double t2KwhPUsage, t2KwhNUsage, t2KVarPUsage, t2KVarNUsage;

    // TODO: DESK-1148 - IED-3483
    private String series, tou1, tou2;

    @SummaryField(tou = true)
    private Double totalKwhUsage;

    @SummaryField(posField = "totalKVarPUsage", negField = "totalKVarNUsage")
    private Double totalKVarUsage;

    @SummaryField(posField = "t1KwhPUsage", negField = "t1KwhNUsage")
    private Double t1KwhUsage;

    @SummaryField(posField = "t2KwhPUsage", negField = "t2KwhNUsage")
    private Double t2KwhUsage;

    @SummaryField(posField = "t1KVarPUsage", negField = "t1KVarNUsage")
    private Double t1KVarUsage;

    @SummaryField(posField = "t2KVarPUsage", negField = "t2KVarNUsage")
    private Double t2KVarUsage;

    // running cumulative
    private double t1KwhCuUsage = 0, t2KwhCuUsage = 0, totalKwhCuUsage = 0;

    @JsonIgnore
    private String serialN;

    @JsonIgnore
    private boolean generatorOn = false;

    private boolean calculated = false;

    // rms
    @SummaryField(exportTotal = false)
    private Double rmsL1V, rmsL2V, rmsL3V;
    @SummaryField(exportTotal = false)
    private Double rmsL1VL2L, rmsL2VL2L, rmsL3VL2L;
    @SummaryField(exportTotal = false)
    private Double rmsL1C, rmsL2C, rmsL3C;

    private Short alarm;

    // WATER VOLUME
    @SummaryField
    @Register
    private Double volume1;
    private Double flow;

    //Give flow as L/s instead of kL/s
    @Register
    private Double flowL;


    @SummaryField
    private Double volume1Usage;

    //Give volume 1 usage in L instead of kL
    @SummaryField
    private Double volume1UsageL;

    // calcs
    @JsonIgnore
    private Double totalKwForward, t1KwForward, t2KwForward;
    @JsonIgnore
    private Double totalKwReverse, t1KwReverse, t2KwReverse;
    @JsonIgnore
    private Double totalKvarImport, t1KvarImport, t2KvarImport;
    @JsonIgnore
    private Double totalKvarExport, t1KvarExport, t2KvarExport;

    @SummaryField(exportTotal = false)
    private Double t1KVA, t2KVA, totalKVA, t1PF, t2PF, totalPF;

    private String recordType;

    private static TimeZone DEFAULT_TMZ = TimeZone.getDefault();

    public MeterResultData() {

    }

    public MeterResultData(String series) {
        this.series = series;
    }

    public MeterResultData(Timestamp entryTime) {
        this.entryTime = entryTime;
        this.plotEntryTime = entryTime;
        this.entryTimeTxt = dFormat.format(entryTime);
        this.entryDay = Integer.parseInt(dayFormat.format(entryTime));
    }

    // Added new constructor with series: IED-5611
    public MeterResultData(Timestamp entryTime, String series) {
        this.entryTime = entryTime;
        this.plotEntryTime = entryTime;
        this.entryTimeTxt = dFormat.format(entryTime);
        this.entryDay = Integer.parseInt(dayFormat.format(entryTime));
        this.series = series;
    }

    public MeterResultData zero() {
        this.plotEntryTime = entryTime;
        this.entryTimeTxt = dFormat.format(entryTime);
        this.entryDay = Integer.parseInt(dayFormat.format(entryTime));
        refFields.stream().filter(f -> Double.class.equals(f.getType())).forEach(f -> {
            try {
                f.set(this, 0d);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        return this;
    }

    public MeterResultData average(MeterResultData next, int numInterpolateSamples, int sampleNumber) {
        MeterResultData entry = new MeterResultData();
        refFields.stream().forEach(f -> handle(() -> {
            if (f.get(next) != null && f.get(this) != null && !Modifier.isStatic(f.getModifiers())) {
                if (Double.class.equals(f.getType()) && f.getAnnotation(Register.class) != null) {
                    Double valueDiff = ((Double) f.get(next) - (Double) f.get(this)) / numInterpolateSamples;
                    f.set(entry, sampleNumber * valueDiff + (Double) f.get(this));
                }
                if (String.class.equals(f.getType()) || boolean.class.equals(f.getType())) {
                    f.set(entry, f.get(this));
                }
                if (Timestamp.class.equals(f.getType())) {
                    long timediff = ((Timestamp) f.get(next)).getTime() - ((Timestamp) f.get(this)).getTime();
                    timediff = timediff / (long) numInterpolateSamples;
                    timediff = timediff * (long) sampleNumber +  ((Timestamp) f.get(this)).getTime();
                    f.set(entry, new Timestamp(timediff));
                }
                entry.entryTimeTxt = dFormat.format(entry.getEntryTime());
                entry.entryDay = Integer.parseInt(dayFormat.format(entry.getEntryTime()));
            }
        }));
        entry.setCalculated(true);
        return entry;
    }


    public MeterResultData(double totalKwhPUsage, double t1KwhPUsage, double totalKwhP, double t1KwhP, int scale) {
        this.totalKwhPUsage = getAdj(totalKwhPUsage, scale);
        this.t1KwhPUsage = getAdj(t1KwhPUsage, scale);
        this.totalKwhP = getAdj(totalKwhP, scale);
        this.t1KwhP = getAdj(t1KwhP, scale);
    }

    public static Double getAdj(double value, int scale) {
        return value / Math.pow(10, scale);
    }

    public MeterResultData(IceTimeOfUseViewSyncService touService, int offset, String meterN,
                           MeterReadingEntity meterReadingEntity, int meterScaleIndex, String series,
                           String touType1, String touType2) {
        try {
            this.setSerialN(meterN);
            this.series = series;
            LocalDateTime time = new Timestamp(meterReadingEntity.entryTime.get().getTime() + offset).toLocalDateTime().
                    withSecond(0).withNano(0);
            time = time.withMinute(time.getMinute() >= 30 ? 30 : 0); //.minusMinutes( 30 );
            this.entryTime = Timestamp.valueOf(time);
            this.entryTimeTxt = dFormat.format(entryTime);
            this.entryDay = Integer.parseInt(dayFormat.format(entryTime));
            //tou = touService.getTOU(meterN, entryTime.toLocalDateTime().toLocalTime());
            //touc = "0".equals(timeOfUseC) ? null : touService.getTOUC(timeOfUseC, entryTime.toLocalDateTime().toLocalTime());
            // TODO: DESK-1148 - IED-3483
            tou1 = touType1;
            tou2 = touType2;

            weekDay = Helper.getDayOfWeek(this.entryTime);

            this.plotEntryTime = new Timestamp(this.entryTime.getTime());
            this.entryTimeTxt = dFormat.format(entryTime);
            for (Field field : meterReadingEntity.getFieldsWithAnnotation(MeterRegister.class)) {
                MeterRegister mr = (MeterRegister) field.getAnnotation(MeterRegister.class);
                java.lang.reflect.Field f = getClass().getDeclaredField(field.getName());
                f.setAccessible(true);
                try {
                    f.set(this, getNonNull((Double) field.get()) / Math.pow(10, mr.scale()[meterScaleIndex]));
                } catch (IndexOutOfBoundsException ie) {
                    throw new RuntimeException(ie);
                }
            }
            this.generatorOn = meterReadingEntity.genTxId.get() != null;
            this.calculated  = meterReadingEntity.generated.get() != null ? meterReadingEntity.generated.get() : false;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateNullValues(MeterResultData prev) {
        Double nullDouble = new Double(0d);
        refFields.filter(Register.class).stream().forEach(f -> {
            try {
                if (f.get(this) == null || f.get(this).equals(nullDouble)) {
                    f.set(this, f.get(prev));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }


    public static double getNonNull(Double value) {
        return value != null ? value : 0;
    }

    public static double getNonNull(String value) {
        return value != null ? Double.parseDouble(value) : 0;
    }

    public static boolean isZero(double number) {
        return Math.abs(number) == 0 || Math.abs(number) < 0.00000001;
    }

    // If meter is consolidated, it means that it has been calculated at meter level and summed
    public MeterResultData updateCalculated(MeterResultData lastMeterReading, Boolean consolidated) {
        if (lastMeterReading != null) {
            // update prev value if null
            refFields.stream().forEach(f -> {
                try {
                    f.set(this, f.get(this) == null ? f.get(lastMeterReading) : f.get(this));
                } catch (IllegalAccessException ie) {
                    throw new RuntimeException(ie);
                }
            });

            double minToHour = 60d / ((entryTime.getTime() - lastMeterReading.getEntryTime().getTime()) / 1000d / 60d);

            if (t1KwhP != null) {
                if (generatorOn) {
                    t1KwhPUsage = lastMeterReading.totalKwhP != null && getNonNull(t1KwhP) > lastMeterReading.totalKwhP ? (getNonNull(t1KwhP) - getNonNull(lastMeterReading.totalKwhP)) : 0;
                    t1KwhNUsage = lastMeterReading.totalKwhN != null && getNonNull(t1KwhN) > lastMeterReading.totalKwhN ? (getNonNull(t1KwhN) - getNonNull(lastMeterReading.totalKwhN)) : 0;
                } else {
                    t1KwhPUsage = lastMeterReading.t1KwhP != null ? (getNonNull(t1KwhP) - getNonNull(lastMeterReading.t1KwhP)) : 0;
                    t1KwhNUsage = lastMeterReading.t1KwhN != null ? (getNonNull(t1KwhN) - getNonNull(lastMeterReading.t1KwhN)) : 0;
                }

                t1KVarPUsage = (getNonNull(t1KVarP) - getNonNull(lastMeterReading.t1KVarP));
                t1KVarNUsage = (getNonNull(t1KVarN) - getNonNull(lastMeterReading.t1KVarN));

                t1KwForward = (getNonNull(t1KwhP) - getNonNull(lastMeterReading.t1KwhP)) * minToHour;
                t1KwReverse = (getNonNull(t1KwhN) - getNonNull(lastMeterReading.t1KwhN)) * minToHour;

                t1KvarImport = (getNonNull(t1KVarP) - getNonNull(lastMeterReading.t1KVarP)) * minToHour;
                t1KvarExport = (getNonNull(t1KVarN) - getNonNull(lastMeterReading.t1KVarN)) * minToHour;

                // TODO: IED-5131 - Ignore Reverse Active Energy When Calculating KVA
                t1KVA = Math.sqrt(Math.pow(getNonNull(t1KwForward), 2) + Math.pow(getNonNull(t1KvarImport), 2));
                t1PF = isZero(t1KVA) ? 0.0 : Math.abs(getNonNull(t1KwForward) / getNonNull(t1KVA));

            }
            if (t2KwhP != null) {
                if (generatorOn) {
                    t2KwhPUsage = lastMeterReading.totalKwhP != null && getNonNull(t2KwhP) > lastMeterReading.totalKwhP ? (getNonNull(t2KwhP) - getNonNull(lastMeterReading.totalKwhP)) : 0;
                    t2KwhNUsage = lastMeterReading.totalKwhN != null && getNonNull(t2KwhN) > lastMeterReading.totalKwhN ? (getNonNull(t2KwhN) - getNonNull(lastMeterReading.totalKwhN)) : 0;
                } else {
                    t2KwhPUsage = lastMeterReading.t2KwhP != null ? (getNonNull(t2KwhP) - getNonNull(lastMeterReading.t2KwhP)) : 0;
                    t2KwhNUsage = lastMeterReading.t2KwhN != null ? (getNonNull(t2KwhN) - getNonNull(lastMeterReading.t2KwhN)) : 0;
                }

                t2KVarPUsage = (getNonNull(t2KVarP) - getNonNull(lastMeterReading.t2KVarP));
                t2KVarNUsage = (getNonNull(t2KVarN) - getNonNull(lastMeterReading.t2KVarN));

                t2KwForward = (getNonNull(t2KwhP) - getNonNull(lastMeterReading.t2KwhP)) * minToHour;
                t2KwReverse = (getNonNull(t2KwhN) - getNonNull(lastMeterReading.t2KwhN)) * minToHour;

                t2KvarImport = (getNonNull(t2KVarP) - getNonNull(lastMeterReading.t2KVarP)) * minToHour;
                t2KvarExport = (getNonNull(t2KVarN) - getNonNull(lastMeterReading.t2KVarN)) * minToHour;

                // TODO: IED-5131 - Ignore Reverse Active Energy When Calculating KVA
                t2KVA = Math.sqrt(Math.pow(getNonNull(t2KwForward), 2) + Math.pow(getNonNull(t2KvarImport), 2));
                t2PF = isZero(t2KVA) ? 0.0 : Math.abs(getNonNull(t2KwForward) / getNonNull(t2KVA));

            }
            if (totalKwhP != null) {
                totalKwhPUsage = (getNonNull(totalKwhP) - getNonNull(lastMeterReading.totalKwhP));
                totalKwhNUsage = (getNonNull(totalKwhN) - getNonNull(lastMeterReading.totalKwhN));

                totalKVarPUsage = (getNonNull(totalKVarP) - getNonNull(lastMeterReading.totalKVarP));
                totalKVarNUsage = (getNonNull(totalKVarN) - getNonNull(lastMeterReading.totalKVarN));

                totalKwForward = (getNonNull(totalKwhP) - getNonNull(lastMeterReading.totalKwhP)) * minToHour;
                totalKwReverse = (getNonNull(totalKwhN) - getNonNull(lastMeterReading.totalKwhN)) * minToHour;

                totalKvarImport = (getNonNull(totalKVarP) - getNonNull(lastMeterReading.totalKVarP)) * minToHour;
                totalKvarExport = (getNonNull(totalKVarN) - getNonNull(lastMeterReading.totalKVarN)) * minToHour;

                // TODO: IED-5131 - Ignore Reverse Active Energy When Calculating KVA
                totalKVA = Math.sqrt(Math.pow(getNonNull(totalKwForward), 2) + Math.pow(getNonNull(totalKvarImport), 2));
                totalPF = isZero(totalKVA) ? 0.0 : Math.abs(getNonNull(totalKwForward) / getNonNull(totalKVA));

            }
            if (volume1 != null) {
                flow = (getNonNull(volume1) - getNonNull(lastMeterReading.volume1)) / ((entryTime.getTime() - lastMeterReading.entryTime.getTime()) / 1000);
                volume1Usage = (getNonNull(volume1) - getNonNull(lastMeterReading.volume1));
                if (flow != null)
                  flowL = flow * 1000;
                if (volume1Usage != null)
                volume1UsageL = volume1Usage * 1000;
            }
            //This else will only happen on first row of consolidation of meters for
            // VM as previous half hour not available for calculations
        } else if (lastMeterReading == null && consolidated) {

            if (totalKVA != null) {
                totalPF = isZero(totalKVA) ? 0.0 : (2.0 * totalKwhUsage) / totalKVA;
            }

            if (t1KVA != null) {
                t1PF = isZero(t1KVA) ? 0.0 : (2.0 * t1KwhUsage) / t1KVA;
            }

            if (t2KVA != null) {
                t2PF = isZero(t2KVA) ? 0.0 : (2.0 * t2KwhUsage) / t2KVA;
            }

        }

        rmsL1VL2L = getNonNull(rmsL1V) * Math.sqrt(3d);
        rmsL2VL2L = getNonNull(rmsL2V) * Math.sqrt(3d);
        rmsL3VL2L = getNonNull(rmsL3V) * Math.sqrt(3d);

        totalKwh = getNonNull(totalKwhP) + getNonNull(totalKwhN);
        totalKVar = getNonNull(totalKVarP) + getNonNull(totalKVarN);

        totalKwhUsage = getNonNull(totalKwhPUsage) - getNonNull(totalKwhNUsage);
        t1KwhUsage = getNonNull(t1KwhPUsage) - getNonNull(t1KwhNUsage);
        t2KwhUsage = getNonNull(t2KwhPUsage) - getNonNull(t2KwhNUsage);

        totalKVarUsage = getNonNull(totalKVarPUsage) - getNonNull(totalKVarNUsage);
        t1KVarUsage = getNonNull(t1KVarPUsage) - getNonNull(t1KVarNUsage);
        t2KVarUsage = getNonNull(t2KVarPUsage) - getNonNull(t2KVarNUsage);

        // update running totals
        t1KwhCuUsage = getNonNull(t1KwhUsage) + (lastMeterReading != null ? lastMeterReading.t1KwhCuUsage : 0);
        t2KwhCuUsage = getNonNull(t2KwhUsage) + (lastMeterReading != null ? lastMeterReading.t2KwhCuUsage : 0);
        totalKwhCuUsage = getNonNull(totalKwhUsage) + (lastMeterReading != null ? lastMeterReading.totalKwhCuUsage : 0);

        // filter corrupt data - usage can not be less than 0
        refFields.filter(f -> f.getName()
                .endsWith("Usage"))
                .filter(f -> call(v -> v.get(this) != null && (Double)v.get(this) < 0,f))
                .forEach(f -> handle(() -> f.set(this,0d)));

        return this;
    }

    public MeterResultData adjustNegative() {
        refFields.stream().forEach(f -> {
            try {
                if ((f.getName().endsWith("N") || f.getName().endsWith("NUsage")) && Double.class.equals(f.getType())) {
                    f.set(this, (f.get(this) != null ? ( (Double) f.get(this) > 0.0 ? (Double) f.get(this) * -1d : (Double) f.get(this) ) : null) );
                }
            } catch (IllegalAccessException ie) {
                throw new RuntimeException(ie);
            }
        });
        return this;
    }

    public MeterResultData adjustTime(int offsetMillies) {
        MeterResultData data = new MeterResultData();
        ObjectUtils.copy(data, this);
        Reflect.getFields(MeterResultData.class).filterType(Timestamp.class).removeNull(data)
                .forEach(f -> handle(() -> f.set(data, new Timestamp(((Timestamp) f.get(data)).getTime() + offsetMillies))));
        data.entryTimeTxt = dFormat.format(data.entryTime);
        data.entryDay = Integer.parseInt(dayFormat.format(data.entryTime));
        return data;
    }

    private static boolean checkTime(LocalTime from, LocalTime to, LocalTime time) {
        return from.compareTo(to) > 0 ?
                time.compareTo(from) >= 0 || time.compareTo(to) < 0 :
                time.compareTo(from) >= 0 && time.compareTo(to) < 0;
    }

    /**
     * All parameters are sent in local time (GMT+3)
     *
     * @param from
     * @param to
     * @param dayOfWeek
     * @param dateRx
     * @param tmz
     * @return
     */
    public boolean filterTOU(LocalTime from, LocalTime to, List<DayOfWeek> dayOfWeek, LocalDate dateRx, int tmz) {

        LocalDateTime entryDateTimeTMZ = getLocalDateTime(entryTime);
        LocalDate entryDateTMZ = entryDateTimeTMZ.toLocalDate();
        LocalTime entryTimeTMZ = entryDateTimeTMZ.toLocalTime();

        boolean timeOk = from == null || to == null || checkTime(from, to, entryTimeTMZ);

        boolean dateOk = dateRx != null ? dateRx.equals( entryDateTMZ ) : dayOfWeek == null || dayOfWeek.stream().anyMatch(d -> d.equals(entryDateTimeTMZ.getDayOfWeek()));

        return timeOk & dateOk;
    }

    public static void main(String args[]) throws Exception {
        System.out.println(new BigDecimal(1.0555555555558587E-4).toString());
        LocalTime from = LocalTime.of(22, 0, 0, 0);
        LocalTime to = LocalTime.of(10, 0, 0, 0);
        System.out.println(IntStream.range(0, 24).filter(i -> checkTime(from, to, LocalTime.of(i, 0, 0, 0))).count());
    }

}
