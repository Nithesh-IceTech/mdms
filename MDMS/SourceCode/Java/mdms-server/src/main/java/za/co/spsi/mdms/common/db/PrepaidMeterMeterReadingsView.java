package za.co.spsi.mdms.common.db;

import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.db.View;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.sql.DataSource;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class PrepaidMeterMeterReadingsView extends View<PrepaidMeterMeterReadingsView> {

    public KamstrupMeterEntity kam = new KamstrupMeterEntity();
    public ElsterMeterEntity els = new ElsterMeterEntity();
    public NESMeterEntity nes = new NESMeterEntity();
    public GenericMeterEntity genericMeterEntity = new GenericMeterEntity();
    public MeterReadingEntity meterReadingEntity = new MeterReadingEntity();

    public PrepaidMeterMeterReadingsView() {
        meterReadingEntity.setName("m");
        aliasNames();
    }

    public PrepaidMeterMeterReadingsView(String id, Boolean onlyLatestReading) {
        this();
        meterReadingEntity.setName("m");
        setSql(onlyLatestReading ? getOnlyLatestReadingForEachMeter() : getCompleteBatch(), id);
        aliasNames();
    }

    private String getCompleteBatch() {

        Driver driver = DriverFactory.getDriver();

        String query = String.format("select * from meter_reading m " +
                " left join kamstrup_meter on m.kam_meter_id = kamstrup_meter.meter_id " +
                " left join nes_meter on m.nes_meter_id = nes_meter.meter_id " +
                " left join elster_meter on m.els_meter_id = elster_meter.meter_id " +
                " left join GENERIC_METER on (m.GENERIC_METER_ID = GENERIC_METER.GENERIC_METER_ID and GENERIC_METER.live = %s) " +
                " where m.PREPAID_METER_READING_BATCH_ID = ? and (m.GENERIC_METER_ID is null or GENERIC_METER.live = %s) ",
                driver.boolToNumber(true),
                driver.boolToNumber(true));

        return driver.orderBy(query, "entry_time", false);
    }

    private String getOnlyLatestReadingForEachMeter() {

        Driver driver = DriverFactory.getDriver();

        String query = String.format("select * from meter_reading m " +
                " left join kamstrup_meter on m.kam_meter_id = kamstrup_meter.meter_id " +
                " left join nes_meter on m.nes_meter_id = nes_meter.meter_id " +
                " left join elster_meter on m.els_meter_id = elster_meter.meter_id " +
                " left join generic_meter on (m.generic_meter_id = generic_meter.generic_meter_id and generic_meter.live = %s ) " +
                " where m.prepaid_meter_reading_batch_id = ? and (m.generic_meter_id is null or generic_meter.live = %s ) ",
                driver.boolToNumber(true),
                driver.boolToNumber(true));

        return driver.orderBy(query, "entry_time", true);
    }

}
