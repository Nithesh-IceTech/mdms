package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.View;
import za.co.spsi.toolkit.io.IOUtil;

import java.io.IOException;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceApprovedMeterReadingsView extends View<IceApprovedMeterReadingsView> {

    private static String METER_READINGS_VIEW_SQL;

    static {
        try {
            METER_READINGS_VIEW_SQL = new String(IOUtil.readFully(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/ice_approved_meter_reading_items_view.sql")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IceProperty property = new IceProperty();
    public IceMeter meter = new IceMeter();
    public IceOUM iceOUM = new IceOUM();
    public IceMeterRegister register = new IceMeterRegister();
    public IceMeterReadings readings = new IceMeterReadings();
    public IceMeterReadingList list = new IceMeterReadingList();
    public IceApprovedMeterReadings approved  = new IceApprovedMeterReadings();
    public IcePriceVersionTimeOfUse priceVersionTimeOfUse = new IcePriceVersionTimeOfUse();
    public IceDayOfWeek iceDayOfWeek = new IceDayOfWeek();

    public IceApprovedMeterReadingsView() {
        setSql(METER_READINGS_VIEW_SQL);
        aliasNames();
    }
}
