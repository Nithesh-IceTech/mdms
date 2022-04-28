package za.co.spsi.mdms.nes.db;

import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.util.MeterFilterService;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.View;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jaspervdbijl on 2016/12/22.
 */
public class NESResultDeviceView extends View<NESResultDeviceView> {

    public NESDeviceEntity device = new NESDeviceEntity();
    public NESResultEntity result = new NESResultEntity();

    public NESResultDeviceView() {

    }

    public NESResultDeviceView init(MeterFilterService meterFilterService,Timestamp timestamp) {
        setSql(String.format("select * from devices,results where " +
                "devices.serialNumber is not null and len(serialNumber) > 0 and Results.EntityId = devices.deviceId and results.dateTimeStamp >= ? %s order by results.dateTimeStamp desc",
                meterFilterService.getFilterSql()),timestamp);
        return this;
    }

    public static Timestamp getStartTime(DataSource dataSource) {
        // get the latest timestamp
        List<List> values =
                DataSourceDB.executeQuery(dataSource,new Class[]{Timestamp.class},"select max(DATE_TIME_STAMP) from NES_METER_RESULT");
        return values.isEmpty()||values.get(0).isEmpty() || values.get(0).get(0) == null?
                Timestamp.valueOf(LocalDateTime.now().minusHours(3)):
                (Timestamp) values.get(0).get(0);
    }

}
