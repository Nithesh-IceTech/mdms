package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.View;
import za.co.spsi.toolkit.entity.DataSource;
import za.co.spsi.toolkit.io.IOUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by johan on 2017/03/30.
 */
public class IceMeterRegisterReadingsView extends View<IceMeterRegisterReadingsView> {

    private static String METER_REGISTER_READINGS_SQL;

    static {
        try {
            METER_REGISTER_READINGS_SQL = new String(IOUtil.readFully(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/ice_meter_register_readings_view.sql")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IceMeter meter = new IceMeter();
    public IceMeterRegister meterRegister = new IceMeterRegister();
    public IceMeterReadings readings = new IceMeterReadings();

    public IceMeterRegisterReadingsView() {
        setSql(METER_REGISTER_READINGS_SQL);
    }

    public static class Container extends ArrayList<IceMeterRegisterReadingsView> {
        public static Container get(DataSource dataSource,String meterNo, String ... meterRegister) {
            for (String register : meterRegister) {

            }
            return null;
        }
    }


}