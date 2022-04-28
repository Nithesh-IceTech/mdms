package za.co.spsi.mdms.common.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;
import za.co.spsi.mdms.common.services.MeterDataService;
import za.co.spsi.toolkit.util.Assert;

import java.io.File;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.*;
import static za.co.spsi.mdms.common.dao.MeterResultData.getNonNull;
import static za.co.spsi.mdms.common.dao.MeterResultData.refFields;
import static za.co.spsi.toolkit.util.Util.call;
import static za.co.spsi.toolkit.util.Util.handle;

/**
 * Created by jaspervdbijl on 2017/03/03.
 */
public class MeterResultDataArray extends ArrayList<MeterResultData> {

    @Getter
    @Setter
    private boolean persisted;

    public MeterResultDataArray() {
    }

    public MeterResultDataArray(List<MeterResultData> values) {
        addAll(values);
    }

    @JsonIgnore
    public MeterResultDataArray filter(MeterDataService.Interval interval) {
        MeterResultDataArray working = new MeterResultDataArray(), result = new MeterResultDataArray();

        if (interval == MeterDataService.Interval.DAILY) {

            List<Integer> yearList = getYears(this);
            List<Integer> daysOfYearList = getDays(this);

            AtomicReference<MeterResultData> firstResult = new AtomicReference<>(new MeterResultData());
            AtomicReference<MeterResultData> lastResult = new AtomicReference<>(new MeterResultData());

            List<String> seriesList = this.stream().map(sn -> sn.getSeries()).distinct().collect(Collectors.toList());

            if (!(CollectionUtils.isEmpty(yearList) && CollectionUtils.isEmpty(daysOfYearList) && CollectionUtils.isEmpty(seriesList))) {

                for (String series : seriesList) {

                    yearList.forEach(year -> {

                        daysOfYearList.forEach(doy -> {

                            List<MeterResultData> subDataset;

                            subDataset = this.stream()
                                    .filter(sample ->
                                            sample.getEntryTime().toLocalDateTime().getDayOfYear() == doy
                                                    && sample.getEntryTime().toLocalDateTime().getYear() == year
                                                    && sample.getSeries().equals(series))
                                    .collect(Collectors.toList());

                            if (!CollectionUtils.isEmpty(subDataset)) {

                                Integer firstSampleIdx = 0;
                                Integer lastSampleIdx = subDataset.size() - 1;

                                firstResult.set(subDataset.get(firstSampleIdx));
                                lastResult.set(subDataset.get(lastSampleIdx));
                                lastResult.set(lastResult.get().updateCalculated(firstResult.get(), false));
                                result.add(lastResult.get());

                                subDataset.clear();

                            }

                        });

                    });

                }

            }

        } else if (interval == MeterDataService.Interval.MONTHLY) {

            List<Integer> yearList = getYears(this);
            List<Integer> monthOfYearList = getMonths(this);

            AtomicReference<MeterResultData> firstResult = new AtomicReference<>(new MeterResultData());
            AtomicReference<MeterResultData> lastResult = new AtomicReference<>(new MeterResultData());

            List<String> seriesList = this.stream().map(sn -> sn.getSeries()).distinct().collect(Collectors.toList());

            if (!(CollectionUtils.isEmpty(yearList) && CollectionUtils.isEmpty(monthOfYearList) && CollectionUtils.isEmpty(seriesList))) {

                for (String series : seriesList) {

                    yearList.forEach(year -> {

                        monthOfYearList.forEach(moy -> {

                            List<MeterResultData> subDataset;

                            subDataset = this.stream()
                                    .filter(sample ->
                                            sample.getEntryTime().toLocalDateTime().getMonthValue() == moy
                                                    && sample.getEntryTime().toLocalDateTime().getYear() == year
                                                    && sample.getSeries().equals(series))
                                    .collect(Collectors.toList());

                            if (!CollectionUtils.isEmpty(subDataset)) {

                                Integer firstSampleIdx = 0;
                                Integer lastSampleIdx = subDataset.size() - 1;

                                firstResult.set(subDataset.get(firstSampleIdx));
                                lastResult.set(subDataset.get(lastSampleIdx));
                                lastResult.set(lastResult.get().updateCalculated(firstResult.get(), false));
                                result.add(lastResult.get());

                                subDataset.clear();

                            }

                        });

                    });

                }

            }

        } else {

            AtomicReference<String> curSeries = new AtomicReference<>("seriesX");
            AtomicReference<String> prevSeries = new AtomicReference<>("seriesX");

            stream().forEach(v -> {

                curSeries.set(v.getSeries());

                if (!curSeries.get().equals(prevSeries.get())) {
                    working.clear();
                }

                if (interval.match(v.getEntryTime().toLocalDateTime()) && !working.isEmpty()) {
//                    v.updateCalculated(working.get(0), false);
                    working.clear();
                    result.add(v);
                }

                if (working.isEmpty()) {
                    working.add(v);
                }

                prevSeries.set(v.getSeries());

            });
        }

        return result;
    }

