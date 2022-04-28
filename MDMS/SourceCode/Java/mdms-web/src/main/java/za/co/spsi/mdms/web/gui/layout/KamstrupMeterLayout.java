package za.co.spsi.mdms.web.gui.layout;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import de.steinwedel.messagebox.MessageBox;
import lombok.SneakyThrows;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.services.MeterDataService;
import za.co.spsi.mdms.common.services.broker.BrokerService;
import za.co.spsi.mdms.kamstrup.db.KamstrupBrokerCommandEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupGroupEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.services.group.KamstrupRestService;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.pjtk.util.StringUtils;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.PlaceOnToolbar;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.custom.SwitchField;
import za.co.spsi.toolkit.crud.gui.query.LayoutViewGrid;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class KamstrupMeterLayout extends Layout<KamstrupMeterEntity> {

    public static final String ALLOCATE_TO_GROUP = "Allocate to Group", DEALLOCATE_FROM_GROUPS = "Deallocate from Groups";

    public static DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    private KamstrupRestService groupService;

    @Inject
    private BeanManager beanManager;

    @Inject
    private BrokerService brokerService;

    @Inject
    private MeterDataService service;

    @Inject
    PrepaidMeterFilterService filterService;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.tmz_offset", folder = "server")
    private int tmzOffset;

    @Inject
    @ConfValue(value = "kamstrup.broker.filter.meters", folder = "server")
    private String kamMeterFilter;

    @Inject
    @ConfValue(value = "global.broker.env", folder = "server")
    private String brokerServiceEnv;

    @EntityRef(main = true)
    private KamstrupMeterEntity meter = new KamstrupMeterEntity();

    @UIGroup(column = 0)
    public Group group = new Group("Group", this);

    @PlaceOnToolbar
    public ActionField addToGroup = new ActionField("Add to Group", FontAwesome.OBJECT_GROUP, this, source -> addToGroup());
    @PlaceOnToolbar
    public ActionField removeFromGroup = new ActionField("Remove from Group", FontAwesome.OBJECT_UNGROUP, this, source -> removeFromGroup());
    @PlaceOnToolbar
    public ActionField fixReadingGap = new ActionField("Fix Reading Gap", FontAwesome.COG, this, source -> fixReadingGap());

    @UIGroup(column = 0)
    public Group detail = new Group("Kamstrup Meter Detail", this);

    @UIField(enabled = false)
    public LField ref = new LField(meter.ref, "Ref", this);
    @UIField(enabled = false)
    public LField profileRef = new LField(meter.profileRef, "Profile Ref", this);
    @UIField(enabled = false)
    public LField routesRef = new LField(meter.routesRef, "Routes Ref", this);

    @UIField(enabled = false)
    public LField state = new LField(meter.state, "State", this);
    @UIField(enabled = false)
    public LField vendorId = new LField(meter.vendorId, "Vendor", this);
    @UIField(enabled = false)
    public LField meterN = new LField(meter.meterN, "Meter Number", this);
    @UIField(enabled = false)
    public LField serialN = new LField(meter.serialN, "Serial Number", this);
    @UIField(enabled = false)
    public LField lastCommsD = new LField(meter.lastCommsD, "Last Comms", this);
    @UIField(enabled = false)
    public LField configurationUpdated = new LField(meter.configurationUpdated, "Config Updated", this);
    @UIField(enabled = false)
    public LField firmware = new LField(meter.firmware, "Firmware", this);
    @UIField(enabled = false)
    public LField typeDescription = new LField(meter.typeDescription, "Type Description", this);
    @UIField(enabled = false)
    public LField consumptionType = new LField(meter.consumptionType, "Consumption type", this);

    @UIGroup(column = 0)
    public Group status = new Group("Meter Status", this);

    @UIField(enabled = false)
    public LField failedNumber = new LField(meter.failedNumber, "Number of read failures", this);

    @UIField(enabled = false)
    @UI(width = "-1px")
    public SwitchField statusOn = new SwitchField(meter.statusOn, "Meter On/Off", this);

    @UIGroup(column = 0)
    @Qualifier(roles = {@Role(value = "PecMeterCutter"),@Role(value = "*",read = false,write = false)})
    public Group control = new Group("Meter Control", this);

    @UI(width = "-1px")
    @Qualifier(roles = {@Role(value = "PecMeterCutter"),@Role(value = "*",write = false)})
    public SwitchField requestOn = new SwitchField(meter.requestStatusOn, "Request Meter On", this);

    public Group nameGroup = new Group("", this, vendorId, meterN, serialN,state, typeDescription, consumptionType, configurationUpdated).setNameGroup();

    public Pane detailPane = new Pane("Meter Details", this, group, detail, status, control);

    public Pane brokerCommandHistoryPane = new Pane("Broker Command History", "select * from broker_command where meter_id = ? order by created_date desc", KamstrupBrokerCommandHistoryLayout.class,
            new Permission(0), this);

    public Pane readingPane = new Pane("Readings", MeterReadingLayout.getSql()+" where kam_meter_id = ? order by entry_time desc", MeterReadingLayout.class,
            new Permission(0), this);

    public Pane registersPane = new Pane("Registers", "select * from kamstrup_meter_register where meter_id = ? order by id desc", KamstrupMeterRegisterLayout.class,
            new Permission(0), this);

    public Pane ordersPane = new Pane("Orders",
            "select * from kamstrup_meter_order,kamstrup_meter_order_history where " +
                    "kamstrup_meter_order_history.meter_id = ? and kamstrup_meter_order_history.meter_order_id = kamstrup_meter_order.meter_order_id order by kamstrup_meter_order.created desc", KamstrupMeterOrderLayout.class,
            new Permission(0), this);

    public Pane failedLogs = new Pane("Failed Meter Re-Orders",
            KamstrupMeterReadFailedLogLayout.getMainSQL("kam_meter_read_failed_log.meter_id = ?"),KamstrupMeterReadFailedLogLayout.class,new Permission(0),this);

    public Pane groupsPane = new Pane("Groups", "select * from kamstrup_group,kamstrup_group_meter where kamstrup_group.group_id = kamstrup_group_meter.group_id and kamstrup_group_meter.meter_id = ?", KamstrupGroupLayout.class,
            new Permission(0), this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor( "kamstrup_meter","meter_id"), MDMSAuditLayout.class, new Permission(0), this);

    public KamstrupMeterLayout() {
        super("Kamstrup Meter Detail");
        setPermission(new Permission(Permission.PERMISSION_WRITE));
    }

    @PostConstruct
    private void init() {
        for (LField field : getFields()) {
            field.getProperties().setCaps(false);
        }
    }

    private Boolean statusDifferentFromRequested() {
        //requestOn.get()
        return !meter.requestStatusOn.getNonNull().equals(meter.statusOn.getNonNull());
    }

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
        } else if (za.co.spsi.toolkit.util.StringUtils.isEmpty(kamMeterFilter)) {
            testMeter = true;
        } else if(Arrays.stream(kamMeterFilter.split(",")).filter(f -> meter.serialN.get().equals(f)).count() >= 1) {
            testMeter = true;
        }
        return testMeter;
    }

    @Override
    public boolean save(Connection connection) throws SQLException {
        super.intoBindings();
        if (requestOn.getField().isChanged() && !brokerLogic()) {
            return false;
        }
        return super.save(connection);
    }

    private boolean brokerLogic() {

        requestOn.getProperties().setEnabled(true);
        requestOn.applyProperties();
        Boolean brokerCommandAllowed = true;

        if ( KamstrupBrokerCommandEntity.meterHasPendingCommands(dataSource, meter.meterId.get()) ) {
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
            brokerService.processKamstrupCommand(meter,requestOn.getNonNull() ? KamstrupBrokerCommandEntity.Command.RELEASE : KamstrupBrokerCommandEntity.Command.CUT,null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from KAMSTRUP_METER where 1 = 1";
    }

    @Override
    public String getExportSheetName() {
        if(meterN != null && meterN.get() != null && meterN.get() != "") {
            return "_" + meterN.getAsString();
        }else{
            return super.getExportSheetName();
        }
    }

    @Override
    public String[][] getFilters() {
        List<String[]> values= MeterFilterHelper.getFilters("kamstrup_meter","kam_meter_id");
        values.add(0,new String[]{"Unallocated", "Not allocated to any group","select * from kamstrup_meter where kamstrup_meter.group_id is null"});
        return values.toArray(new String[][]{});
    }

    @Override
    public String[] getActions() {
        return new String[]{ALLOCATE_TO_GROUP, DEALLOCATE_FROM_GROUPS};
    }

    @Override
    public void action(String action, List<EntityDB> entities) {
        if (DEALLOCATE_FROM_GROUPS.equals(action)) {
            entities.stream().forEach(e -> {
                KamstrupMeterEntity meter = (KamstrupMeterEntity) e;
                meter.meterGroups.getAllAsList(getDataSource(), null).stream().forEach(g -> {
                    DataSourceDB.executeInTx(getDataSource(), connection -> {

                        if (groupService.deleteMeter(g.group.getOne(getDataSource()), meter).getStatus() == Response.Status.OK.getStatusCode()) {
                            DataSourceDB.delete(connection, g);
                        }
                    });
                });
            });
            refresh();
        }
        if (ALLOCATE_TO_GROUP.equals(action)) {
            // ensure that all entities are not allocated
            // select the meter
            allocateToGroup(entities);
        }
    }

    private void fixReadingGap(Date from,Date to) {
        if (from != null && to != null) {
            LocalDateTime lFrom = new Timestamp(from.getTime()).toLocalDateTime();
            LocalDateTime lTo = new Timestamp(to.getTime()).toLocalDateTime();
            if (Duration.between(lFrom,lFrom).get(ChronoUnit.DAYS) < 30) {
                MeterResultDataArray dataSet = service.getDetailData(meter.meterN.get(), from, to, tmzOffset
                        , MeterDataService.Interval.HALF_HOURLY, "series1", null, null, false);
                boolean is_Prepaid = filterService.isPrepaid(meter.meterN.get());
                boolean is_water = isWater(meter.meterN.get());
                DSDB.executeInTx(dataSource,c -> {
                    dataSet.adjustTime((int) TimeUnit.MINUTES.toMillis(-tmzOffset)).getCalculated().stream()
                            .forEach(d -> MeterReadingEntity.generate(c, d, meter.meterId.get(),null,null,null,
                                    is_water?"VOLUME":"TOTAL", is_Prepaid, 0));

                });
            } else {
                Util.showError("Fix Reading Gap","Requested date must be less than 30 days");
            }
        }
    }

    public void fixReadingGap() {
        Util.showDateInput("Fix Reading Gap", "From date", from -> {
            if (from != null) {
                Util.showDateInput("Fix Reading Gap", "To Date", to -> {
                    fixReadingGap(from,to);
                    return null;
                },() -> {});
            }
            return null;
        }, () -> {});
    }

    @SneakyThrows
    private KamstrupMeterEntity getKamstrupMeterEntityBySerialN(String serialN) {
        KamstrupMeterEntity kamstrupMeterEntity = null;
        try (Connection connection = dataSource.getConnection()) {
            kamstrupMeterEntity = (KamstrupMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new KamstrupMeterEntity().serialN.set( serialN ));
        }
        return kamstrupMeterEntity;
    }

    @SneakyThrows
    private boolean isWater(String serialN) {
        boolean is_Water = false;
        KamstrupMeterEntity meterEntity = getKamstrupMeterEntityBySerialN(serialN);
        String meterType = meterEntity.meterType.get();
        if(!StringUtils.isEmpty(meterType)) {
            if( meterType.contains("Water") ) {
                is_Water = true;
            }
        }
        return is_Water;
    }

    public void removeFromGroup() {
        final KamstrupGroupLayout layout = getBean(beanManager, KamstrupGroupLayout.class);

        final VerticalLayout root = new VerticalLayout();
        final Window window = Util.showInWindow("Remove Meters", root, "90%", "90%");
        LayoutViewGrid<KamstrupGroupLayout> view = new LayoutViewGrid<KamstrupGroupLayout>(
                getDataSource(), layout, false, getRowLimit(),
                String.format("select * from kamstrup_group,kamstrup_group_meter where " +
                "kamstrup_group.group_id = kamstrup_group_meter.group_id and kamstrup_group_meter.meter_id = '%s' order by name asc",meter.meterId.get()),
                (layout1, source, newEvent, group) -> {
                    KamstrupGroupEntity.GroupMeter groupMeter =
                            DataSourceDB.getFromSet(getDataSource(),new KamstrupGroupEntity.GroupMeter(group.getSingleId().get().toString(),meter.meterId.get()));
                    if (groupMeter != null) {
                        DataSourceDB.executeInTx(getDataSource(), connection -> {
                            if (groupService.deleteMeter((KamstrupGroupEntity) group, meter).getStatus() == Response.Status.OK.getStatusCode()) {
                                DataSourceDB.delete(connection,groupMeter);
                                Notification.show("Meters removed from group", Notification.Type.HUMANIZED_MESSAGE);
                                window.close();
                            }
                        });
                    }
                }
        );
        view.build(true);
        root.addComponent(view);
        window.setContent(root);
        window.addCloseListener((Window.CloseListener) closeEvent -> refresh());

    }

    public void addToGroup() {
        allocateToGroup(Arrays.asList(meter));
    }


    public void allocateToGroup(List<EntityDB> entities) {
        final KamstrupGroupLayout layout = getBean(beanManager, KamstrupGroupLayout.class);

        Driver driver = DriverFactory.getDriver();
        String query = String.format("select * from kamstrup_group where enabled = %s", driver.boolToNumber(true));
        query = driver.orderBy(query, "name", false);

        LayoutViewGrid<KamstrupGroupLayout> view = new LayoutViewGrid<KamstrupGroupLayout>(
                getDataSource(), layout, false, getRowLimit(), query,
                (layout1, source, newEvent, group) -> {
                    List partial = new ArrayList<>(), completed = new ArrayList();
                    entities.stream().
                            filter(e -> DataSourceDB.getFromSet(getDataSource(),new KamstrupGroupEntity.GroupMeter(
                                    group.getSingleId().get().toString(),e.getSingleId().get().toString())) == null).forEach(e -> {
                        DataSourceDB.executeInTx(getDataSource(), new DataSourceDB.EmptyCallback() {
                            @Override
                            public void run(Connection connection) throws Exception {
                                if (groupService.allocateMeter((KamstrupGroupEntity) group, (KamstrupMeterEntity) e).getStatus() == Response.Status.OK.getStatusCode()) {
                                    DataSourceDB.set(connection,new KamstrupGroupEntity.GroupMeter(group.getSingleId().get().toString(),e.getSingleId().get().toString()));
                                    completed.add(e);
                                } else {
                                    partial.add(e);
                                }
                            }
                        });
                    });
                    if (!partial.isEmpty()) {
                        Notification.show("Meters partially allocated", Notification.Type.HUMANIZED_MESSAGE);
                    } else if (!completed.isEmpty()) {
                        Notification.show("Meters allocated", Notification.Type.TRAY_NOTIFICATION);
                    }
                    Util.getComponentParent(com.vaadin.ui.Window.class, source).close();
                });
        view.build(true);
        Util.showInWindow("Allocate Meters", view, "90%", "90%").addCloseListener(new com.vaadin.ui.Window.CloseListener() {
            @Override
            public void windowClose(com.vaadin.ui.Window.CloseEvent closeEvent) {
                refresh();
            }
        });
    }

    public static void main(String args[]) throws Exception {
        System.out.println(LocalDate.now().with(DayOfWeek.MONDAY));
    }
}
