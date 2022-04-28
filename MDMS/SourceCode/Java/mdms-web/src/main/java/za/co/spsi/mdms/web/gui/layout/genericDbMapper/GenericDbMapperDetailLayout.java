package za.co.spsi.mdms.web.gui.layout.genericDbMapper;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.J2EEConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.ButtonType;
import de.steinwedel.messagebox.MessageBox;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.common.services.ReadingGenericGapIdentifier;
import za.co.spsi.mdms.generic.meter.db.DbToDbMappingDetailEntity;
import za.co.spsi.mdms.generic.meter.db.DbToDbMappingEntity;
import za.co.spsi.mdms.util.DBToDBUtil;
import za.co.spsi.mdms.util.DBUtil;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.fields.DynamicLookupField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.util.StringUtils;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

@Qualifier(roles = {@Role(value = "Supervisor")})
public class GenericDbMapperDetailLayout extends Layout<DbToDbMappingEntity> implements ActionField.Callback, DynamicLookupField.Callback {

    public static final Logger TAG = Logger.getLogger(GenericDbMapperDetailLayout.class.getName());

    @Inject
    private PrepaidMeterFilterService prepaidFilter;

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    private ReadingGenericGapIdentifier gapIdentifier;


    @EntityRef(main = true)
    private DbToDbMappingDetailEntity dbToDbMappingDetailEntity = new DbToDbMappingDetailEntity();
    private DbToDbMappingEntity dbToDbMappingEntity = new DbToDbMappingEntity();

    @UIGroup(column = 0)
    public Group statusDetail = new Group("Status Detail", this);

    @UIField()
    public ActionField deleteData = new ActionField("Delete Imported Data", FontAwesome.WRENCH, this, this);

    @UIField()
    public ActionField importDataReq = new ActionField("Import Data", FontAwesome.WRENCH, this, this);

    @UIField()
    public ActionField viewImportedData = new ActionField("View Imported Data", FontAwesome.WRENCH, this, this);

    @UIGroup(column = 0)
    public Group db2DbMapping = new Group("DB-to-DB Mapping Detail", this);

    @UIField(mandatory = true)
    public LField mapName = new LField(dbToDbMappingDetailEntity.mapName, "Map Detail Name", this);

    @UIGroup(column = 1)
    public Group sqlDetail = new Group("SQL Query", this);

    @UIField(mandatory = true, rows = 5, enabled = false)
    public TextAreaField sqlSelect = new TextAreaField(dbToDbMappingDetailEntity.sqlSelect, "SQL Select Columns", this);

    @UIField(mandatory = true, rows = 5, enabled = false)
    public TextAreaField sqlFrom = new TextAreaField(dbToDbMappingDetailEntity.sqlFrom, "SQL Select From", this);

    @UIField(mandatory = false, rows = 5, enabled = false)
    public TextAreaField sqlWhere = new TextAreaField(dbToDbMappingDetailEntity.sqlWhere, "SQL Select Where", this);

    @UIField()
    public ActionField testSql = new ActionField("Test Sql", FontAwesome.WRENCH, this, this);

    @UIField(enabled = false, rows = 5, uppercase = false)
    public TextAreaField columns = new TextAreaField(dbToDbMappingDetailEntity.columnFields, "Columns", this);

    @UIGroup(column = 1)
    public Group energyGroup = new Group("Energy Group", this);

    @UIField()
    public DynamicLookupField importKWH = new DynamicLookupField(dbToDbMappingDetailEntity.importKWH, "Import kWh", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> importKWHScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.importKWHScalingFactor, "Import kWh Scaling Factor", this);

    @UIField()
    public DynamicLookupField exportKWH = new DynamicLookupField(dbToDbMappingDetailEntity.exportKWH, "Export kWh", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> exportKWHScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.exportKWHScalingFactor, "Export kWh Scaling Factor", this);

    @UIField()
    public DynamicLookupField inductiveKvarh = new DynamicLookupField(dbToDbMappingDetailEntity.inductiveKVARH, "Inductive kvarh", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> inductiveKvarhScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.inductiveKVARHScalingFactor, "Inductive kvar Scaling Factor", this);

    @UIField()
    public DynamicLookupField capacitiveKvarh = new DynamicLookupField(dbToDbMappingDetailEntity.capacitiveKVARH, "Capacitive kvarh", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> capacitiveKvarhScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.capacitiveKVARHScalingFactor, "Capacative kvar Scaling Factor", this);

