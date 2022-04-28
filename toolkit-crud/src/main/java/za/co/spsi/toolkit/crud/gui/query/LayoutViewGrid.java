package za.co.spsi.toolkit.crud.gui.query;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.J2EEConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.SelectionEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.renderers.NumberRenderer;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.LFieldList;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.LayoutToolbar;
import za.co.spsi.toolkit.crud.gui.custom.CustomImageField;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.locale.Translatable;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StringUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

/**
 * Created by jaspervdb on 15/03/27.
 */
public class LayoutViewGrid<E extends Layout> extends Grid implements Translatable {

    private static final Logger LOG = LoggerFactory.getLogger(LayoutViewGrid.class);

    private DataSource dataSource;

    private JDBCConnectionPool pool;
    private SQLContainer sqlContainer;
    private boolean addFilter;
    private Driver driver;

    private E layout;
    private ViewQueryDelegate viewQueryDelegate;
    private Set<Object> selected;
    private String sql;
    private LFieldList columns;
    private boolean allowMultiSelec, allowSingleClickSelection;
    private Callback callback;
    public static final String UNIQUE_ROW_ID = "UNIQUE_ROW_ID";
    public static final String GRID_IMAGE = "GRID_IMAGE";

    public LayoutViewGrid(DataSource dataSource, E layout, boolean allowMultiSelec, int rowLimit, String sql, Callback callback) {
        this.layout = layout;
        this.dataSource = dataSource;
        this.callback = callback;
        this.sql = sql;
        this.allowMultiSelec = allowMultiSelec;
        driver = DriverFactory.getDriver();

        Assert.notNull(layout.getGroups().getNameGroup(), "No name groups defined for " + layout.getClass());
        this.columns = layout.getGroups().getNameGroup().getFields();
        Assert.isTrue(columns != null, "Layout %s does not have any NameGroup's defined", layout.getClass());
        viewQueryDelegate = new ViewQueryDelegate(driver, this, this.sql, rowLimit);
    }

    public ViewQueryDelegate getViewQueryDelegate() {
        return viewQueryDelegate;
    }

    public SQLContainer getSqlContainer() {
        return sqlContainer;
    }


