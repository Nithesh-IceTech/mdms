package za.co.spsi.toolkit.crud.gui;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.hene.popupbutton.PopupButton;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.entity.DataExportEntity;
import za.co.spsi.toolkit.crud.excel.DataSourceXls;
import za.co.spsi.toolkit.crud.gui.custom.Toolbar;
import za.co.spsi.toolkit.crud.gui.query.EntityDateRangeFilter;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.util.DownloaderHelper;

import java.io.FileInputStream;

/**
 * Created by jaspervdb on 4/20/16.
 */
public class LayoutToolbar extends Toolbar {

    private Button btnNew = new Button(FontAwesome.FILE),
            btnView = new Button(FontAwesome.EDIT),
            btnSave = new Button(FontAwesome.SAVE),
            btnRefresh = new Button(FontAwesome.REFRESH),
            btnDelete = new Button(FontAwesome.TRASH);
    private PopupButton btnExport = new PopupButton();
    private boolean isList;
    private Layout layout;
    private EntityDateRangeFilter entityDateRangeFilter;


    public LayoutToolbar(boolean isList, Layout layout) {
        this.layout = layout;
        init(isList, layout);
        init(layout.getPermission());
    }

    public void init(Permission permission) {
        btnNew.setEnabled(permission.mayCreate() && isList);
        btnView.setEnabled(isList && permission.mayRead());
        btnSave.setEnabled(!isList && permission.mayUpdate());
        btnRefresh.setEnabled(isList && btnRefresh.isEnabled() || !isList && permission.mayRead());
        btnDelete.setEnabled(permission.mayDelete());

        btnNew.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.CREATE_RECORD));
        btnView.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.OPEN_RECORD));
        btnSave.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.SAVE_RECORD));
        btnDelete.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.DELETE_RECORD));
        btnRefresh.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.REFRESH_RECORD));
    }

    private void init(boolean isList, Layout layout) {
        this.isList = isList;
        btnExport.setIcon(FontAwesome.TABLE);
        addAll(btnNew, btnView, btnSave, btnRefresh, btnDelete, btnExport);
        initButtons();
        initExportBtn(isList, layout);
    }

    /**
     * export entity data template
     *
     * @param layout
     * @return
     */
    private Button getExportExcelTemplate(Layout layout) {
        Button excelExport = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.DATA_EXPORT_EXCEL_TEMPLATE),FontAwesome.SITEMAP);
        DownloaderHelper.extend(excelExport, layout.getMainEntity().getName() + ".xls"
                , () -> DataSourceXls.exportTemplate(layout.getMainEntity(), layout.getMainEntity().getName()));
        excelExport.setWidth("100%");
        return excelExport;
    }

    private Button getExportButton(Layout layout) {
        Button btn = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.DATA_EXPORT),FontAwesome.FILE_EXCEL_O);
        btn.addClickListener((Button.ClickListener) event -> {
            if (entityDateRangeFilter == null) {
                entityDateRangeFilter = new EntityDateRangeFilter(layout);
            }
            entityDateRangeFilter.reset();
            EntityDateRangeFilter.showInWindow(entityDateRangeFilter);
        });
        return btn;
    }

    private void initExportBtn(boolean isList, Layout layout) {
        VerticalLayout root = new VerticalLayout();
        // add excel export

        if (layout.getLayoutViewGrid() != null) {
            root.addComponent(getExportExcelTemplate(layout));
            root.addComponent(getExportButton(layout));
        }

        for (DataExportEntity dataExportEntity : DataExportEntity.getExportsForView(
                layout.getDataSource(), layout.getClass().getSimpleName() + (isList ? "" : ".Detail"))) {
            Button btn = new Button(AbstractView.getLocaleValue(dataExportEntity.exportName.get()));
            btn.setDescription(AbstractView.getLocaleValue(dataExportEntity.exportDescription.get()));
            DownloaderHelper.extend(btn, layout.getMainEntity().getName() + layout.getExportSheetName() + ".xls"
                    , () -> dataExportEntity.export(layout.getDataSource(), layout.getMainEntity()));
            root.addComponent(btn);
        }
        btnExport.setClosePopupOnOutsideClick(true);
        btnExport.setContent(root);
        btnExport.setEnabled(root.getComponentCount() > 0);
    }

    private void initButtons() {
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) instanceof Button) {
                (getComponent(i)).setEnabled(false);
            }
        }
    }

    private LayoutToolbar addClick(Button btn, Button.ClickListener clickListener) {
        btn.setEnabled(true);
        btn.addClickListener(clickListener);
        init(layout.getPermission());
        return this;
    }

    public LayoutToolbar addNewClick(Button.ClickListener clickListener) {
        return addClick(btnNew, clickListener);
    }

    public LayoutToolbar addViewClick(Button.ClickListener clickListener) {
        return addClick(btnView, clickListener);
    }

    public LayoutToolbar addSaveClick(Button.ClickListener clickListener) {
        return addClick(btnSave, clickListener);
    }

    public LayoutToolbar addRefreshClick(Button.ClickListener clickListener) {
        return addClick(btnRefresh, clickListener);
    }

    public LayoutToolbar addDeleteClick(Button.ClickListener clickListener) {
        return addClick(btnDelete, clickListener);
    }

    public void dataChangeEvent() {
        btnSave.setEnabled(!isList && layout.getPermission().mayUpdate());
    }

    public void successfulSaveEvent() {
        //  btnSave.setEnabled(false);
    }

}
