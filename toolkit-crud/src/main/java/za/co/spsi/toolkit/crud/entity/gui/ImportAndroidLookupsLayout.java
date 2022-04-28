package za.co.spsi.toolkit.crud.entity.gui;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.entity.ImportAndroidLookupsEntity;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.fields.FileField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.ejb.Asynchronous;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;


/**
 * Created by jaspervdb on 2016/04/19.
 */
@Qualifier(roles = {@Role(value = ToolkitCrudConstants.SYS_ADMIN)})
public abstract class ImportAndroidLookupsLayout extends Layout<ImportAndroidLookupsEntity> {

    public static final Logger TAG = Logger.getLogger(ImportAndroidLookupsLayout.class.getName());

    @Inject
    private BeanManager beanManager;

    @EntityRef(main = true)
    public ImportAndroidLookupsEntity importAndroidLookupsEntity = new ImportAndroidLookupsEntity();

    public LField filename = new LField(importAndroidLookupsEntity.filename, ToolkitLocaleId.DATA_IMPORT_FILENAME, this);

    public Group detail = new Group("Import Android Lookups", this);

    @UIField(writeOnce = true, mandatory = true)
    public LField<String> apkVersion = new LField<>(importAndroidLookupsEntity.apkVersion, "APK Version", this);

    @UIField(enabled = false)
    public LField<Integer> lookupVersion = new LField<>(importAndroidLookupsEntity.lookupVersion, "Lookup Version", this);

    @UIField(rows = 3)
    public TextAreaField notes = new TextAreaField(importAndroidLookupsEntity.notes, ToolkitLocaleId.DATA_IMPORT_NOTES, this);

    @UIField(enabled = false)
    public UidField userId = new UidField(importAndroidLookupsEntity.userId, "username", this);

    @UI(width = "-1px")
    @UIField(mandatory = true, writeOnce = true)
    public FileField file = new FileField(importAndroidLookupsEntity.fileData, filename, ToolkitLocaleId.DATA_IMPORT_FILENAME, this);

    @UIField(enabled = false)
    public LField<Timestamp> importTime = new LField<>(importAndroidLookupsEntity.createT, ToolkitLocaleId.DATA_IMPORT_TIME, this);

    public Group nameGroup = new Group("", this, apkVersion, filename, lookupVersion, importTime, userId).setNameGroup();

    public Pane detailPane = new Pane("", this, detail);

    public ImportAndroidLookupsLayout() {
        super("Import Android Lookups");
        getPermission().setMayCreate(true);
    }

    @Override
    public boolean save() {
        importTime.set(new Timestamp(System.currentTimeMillis()));
        return super.save();
    }

    @Asynchronous
    public void processQuery() {
        List<List> set =
                DataSourceDB.executeQuery(getDataSource(),
                        "select max(IMPORT_ANDROID_LOOKUPS.LOOKUP_VERSION) from IMPORT_ANDROID_LOOKUPS " +
                                "where IMPORT_ANDROID_LOOKUPS.APK_VERSION = ?", apkVersion.getVaadinField().getValue());
        lookupVersion.set(set.get(0).get(0)!=null?Integer.parseInt(set.get(0).get(0).toString())+1:1);
    }

    @Override
    public void newEvent() {
        ValueChangeListener query = (srcField, field, inConstruction, valueIsNull) -> {
            if (!inConstruction) {
                processQuery();
            }
        };
        apkVersion.addValueChangeListener(query);
        super.newEvent();
    }

    @Override
    public String getMainSql() {
        return "select * from Import_Android_Lookups";
    }
}