    public E getLayout() {
        return layout;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Callback getCallback() {
        return callback;
    }

    public LayoutViewGrid setAllowSingleClickSelection(boolean allowSingleClickSelection) {
        this.allowSingleClickSelection = allowSingleClickSelection;
        return this;
    }

    public void build(boolean addFilter) {
        try {
            this.addFilter = addFilter;
            addStyleName(ValoTheme.TABLE_SMALL);
            pool = new J2EEConnectionPool(dataSource);
            setWidth("100%");
            LOG.info(viewQueryDelegate.getSqlSelect());
            FreeformQuery freeformQuery = new FreeformQuery(viewQueryDelegate.getSqlSelect(), pool,
                    EntityDB.getColumnNames(layout.getMainEntity().getId()).toStringArray());
            freeformQuery.setDelegate(viewQueryDelegate);
            sqlContainer = new SQLContainer(freeformQuery);
            setContainerDataSource(sqlContainer);
            StringList cols = formatColumnNameList(columns);
            cols.add(0, formatColumnName(UNIQUE_ROW_ID));
            setColumns(cols.toStringArray());
            getColumns().get(0).setHeaderCaption("").setRenderer(new HtmlRenderer(), new TxLockedStateConverter(layout));
            getColumns().get(0).setMaximumWidth(80);

            // Set column headers
            getColumn(formatColumnName(UNIQUE_ROW_ID)).setSortable(false);

            columns.stream().forEach(headingId -> initColumn(headingId, getColumn( formatColumnName(headingId.getColName()) )));

            setSelectionMode(allowMultiSelec ? SelectionMode.MULTI : SelectionMode.SINGLE);
            setColumnReorderingAllowed(true);

            setCellStyleGenerator((CellStyleGenerator) cellReference -> {

                if (cellReference.getProperty().getType().isAssignableFrom(BigDecimal.class) ||
                        cellReference.getProperty().getType().isAssignableFrom(Double.class) ||
                        cellReference.getProperty().getType().isAssignableFrom(Float.class)) {
                    return "rightAligned";
                } else if (formatColumnName(GRID_IMAGE).equals(cellReference.getPropertyId())) {
                    return "centerAligned";
                } else {
                    return "leftAligned";
                }
            });


            if (allowMultiSelec) {
                addItemClickListener((ItemClickEvent.ItemClickListener) event -> {
                    MultiSelectionModel m = (MultiSelectionModel) getSelectionModel();
                    m.deselectAll();
                    m.select(Arrays.asList(event.getItemId()));
                });
            } else {
                ((SingleSelectionModel) getSelectionModel()).setDeselectAllowed(false);
            }

            // add the filters
            // Create a header row to hold column filters
            if (addFilter) {
                HeaderRow filterRow = appendHeaderRow();

                for (final Object pid : getContainerDataSource().getContainerPropertyIds()) {
                    HeaderCell cell = filterRow.getCell(pid);
                    if (cell != null && ! formatColumnName(UNIQUE_ROW_ID).equals(pid) && !formatColumnName(GRID_IMAGE).equals(pid) &&
                            (!pid.equals(EntityDB.getColumnName(layout.getMainEntity().getSingleId())) ||
                                    layout.getMainEntity().getSingleId().getAnnotation(Id.class) != null &&
                                            !((Id) layout.getMainEntity().getSingleId().getAnnotation(Id.class)).uuid())
                            ) {

                        // Have an input field to use for filter
                        TextField filterField = new TextField();
                        filterField.setWidth("100%");
                        filterField.addStyleName(ValoTheme.TEXTFIELD_TINY);
                        filterField.addStyleName("uppercase");

                        // Update filter When the filter input is changed
                        filterField.addTextChangeListener((FieldEvents.TextChangeListener) event -> {
                            sqlContainer.removeContainerFilters(pid);

                            // (Re)create the filter if necessary
                            if (!event.getText().isEmpty())
                                sqlContainer.addContainerFilter(
                                        new SimpleStringFilter(pid,
                                                event.getText(), true, false));
                        });

                        cell.setComponent(filterField);
                    }
                }
            }

            addContextClickListener((ContextClickEvent.ContextClickListener) event -> {
                deselectAll();
                select(((GridContextClickEvent) event).getItemId());
                // show the action menu
                ComboBox action = layout.getActionBox();
                MessageBox mb  = MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.ACTIONS)).
                        withMessage(action).withOkButton(ButtonOption.closeOnClick(true)).
                        withCancelButton(ButtonOption.closeOnClick(true));
                action.addValueChangeListener((Property.ValueChangeListener) event1 -> mb.close());
                mb.open();
            });

            addSelectionListener((SelectionEvent.SelectionListener) event -> {
                if (!event.getSelected().isEmpty()) {
                    selected = event.getSelected();
                    if (allowSingleClickSelection) {
                        selected();
                    }
                }
            });

            addItemClickListener((ItemClickEvent.ItemClickListener) event -> {
                // fire event
                if (event.isDoubleClick() && !allowSingleClickSelection) {
                    selected();
                }
            });

        } catch (Exception sqle) {
            sqle.printStackTrace();
            throw new RuntimeException(sqle);
        }
    }

    public void initColumn(LField columnField, Column column) {
        getColumn( formatColumnName(columnField.getColName()) ).setHeaderCaption(
                !StringUtils.isEmpty(columnField.getProperties().getCaption()) ?
                        getLocaleValue(columnField.getProperties().getCaption()) : columnField.getCaption());

        if (columnField.getGridColumnRenderer() != null) {
            columnField.getGridColumnRenderer().initColumn(column);
        } else {
            if (columnField.getFieldType().isAssignableFrom(BigDecimal.class) ||
                    columnField.getFieldType().isAssignableFrom(Double.class) ||
                    columnField.getFieldType().isAssignableFrom(Float.class)) {

                DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
                otherSymbols.setDecimalSeparator(ToolkitCrudConstants.getDecimalSeparator());
                otherSymbols.setGroupingSeparator(ToolkitCrudConstants.getGroupingSeparator());
                DecimalFormat df = new DecimalFormat(ToolkitCrudConstants.getDecimalFormat(), otherSymbols);
                column.setRenderer(new NumberRenderer(df));

            } else if (columnField.getClass().equals(ComboBoxField.class)) {
                getColumn( formatColumnName(columnField.getColName()) ).setRenderer(
                        new HtmlRenderer(),
                        new ComboBoxConverter((ComboBoxField) columnField));
            } else if (columnField.getFieldType().isAssignableFrom(Boolean.class)) {

                if( getColumn( formatColumnName(columnField.getColName()) ).getConverter()
                        .getClass().equals(StringToBigDecimalConverter.class) ) {
                    getColumn( formatColumnName(columnField.getColName()) )
                            .setRenderer(new HtmlRenderer(), new StringToBigDecimal(
                                    FontAwesome.CHECK_CIRCLE_O.getHtml(), FontAwesome.CIRCLE_O.getHtml()));
                } else if(getColumn( formatColumnName(columnField.getColName()) ).getConverter()
                        .getClass().equals(StringToBooleanConverter.class)) {
                    getColumn( formatColumnName(columnField.getColName()) )
                            .setRenderer(new HtmlRenderer(), new StringToBooleanConverter(
                                    FontAwesome.CHECK_CIRCLE_O.getHtml(), FontAwesome.CIRCLE_O.getHtml()));
                }

            } else if (columnField.getVaadinField() instanceof CustomImageField) {
                getColumn( formatColumnName(columnField.getColName()) ).setRenderer(
                        new ImageRenderer(),
                        new ResourceToStringConverter());

                getColumn( formatColumnName(columnField.getColName()) ).setWidth(70);
            }
        }

    }

    public String formatColumnName(String columnName) {
        return DriverFactory.getDriver().formatColumnCase(columnName);
    }

    public StringList formatColumnNameList(LFieldList columns) {
        StringList cols = new StringList(columns.getColNames());
        StringList formattedCols = new StringList();
        for(String col: cols) {
            formattedCols.add(formatColumnName(col));
        }
        return formattedCols;
    }

    public void selected() {
        if (callback != null) {
            callback.selected(layout, this, false, getSelectedEntity());
        }
    }

    public EntityDB getSelectedEntity() {
        List<EntityDB> entities = getSelectedEntities();
        return entities.isEmpty() ? null : entities.get(0);
    }

    public List<EntityDB> getSelectedEntities() {
        try {
            List<EntityDB> entities = new ArrayList<>();
            if (getSelected() != null) {
                for (Object selected : getSelected()) {
                    String id = selected.toString();
                    EntityDB entityDB = layout.getMainEntity().getClass().newInstance();
                    entityDB.getId().get(0).set(id);
                    entities.add(DataSourceDB.loadFromId(dataSource, entityDB));
                }
            }
            return entities;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static interface Callback<E extends EntityDB> {
        void selected(Layout<E> layout, Component source, boolean newEvent, E entityDB);
    }


    public Set<Object> getSelected() {
        return selected;
    }

    @Override
    public void translate(String oldLocale, String newLocale) {
        // translate the headers
        for (LField headingId : columns) {
            getColumn( formatColumnName(headingId.getColName()) ).setHeaderCaption(headingId.getCaption());
        }
        viewQueryDelegate.setSql(this.sql);
        this.getSqlContainer().refresh();
        setColumns(columns.getColNames().toUpperCase().toStringArray());
    }

    public static class Container extends MVerticalLayout {
        LayoutToolbar toolbar;
        LayoutViewGrid grid;

        public Container(Component top, LayoutToolbar toolbar, LayoutViewGrid grid) {
            super(top, grid);
            this.grid = grid;
            this.toolbar = toolbar;
        }

        public Container(Component... components) {
            super(components);
        }

        public LayoutViewGrid getGrid() {
            return grid;
        }

        public LayoutToolbar getToolbar() {
            return toolbar;
        }
    }

    public static void main(String[] args) {
        System.out.println(Math.round(Double.parseDouble("4.6464E7")));
    }
}

