package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterReadFailedLog;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by johan on 2016/12/05.
 */
public class KamstrupMeterReadFailedLogLayout extends Layout<KamstrupMeterReadFailedLog> {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private KamstrupMeterReadFailedLog log = new KamstrupMeterReadFailedLog();

    @EntityRef(main = false)
    private KamstrupMeterEntity meter = new KamstrupMeterEntity();

    public LField meterN = new LField(meter.meterN, "Meter Number", this);

    @UIGroup(column = 0)
    public Group detail = new Group("Log details",this);

    public LField created = new LField(log.created, "Created",this);
    public LField fromDate = new LField(log.fromDate, "From date",this);
    public LField toDate = new LField(log.toDate, "To date",this);
    public LField registers = new LField(log.registers, "Registers",this);
    public LField loggerId = new LField(log.loggerId, "LoggerId",this);
    public LField commandRef = new LField(log.commandRef, "Command ref",this);

    @UIField(rows = 5,enabled = false)
    public TextAreaField error = new TextAreaField(log.error, "Error",this);

    public Group nameGroup = new Group("",this,meterN,created,fromDate,toDate,registers,loggerId).setNameGroup();

    public Pane detailPane = new Pane("",this, detail);

    public Pane meterPane = new Pane("Meter","select * from kamstrup_meter where kamstrup_meter.meter_id = kam_meter_read_failed_log.meter_id",
            KamstrupMeterLayout.class,new Permission(0),this);

    public Pane orderPane = new Pane("Re Orders","select * from kamstrup_meter_order where kamstrup_meter_order.meter_order_id = kam_meter_read_failed_log.re_order_id",
            KamstrupMeterOrderLayout.class,new Permission(0),this);

    public KamstrupMeterReadFailedLogLayout() {
        super("Failed Re-Order");
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public static String getMainSQL(String where) {
        return "select * from kam_meter_read_failed_log left join kamstrup_meter on kamstrup_meter.meter_id = kam_meter_read_failed_log.meter_id where " + where;
    }

    @Override
    public String getMainSql() {
        return getMainSQL("1 = 1");
    }
}