    @UIField()
    public DynamicLookupField voltageL1 = new DynamicLookupField(dbToDbMappingDetailEntity.voltageL1, "Voltage L1", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> voltageL1ScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.voltageL1ScalingFactor, "Voltage L1 Scaling Factor", this);

    @UIField()
    public DynamicLookupField voltageL2 = new DynamicLookupField(dbToDbMappingDetailEntity.voltageL2, "Voltage L2", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> voltageL2ScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.voltageL2ScalingFactor, "Voltage L2 Scaling Factor", this);

    @UIField()
    public DynamicLookupField voltageL3 = new DynamicLookupField(dbToDbMappingDetailEntity.voltageL3, "Voltage L3", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> voltageL3ScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.voltageL3ScalingFactor, "Voltage L3 Scaling Factor", this);

    @UIField()
    public DynamicLookupField currentL1 = new DynamicLookupField(dbToDbMappingDetailEntity.currentL1, "Current L1", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> currentL1ScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.currentL1ScalingFactor, "Current L1 Scaling Factor", this);

    @UIField()
    public DynamicLookupField currentL2 = new DynamicLookupField(dbToDbMappingDetailEntity.currentL2, "Current L2", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> currentL2ScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.currentL2ScalingFactor, "Current L2 Scaling Factor", this);

    @UIField()
    public DynamicLookupField currentL3 = new DynamicLookupField(dbToDbMappingDetailEntity.currentL3, "Current L3", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> currentL3ScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.currentL3ScalingFactor, "Current L3 Scaling Factor", this);

    @UIGroup(column = 0)
    public Group volumeGroup = new Group("Volume Group", this);

    @UIField()
    public DynamicLookupField volumeWater = new DynamicLookupField(dbToDbMappingDetailEntity.volumeWater, "Volume Water m^3", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> volumeWaterScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.volumeWaterScalingFactor, "Volume Water m^3 Scaling Factor", this);

    @UIField()
    public DynamicLookupField volumeGas = new DynamicLookupField(dbToDbMappingDetailEntity.volumeGas, "Volume Gas m^3", this, this);

    @UIField(setDefault = "1", format = "#,##0.000000")
    public LField<Double> volumeGasScalingFactor = new LField<Double>(dbToDbMappingDetailEntity.volumeGasScalingFactor, "Volume Gas m^3 Scaling Factor", this);

    @UIGroup(column = 0)
    public Group dataPreviewGroup = new Group("Data Preview", this);

    @UIField()
    public ActionField mappedDataPreview = new ActionField("Data Preview", FontAwesome.WRENCH, this, this);

    public Group nameGroup = new Group("", this, mapName).setNameGroup();

    public Pane detailPane = new Pane("", this, statusDetail, sqlDetail, db2DbMapping, energyGroup, volumeGroup, dataPreviewGroup);

