package za.co.spsi.mdms.web.gui.layout.generator;

import za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class GeneratorTransactionDetailLayout extends Layout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private GeneratorTransactionEntity.DetailLine detailLine = new GeneratorTransactionEntity.DetailLine();

    @EntityRef()
    private NESMeterEntity nesMeter = new NESMeterEntity();

    @EntityRef()
    private KamstrupMeterEntity kamMeter = new KamstrupMeterEntity();

    @UIGroup(column = 0)
    public Group meterDetail = new Group("Meter Detail",this).setNameGroup();

    public LField nesMeterSerialN = new LField(nesMeter.serialN,"NES Serial Number",this);
    public LField kamMeterN = new LField(kamMeter.meterN,"Meter Number",this);

    public Pane detailPane = new Pane("Details", this, meterDetail);

    public GeneratorTransactionDetailLayout() {
        super("Generator Transactions Details");
    }

    public static String getSQL() {
        return "select * from gen_tx_detail " +
                "left join kamstrup_meter on gen_tx_detail.kam_meter_id = kamstrup_meter.meter_id " +
                "left join nes_meter on gen_tx_detail.nes_meter_id = nes_meter.meter_id ";
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

}
