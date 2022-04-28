package za.co.spsi.mdms.web.gui.layout.generator;

import za.co.spsi.mdms.common.db.generator.GeneratorEntity;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.LookupField;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class GeneratorMeterLinkLayout extends Layout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private GeneratorEntity.MeterLink meterLink = new GeneratorEntity.MeterLink();



    public LField nesMeterSerialN = new LField("NES Serial Number", "nes_serial_n", this);
    public LField kamMeterN = new LField("Kam Meter Number", "kam_serial_n", this);
    public LField elsMeterN = new LField("Els Meter Number", "els_serial_n", this);
    public LField genMeterN = new LField("Generic Meter Number", "gen_serial_n", this);

    @UIGroup(column = 0)
    public Group detailGroup = new Group("Generator Meter Detail", this);

    public LookupField<String> kamMeterId = new LookupField(meterLink.kamMeterId, "Kamstrup Meter",
            "select meter_id,serial_n from kamstrup_meter where serial_n is not null", this);

    public LookupField<String> nesMeterId = new LookupField(meterLink.nesMeterId, "Nes Meter",
            "select meter_id,serial_n from nes_meter where serial_n is not null", this);

    public LookupField<String> elsMeterId = new LookupField(meterLink.elsMeterId, "Els Meter",
            "select meter_id,serial_n from elster_meter where serial_n is not null", this);

    public LookupField<String> genMeterId = new LookupField(meterLink.genericMeterId, "Generic Meter",
            "select generic_meter_id,meter_serial_n from generic_meter where meter_serial_n is not null", this);

    public Group nameGroup = new Group("", this, nesMeterSerialN, kamMeterN, elsMeterN, genMeterN).setNameGroup();

    public Pane detailPane = new Pane("Group Details", this, detailGroup);

    public GeneratorMeterLinkLayout() {
        super("Generator Meters");
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public static String getSQL() {
        return "select  " +
                "   gen_meter_link.id, " +
                "   kamstrup_meter.serial_n as kam_serial_n, " +
                "   elster_meter.serial_n as els_serial_n," +
                "   generic_meter.METER_SERIAL_N as gen_serial_n," +
                "   nes_meter.serial_n as nes_serial_n" +
                " from  " +
                "   gen_meter_link  " +
                "       left join kamstrup_meter on gen_meter_link.kam_meter_id = kamstrup_meter.meter_id  " +
                "       left join elster_meter on gen_meter_link.els_meter_id = elster_meter.meter_id  " +
                "       left join generic_meter on gen_meter_link.generic_meter_id = generic_meter.generic_meter_id  " +
                "       left join nes_meter on gen_meter_link.nes_meter_id = nes_meter.meter_id";
    }


}
