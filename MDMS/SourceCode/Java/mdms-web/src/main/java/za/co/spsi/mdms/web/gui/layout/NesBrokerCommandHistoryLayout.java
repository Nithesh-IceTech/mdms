package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.mdms.kamstrup.db.KamstrupBrokerCommandEntity;
import za.co.spsi.mdms.nes.db.NESBrokerCommandEntity;
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
public class NesBrokerCommandHistoryLayout extends Layout<NESBrokerCommandEntity> {
    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private NESBrokerCommandEntity broker = new NESBrokerCommandEntity();

    @UIGroup(column = 0)
    public Group detail = new Group("Broker Command History Detail",this);

    public LField submitDate = new LField(broker.submitDate,"Submit date",this);

    public LField meterSerialNumber = new LField(broker.meterSerialNumber,"Meter Serial Number",this);

    public LField isCTMeter = new LField(broker.isCTMeter,"ICT Meter",this);

    public LField command = new LField(broker.command,"Command",this);

    public LField commandStatus = new LField(broker.commandStatus,"Command Status",this);

    public LField deviceCommandStatus = new LField(broker.deviceCommandStatus,"Device Command Status",this);

    public LField gatewayCommandStatus = new LField(broker.gatewayCommandStatus,"Gateway Command Status",this);

    public LField deviceCommandTrackingID = new LField(broker.deviceCommandTrackingID,"Command Tracking Id",this);

    public LField deviceCommandResultStatus = new LField(broker.deviceCommandResultStatus,"Command Result",this);

    public LField gatewayCommandTrackingID = new LField(broker.gatewayCommandTrackingID,"Gateway Command Tracking",this);

    @UIField(enabled = false)
    public TextAreaField error = new TextAreaField(broker.error,"Error",this);

    public Group nameGroup = new Group("",this,submitDate,isCTMeter,command,commandStatus).setNameGroup();

    public Pane detailPane = new Pane("",this, detail);

    public NesBrokerCommandHistoryLayout() {
        super("Broker Command History");
        setPermission(new Permission(0));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from nes_broker_command where 1 = 1";
    }
}
