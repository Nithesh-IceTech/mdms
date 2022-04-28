package za.co.spsi.mdms.nes.db;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.db.View;
import za.co.spsi.toolkit.db.drivers.Driver;

import javax.inject.Inject;

/**
 * Created by jaspervdbijl on 2016/12/22.
 */
public class NESMeterResultView extends View<NESMeterResultView> {

    public NESMeterEntity meter = new NESMeterEntity();
    public NESMeterResultEntity result = new NESMeterResultEntity();

    public NESMeterResultView() {
        String query = String.format("select * from nes_meter,nes_meter_result where nes_meter.meter_id = nes_meter_result.meter_id and " +
                        " nes_meter_result.status = %d order by nes_meter_result.date_time_stamp asc fetch first 1000 rows only",
                NESMeterResultEntity.Status.RECEIVED.code);
        setSql(query);
        aliasNames();
    }

    public NESMeterResultView(Integer batchSize) {
        String query = String.format("select * from nes_meter,nes_meter_result where nes_meter.meter_id = nes_meter_result.meter_id and " +
                                     " nes_meter_result.status = %d order by nes_meter_result.date_time_stamp asc fetch first %d rows only",
                NESMeterResultEntity.Status.RECEIVED.code, batchSize);
        setSql(query);
        aliasNames();
    }

}
