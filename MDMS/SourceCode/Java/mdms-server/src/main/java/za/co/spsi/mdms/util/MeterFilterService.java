package za.co.spsi.mdms.util;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.StringUtils;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaspervdbijl on 2017/06/28.
 */
@Dependent
public class MeterFilterService {

    @Inject
    private PropertiesConfig propertiesConfig;

    public boolean shouldProcessMeter(String serialN) {
        return StringUtils.isEmpty(propertiesConfig.getFiltered_meters()) ||
                Arrays.stream(propertiesConfig.getFiltered_meters().split(","))
                        .anyMatch(m -> m.equalsIgnoreCase(serialN));
    }

    public List<String> getFilteredMeters() {
        return Arrays.stream(propertiesConfig.getFiltered_meters().split(",")).collect(Collectors.toList());
    }

    public String getFilterSql() {
        return !StringUtils.isEmpty(propertiesConfig.getFiltered_meters())?
                String.format("and (%s)",
                        Arrays.stream(propertiesConfig.getFiltered_meters().split(","))
                                .map(s -> "devices.serialNumber = '"+s+"'").reduce((a,b)-> a+" or "+b).get()):"";
    }

    public static Timestamp getMax(Timestamp currentMax, Timestamp entryTime) {
        if (entryTime == null || entryTime.getTime() > System.currentTimeMillis() + Duration.ofHours(10).toMillis()) {
            // entry time can not be in the future
            return currentMax;
        } else return currentMax == null || currentMax.compareTo(entryTime) < 0 ? entryTime : currentMax;
    }

    public static void main(String[] args) {
        System.out.println(getMax(Timestamp.valueOf(LocalDateTime.now()),Timestamp.valueOf(LocalDateTime.now())));
    }

}
