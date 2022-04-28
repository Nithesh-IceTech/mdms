package za.co.spsi.toolkit.crud.entity.gui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.entity.DataExportEntity;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.InvalidFieldValueException;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.StringList;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by jaspervdb on 2016/04/19.
 */
@Qualifier(roles = {@Role(value = ToolkitCrudConstants.SYS_ADMIN)})
public abstract class DataExportLayout extends Layout<DataExportEntity> {

    @Inject
    private BeanManager beanManager;

    @EntityRef(main = true)
    public DataExportEntity dataExport = new DataExportEntity();

    public Group detail = new Group(ToolkitLocaleId.DATA_EXPORT, this);

    @UIField(mandatory = true)
    public TypeField viewName = new TypeField(dataExport.viewName, ToolkitLocaleId.DATA_EXPORT_VIEW_NAME, this);
    @UIField(mandatory = true)
    public LField exportName = new LField(dataExport.exportName, ToolkitLocaleId.DATA_EXPORT_EXPORT_NAME, this);
    public LField exportDescription = new LField(dataExport.exportDescription, ToolkitLocaleId.DATA_EXPORT_DESCRIPTION, this);
    @UIField(mandatory = true, rows = 5)
    public TextAreaField exportsQL = new TextAreaField(dataExport.exportsQL, ToolkitLocaleId.DATA_EXPORT_SQL, this);

    public ActionField validate = new ActionField(ToolkitLocaleId.DATA_EXPORT_VALIDATE, FontAwesome.CHECK, this, source -> validate());

    public Group nameGroup = new Group("", this, viewName, exportName, exportDescription).setNameGroup();

    public Pane detailPane = new Pane("", this, detail);

    public DataExportLayout() {
        super(ToolkitLocaleId.DATA_EXPORT);
        getPermission().setMayCreate(true);
    }

    private String formatSql(String sql) {
        if (sql.indexOf("{") != -1 && sql.indexOf("}") != -1) {
            sql = sql.substring(0, sql.indexOf("{")) + "'000'" + sql.substring(sql.indexOf("}") + 1);
        }
        return sql.indexOf("{") == -1 ? sql : formatSql(sql);
    }

    private void validate() {
        try {
            intoBindings();
            // check the sql
            try (Connection connection = getDataSource().getConnection()) {
                try (Statement smt = connection.createStatement()) {
                    smt.executeQuery(formatSql(exportsQL.get()));
                }
            }
            VaadinNotification.show("Valid", Notification.Type.TRAY_NOTIFICATION);
        } catch (InvalidFieldValueException e) {
        } catch (SQLException e) {
            VaadinNotification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public String getMainSql() {
        return "select * from DATA_EXPORT";
    }


    public static class TypeField extends ComboBoxField<String> {

        public TypeField(Field field, String captionId, Layout layout) {
            super(field, captionId, null, null, layout);
            init();
        }

        private void init() {
            StringList values = new StringList();
            Util.getNonAbstractSubTypesOf(Layout.class).stream().forEach(c -> {
                values.add(c.getSimpleName());
                values.add(c.getSimpleName() + ".Detail");
            });
            setValues(values.toStringArray());
            setOptions(values.toStringArray());
        }
    }
}
