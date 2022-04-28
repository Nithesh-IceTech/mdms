package za.co.spsi.toolkit.crud.entity.gui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import de.steinwedel.messagebox.MessageBox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.entity.DataImportEntity;
import za.co.spsi.toolkit.crud.entity.DataImportMapEntity;
import za.co.spsi.toolkit.crud.excel.DataSourceXls;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.FileField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by jaspervdb on 2016/04/19.
 */
@Qualifier(roles = {@Role(value = ToolkitCrudConstants.SYS_ADMIN)})
public abstract class DataImportLayout extends Layout<DataImportEntity> {

    public static final Logger TAG = Logger.getLogger(DataImportLayout.class.getName());
    @Inject
    private BeanManager beanManager;

    @EntityRef(main = true)
    public DataImportEntity dataImport = new DataImportEntity();

    public LField filename = new LField(dataImport.filename, ToolkitLocaleId.DATA_IMPORT_FILENAME, this);

    public Group detail = new Group(ToolkitLocaleId.DATA_IMPORT, this);


    @UIField(mandatory = true, writeOnce = true)
    public ComboBoxField<String> type = new ComboBoxField<String>(dataImport.type, ToolkitLocaleId.DATA_IMPORT_TYPE,
            new String[]{ToolkitLocaleId.DATA_IMPORT_TYPE_INSERT, ToolkitLocaleId.DATA_IMPORT_TYPE_UPDATE},
            new String[]{DataImportEntity.Type.Insert.name(), DataImportEntity.Type.Update.name()}, this);

    @UIField(mandatory = true, writeOnce = true)
    public LField description = new LField(dataImport.description, ToolkitLocaleId.DATA_IMPORT_DESCRIPTION, this);

    @UIField(writeOnce = true)
    public TextAreaField notes = new TextAreaField(dataImport.notes, ToolkitLocaleId.DATA_IMPORT_NOTES, this);

    public UidField username = new UidField(dataImport.username, ToolkitLocaleId.DATA_IMPORT_USERNAME, this);

    @UI(width = "-1px")
    @UIField(mandatory = true, writeOnce = true)
    public FileField file = new FileField(dataImport.fileData, filename, ToolkitLocaleId.DATA_IMPORT_FILENAME, this);

    @UIField(enabled = false)
    public LField<Timestamp> importTime = new LField<>(dataImport.importTime, ToolkitLocaleId.DATA_IMPORT_TIME, this);

    public ActionField process = new ActionField(ToolkitLocaleId.DATA_IMPORT_PROCESS, FontAwesome.CHECK, this, source -> process());

    public Group nameGroup = new Group("", this, type,description, filename, username).setNameGroup();

    public Pane detailPane = new Pane("", this, detail);

    public DataImportLayout() {
        super(ToolkitLocaleId.DATA_IMPORT);
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        process.getProperties().setEnabled(importTime.get() == null);
        file.getProperties().setEnabled(importTime.get() == null);
    }

    private void process() {
        if (save()) {
            if (processImport()) {
                importTime.set(new Timestamp(System.currentTimeMillis()));
                file.getProperties().setEnabled(false);
                save();
                intoControl();
            }
        }
    }

