package za.co.spsi.mdms.web.gui.layout;

import com.sun.org.apache.xpath.internal.operations.Bool;
import de.steinwedel.messagebox.MessageBox;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.services.broker.BrokerService;
import za.co.spsi.mdms.nes.db.NESBrokerCommandEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.custom.SwitchField;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class NesMeterLayout extends Layout<NESMeterEntity> {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private NESMeterEntity meter = new NESMeterEntity();

    @UIGroup(column = 0)
    public Group detail = new Group("Nes Meter Detail",this);

    @Inject
    private BrokerService brokerService;

    @Inject
    @ConfValue(value = "nes.broker.filter.meters", folder = "server")
    private String nesMeterFilter;

    @Inject
    @ConfValue(value = "global.broker.env", folder = "server")
    private String brokerServiceEnv;

    @UIField(enabled= false)
    public LField name = new LField(meter.name,"Name",this);
    @UIField(enabled= false)
    public LField description = new LField(meter.description,"Description",this);
    @UIField(enabled= false)
    public LField gatewayId = new LField(meter.gatewayId,"Gateway Id",this);
    @UIField(enabled= false)
    public LField serialN = new LField(meter.serialN,"Serial Number",this);
    @UIField(enabled= false)
    public LField installationDate = new LField(meter.installationDate,"Installation Date",this);
    @UIField(enabled= false)
    public LField hardwareVersion = new LField(meter.hardwareVersion,"Hardware Version",this);
    @UIField(enabled= false)
    public LField softwareVersion = new LField(meter.softwareVersion,"Software Version",this);

    @UIGroup(column = 0)
    public Group status = new Group("Meter Status",this);

    @UIField(enabled= false)
    @UI(width = "-1px")
    public SwitchField statusOn = new SwitchField(meter.statusOn,"Meter On/Off",this);

    @UIGroup(column = 0)
    @Qualifier(roles = {@Role(value = "PecMeterCutter"),@Role(value = "*",read = false,write = false)})
    public Group control = new Group("Meter Control",this);

    @UI(width = "-1px")
    @Qualifier(roles = {@Role(value = "PecMeterCutter"),@Role(value = "*",write = false)})
    public SwitchField requestOn = new SwitchField(meter.requestStatusOn,"Request Meter On",this);

//    @UIGroup(column = 0)
//    public Group status = new Group("Meter Status",this);
//
//    @UIField(enabled= false)
//    @UI(width = "-1px")
//    public SwitchField statusOn = new SwitchField(meter.statusOn,"Meter On/Off",this);
    //@UIField(enabled= false)
    //@UI(width = "-1px")
    //public SwitchField statusOff = new SwitchField(meter.statusOff,"Meter Off",this);

//    @UIGroup(column = 0)
//    public Group control = new Group("Meter Control",this);
//
//    @UI(width = "-1px")
//    public SwitchField requestOn = new SwitchField(meter.requestStatusOn,"Request Meter On",this);
    //@UI(width = "-1px")
    //public SwitchField requestOff = new SwitchField(meter.requestStatusOff,"Request Meter Off",this);

    public Group nameGroup = new Group("",this, name, description, gatewayId,serialN, installationDate).setNameGroup();

//    public Pane detailPane = new Pane("Meter Details",this, detail,status,control);
    public Pane detailPane = new Pane("Meter Details",this, detail,status,control);

    public Pane brokerCommandHistoryPane = new Pane("Broker Command History","select * from nes_broker_command where meter_id = ? order by SUBMIT_DATE desc",NesBrokerCommandHistoryLayout.class,
            new Permission(0),this);

    public Pane readingPane = new Pane("Readings",MeterReadingLayout.getSql()+" where nes_meter_id = ? order by entry_time desc",MeterReadingLayout.class,
            new Permission(0),this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor("nes_meter","meter_id"),MDMSAuditLayout.class,new Permission(0),this);

    public NesMeterLayout() {
        super("Nes Meter Detail");
        setPermission(new Permission(Permission.PERMISSION_WRITE));
        init();
    }

    @PostConstruct
    private void init() {
        for (LField field : getFields()) {
            field.getProperties().setCaps(false);
        }
    }

//    public void processCommand(NESBrokerCommandEntity.Command command) {
//        NESBrokerCommandEntity ent = new NESBrokerCommandEntity();
//        ent.meterSerialNumber.set(meter.serialN.get());
//        ent.meterId.set(meter.meterId.get());
//        ent.command.set(command.getCode());
//        ent.commandStatus.set(Status.CREATED.getCode());
//        DataSourceDB.set(dataSource, ent);
//    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        if (!meter.requestStatusOn.getNonNull().equals(meter.statusOn.getNonNull())) {
            requestOn.set(meter.statusOn.getNonNull());
        }
    }

    protected boolean isTestMeter() {
        Boolean testMeter = false;
        if(brokerServiceEnv.equalsIgnoreCase("prod")) {
            testMeter = true;
        } else if (StringUtils.isEmpty(nesMeterFilter)) {
            testMeter = true;
        } else if(Arrays.stream(nesMeterFilter.split(",")).filter(f -> meter.serialN.get().equals(f)).count() >= 1) {
            testMeter = true;
        }
        return testMeter;
    }

    @Override
    public boolean save() {
        super.intoBindings();
        if (requestOn.getField().isChanged() && !brokerLogic()) {
            return false;
        } else {
            return super.save();
        }
    }

    private boolean brokerLogic() {

        requestOn.getProperties().setEnabled(true);
        requestOn.applyProperties();
        Boolean brokerCommandAllowed = true;

        if ( NESBrokerCommandEntity.meterHasPendingCommands(dataSource, meter.meterId.get()) ) {
            brokerCommandAllowed = false;
            MessageBox.createInfo().asModal(true).withCaption("Information").withMessage(String.format("Command could not be executed, meter %s has pending broker commands.",meter.serialN.get())).open();
        }

        if (!requestOn.getProperties().isEnabled()) {
            brokerCommandAllowed = false;
            MessageBox.createInfo().asModal(true).withCaption("Information").withMessage("Command could not be executed, requestOn field is disabled.").open();
        }

        if (!isTestMeter()) {
            brokerCommandAllowed = false;
            MessageBox.createInfo().asModal(true).withCaption("Information").withMessage(String.format("Command could not be executed, meter %s is not in the broker filter.",meter.serialN.get())).open();
        }

        if(brokerCommandAllowed) {
            brokerService.processNesCommand(meter, requestOn.getNonNull() ? NESBrokerCommandEntity.Command.CONNECT : NESBrokerCommandEntity.Command.DISCONNECT,null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String[][] getFilters() {
        return MeterFilterHelper.getFilters("nes_meter","nes_meter_id").toArray(new String[][]{});
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from NES_METER where 1 = 1";
    }

    @Override
    public String getExportSheetName() {
        if(serialN != null && serialN.get() != null && serialN.get() != "") {
            return "_" + serialN.getAsString();
        }else{
            return super.getExportSheetName();
        }
    }
}
