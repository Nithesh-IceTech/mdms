package za.co.spsi.mdms.kamstrup.services.meter;

import za.co.spsi.mdms.kamstrup.services.meter.domain.Meters;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 2016/10/13.
 */
public class MeterDaoList extends ArrayList<MeterDao> {

    public MeterDaoList() {}

    public MeterDaoList(List<Meters> metersList) {
        metersList.stream().forEach(meters -> Arrays.asList(meters.meters).stream().forEach(
                m->  add(new MeterDao(m))
        ));
    }

    public MeterDao getBySerialNo(String serialNo) {
        Assert.notNull(serialNo,"Can not search on null serial No");
        for (MeterDao meter : this) {
            if (serialNo.equals(meter.getMeter().serialNumber)) {
                return meter;
            }
        }
        return null;
    }

    public MeterDaoList getActive() {
        return stream().
                filter(m -> MeterConstants.STATE_IN_USE.equals(m.getMeter().state) && !StringUtils.isEmpty(m.getMeter().serialNumber))
                .collect(Collectors.toCollection(MeterDaoList::new));
    }

}
