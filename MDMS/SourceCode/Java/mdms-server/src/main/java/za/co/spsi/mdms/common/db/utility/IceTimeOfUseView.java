package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.View;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceTimeOfUseView extends View {

    public IceTimeOfUseType timeOfUseType = new IceTimeOfUseType();
    public IcePriceVersionTimeOfUse priceVersionTimeOfUse = new IcePriceVersionTimeOfUse();

    public IceTimeOfUseView() {

    }

    public IceTimeOfUseView setMeterN(String meterN) {
        setSql("select * from ICE_PRICEVERSIONTIMEOFUSE,ICE_TIMEUSETYPE\n" +
                "  where ICE_PRICEVERSIONTIMEOFUSE.ICE_TIMEUSETYPE_ID = ICE_TIMEUSETYPE.ICE_TIMEUSETYPE_ID and\n" +
                "      ICE_PRICEVERSIONTIMEOFUSE.ISACTIVE = 'Y' and ICE_TIMEUSETYPE.ISACTIVE = 'Y' and " +
                "M_PRICELIST_VERSION_ID in (" +
                "  select M_PRICELIST_VERSION_ID from ICE_PROPERTY,ICE_METER,ice_entity_hierarchy where " +
                "ICE_METER.ICE_METER_NUMBER = ? and ICE_METER.ICE_METER_ID = ice_entity_hierarchy.ICE_METER_ID and " +
                "ICE_PROPERTY.ICE_PROPERTY_ID =  ice_entity_hierarchy.ICE_PROPERTY_ID)");
        setData(meterN);
        return this;
    }

    public IceTimeOfUseView setId(String id) {
        setSql("select * from ICE_PRICEVERSIONTIMEOFUSE,ICE_TIMEUSETYPE\n" +
                "  where ICE_PRICEVERSIONTIMEOFUSE.M_PRICELIST_VERSION_ID = ? and ICE_PRICEVERSIONTIMEOFUSE.ICE_TIMEUSETYPE_ID = ICE_TIMEUSETYPE.ICE_TIMEUSETYPE_ID and\n" +
                "      ICE_PRICEVERSIONTIMEOFUSE.ISACTIVE = 'Y' and ICE_TIMEUSETYPE.ISACTIVE = 'Y'");
        setData(id);
        return this;
    }

    public boolean hasTimeOfUse() {
        return priceVersionTimeOfUse.startTime.get() != null && priceVersionTimeOfUse.endTime.get() != null;
    }

    public static boolean inTimeOfUse(LocalTime from, LocalTime to, LocalTime time) {
        return from.isBefore(to) ? time.compareTo(from) >= 0 && time.compareTo(to) < 0 :
                from.compareTo(time) <= 0 || to.isAfter(time);
    }

    public boolean inTimeOfUse(LocalTime time) {
        return hasTimeOfUse() ?
                inTimeOfUse(priceVersionTimeOfUse.startTime.get().toLocalTime(), priceVersionTimeOfUse.endTime.get().toLocalTime(), time) : true;
    }


    public static class IceTimeOfUseViewList extends ArrayList<IceTimeOfUseView> {

        public IceTimeOfUseViewList(List<IceTimeOfUseView> values) {
            addAll(values);
        }

        public String getTOU(LocalTime time) {
            Optional<IceTimeOfUseView> value = stream().filter(v -> v.inTimeOfUse(time)).findFirst();
            return value.isPresent() ? value.get().timeOfUseType.value.get() : null;
        }
    }

}
