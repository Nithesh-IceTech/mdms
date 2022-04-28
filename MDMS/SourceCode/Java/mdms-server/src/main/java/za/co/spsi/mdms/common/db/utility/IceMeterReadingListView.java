package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.View;
import za.co.spsi.toolkit.io.IOUtil;

import java.io.IOException;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceMeterReadingListView extends View<IceMeterReadingListView> {

    private static String METER_READINGS_VIEW_SQL;

    static {
        try {
            METER_READINGS_VIEW_SQL = new String(IOUtil.readFully(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/ice_meter_readings_view.sql")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IceMeterRegister register = new IceMeterRegister();
    public IceMeterReadingList iceMeterReadingList = new IceMeterReadingList();
    public IceProperty property = new IceProperty();
    public IceMeter meter = new IceMeter();
    public IceMeterReadings readings = new IceMeterReadings();
    public IceMeterReadingsHistInvView history = new IceMeterReadingsHistInvView();
    public IceOUM iceOUM = new IceOUM();

    public IceMeterReadingListView() {
        setSql(METER_READINGS_VIEW_SQL);
        aliasNames();
    }
}
