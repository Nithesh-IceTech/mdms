package za.co.spsi.mdms.common.services;

import za.co.spsi.mdms.common.dao.MeterResultData;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.utility.MDMSUtilityHelper;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.common.dao.MeterResultData.refFields;

@Dependent
public class VMMeterDataService {

    @Inject
    private MDMSUtilityHelper mdmsUtilityHelper;

    private Map<String, Optional<Map<String, String>>> meterMap = new HashMap<>();

    public boolean checkMeter(String meterN) {
        if (!meterMap.containsKey(meterN)) {
            meterMap.put(meterN, Optional.ofNullable(mdmsUtilityHelper.getMeterHierarchy(meterN)));
        }
        return meterMap.get(meterN).isPresent() && meterMap.get(meterN).get().size() > 1;
    }

    private void consolidate(MeterResultData data1, MeterResultData data2) {
        // add all doubles
        refFields.filterType(Double.class).foreach(f -> {
            f.set(data1, f.get(data1) != null && f.get(data2) != null
                    ? ((Double) f.get(data1)) + ((Double) f.get(data2)) : null);
        });
    }

    private MeterResultDataArray consolidate(List<MeterResultDataArray> set, int interval) {
        LocalDateTime start = null;
        LocalDateTime end = null;
        for (MeterResultDataArray value : set) {
            start = start == null || start.isBefore(value.get(0).getEntryTime().toLocalDateTime())
                    ? value.get(0).getEntryTime().toLocalDateTime() : start;
            end = end == null || end.isAfter(value.get(value.size() - 1).getEntryTime().toLocalDateTime())
                    ? value.get(value.size() - 1).getEntryTime().toLocalDateTime() : end;
        }

        MeterResultDataArray summed = new MeterResultDataArray();

        for (LocalDateTime entry = start; entry.isBefore(end) || entry.isEqual(end); entry = entry.plusMinutes(interval)) {
            // Added new constructor in MeterResultData: IED-5611
            summed.add(new MeterResultData(Timestamp.valueOf(entry),"series1").zero());
            for (int i = 0; i < set.size(); i++) {
                if (set.get(i).get(entry).isPresent()) {
                    consolidate(summed.get(summed.size()-1),set.get(i).get(entry).get());
                } else {
                    System.out.println("Unexpeted");
                }
            }
        }
        return summed;
    }

    public MeterResultDataArray getDetailDataTmzMilli(MeterDataService dataService, String serialN, Date from, Date to, MeterDataService.Interval interval,
                                                      int tmzOffset, String series, String touc1, String touc2, Boolean removeTmz) {
        // adjust time to include the previous step
        Date adjFrom = new Date(from.getTime() - (TimeUnit.MINUTES.toMillis(interval.minutes)));
        // add all the sets together
        List<String> meters = new ArrayList<>(meterMap.get(serialN).get().keySet()).stream().filter(s -> !serialN.equals(s))
                .collect(Collectors.toCollection(ArrayList::new));
        // collect all the data
        List<MeterResultDataArray> dataSet = meters
                .stream()
                .map(m -> dataService.getDetailDataTmzMilli(m, adjFrom, to, tmzOffset, interval, series, touc1, touc2, removeTmz))
                .filter(d -> !d.isEmpty())
                .collect(Collectors.toList());

        System.out.println("Meters == " + meters);

        return !dataSet.isEmpty()
                ? consolidate(dataSet, interval.minutes).updateCalcs().subset(from, to, 0)
                : dataService.getZeroPad(new Timestamp(from.getTime()).toLocalDateTime(),
                new Timestamp(to.getTime()).toLocalDateTime(), MeterDataService.Interval.HALF_HOURLY, serialN, series, true);
    }
}
