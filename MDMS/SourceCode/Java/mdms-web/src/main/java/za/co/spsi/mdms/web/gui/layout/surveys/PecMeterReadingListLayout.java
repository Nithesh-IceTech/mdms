package za.co.spsi.mdms.web.gui.layout.surveys;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Notification;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.ButtonType;
import de.steinwedel.messagebox.MessageBox;
import lombok.SneakyThrows;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.mdms.common.db.survey.PecUtilityMeterReadingListEntity;
import za.co.spsi.mdms.kamstrup.services.utility.MeterRegisterUpdateService;
import za.co.spsi.mdms.kamstrup.services.utility.UtilityMeterReadingSyncService;
import za.co.spsi.mdms.web.gui.MdmsLayout;
import za.co.spsi.mdms.web.gui.fields.EntityStatusCdField;
import za.co.spsi.mdms.web.gui.fields.NotesField;
import za.co.spsi.mdms.web.gui.layout.MDMSAuditLayout;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.PlaceOnToolbar;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.ano.UILayout;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.idempiere.BaseIceUtilityHelper;
import za.co.spsi.toolkit.crud.sync.db.SharedEntity;
import za.co.spsi.toolkit.crud.sync.gui.fields.EntityStatusActionField;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity.Status.Error;
import static za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity.Status.RequestClose;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class PecMeterReadingListLayout extends MDMSSyncLayout<PecMeterReadingListEntity> {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private PecMeterReadingListEntity meterList = new PecMeterReadingListEntity();

    @Inject
    private MeterRegisterUpdateService service;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.tmz_offset", folder = "server")
    private int tmzOffset;

    @UIGroup(column = 0)
    public Group detail = new Group("Meter Reading List", this);

    @UIField(writeOnce = true)
    public LField<String> name = new LField<>(meterList.name, MdmsLocaleId.NAME, this);

    @UIField(enabled = false)
    public LField<String> meterReadingListN = new LField<>(meterList.meterReadingListN, MdmsLocaleId.METER_READING_LIST_N, this);

    @UIField(enabled = false)
    public LField<Timestamp> readingDate = new LField<>(meterList.readingDate, MdmsLocaleId.METER_READING_DATE, this);

    @UIField(enabled = false)
    public LField<String> cycleStartDate = new LField<>(meterList.cycleStartDate, "Cycle Start Date", this);

    @UIField(enabled = false)
    public LField<String> closureDate = new LField<>(meterList.closureDate, MdmsLocaleId.METER_CLOSURE_DATE, this);

    public EntityStatusCdField entityStatusCdField = new EntityStatusCdField(meterList.sharedEntity.entityStatusCd, this);

    @UIField(enabled = false)
    public LField<Timestamp> capturedD = new LField<Timestamp>(meterList.sharedEntity.capturedD, MdmsLocaleId.ENTITY_CAPTURE_D, this);

    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group reviewGroup = new Group(MdmsLocaleId.REVIEW_STATUS, null, this);

    @UIField(enabled = false)
    public MLCSLookupField<Integer> status = new MLCSLookupField<Integer>(meterList.status, "Status", this, "METERPROCESS");


    @UIGroup(column = 1, layout = {@UILayout(column = 0, minWidth = 1280)})
    public Group noteGroup = new Group(MdmsLocaleId.NOTES_GROUP_CAPTION, null, this);

    public NotesField notes = new NotesField(meterList.sharedEntity.notes, this);

    @PlaceOnToolbar
    public EntityStatusActionField entityStatusActionField = new EntityStatusActionField(entityStatusCdField, this, this);

    @PlaceOnToolbar
    public ActionField simulateBilling = new ActionField(MdmsLocaleId.SIM_BILLING, FontAwesome.VIDEO_CAMERA, this,
            source -> {
                performSimulation();
            });

    public Group nameGroup = new Group("", this, name, meterReadingListN, readingDate, status, entityStatusCdField, capturedD).setNameGroup();

    public Pane detailPane = new Pane("", this, detail, reviewGroup, noteGroup);

    public Pane meterReadings = new Pane("Meter readings",
            PecMeterReadingLayout.getMainSQL() + " where pec_meter_register.meter_reading_list_id = ? order by pec_meter_register.meter_seq asc",
            PecMeterReadingLayout.class, new Permission().setMayCreate(false).setMayDelete(false), this);

    public Pane logs = new Pane("Logs", "select * from pec_meter_reading_list_log where pec_meter_reading_list_log.meter_reading_list_id = ? order by log_time asc", LogLayout.class,
            new Permission(0), this);

    public Pane auditPane = new Pane(ToolkitLocaleId.AUDIT_CAPTION, AuditConfig.getSqlFor( "pec_meter_reading_list", "meter_reading_list_id"), MDMSAuditLayout.class, new Permission(0), this);

    public PecMeterReadingListLayout() {
        super(MdmsLocaleId.METER_READING_LIST_CAPTION);
        setPermission(new Permission(Permission.PERMISSION_WRITE));
    }

    @PostConstruct
    private void init() {
        entityStatusActionField.setBillingEnabled(false);
        status.getProperties().setDefault("0");

    }


    @Override
    public SharedEntity getSharedEntity() {
        return meterList.sharedEntity;
    }

    @Override
    public BaseIceUtilityHelper getIceUtilityHelper() {
        return null;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from pec_meter_reading_list where 1 = 1 order by READING_DATE desc";
    }

    @Override
    public void pushToDeviceEvent(String deviceId) {
        // update status
        status.set(PecMeterReadingListEntity.Status.Started.getCode());
        DataSourceDB.executeInTx(dataSource, connection -> {
            meterList.log(connection, "SEND TO DEVICE", deviceId, null);
        });
    }

    private boolean isNewDateOk(PecUtilityMeterReadingListEntity uList, LocalDateTime newDate) {
        return newDate.isAfter(uList.cycleStartDate.get().toLocalDateTime().truncatedTo(ChronoUnit.DAYS)) &&
                newDate.truncatedTo(ChronoUnit.DAYS).isBefore(LocalDateTime.now());
    }

    private void updateMeterReadingDate(Connection connection, PecUtilityMeterReadingListEntity uList) {
        final DateField dateField = new DateField("Date", new Date());
        dateField.setValue(readingDate.get());
        MessageBox.createQuestion().asModal(true).withCaption("Confirm reading date").withMessage(dateField).
                withButton(ButtonType.OK, () -> {
                    if (dateField.getValue() != null && isNewDateOk(uList, new Timestamp(dateField.getValue().getTime()).toLocalDateTime())) {
                        meterList.readingDate.set(new Timestamp(dateField.getValue().getTime())); // Set the new reading date
                        meterList.closureDate.set(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).minusHours(48))); // Explicitly expire the reading list
                        meterList.status.set(PecMeterReadingListEntity.Status.Completed.ordinal()); // Set the reading list status to completed
                        readingDate.intoControl();
                        try(Connection c = getDataSource().getConnection()) {
                            DataSourceDB.set(c, meterList);
                            UtilityMeterReadingSyncService.proceedWithCompleted(c, meterList,ToolkitUI.getToolkitUI().getUsername());
                        } catch(Exception ex) {
                            ex.printStackTrace();
                        }
                        Notification.show("Data send to utility requested", Notification.Type.TRAY_NOTIFICATION);
                    } else {
                        Notification.show("Select valid date (out of range). Cancelled", Notification.Type.ERROR_MESSAGE);
                    }
                }).
                withCancelButton(ButtonOption.closeOnClick(true)).open();
    }

    public void updateListSimulation() {
        DataSourceDB.executeInTx(dataSource, connection -> {
            PecUtilityMeterReadingListEntity uList = PecUtilityMeterReadingListEntity.ReadingListLinkEntity.getUtilityMeterReadingLists(
                    getDataSource(), meterList.meterReadingListId.get() ); // meterList.meterReadingListId.get()
            Timestamp toDate = Timestamp.valueOf(uList.getMaxReadingDate(connection).toLocalDateTime().truncatedTo(ChronoUnit.DAYS));
            uList.linkToSmartMeters(connection);
            for (PecMeterRegisterEntity register : uList.smartMeterRegisterList.get(connection)) {
                service.update(connection, register.meter.getOne(connection), register, uList.cycleStartDate.get(), toDate, tmzOffset, true);
            }
        });
    }

    public void performSimulation() {
        MessageBox.createQuestion().asModal(true).withCaption("Confirm billing simulation").withMessage("A billing simulation about to be performed. Continue?").
                withButton(ButtonType.OK, () -> {
                    updateListSimulation();
                    MessageBox.createInfo().withCaption("Billing Simulation").withMessage("Billing Simulation finished. Click refresh on Meter Reading Panel to see result.").withWidth("30em").open();
                }).
                withCancelButton(ButtonOption.closeOnClick(true)).open();
    }

    private void processBilling(Connection connection, PecUtilityMeterReadingListEntity uList) {
        if (!UtilityMeterReadingSyncService.proceedWithCompleted(connection, meterList,ToolkitUI.getToolkitUI().getUsername())) {
            MessageBox.createInfo().withCaption("Split Lists").
                    withMessage("Utility processing will proceed once all connected lists have been pushed").
                    withOkButton(ButtonOption.closeOnClick(true)).open();
        } else {
            Notification.show("Data send to utility requested", Notification.Type.TRAY_NOTIFICATION);
        }
    }

    @Override
    public void confirmForBilling(Connection connection, EntityDB... entities) {
        for (EntityDB entity : entities) {
            PecMeterReadingListEntity meterList = (PecMeterReadingListEntity) entity;
            PecUtilityMeterReadingListEntity uList = PecUtilityMeterReadingListEntity.ReadingListLinkEntity.getUtilityMeterReadingLists(
                    getDataSource(), meterList.meterReadingListId.get() );
                if(uList.onlySmartMeters(getDataSource())) {
                    // confirm the meter reading date that you want sent through
                    updateMeterReadingDate(connection, uList);
                } else {
                List<String> messages = UtilityMeterReadingSyncService.verifySplitLists(connection, meterList);
                final Runnable process = () -> {
                    DataSourceDB.executeInTx(getDataSource(), c -> {
                        UtilityMeterReadingSyncService.proceedWithCompleted(c, meterList, ToolkitUI.getToolkitUI().getUsername());
                        processBilling(c, uList);
                        intoControl();
                        DataSourceDB.loadFromId(c, meterList);
                        intoControl();
                        beforeOnScreenEvent();
                    });
                };
                if (messages.isEmpty()) {
                    process.run();
                } else {
                    messages.add("Are you sure you wish to continue");
                    MessageBox.createWarning().withCaption("Please confirm").withHtmlMessage(messages.stream().reduce((a, b) -> a + "<br>" + b).get()).
                            withYesButton(process, ButtonOption.closeOnClick(true)).withNoButton(ButtonOption.closeOnClick(true)).open();
                }
            }
        }
    }

    @SneakyThrows
    @Override
    protected void confirmForBilling() {
        DataSourceDB.executeInTx(getDataSource(), c -> confirmForBilling(c, meterList));
    }

    @Override
    public void intoBindings() {
        // reload the status field from the db
        super.intoBindings();
        status.set(((PecMeterReadingListEntity)DSDB.loadFromId(getDataSource(),meterList)).status.get());
    }

    public static boolean isInEditableState(PecMeterReadingListEntity reading) {
        return reading.sharedEntity.entityStatusCd.getNonNull().equals(ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING) &&
                (reading.status.getNonNull() < RequestClose.getCode() || reading.status.getNonNull() == Error.getCode());
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        entityStatusActionField.setTabletEnabled(isInEditableState(meterList) && meterList.status.getNonNull() != Error.getCode());

        entityStatusActionField.setBillingIcon(FontAwesome.DATABASE);
        entityStatusActionField.setBillingEnabled(entityStatusCdField.getNonNull().equals(ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING));
        simulateBilling.getProperties().setEnabled(meterList.status.get().equals(PecMeterReadingListEntity.Status.Received.getCode()));
        simulateBilling.getProperties().setEnabled(true);
    }

    public static class LogLayout extends MdmsLayout {

        @EntityRef(main = true)
        private PecMeterReadingListEntity.Log log = new PecMeterReadingListEntity.Log();

        @UIGroup(column = 0)
        public Group detailGroup = new Group("Log Detail", this);

        public LField event = new LField(log.event, "Event", this);

        @UIField(rows = 5, uppercase = false)
        public TextAreaField detail = new TextAreaField(log.detail, "Detail", this);

        @UIField(rows = 5, uppercase = false)
        public TextAreaField error = new TextAreaField(log.error, "Error", this);

        public LField logTime = new LField(log.logTime, "Log time", this);

        public Group nameGroup = new Group("Log Detail", this, event, logTime, detail).setNameGroup();

        public Pane detailPane = new Pane("Log detail", this, detailGroup);

        public LogLayout() {
            super("Log detail");
            setPermission(new Permission(0));
        }

    }


}
