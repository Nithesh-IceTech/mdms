package za.co.spsi.toolkit.crud.gui.query;

import com.vaadin.data.Validator;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import lombok.SneakyThrows;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.entity.DataExportEntity;
import za.co.spsi.toolkit.crud.excel.DataSourceXls;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.util.DownloaderHelper;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.FormattedSql;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static za.co.spsi.toolkit.crud.util.Util.getComponentParent;
import static za.co.spsi.toolkit.db.EntityDB.getColumnName;

public class EntityDateRangeFilter extends FormLayout {

    private ComboBox filterField = new ComboBox(AbstractView.getLocaleValue(ToolkitLocaleId.FILTER_FIELD_FIELD));
    private DateField dateFrom = new DateField(AbstractView.getLocaleValue(ToolkitLocaleId.FILTER_FIELD_DATE_FROM));
    private DateField dateTo = new DateField(AbstractView.getLocaleValue(ToolkitLocaleId.FILTER_FIELD_DATE_TO));
    private Button excelExport = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.DATA_EXPORT));
    private CheckBox localize = new CheckBox(AbstractView.getLocaleValue(ToolkitLocaleId.FILTER_FIELD_LOCALE));
    private ComboBox dataOrView = new ComboBox(AbstractView.getLocaleValue(ToolkitLocaleId.DATA_EXPORT_TYPE), Arrays.asList(
            AbstractView.getLocaleValue(ToolkitLocaleId.DATA_EXPORT_VIEW), AbstractView.getLocaleValue(ToolkitLocaleId.DATA_EXPORT_DATA), "Data"));

    private Layout layout;

    public EntityDateRangeFilter(Layout layout) {
        this.layout = layout;
        excelExport.setDisableOnClick(true);
        excelExport.setImmediate(true);
        dataOrView.setNullSelectionAllowed(false);
        dataOrView.setValue(AbstractView.getLocaleValue(ToolkitLocaleId.DATA_EXPORT_VIEW));
        filterField.setNullSelectionAllowed(false);
        layout.getFields().stream().filter(f -> Date.class.isAssignableFrom(f.getFieldType())).forEach(f -> {
            filterField.addItem(f.getName());
            filterField.setItemCaption(f.getName(), f.getCaption());
            filterField.setValue(layout.getExportFilterField() == null ? f.getName()
                    : f.getName().equalsIgnoreCase(layout.getExportFilterField()) ? f.getName() : null);
        });
        dateFrom.setValue(Date.from(LocalDateTime.now().minusWeeks(1).atZone(ZoneId.systemDefault()).toInstant()));
        dateTo.setValue(new Date());
        // set the defaults
        addComponents(filterField, dateFrom, dateTo, dataOrView, localize, excelExport);
        Arrays.asList(filterField, dateFrom, dateTo).stream().forEach(c -> c.setRequired(true));
        if (filterField.getItemIds().isEmpty()) {
            Arrays.asList(filterField, dateFrom, dateTo).stream().forEach(c -> c.setEnabled(false));
        }
        setSizeUndefined();

        localize.setValue(true);
        initBtn();
    }

    public void reset() {
        excelExport.setEnabled(true);
        closeWindow();
    }

    private boolean filter() {
        return !filterField.getItemIds().isEmpty();
    }

    private boolean validate() {
        try {
            if (filter()) {
                Arrays.asList(filterField, dateFrom, dateTo).stream().forEach(c -> c.validate());
            }
            return true;
        } catch (Validator.InvalidValueException ie) {
            Util.showError(ToolkitLocaleId.FILTER_FIELD_COMPLETE_ALL_FIELDS, ToolkitLocaleId.FILTER_FIELD_COMPLETE_ALL_PLS_FIELDS);
            return false;
        }
    }

    private File exportData(FormattedSql sql) throws Exception {
        sql.setSelect(EntityDB.getColumnNames(layout.getMainEntity().getId()).toString(","));
        EntityDB entity = layout.getMainEntity().clone();
        File file = File.createTempFile("export", ".xls");
        file.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            try (DataSourceXls xls = new DataSourceXls(entity)) {
                xls.init(fos);
                xls.writeHeaders();
                try (Connection connection = layout.getDataSource().getConnection()) {
                    DataSourceDB.executeQuery(connection, result -> {
                        DataSourceDB.loadFromId(connection, (EntityDB) entity.getSingleId().set(result.get(0)));
                        xls.set(localize(entity));
                    }, new Class[]{String.class}, sql.toString(), getParams());
                }
            }
        }
        return file;
    }

    private File exportView(FormattedSql sql) throws Exception {
        if (filter()) {
            return DataExportEntity.export(layout.getDataSource(),
                    layout.getMainEntity().getName() + layout.getExportSheetName() + ".xls",
                    sql.toString(), new Timestamp(dateFrom.getValue().getTime()), new Timestamp(dateTo.getValue().getTime()));
        } else {
            return DataExportEntity.export(layout.getDataSource(),
                    layout.getMainEntity().getName() + layout.getExportSheetName() + ".xls",
                    sql.toString());
        }
    }


    private void initBtn() {
        excelExport.addClickListener((Button.ClickListener) event -> {
            try {
                if (validate()) {
                    FormattedSql sql = formatSQL(new FormattedSql(layout.getLayoutViewGrid().getViewQueryDelegate().getSql(null, null, null)));
                    final File file =
                            AbstractView.getLocaleValue(ToolkitLocaleId.DATA_EXPORT_VIEW)
                                    .equals(dataOrView.getValue())
                                    ? exportView(sql)
                                    : exportData(sql);
                    Page.getCurrent().open(new FileResource(file),null, false);
                    closeWindow();
                }
            } catch (Exception ex) {
                VaadinNotification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
    }

    public void closeWindow() {
        Window window = getComponentParent(Window.class, EntityDateRangeFilter.this);
        if (window != null) {
            window.close();
        }
    }

    private EntityDB localize(EntityDB entity) {
        if (localize.getValue()) {
            entity.getFields().getFieldsOfInstance(FieldLocalDate.class).forEach(f -> f.set(((FieldLocalDate) f).getLocal()));
        }
        return entity;
    }

    private FormattedSql formatSQL(FormattedSql sql) {
        if (filter()) {
            Field field = layout.getMainEntity().getFields().getByName(filterField.getValue().toString());
            sql.setWhere(String.format("((%s) and (%s >= ? and %s <= ?))", sql.getWhere(), getColumnName(field, false), getColumnName(field, false)));
        }
        return sql;
    }

    @SneakyThrows
    private Object getParams() {
        Class type = layout.getMainEntity().getFields().getByName(filterField.getValue().toString()).getType();
        return filter() ? new Object[]{
                type.getConstructor(long.class).newInstance(dateFrom.getValue().getTime())
                , type.getConstructor(long.class).newInstance(dateTo.getValue().getTime())}
                : null;
    }

    public static void showInWindow(EntityDateRangeFilter filter) {
        Window window = new Window();
        window.setHeight("280px");
        window.setWidth("250px");

        VerticalLayout main = new VerticalLayout(filter);
        main.setComponentAlignment(filter,Alignment.MIDDLE_CENTER);
        main.setSizeFull();

        window.setContent(main);
        window.center();
        UI.getCurrent().addWindow(window);
    }

}