    public MeterResultDataArray subset(Date from, Date to, int tmzOffset) {
        Date adjFrom = new Date(from.getTime() + tmzOffset);
        Date adjTo = new Date(to.getTime() + tmzOffset);
        return stream()
                .filter(d -> d.getEntryTime().compareTo(adjFrom) >= 0 && d.getEntryTime().compareTo(adjTo) <= 0)
                .collect(Collectors.toCollection(MeterResultDataArray::new));
    }

    public MeterResultDataArray adjustTime(int time) {
        return stream().map(m -> m.adjustTime(time)).collect(Collectors.toCollection(MeterResultDataArray::new));
    }

    public MeterResultDataArray adjustNegative() {
        return stream().map(f -> f.adjustNegative()).collect(Collectors.toCollection(MeterResultDataArray::new));
    }

    public Optional<MeterResultData> get(Timestamp time) {
        return stream().filter(d -> d.getEntryTime().equals(time)).findFirst();
    }

    public Optional<MeterResultData> get(LocalDateTime time) {
        return stream().filter(d -> d.getEntryTime().toLocalDateTime().isEqual(time)).findFirst();
    }

    public boolean contain(Date date) {
        return stream().filter(f -> f.getEntryTime().getTime() == date.getTime()).findAny().isPresent();
    }

    private int getOpType(String opType) {
        switch (opType) {
            case "ADD":
                return 1;
            case "SUBTRACT":
                return -1;
            default:
                throw new RuntimeException("Unsupported operation " + opType);
        }
    }