    public GenericDbMapperDetailLayout() {
        super("Generic Connection Detail");
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void callback(ActionField source) {
        if (source == testSql) {
            runSqlQuery();
        } else if (source == mappedDataPreview) {
            mappedDataPreview();
        } else if (source == deleteData) {
            deleteData();
        } else if (source == importDataReq) {
            importData();
        } else if (source == viewImportedData) {
            viewImportedData();
        }
    }

    public void viewImportedData() {

        try {
            FreeformQuery tq = new FreeformQuery(buildQueryMappedData(), null,
                    new J2EEConnectionPool(dataSource));

            SQLContainer container = new SQLContainer(tq);
            Grid grid = new Grid();
            grid.setReadOnly(true);
            grid.setContainerDataSource(container);
            grid.setSizeFull();
            Util.showInWindow("Table data", grid, "80%", "80%");
        } catch (Exception ex) {
            TAG.severe(ex.getMessage());
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }

    }

    public void importData() {

        /**
         *Cant import data if mapping has been marked as live
         */
        if (dbToDbMappingEntity.live.get()) {
            Notification.show("Live status has been set, data can not be imported", Notification.Type.ERROR_MESSAGE);
            return;
        }

        /**
         * Cant import data if mapping has been marked as active
         */
        if (dbToDbMappingEntity.active.get()) {
            Notification.show("Active status has been set, data can not be imported", Notification.Type.ERROR_MESSAGE);
            return;
        }

        for (DbToDbMappingDetailEntity dbToDbMappingDetailEntity : dbToDbMappingEntity.dbToDbMappingDetailEntityEntityRef.getAllAsList(dataSource)) {
            if (dbToDbMappingDetailEntity.batchImportRunning.getNonNull()) {
                Notification.show("A batch import is currently running, data can not be imported", Notification.Type.ERROR_MESSAGE);
                return;
            }
        }

        MessageBox.createQuestion().asModal(true).withCaption("Import Data").
                withMessage("Importing data. Continue?").
                withButton(ButtonType.OK, () -> {
                    try {

                        dbToDbMappingDetailEntity.importRunning.set(true);
                        DSDB.setUpdate(dataSource, dbToDbMappingDetailEntity);

                        String sql = DBToDBUtil.buildMappingSql(dbToDbMappingEntity,
                                dbToDbMappingDetailEntity, true);

                        DBToDBUtil.persistMapping(gapIdentifier,sql,
                                DBUtil.createDataSource(
                                        dbToDbMappingEntity.driver.get(),
                                        dbToDbMappingEntity.serverAddress.get(),
                                        dbToDbMappingEntity.serviceName.get(),
                                        dbToDbMappingEntity.portNumber.get(),
                                        dbToDbMappingEntity.dbName.get(),
                                        dbToDbMappingEntity.userName.get(),
                                        dbToDbMappingEntity.password.get()
                                ), dataSource, dbToDbMappingEntity, dbToDbMappingDetailEntity, prepaidFilter);

                        Notification.show("Data Import", "Requested data was imported", Notification.Type.TRAY_NOTIFICATION);
                    } catch (Exception e) {
                        VaadinNotification.show("Connection failed", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    } finally {
                        dbToDbMappingDetailEntity.importRunning.set(false);
                        try {
                            DSDB.setUpdate(dataSource, dbToDbMappingDetailEntity);
                        } catch (SQLException e) {
                            TAG.severe(e.getMessage());
                        }
                    }
                }).
                withCancelButton(ButtonOption.closeOnClick(true)).open();
    }

    public void deleteData() {

        /**
         *
         * Cant delete data if mapping has been marked as live
         */
        if (dbToDbMappingEntity.live.get()) {
            Notification.show("Live status has been set, data can not be deleted", Notification.Type.ERROR_MESSAGE);
            return;
        }

        /**
         *
         * Cant delete data if mapping has been marked as live
         */
        if (dbToDbMappingEntity.active.get()) {
            Notification.show("Live status has been set, data can not be deleted", Notification.Type.ERROR_MESSAGE);
            return;
        }

        MessageBox.createQuestion().asModal(true).withCaption("Delete Data").
                withMessage("Data will be deleted. Continue?").
                withButton(ButtonType.OK, () -> {
                    try {
                        deleteDataForMapping(dataSource, dbToDbMappingDetailEntity);
                        dbToDbMappingDetailEntity.lastSyncTime.set(new Timestamp(0));
                        DSDB.setUpdate(dataSource, dbToDbMappingDetailEntity);
                        Notification.show("Data deleted", "Requested data was deleted", Notification.Type.TRAY_NOTIFICATION);
                    } catch (Exception e) {
                        VaadinNotification.show("Connection failed", e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    }
                }).withCancelButton(ButtonOption.closeOnClick(true)).open();

    }

    public void mappedDataPreview() {

        try {
            FreeformQuery tq = new FreeformQuery(buildMappingPreviewQuery(), null,
                    new J2EEConnectionPool(DBUtil.createDataSource(
                            dbToDbMappingEntity.driver.get(),
                            dbToDbMappingEntity.serverAddress.get(),
                            dbToDbMappingEntity.serviceName.get(),
                            dbToDbMappingEntity.portNumber.get(),
                            dbToDbMappingEntity.dbName.get(),
                            dbToDbMappingEntity.userName.get(),
                            dbToDbMappingEntity.password.get())));

            SQLContainer container = new SQLContainer(tq);
            Grid grid = new Grid();
            grid.setReadOnly(true);
            grid.setContainerDataSource(container);
            grid.setSizeFull();
            Util.showInWindow("Preview data", grid, "80%", "80%");
        } catch (Exception ex) {
            TAG.severe(ex.getMessage());
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private String buildQueryMappedData() {
        Driver driver = DriverFactory.getDriver();
        String query = String.format(
                "select * " +
                        " from (" +
                        "SELECT\n" +
                        "\n" +
                        "GENERIC_METER.GENERIC_METER_ID," +
                        "GENERIC_METER.METER_ID," +
                        "GENERIC_METER.METER_SERIAL_N," +
                        "GENERIC_METER.METER_READING_ID as GEN_METER_READING_ID," +
                        "GENERIC_METER.METER_MAN_ID," +
                        "GENERIC_METER.METER_TYPE," +
                        "METER_READING.METER_READING_ID," +
                        "METER_READING.ENTRY_TIME %s as ENTRY_TIME," +
                        "METER_READING.TOTAL_KWHP," +
                        "METER_READING.TOTAL_KWHN," +
                        "METER_READING.TOTAL_KVARP," +
                        "METER_READING.TOTAL_KVARN," +
                        "METER_READING.T1_KWHP," +
                        "METER_READING.T1_KWHN," +
                        "METER_READING.T1_KVARP," +
                        "METER_READING.T1_KVARN," +
                        "METER_READING.T2_KWHP," +
                        "METER_READING.T2_KWHN," +
                        "METER_READING.T2_KVARP," +
                        "METER_READING.T2_KVARN," +
                        "METER_READING.TOTAL_MAX_DEMAND_PO_KVA," +
                        "METER_READING.TOTAL_MAX_DEMAND_PO_RTC," +
                        "METER_READING.TOTAL_MAX_DEMAND_PR_KVA," +
                        "METER_READING.TOTAL_MAX_DEMAND_PR_RTC," +
                        "METER_READING.RMS_L1_V," +
                        "METER_READING.RMS_L2_V," +
                        "METER_READING.RMS_L3_V," +
                        "METER_READING.RMS_L1_C," +
                        "METER_READING.RMS_L2_C," +
                        "METER_READING.RMS_L3_C," +
                        "METER_READING.ALARM," +
                        "METER_READING.PF," +
                        "METER_READING.ENTRY_DAY," +
                        "METER_READING.VOLUME_1 " +
                        " " +
                        "from " +
                        "   GENERIC_METER,METER_READING,DB_TO_DB_MAP_METER " +
                        "where " +
                        "   DB_TO_DB_MAP_METER.DB_TO_DB_MAPPING_DETAIL_ID = '%s'" +
                        "   and DB_TO_DB_MAP_METER.METER_ID = GENERIC_METER.GENERIC_METER_ID" +
                        "   and GENERIC_METER.GENERIC_METER_ID = METER_READING.GENERIC_METER_ID " +
                        "order by METER_SERIAL_N, ENTRY_TIME desc)",
                driver.addTimezoneOffset(),
                dbToDbMappingDetailEntity.dbToDbMappingDetailId.get());
        query = driver.limitSql(query, 500);
        return query;

    }

    private String buildMappingPreviewQuery() {

        StringBuilder columns = new StringBuilder();

        if (dbToDbMappingEntity.meterId.get() != null) {
            columns.append(dbToDbMappingEntity.meterId.get() + " as " + dbToDbMappingEntity.meterId.getColumnName()).append(",");
        }

        if (dbToDbMappingEntity.meterReadingId.get() != null) {
            columns.append(dbToDbMappingEntity.meterReadingId.get() + " as " + dbToDbMappingEntity.meterReadingId.getColumnName()).append(",");
        }

        if (dbToDbMappingEntity.meterSerialN.get() != null) {
            columns.append(dbToDbMappingEntity.meterSerialN.get() + " as " + dbToDbMappingEntity.meterSerialN.getColumnName()).append(",");
        }

        if (dbToDbMappingEntity.timestamp.get() != null) {
            columns.append(dbToDbMappingEntity.timestamp.get() + " as " + dbToDbMappingEntity.timestamp.getColumnName()).append(",");
        }

        if (dbToDbMappingEntity.meterManId.get() != null) {
            columns.append(dbToDbMappingEntity.meterManId.get() + " as " + dbToDbMappingEntity.meterManId.getColumnName()).append(",");
        }

        for (LField lField : energyGroup.getFields()) {

            if (!lField.getName().contains("Scaling") &&
                    lField.getVaadinField().getValue() != null && !StringUtils.isEmpty(lField.getVaadinField().getValue().toString())) {

                columns.append(",");
                columns.append(lField.getVaadinField().getValue().toString() + " as " + lField.getColName());
            }
        }

        for (LField lField : volumeGroup.getFields()) {

            if (!lField.getName().contains("Scaling") &&
                    lField.getVaadinField().getValue() != null && !StringUtils.isEmpty(lField.getVaadinField().getValue().toString())) {

                columns.append(",");
                columns.append(lField.getVaadinField().getValue().toString() + " as " + lField.getColName());
            }
        }

        String mergeColumns = DBUtil.trimSql(dbToDbMappingEntity.sqlSelect.get()) + "," + DBUtil.trimSql(sqlSelect.getVaadinField().getValue().toString());
        String[] cols = mergeColumns.split(",");
        ArrayList<String> colList = new ArrayList<>();
        for (String item : cols) {
            if (!colList.contains(item)) {
                colList.add(item);
            }
        }

        mergeColumns = String.join(",", colList);

        String mergeFrom = DBUtil.trimSql(dbToDbMappingEntity.sqlFrom.get()) + "," + DBUtil.trimSql(sqlFrom.getVaadinField().getValue().toString());
        String[] from = mergeFrom.split(",");
        ArrayList<String> fromList = new ArrayList<>();
        for (String item : from) {
            if (!fromList.contains(item)) {
                fromList.add(item);
            }
        }

        mergeFrom = String.join(",", fromList);

        return DBUtil.returnFirstRows(
                dbToDbMappingEntity.driver.get(),
                "select " + mergeColumns + " from " + mergeFrom + " where " + sqlWhere.getVaadinField().getValue().toString() +
                        " order by " + dbToDbMappingEntity.timestamp.get(), 100);
    }


    private void runSqlQuery() {

        try {
            DataSource dataSource = DBUtil.createDataSource(
                    dbToDbMappingEntity.driver.get(),
                    dbToDbMappingEntity.serverAddress.get(),
                    dbToDbMappingEntity.serviceName.get(),
                    dbToDbMappingEntity.portNumber.get(),
                    dbToDbMappingEntity.dbName.get(),
                    dbToDbMappingEntity.userName.get(),
                    dbToDbMappingEntity.password.get());

            try( Connection connection = dataSource.getConnection() ) {

                String query = DBUtil.returnFirstRows(dbToDbMappingEntity.driver.get(),
                        "select " + sqlSelect.getVaadinField().getValue().toString() + " from " +
                                sqlFrom.getVaadinField().getValue().toString() + " where " + sqlWhere.getVaadinField().getValue().toString(),
                        1);

                try (Statement stmt = connection.createStatement() ) {

                    ResultSet rs = stmt.executeQuery(query);

                    connection.close();
                    columns.set(DBUtil.trimSql(sqlSelect.getVaadinField().getValue().toString()));
                    columns.intoControl();
                    populateLookupFields();

                } catch (SQLException e) {
                    throw e;
                }

            }

        } catch (Exception e) {
            VaadinNotification.show("Connection failed", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }


    @Override
    public Map<String, String> getLookupCation() {
        Map<String, String> returnedMap = new TreeMap<>();

        if (!StringUtils.isEmpty(columns.get())) {
            List<String> columnsList = new ArrayList<String>(Arrays.asList(columns.getNonNull().split(",")));
            for (String value : columnsList) {
                returnedMap.put(value, value);
            }
        }
        return returnedMap;
    }

    private void populateLookupFields() {

        importKWH.populateLookupFieldComboBox();
        importKWH.intoControl();

        exportKWH.populateLookupFieldComboBox();
        exportKWH.intoControl();

        inductiveKvarh.populateLookupFieldComboBox();
        inductiveKvarh.intoControl();

        capacitiveKvarh.populateLookupFieldComboBox();
        capacitiveKvarh.intoControl();

        voltageL1.intoControl();
        voltageL1.populateLookupFieldComboBox();

        voltageL2.populateLookupFieldComboBox();
        voltageL2.intoControl();

        voltageL3.populateLookupFieldComboBox();
        voltageL3.intoControl();

        currentL1.populateLookupFieldComboBox();
        currentL1.intoControl();

        currentL2.populateLookupFieldComboBox();
        currentL2.intoControl();

        currentL3.populateLookupFieldComboBox();
        currentL3.intoControl();

        volumeWater.populateLookupFieldComboBox();
        volumeWater.intoControl();

        volumeGas.populateLookupFieldComboBox();
        volumeGas.intoControl();
    }

    @Override
    public boolean save() {
        if (!dbToDbMappingEntity.active.get() && dbToDbMappingEntity.live.get()) {

            VaadinNotification.show("Save failed",
                    "Cant set live status to true if active is not set to true.",
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }

        if (super.save()) {
            if (dbToDbMappingEntity.active.get()) {
                setReadonlyFields();
            }
            return true;
        } else {
            return false;
        }
    }

    private void setReadonlyFields() {

        sqlDetail.getFields().forEach(field -> {
            field.getProperties().setReadOnly(true);
            field.applyProperties();
        });

        energyGroup.getFields().forEach(field -> {
            field.getProperties().setReadOnly(true);
            field.applyProperties();
        });

        db2DbMapping.getFields().forEach(field -> {
            field.getProperties().setReadOnly(true);
            field.applyProperties();
        });

        volumeGroup.getFields().forEach(field -> {
            field.getProperties().setReadOnly(true);
            field.applyProperties();
        });
    }

    private void setNotReadonlyFields() {

        sqlDetail.getFields().forEach(field -> {
            field.getProperties().setReadOnly(false);
            field.applyProperties();
        });

        energyGroup.getFields().forEach(field -> {
            field.getProperties().setReadOnly(false);
            field.applyProperties();
        });

        db2DbMapping.getFields().forEach(field -> {
            field.getProperties().setReadOnly(false);
            field.applyProperties();
        });

        volumeGroup.getFields().forEach(field -> {
            field.getProperties().setReadOnly(false);
            field.applyProperties();
        });
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();

        // Select the DbToDbMappingEntity
        DbToDbMappingEntity dbToDbMappingEntityTmp =
                DataSourceDB.get(DbToDbMappingEntity.class, dataSource,
                        "select * from DB_TO_DB_MAPPING where DB_TO_DB_MAPPING_ID = ?", dbToDbMappingDetailEntity.dbToDbMappingId.get());

        if (dbToDbMappingEntityTmp == null) {
            throw new RuntimeException("dbToDbMappingEntity not selected");
        }

        dbToDbMappingEntity.copyStrict(dbToDbMappingEntityTmp);

        if (dbToDbMappingEntity.active.getNonNull()) {
            setReadonlyFields();
        } else {
            setNotReadonlyFields();
        }

        columns.getProperties().setReadOnly(true);
        columns.applyProperties();

    }

    @Override
    public boolean delete(EntityDB entityDB) {

        DbToDbMappingDetailEntity dbToDbMappingDetailEntity = (DbToDbMappingDetailEntity) entityDB;

        DbToDbMappingEntity dbToDbMappingEntity =
                DSDB.getFromSet(dataSource, new DbToDbMappingEntity().dbToDbMappingId.set(dbToDbMappingDetailEntity.dbToDbMappingId.get()));

        if (dbToDbMappingEntity == null) {
            throw new RuntimeException("dbToDbMappingEntity not found");
        }

        if (dbToDbMappingEntity.active.get() || dbToDbMappingEntity.live.get()) {

            VaadinNotification.show("Delete failed",
                    "Cant delete record thats been set as active or live.",
                    Notification.Type.ERROR_MESSAGE);
            return false;
        } else {

            deleteDataForMapping(dataSource, dbToDbMappingDetailEntity);
            DataSourceDB.delete(getDataSource(), entityDB);
            // refresh
            layoutViewGrid.getSqlContainer().refresh();
            VaadinNotification.show(String.format(getLocaleValue(ToolkitLocaleId.ENTITY_DELETED), getCaption()),
                    Notification.Type.TRAY_NOTIFICATION);
            return true;
        }
    }

    public static void deleteDataForMapping(DataSource dataSource, DbToDbMappingDetailEntity dbToDbMappingDetailEntity) {
        try (Connection localConnection = dataSource.getConnection()) {
            localConnection.setAutoCommit(false);
            try {

                DataSourceDB.execute(localConnection,
                        String.format("delete from GENERIC_METER " +
                                        " where GENERIC_METER_ID in (select DB_TO_DB_MAP_METER.METER_ID from DB_TO_DB_MAP_METER" +
                                        "   where DB_TO_DB_MAPPING_DETAIL_ID = '%s')" +
                                        " and GENERIC_METER_ID NOT in(select DB_TO_DB_MAP_METER.METER_ID from DB_TO_DB_MAP_METER " +
                                        "   where DB_TO_DB_MAPPING_DETAIL_ID != '%s')",
                                dbToDbMappingDetailEntity.dbToDbMappingDetailId.get(), dbToDbMappingDetailEntity.dbToDbMappingDetailId.get()));

                DataSourceDB.execute(localConnection,
                        String.format("delete from DB_TO_DB_MAP_METER " +
                                " where DB_TO_DB_MAPPING_DETAIL_ID = '%s'", dbToDbMappingDetailEntity.dbToDbMappingDetailId.get()));

                localConnection.commit();
            } catch (Exception e) {
                localConnection.rollback();
                throw new RuntimeException(e);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