    private List<EntityDB> getAsList(EntityDB entity, InputStream is, String sheetName) {
        try {
            DataSourceXls xls = new DataSourceXls(entity);
            xls.init(is);
            return xls.getAllAsList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private EntityDB getEntityByExportName(List<EntityDB> entities, String name) {
        for (EntityDB entity : entities) {
            if (entity.getName().equalsIgnoreCase(name)) {
                return entity;
            }
        }
        throw new RuntimeException(String.format("Unable to locate Entity type by name %s", name));
    }

    private void linkIds(EntityDB entity, String oldValue, EntityDB dest) {
        for (Field field : dest.getForeignKeyFields(entity.getClass())) {
            if (field.getAsString().equals(oldValue)) {
                field.set(entity.getSingleId().getAsString());
            }
        }
        if (entity.getClass().equals(dest.getClass()) && oldValue.equals(dest.getSingleId().getAsString())) {
            dest.getSingleId().set(entity.getSingleId().get());
        }
    }

    private void linkIds(EntityDB entity, String oldValue, Map<EntityDB, List<EntityDB>> valueMap) {
        for (EntityDB key : valueMap.keySet()) {
            for (EntityDB dest : valueMap.get(key)) {
                linkIds(entity, oldValue, dest);
            }
        }
    }

    // assume that ID's are UID's, of length 36
    // assume that any ID of length shorter than 36, needs to be linked
    private void linkIds(List<EntityDB> entities, Map<EntityDB, List<EntityDB>> valueMap) {
        for (EntityDB entity : entities) {
            Assert.isTrue(entity.getId().size() == 1, "Only single Id entity's are supported %s", entity.getName());
            Id idCol = (Id) entity.getSingleId().getAnnotation(Id.class);
            Assert.isTrue(idCol != null && idCol.uuid(), "Id fields must be UID %s", entity.getName(), entity.getSingleId().toString());
            Assert.notNull(entity.getSingleId().get(), "Id field must be not be null: %s", entity.getName(), entity.getSingleId().toString());
            if (entity.getSingleId().getAsString().length() < 36) {
                String oldValue = entity.getSingleId().getAsString();
                entity.getSingleId().set(UUID.randomUUID().toString());
                linkIds(entity, oldValue, valueMap);
            }
        }
    }

    private void linkIds(Map<EntityDB, List<EntityDB>> valueMap) {
        for (EntityDB key : valueMap.keySet()) {
            linkIds(valueMap.get(key), valueMap);
        }
    }

    /**
     * verify that all Id's and FKey's UID's
     */
    private void verify(EntityDB entity, StringList report) {
        if (entity.getSingleId().getAsString().length() != 36) {
            report.add(String.format("Sheet %s. ID %s has not been linked", entity.getExportName(), entity.getSingleId().getAsString()));
        }
        for (Field field : entity.getFieldsWithAnnotation(ForeignKey.class)) {
            if (entity.getSingleId().getAsString().length() != 36) {
                report.add(String.format("Sheet %s. F-KEY %s has not been linked", entity.getExportName(), field.getName()));
            }
        }
    }

    private StringList verify(Map<EntityDB, List<EntityDB>> valueMap) {
        StringList report = new StringList();
        // there should be no more
        for (List<EntityDB> values : valueMap.values()) {
            for (EntityDB entity : values) {
                verify(entity, report);
            }
        }
        return report;
    }

    private void displayReport(StringList report) {
        TextArea textArea = new TextArea();
        textArea.setValue(report.toString("\n"));
        textArea.setReadOnly(true);
        MessageBox.createError().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.DATA_IMPORT_ERROR_REPORT))
                .withMessage(textArea).open();
    }

    private void processImportUpdate(Map<EntityDB, List<EntityDB>> valueMap, boolean insert, StringList report) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            // only insert entities that does not exist
            for (List<EntityDB> values : valueMap.values()) {
                for (EntityDB entity : values) {
                    EntityDB refEntity = DataSourceDB.loadFromId(connection, entity.clone());
                    if (refEntity == null && insert || refEntity != null && !insert) {
                        entity.setInDatabase(refEntity != null);
                        if (refEntity != null) {
                            entity.getFields().setSet();
                        }
                        DataSourceDB.set(connection, entity);
                        DataSourceDB.set(connection, new DataImportMapEntity(dataImport, entity));
                    } else if (insert) {
                        report.add(String.format("Record %s was not found in database",entity.getSingleId().toString()));
                    }
                }
            }
            if (report.isEmpty()) {
                connection.commit();
            } else {
                connection.rollback();
            }
        }
    }

    // assume its a multi sheet xls file
    private boolean processImport() {
        try {
            List<EntityDB> entities = Util.getInstances(EntityDB.class, true);
            POIFSFileSystem poiIs = new POIFSFileSystem(new ByteArrayInputStream(file.get()));
            HSSFWorkbook workBook = new HSSFWorkbook(poiIs);
            Map<EntityDB, List<EntityDB>> valueMap = new HashMap<>();
            for (int i = 0; i < workBook.getNumberOfSheets(); i++) {
                EntityDB entity = getEntityByExportName(entities, workBook.getSheetName(i));
                valueMap.put(entity, getAsList(entity, new ByteArrayInputStream(file.get()), workBook.getSheetName(i)));
            }
            linkIds(valueMap);
            StringList report = verify(valueMap);
            if (report.isEmpty()) {
                // proceed
                processImportUpdate(valueMap, type.get().equals(DataImportEntity.Type.Insert.name()), report);
            }
            if (!report.isEmpty()) {
                displayReport(report);
            }
            return report.isEmpty();
        } catch (Exception ex) {
            TAG.log(Level.WARNING, ex.getMessage(), ex);
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            return false;
        }
    }


    @Override
    public String getMainSql() {
        return "select * from DATA_IMPORT";
    }


}