    public List<Integer> getYears(MeterResultDataArray resultDataArray) {

        return resultDataArray.stream()
                .map(MeterResultData::getEntryTime)
                .map(Timestamp::toLocalDateTime)
                .map(LocalDateTime::getYear)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Integer> getMonths(MeterResultDataArray resultDataArray) {

        return resultDataArray.stream()
                .map(MeterResultData::getEntryTime)
                .map(Timestamp::toLocalDateTime)
                .map(LocalDateTime::getMonthValue)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Integer> getDays(MeterResultDataArray resultDataArray) {

        return resultDataArray.stream()
                .map(MeterResultData::getEntryTime)
                .map(Timestamp::toLocalDateTime)
                .map(LocalDateTime::getDayOfYear)
                .distinct()
                .collect(Collectors.toList());
    }

    //Used to consolidate all meters connected to a VM
    public void consolidate(MeterResultDataArray dataArray, String operationType) {
        AtomicInteger i = new AtomicInteger(0);
        long logMinTime = dataArray.get(0).getEntryTime().getTime();
        long logMaxTime = dataArray.get(dataArray.size() - 1).getEntryTime().getTime();
        stream().forEach(data ->
        {
            long destListTime = data.getEntryTime().getTime();
            if ((destListTime >= logMinTime & destListTime <= logMaxTime) &&
                    data.getEntryTime().getTime() == dataArray.get(i.get()).getEntryTime().getTime()) {
                int i2 = i.getAndIncrement();
                refFields.filterType(Double.class).stream().forEach(f
                        -> handle(()
                        -> f.set(data, ((Double) f.get(data)) + ((Double) f.get(dataArray.get(i2)) == null
                        ? new Double(0.0)
                        : (Double) f.get(dataArray.get(i2))) * getOpType(operationType))));
                data.updateCalculated(i2 > 0 ? this.get(i2 - 1) : null, true);
            }
        });
    }

    /**
     * Create a meter result array without null readings
     *
     * @return
     */
    public MeterResultDataArray excludeNullReadings(boolean total, boolean t1, boolean t2, boolean voltage, boolean current, boolean water) {

        MeterResultDataArray copy = new MeterResultDataArray();

        if (!CollectionUtils.isEmpty(this)) {

            MeterResultData curMRD = get(0);

            for (int i = 0; i < size(); i++) {
                curMRD = get(i);
                if (total) {
                    if (curMRD.getTotalKwhP() > 0.0) {
                        copy.add(curMRD);
                    }
                }
                if (t1) {
                    if (curMRD.getT1KwhP() > 0.0) {
                        copy.add(curMRD);
                    }
                }
                if (t2) {
                    if (curMRD.getT2KwhP() > 0.0) {
                        copy.add(curMRD);
                    }
                }
                if (voltage) {
                    if (curMRD.getRmsL1V() > 0.0) {
                        copy.add(curMRD);
                    }
                }
                if (current) {
                    if (curMRD.getRmsL1C() > 0.0) {
                        copy.add(curMRD);
                    }
                }
                if (water) {
                    if (curMRD.getVolume1() > 0.0) {
                        copy.add(curMRD);
                    }
                }
            }
        } else {
            return this;
        }

        return copy;
    }

    public static boolean set(Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }

    public static <V> V get(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return (V) field.get(object);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }

    /**
     * Remove outlier values from the meter data array
     *
     * @return
     */
    public MeterResultDataArray statisticalFilter(MeterResultDataArray unfilteredDataset, boolean waterMeter) {

        MeterResultDataArray filteredDataset = null;

        if (!CollectionUtils.isEmpty(unfilteredDataset)) {

            List<Double> eDataset = unfilteredDataset.stream().map(MeterResultData::getTotalKVA).collect(Collectors.toList());
            List<Double> wDataset = unfilteredDataset.stream().map(MeterResultData::getVolume1Usage).collect(Collectors.toList());

            List<Double> dataset = waterMeter ? wDataset : eDataset;

            Double statsOutlier = outlierValue(dataset);

            filteredDataset = unfilteredDataset.stream().collect(Collectors.toCollection(MeterResultDataArray::new));

            if (statsOutlier > 0.0) {

                filteredDataset = waterMeter ?
                        unfilteredDataset.stream()
                                .filter(val -> val.getVolume1Usage() != null ? val.getVolume1Usage().compareTo(statsOutlier) < 0 : false)
                                .collect(Collectors.toCollection(MeterResultDataArray::new)) :
                        unfilteredDataset.stream()
                                .filter(val -> val.getTotalKVA() != null ? val.getTotalKVA().compareTo(statsOutlier) < 0 : false)
                                .collect(Collectors.toCollection(MeterResultDataArray::new));
            }

        } else {

            filteredDataset = new MeterResultDataArray();

        }

        return filteredDataset;
    }

    public Double outlierValue(List<Double> valueList) {

        Double outlierVal = 0.0;

        List<Double> nonNullValueList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(valueList)) {

            for (Double v : valueList) {
                nonNullValueList.add(getNonNull(v));
            }

            Double totalSum = nonNullValueList.stream().reduce(0.0, Double::sum);

            if (totalSum > 0.0) {

                Double max = nonNullValueList.stream().reduce(0.0, Double::max);
                Double mean = this.mean(nonNullValueList);
                Double std = this.standardDeviation(nonNullValueList, mean);
                Double preOutlierVal = mean + (3.0 * std);
                double ratio = max / mean;

                if (ratio >= 10.0) {
                    outlierVal = mean + (3.0 * std);
                }

            }

        }

        return outlierVal;
    }

    public Double mean(List<Double> numArray) {
        Double sum = 0.0;
        int length = numArray.size();

        for (Double num : numArray) {
            sum += num;
        }

        Double mean = sum / length;

        return mean;
    }

    public Double standardDeviation(List<Double> numArray, Double mean) {
        Double standardDeviation = 0.0;
        int length = numArray.size();

        for (Double num : numArray) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    /**
     * fill the gaps (greater than 30 mins, with averages
     *
     * @return
     */
    public MeterResultDataArray normalize(int intervalMins) {

        MeterResultDataArray copy = new MeterResultDataArray();

        for (int i = 0; i < size() - 1; i++) {

            copy.add(get(i));

            long timeDiff = (get(i + 1).getEntryTime().getTime() - get(i).getEntryTime().getTime());
            long timeDiffMins = timeDiff / TimeUnit.MINUTES.toMillis(1);
            long samples = timeDiffMins / intervalMins;

            if (timeDiffMins > intervalMins) {
                for (int g = 1; g < samples; g++) {
                    copy.add(get(i).average(get(i + 1), (int) samples, g));
                }
            }

        }

        // add very last one
        if (size() > 1) {
            copy.add(get(size() - 1));
            copy.get(copy.size() - 1).updateCalculated(copy.get(size() - 2), false);
        }

        return copy;
    }

    public MeterResultDataArray updateCalcs() {
        for (int i = 1; i < size(); i++) {
            get(i).updateCalculated(get(i - 1), false);
        }
        return this;
    }

    public MeterResultDataArray distinct() {
        return new MeterResultDataArray(stream().distinct().collect(Collectors.toList()));
    }

    public MeterResultDataArray getCalculated() {
        return stream().filter(d -> d.isCalculated()).collect(Collectors.toCollection(MeterResultDataArray::new));
    }


    public MeterResultDataArray filterTOU(LocalTime from, LocalTime to, List<DayOfWeek> dayOfWeek, LocalDate dateRx, int tmz) {
        return stream().filter(f -> f.filterTOU(from, to, dayOfWeek, dateRx, tmz)).collect(Collectors.toCollection(MeterResultDataArray::new));
    }

    @SneakyThrows
    public double calculateConsumption(String fieldName) {
        Field field = MeterResultData.refFields.get(fieldName);
        // sort meter reading data by the value asc to avoid inconsistent data readings
        Assert.notNull(field, String.format("No such field %s", fieldName));
        return stream().mapToDouble(d -> call(v -> getNonNull((Double) field.get(v)), d)).sum();
    }

    public OptionalDouble getMaxValue(String fName) {
        Field field = refFields.get(fName);
        Assert.notNull(field, String.format("No such field %s", fName));
        return stream().filter(d -> call(v -> field.get(v), d) != null)
                .mapToDouble(d -> call(v -> (Double) field.get(v), d))
                .max();
    }

    public MeterResultData getFirstValue() {
        List<MeterResultData> meterResultDataList = stream().collect(Collectors.toList());
        int listLen = meterResultDataList.size();
        return listLen > 0 ? meterResultDataList.get(0) : new MeterResultData();
    }

    public MeterResultData getLastValue() {
        List<MeterResultData> meterResultDataList = stream().collect(Collectors.toList());
        int listLen = meterResultDataList.size();
        int lastValIdx = listLen > 0 ? listLen - 1 : 0;
        return listLen > 0 ? meterResultDataList.get(lastValIdx) : new MeterResultData();
    }

    public MeterResultData getValueByTimestamp(Timestamp entryTime) throws NoSuchElementException {
        List<MeterResultData> meterResultDataList = stream().collect(Collectors.toList());
        int listLen = meterResultDataList.size();
        if(listLen > 0) {
            Optional<MeterResultData> meterResultData = meterResultDataList.stream()
                    .filter(md -> md.getEntryTime().equals(entryTime))
                    .findFirst();
            return meterResultData.orElseGet(MeterResultData::new);
        }
        return new MeterResultData();
    }

    public double getMaxTotalKVA() {
        return !isEmpty() ? stream().mapToDouble(d -> d.getTotalKVA() != null ? d.getTotalKVA() : 0).max().getAsDouble() : 0;
    }

    public MeterResultData getMaxTotalKVA_MeterResultData() {

        MeterResultData maxkVAResult = null;
        Double maxkVADouble = 0.0;

        List<MeterResultData> notNullList = stream().filter(mr -> mr.getTotalKVA() != null).collect(Collectors.toList());

        for (MeterResultData mr : notNullList) {
            if (mr.getTotalKVA() > maxkVADouble) {
                maxkVADouble = mr.getTotalKVA();
                maxkVAResult = mr;
            }
        }

        return maxkVAResult;
    }

    public Map<Integer, Double> getMeterRegisterDailyUsages(String registerNamePrefix) {

        List<MeterResultData> notNullList = new ArrayList<>();
        Map<Integer, Double> registerDailyUsage = new HashMap<>();

        if (registerNamePrefix.matches("total")) {
            notNullList = stream().filter(mr -> mr.getTotalKwhPUsage() != null).collect(Collectors.toList());
            registerDailyUsage = notNullList.stream().collect(
                    Collectors.groupingBy(MeterResultData::getEntryDay, Collectors.summingDouble(MeterResultData::getTotalKwhPUsage)));
        } else if (registerNamePrefix.matches("t1")) {
            notNullList = stream().filter(mr -> mr.getT1KwhPUsage() != null).collect(Collectors.toList());
            registerDailyUsage = notNullList.stream().collect(
                    Collectors.groupingBy(MeterResultData::getEntryDay, Collectors.summingDouble(MeterResultData::getT1KwhPUsage)));
        } else if (registerNamePrefix.matches("t2")) {
            notNullList = stream().filter(mr -> mr.getT2KwhPUsage() != null).collect(Collectors.toList());
            registerDailyUsage = notNullList.stream().collect(
                    Collectors.groupingBy(MeterResultData::getEntryDay, Collectors.summingDouble(MeterResultData::getT2KwhPUsage)));
        }

        return registerDailyUsage;
    }

    public Double getMeterRegisterSumOfDailyUsages(String registerNamePrefix) {

        Map<Integer, Double> registerDailyUsages = getMeterRegisterDailyUsages(registerNamePrefix);
        final Double[] totalUsageSum = {0.0};

        registerDailyUsages.forEach( (k,v) -> {
            totalUsageSum[0] = totalUsageSum[0] + v.doubleValue();
        } );

        return totalUsageSum[0];
    }

    public Double getMeterRegisterAverageDailyUsage(String registerNamePrefix) {

        Double averageDailyUsage = 0.0;
        Map<Integer, Double> registerDailyUsages = getMeterRegisterDailyUsages(registerNamePrefix);
        Double registerSumOfDailyUsages = getMeterRegisterSumOfDailyUsages(registerNamePrefix);
        Integer numberOfDays = registerDailyUsages.size();

        averageDailyUsage = registerSumOfDailyUsages / numberOfDays;

        return averageDailyUsage;
    }

    public static void execFile(String args[]) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MeterResultDataResponse value = mapper.readValue(new File("/Users/jaspervdbijl/Downloads/mdms.json"), MeterResultDataResponse.class);
        MeterResultDataArray arr = new MeterResultDataArray();
        arr.addAll(value.getMeterResultDataList());
        System.out.println("Total " + arr.calculateConsumption("totalKwhPUsage"));
        List<LocalTime> times = Arrays.asList(
                LocalTime.of(0, 0, 0),
                LocalTime.of(6, 0, 0),
                LocalTime.of(9, 0, 0),
                LocalTime.of(17, 0, 0),
                LocalTime.of(19, 0, 0),
                LocalTime.of(22, 0, 0),
                LocalTime.of(11, 59, 59));
        List<DayOfWeek> weekdays = Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
        double total = 0;
        for (int i = 0; i < times.size() - 1; i++) {
            MeterResultDataArray filter = arr.filterTOU(times.get(i), times.get(i + 1), weekdays, null, 120);
            System.out.println("Filter " + filter.calculateConsumption("totalKwhPUsage"));
            total += filter.calculateConsumption("totalKwhPUsage");
        }
        // remove some random data
        for (int i =0; i< arr.size();i++) {
            if (i % 15 == 0) {
                arr.remove(i);
            }
        }
        MeterResultDataArray filter = arr.normalize(30);
        System.out.print("Total " + total);
    }

    public static void testAverage() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MeterResultDataResponse value = mapper.readValue(new File("/Users/jaspervdbijl/Downloads/meterData.json"), MeterResultDataResponse.class);
        // now remove some data
        final MeterResultDataArray mrda = new MeterResultDataArray(value.getMeterResultDataList()).distinct();
        MeterResultDataArray normal = mrda.normalize(30);
        System.out.println(mrda);

    }

    public MeterResultDataArray getCurrentMonthSubDataset(Month month, MeterResultDataArray dataset) {

        return dataset.stream().filter( ds ->
                         Instant.ofEpochSecond( ds.getEntryTime().getTime() )
                        .get(ChronoField.MONTH_OF_YEAR) == month.getValue() )
                .collect(Collectors.toCollection(MeterResultDataArray::new));
    }

    public static void main(String args[]) throws Exception {
        testAverage();
    }

}
