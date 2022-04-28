package za.co.spsi.toolkit.crud.gui;


import com.vaadin.data.Property;
import com.vaadin.data.sort.SortOrder;
import com.vaadin.server.Page;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.ButtonType;
import de.steinwedel.messagebox.MessageBox;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import rx.Observable;
import rx.schedulers.Schedulers;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.PlaceOnToolbar;
import za.co.spsi.toolkit.crud.gui.auth.RoleProvider;
import za.co.spsi.toolkit.crud.gui.fields.InvalidFieldValueException;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitMaskMessage;
import za.co.spsi.toolkit.crud.gui.query.LayoutViewGrid;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.gui.rule.Rule;
import za.co.spsi.toolkit.crud.gui.rule.RuleHelper;
import za.co.spsi.toolkit.crud.util.AgencyHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.FormattedSql;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.audit.AuditEntity;
import za.co.spsi.toolkit.db.audit.AuditHelper;
import za.co.spsi.toolkit.ee.db.DefaultConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

/**
 * Created by jaspervdb on 2/11/16.
 */
public class Layout<E extends EntityDB> implements LayoutViewGrid.Callback<E> {

    static {
        // make sure the locale are initialised
//        AuditConfig auditConfig = new AuditConfig();
        System.out.println("Page Title " + ToolkitLocaleId.ACCEPT_RESOURCE.getName());
    }

//    @Inject
//    @ConfValue(value = "system.datachange.listener.enabled")
    private boolean dataChangeEventListener = true;


    private String captionId;
    private LFieldList fields = new LFieldList();

    private GroupList groups = new GroupList();
    private PaneList panes = new PaneList();
    private Layout parentLayout;
    public LayoutViewGrid layoutViewGrid;
    private LayoutToolbar toolbar;
    private String originalQuery;
    private boolean busyConstructing = false;

    private List<Rule> rules = new ArrayList<>();
    private StringList reviewFields = new StringList();

    private Permission permission = new Permission(this);

    @Inject
    private DefaultConfig defaultConfig;

    @Inject
    private ToolkitMaskMessage toolkitMaskMessage;

    @Inject
    private RuleHelper ruleHelper;

    @Inject
    private ToolkitLookupServiceHelper lookupServiceHelper;

    @Inject
    private BeanManager beanManager;

    @Inject
    @ConfValue(value = "grid.row_limit")
    private Integer rowLimit;

    private RoleProvider roleProvider;

    public Layout() {
    }

    // data export filter

    /**
     * override to implement logic
     * @return
     */
    public String getExportFilterField() {
        return null;
    }

    public Integer getRowLimit() {
        return rowLimit;
    }


    @PostConstruct
    private void initPost() {
        rowLimit = rowLimit == null ? 1000 : rowLimit;
        // ensure that the fields are wired
        getMainEntity().getFields();
        // find all methods that are annotated with @AgencyPostContruct
        invokePostConstruct();
    }

    protected void invokePostConstruct() {
        AgencyHelper.getAgencyPostConstruct(getClass()).stream().forEach(m -> {
            try {
                m.setAccessible(true);
                m.invoke(this, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public boolean isDataChangeEventListener() {
        return dataChangeEventListener;
    }

    public Layout(Permission permission) {
        this.permission = permission;
    }

    public Layout(Layout layout) {
        layout.initLayout(this);
    }

    public LayoutToolbar getToolbar() {
        return toolbar;
    }

    public Layout(String captionId) {
        this.captionId = captionId;
    }

    public void setCaptionId(String captionId) {
        this.captionId = captionId;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public void initPermission(Permission parent) {
        getPermission().setMayUpdate(parent.mayUpdate() && getPermission().mayUpdate());
        // default for children - may create if parent may update
        getPermission().setMayCreate(getPermission().mayCreate() && parent.mayUpdate());
        getPermission().setMayDelete(getPermission().mayDelete() && parent.mayUpdate());
    }

    public StringList getReviewFields() {
        return reviewFields;
    }

    public DataSource getDataSource() {
        return defaultConfig.getDataSource();
    }

    public BeanManager getBeanManager() {
        return beanManager;
    }

    public void setParentLayout(Layout parentLayout) {
        this.parentLayout = parentLayout;
        // copy the permission
        parentLayout.getPermission().addChild(getPermission());
        getPermission().initFromParent(parentLayout.getPermission());
    }

    public Layout getParentLayout() {
        return parentLayout;
    }

    public LayoutViewGrid getLayoutViewGrid() {
        return layoutViewGrid;
    }

    public void initLayout(Layout layout) {
        layout.getFields().stream().forEach(f -> {
            f.setField(getMainEntity().getFields().getByName(f.getField().getName()));
            f.setLayout(this);
            add(f);
        });
    }

    public void add(LField field) {
        fields.add(field);
        if (!groups.isEmpty() && !(groups.get(groups.size() - 1) instanceof Group.Disable)) {
            groups.get(groups.size() - 1).addField(field);
        }
    }

    public void replaceField(LField lF, za.co.spsi.toolkit.entity.Field field) {
        LField cField = getFields().stream().filter(f -> f.getField() == field).findFirst().get();
        Assert.isTrue(cField != null, "Can not find LField for Field " + field.getName());
        for (Group group : groups) {
            if (group.getFields().contains(cField)) {
                group.getFields().set(group.getFields().indexOf(cField), lF);
            }
        }
        getFields().set(getFields().indexOf(cField), lF);
    }

    public boolean isAuditable() {
        return getMainEntity().isAuditable() && AuditHelper.isSupervisor();
    }

    public ToolkitMaskMessage getMaskMessage() {
        return toolkitMaskMessage;
    }

    public ToolkitLookupServiceHelper getLookupServiceHelper() {
        return lookupServiceHelper;
    }

    private java.lang.reflect.Field getReflectionField(Class refClass, Object src) throws IllegalAccessException {
        for (java.lang.reflect.Field field : refClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(this) == src) {
                return field;
            }
        }
        if (!refClass.getSuperclass().equals(Object.class)) {
            return getReflectionField(refClass.getSuperclass(), src);
        } else {
            return null;
        }
    }

    public java.lang.reflect.Field getReflectionField(Object src) {
        try {
            return getReflectionField(this.getClass(), src);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Group group) {
        groups.add(group);
        if (!panes.isEmpty()) {
            panes.get(panes.size() - 1).addView(group);
        }
    }

    public void add(Pane pane) {
        panes.add(pane);
    }

    public PaneList getPanes() {
        return panes.apply();
    }

    public void addBusinessRule(Rule rule) {
        rules.add(rule);
    }


    public LFieldList getFields() {
        return fields.apply();
    }

    private Field getField(Class thisClass, Object src) {
        try {
            for (Field field : thisClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(this) == src) {
                    return field;
                }
            }
            if (!thisClass.getSuperclass().equals(Object.class)) {
                return getField(thisClass.getSuperclass(), src);
            } else {
                return null;
            }
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public Field getField(Object src) {
        return getField(this.getClass(), src);
    }

    public <T extends Annotation> T getAnnotation(Object src, Class<T> aClass) {
        Field field = getField(src);
        return field != null ? field.getAnnotation(aClass) : null;
    }

    public String getCaption() {
        return AbstractView.getLocaleValue(captionId).toUpperCase();
    }

    /**
     * caption with unique value
     *
     * @return
     */
    public String getUniqueCaption() {
        LFieldList captionField = getFields().getFieldsWithAnnotationWithMethodSignature(UIField.class, "isForCaption", true);
        StringList strings = new StringList();
        for (LField field : captionField) {
            if (field.get() != null) {
                strings.add(field.getAsString());
            }
        }
        if (strings.isEmpty()) {
            return ToolkitLocaleId.DETAILS;
        } else {
            return strings.toString(" - ");
        }
    }

    public String getOriginalCaption() {
        return AbstractView.getLocaleValue(captionId);
    }

    public boolean save() {
        return DataSourceDB.executeResultInTx(getDataSource(), new DataSourceDB.Callback<Boolean>() {
            @Override
            public Boolean run(Connection connection) throws Exception {
                return save(connection);
            }
        });
    }

    public boolean save(Connection connection) throws SQLException {
        try {

            intoBindings();

            if (ruleHelper.validate(rules)) {

                // send save signal to fields
                getFields().saveEvent(connection);
                DataSourceDB.set(connection, getMainEntity());
                if (getParentLayout() != null && getParentLayout().getLayoutViewGrid() != null) {
                    getParentLayout().getLayoutViewGrid().getSqlContainer().refresh();
                }
                // load default values that has been set back into layout fields values
                intoControl();

                if (toolbar != null) {
                    toolbar.successfulSaveEvent();
                }
                return true;
            }
        } catch (InvalidFieldValueException ve) {
            VaadinNotification.show(getLocaleValue(ToolkitLocaleId.VALIDATION_ERROR), ve.getMessage(), Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return false;
    }

    /**
     * override to intercept the new event
     */
    public void newEvent() {
    }

    public void dataChangeEvent() {
        if (toolbar != null) {
            toolbar.dataChangeEvent();
        }
    }

    public void executeAuditingLogic() {
        if (getMainEntity().isAuditable()) {
            // review
            reviewFields = AuditEntity.getReviewFields(getDataSource(), getMainEntity());
            fields.refreshAuditEvent();
        }
    }

    public void beforeOnScreenEvent() {
        executeAuditingLogic();
        // if this is a list then only refresh
        if (layoutViewGrid != null) {
            layoutViewGrid.getSqlContainer().refresh();
        } else {
            getFields().beforeOnScreenEvent();
        }
    }

    public boolean delete() {
        EntityDB entityDB = layoutViewGrid.getSelectedEntity();
        if (entityDB != null) {
            return delete(entityDB);
        } else {
            VaadinNotification.show(
                    String.format(AbstractView.getLocaleValue(ToolkitLocaleId.FIRST_SELECT_ENTITY_TO_DELETE), getCaption()),
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean delete(EntityDB entityDB) {
        DataSourceDB.delete(getDataSource(), entityDB);
        // refresh
        layoutViewGrid.getSqlContainer().refresh();
        VaadinNotification.show(String.format(getLocaleValue(ToolkitLocaleId.ENTITY_DELETED), getCaption()),
                Notification.Type.TRAY_NOTIFICATION);
        return true;
    }

    public void refresh() {
        if (layoutViewGrid != null) {
            layoutViewGrid.getSqlContainer().refresh();
        }
    }

    private List<Field> getFieldsOfTypeAnnotatedWith(Class myClass, Class type, Class<? extends Annotation> aClass) {
        List<java.lang.reflect.Field> fields = new ArrayList<>();
        for (java.lang.reflect.Field field : myClass.getDeclaredFields()) {
            if (field.getAnnotation(aClass) != null && type.isAssignableFrom(field.getType())) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        if (!myClass.getSuperclass().equals(Object.class)) {
            fields.addAll(getFieldsOfTypeAnnotatedWith(myClass.getSuperclass(), type.getSuperclass(), aClass));
        }
        return fields;
    }

    public List<Field> getFieldsOfTypeAnnotatedWith(Class type, Class<? extends Annotation> aClass) {
        return getFieldsOfTypeAnnotatedWith(getClass(), type, aClass);
    }

    private <T> List<T> getFieldValuesOfTypeAnnotatedWith(Class myClass, Class<T> type, Class<? extends Annotation> aClass) {
        try {
            List<T> values = new ArrayList<>();
            for (java.lang.reflect.Field field : getFieldsOfTypeAnnotatedWith(myClass, type, aClass)) {
                values.add((T) field.get(this));
            }
            return values;
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public <T> List<T> getFieldValuesOfTypeAnnotatedWith(Class<T> type, Class<? extends Annotation> aClass) {
        return getFieldValuesOfTypeAnnotatedWith(getClass(), type, aClass);
    }

    public E getMainEntity() {
        List<EntityDB> entities = getRefEntity(true);
        Assert.isTrue(entities.size() == 1, "No EntityRef(main=true), or more than one found with. Found " + entities.size());
        return (E) entities.get(0);
    }

    public List<EntityDB> getRefEntity(boolean main) {
        return getFieldsOfTypeAnnotatedWith(EntityDB.class, EntityRef.class).stream().
                filter(f -> main == f.getAnnotation(EntityRef.class).main()).map(f -> {
            try {
                return (EntityDB) f.get(this);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("No @EntityRef annotated entityDB found in layout %s", getClass().getName()));
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }

    public GroupList getGroups() {
        return groups.apply();
    }

    public void intoControl() {
        getFields().intoControl();
    }

    public void intoBindings() {
        getPanes().intoBindings();
//        // also non visible fields
        for (LField field : getFields().subtract(getPanes().getFields())) {
            field.intoBindings();
        }
    }

    public void intoBindingsNoValidation() {
        getFields().intoBindingsWithNoValidation();
    }

    public String getOriginalQuery() {
        originalQuery = originalQuery == null ? layoutViewGrid.getViewQueryDelegate().getSqlSelect() : originalQuery;
        return originalQuery;
    }

    /**
     * overload to return single entry
     *
     * @return
     */
    public String getMainSql() {
        return "select * from " + getMainEntity().getName();
    }

    public String getExportSheetName() {
        return "";
    }

    public RoleProvider getRoleProvider() {
        return roleProvider;
    }

    public void setRoleProvider(RoleProvider roleProvider) {
        this.roleProvider = roleProvider;
    }

    private Component getHeaderPane() {
        toolbar = new LayoutToolbar(false, this);
        toolbar.addRefreshClick((Button.ClickListener) clickEvent -> {
            refreshButton();
        }).addSaveClick((Button.ClickListener) clickEvent -> {
            if (save()) {
                VaadinNotification.show(String.format("%s %s", getOriginalCaption(), AbstractView.getLocaleValue(ToolkitLocaleId.SAVED).toLowerCase()));
            }
        });

        MHorizontalLayout headerLayout = new MHorizontalLayout(toolbar).withAlign(toolbar, Alignment.MIDDLE_LEFT).withFullWidth();
        for (LField field : getFields().getFieldsWithAnnotation(PlaceOnToolbar.class)) {
            field.getComponent().setWidthUndefined();
            toolbar.add(field.getComponent(), 0);
            // remove from group
            getPanes().getQualifiedPanes().stream().forEach(pane -> pane.getViews(Group.class).stream().forEach(group -> group.getFields().remove(field)));
            // remove empty groups
            getPanes().getQualifiedPanes().stream().forEach(pane -> pane.getViews(Group.class)
                    .removeAll(pane.getViews(Group.class).stream().filter(group -> group.getFields().isEmpty()).
                            collect(Collectors.toCollection(GroupList::new))));
        }

        return headerLayout;
    }

    public boolean isBusyConstructing() {
        return busyConstructing;
    }

    public void setBusyConstructing(boolean busyConstructing) {
        this.busyConstructing = busyConstructing;
    }

    public void refreshButton(){
        DataSourceDB.loadFromId(getDataSource(), getMainEntity());
        intoControl();
        executeAuditingLogic();
    }

    public Component buildComponent(DataSource dataSource, BeanManager beanManager, LayoutViewGrid.Callback callback) {
        // ensure fields are wired
        busyConstructing = true;
        try {
            getMainEntity().getFields();
            MVerticalLayout root = new MVerticalLayout(getHeaderPane()).withFullWidth().withMargin(false).withFullHeight().withStyleName("layout-panel");
            root.add(getPanes().buildComponent(dataSource, beanManager, callback));
            root.expand(root.getComponent(root.getComponentCount() - 1));
            intoControl();
            return root;
        } finally {
            busyConstructing = false;
        }
    }

    public EntityDB getNewInstance(EntityDB parent) {
        try {
            EntityDB entityDB = getMainEntity().getClass().newInstance();
            // autowire all the UUID's
            entityDB.prepareFieldsForInsert();
            if (parent != null) {
                entityDB.updateForeignKeyFieldValues(parent);
            }
            return entityDB;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * overload to change behavior
     *
     * @return if a filter will be added to the list view
     */
    public boolean shouldAddFilterOnPaneList() {
        return false;
    }

    public LayoutViewGrid.Container buildList(DataSource dataSource, String sql, LayoutViewGrid.Callback callback, boolean addFilter) {
        layoutViewGrid = new LayoutViewGrid(dataSource, this, getActions().length > 0, rowLimit, sql, callback);
        layoutViewGrid.build(addFilter);
        layoutViewGrid.setSizeFull();
        toolbar = new LayoutToolbar(true, this);
        toolbar.addNewClick((Button.ClickListener) clickEvent -> {
            if (Layout.this.parentLayout == null || Layout.this.parentLayout.save()) {
                callback.selected(Layout.this, layoutViewGrid, true, getNewInstance(
                        parentLayout != null ? parentLayout.getMainEntity() : null));
            }
        }).addViewClick((Button.ClickListener) clickEvent -> {
            if (layoutViewGrid.getSelected() != null) {
                layoutViewGrid.selected();
            } else {
                VaadinNotification.show(String.format(AbstractView.getLocaleValue(ToolkitLocaleId.FIRST_SELECT_ENTITY_TO_VIEW), getCaption()),
                        Notification.Type.TRAY_NOTIFICATION);
            }
        }).addRefreshClick((Button.ClickListener) clickEvent -> {
            layoutViewGrid.getSqlContainer().refresh();
            // re init
        }).addDeleteClick((Button.ClickListener) clickEvent -> {
            if (layoutViewGrid.getSelected() != null) {
                MessageBox.createInfo().withCaption(getLocaleValue(ToolkitLocaleId.DELETE_RECORD))
                        .withMessage(getLocaleValue(ToolkitLocaleId.CONFIRM_DELETE))
                        .withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true))
                        .withOkButton(() -> {
                            delete();
                        }, ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()))
                        .open();
            } else {
                VaadinNotification.show(String.format(AbstractView.getLocaleValue(ToolkitLocaleId.FIRST_SELECT_ENTITY_TO_DELETE), getCaption()),
                        Notification.Type.TRAY_NOTIFICATION);
            }

        });

        MHorizontalLayout filterBar = new MHorizontalLayout().withWidth("-1px");
        if (getFilters().length > 0) {
            filterBar.add(getFilterBox());
        }
        if (getActions().length > 0) {
            filterBar.add(getActionBox());
        }
        filterBar.setMargin(new MarginInfo(false, true, false, false));
        toolbar.init(getPermission());
        HorizontalLayout top = new HorizontalLayout(toolbar, filterBar);
        top.setComponentAlignment(toolbar, Alignment.MIDDLE_LEFT);
        top.setComponentAlignment(filterBar, Alignment.MIDDLE_RIGHT);
        top.setWidth("100%");
        return (LayoutViewGrid.Container) new LayoutViewGrid.Container(top, layoutViewGrid).expand(layoutViewGrid).withMargin(false).withFullWidth().withFullHeight();//.withStyleName("layout-list");
    }

    private ComboBoxMultiselect getFilterBox() {
        ComboBoxMultiselect filterBox = new ComboBoxMultiselect();
//        ComboBox filterBox = new ComboBox();
        filterBox.setInputPrompt(AbstractView.getLocaleValue(ToolkitLocaleId.FILTERS));
        filterBox.setClearButtonCaption(AbstractView.getLocaleValue(ToolkitLocaleId.CLEAR));
        final String[][] filters = getFilters();
        StringList values = new StringList();
        for (String filter[] : filters) {
            filterBox.addItem(filter[0]);
            filterBox.setItemCaption(filter[0], AbstractView.getLocaleValue(filter[0]));
            values.add(filter[0]);
        }
        filterBox.addValueChangeListener((Property.ValueChangeListener) event -> {
            Collection selection = (Collection) event.getProperty().getValue();
            StringList msg = new StringList();
            if (!selection.isEmpty()) {
                FormattedSql format = new FormattedSql(getOriginalQuery());
                for (Object value : selection) {
                    FormattedSql sql = new FormattedSql(filters[values.indexOf(value)][2]);
                    format = format.addWhere(sql.getWhere());
//                    format.setWhere(format.getWhere() == null ? sql.getWhere() : String.format("((%s) and (%s))", sql.getWhere(), format.getWhere()));
                    msg.add(AbstractView.getLocaleValue(filters[values.indexOf(value)][1]));
                    if (sql.getOrder() != null) {
                        layoutViewGrid.setSortOrder(getSortOrder(sql.getOrder()));
                    }
                }
                layoutViewGrid.getViewQueryDelegate().setSqlSelect(format.toString());
            } else {
                layoutViewGrid.getViewQueryDelegate().setSqlSelect(getOriginalQuery());
            }
            layoutViewGrid.getSqlContainer().refresh();
            layoutViewGrid.getSelectionModel().reset();
            new Notification(AbstractView.getLocaleValue(ToolkitLocaleId.FILTER_APPLIED), msg.prepend("<p>").append("</p>").toString("<p>and</p>").toString(),
                    Notification.Type.TRAY_NOTIFICATION, true)
                    .show(Page.getCurrent());
        });
        return filterBox;
    }

    private List<SortOrder> getSortOrder(String order) {
        List<SortOrder> sortOrders = new ArrayList<>();
        Arrays.stream(order.split(",")).forEach(s -> {
                    String name = (s + " ").substring(0, s.indexOf(" ")).toUpperCase();
                    if (s.toLowerCase().endsWith(" asc")) {
                        sortOrders.add(new SortOrder(name, SortDirection.ASCENDING));
                    } else if (s.toLowerCase().endsWith(" desc")) {
                        sortOrders.add(new SortOrder(name, SortDirection.DESCENDING));
                    } else {
                        sortOrders.add(new SortOrder(name, SortDirection.ASCENDING));
                    }
                }
        );
        return sortOrders;
    }

    public ComboBox getActionBox() {
        ComboBox actionBox = new ComboBox();
        actionBox.setInputPrompt(AbstractView.getLocaleValue(ToolkitLocaleId.ACTIONS));
        for (String action : getActions()) {
            actionBox.addItem(action);
            actionBox.setItemCaption(action, AbstractView.getLocaleValue(action));
        }
        actionBox.addValueChangeListener((Property.ValueChangeListener) event -> {
            if (event.getProperty().getValue() != null) {
                MessageBox billingMessageBox = MessageBox.createInfo();
                billingMessageBox.withCaption(getLocaleValue(ToolkitLocaleId.INFO))
                        .withMessage(getLocaleValue(event.getProperty().getValue().toString()))
                        .withCancelButton(() ->
                                        UI.getCurrent().access(() -> actionBox.setValue(null)),
                                ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true))
                        .withOkButton(() -> {
                                    billingMessageBox.getButton(ButtonType.OK).setEnabled(false);
                                    billingMessageBox.getButton(ButtonType.CANCEL).setEnabled(false);
                                    Observable.fromCallable(
                                            actionRequest((String) event.getProperty().getValue())).
                                            subscribeOn(Schedulers.newThread()).
                                            doOnError(throwable -> {
                                                if (billingMessageBox != null) {
                                                    UI.getCurrent().access(() -> billingMessageBox.close());
                                                    UI.getCurrent().access(() -> actionBox.setValue(null));
                                                }
                                                throw new RuntimeException(throwable);
                                            }).subscribe(aVoid -> {
                                        UI.getCurrent().access(() -> billingMessageBox.close());
                                        UI.getCurrent().access(() -> actionBox.setValue(null));
                                    });
                                },
                                ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()), ButtonOption.closeOnClick(false)).open();

            }
        });
        return actionBox;
    }

    private Callable<Void> actionRequest(String event) {
        return () -> {
            action(event, layoutViewGrid.getSelectedEntities());
            return null;
        };
    }

    public LayoutViewGrid.Container buildList(DataSource dataSource, LayoutViewGrid.Callback callback, boolean addFilter) {
        return buildList(dataSource, getMainSql(), callback, addFilter);
    }

    public LayoutViewGrid.Container buildList(DataSource dataSource, LayoutViewGrid.Callback callback) {
        return buildList(dataSource, callback, true);
    }

    @Override
    public void selected(Layout layout, Component source, boolean newEvent, EntityDB entityDB) {
        // init your own entity from this
        getMainEntity().copyStrict(entityDB);
        // init all the non main refs
        Map<za.co.spsi.toolkit.entity.Field, ForeignKey> map = getMainEntity().getFields().getFieldsWithAnnotation(ForeignKey.class);
        getRefEntity(false).stream().forEach(e -> {
            Optional<za.co.spsi.toolkit.entity.Field> oField = map.keySet().stream().filter(k -> map.get(k).table().equals(e.getClass())).findFirst();
            if (oField.isPresent()) {
                DataSourceDB.getFromSet(getDataSource(), (EntityDB) e.getSingleId().set(oField.get().get()));
            }
        });
    }

    private String txUid = null;

    /**
     * @return the unique name for this tx
     */
    public String getTxUID() {
        txUid = txUid == null ? getTxUID(getMainEntity()) : txUid;
        return txUid;
    }

    public static String getTxUID(EntityDB entity, String id) {
        return String.format("%s|%s", entity.getName(), id);
    }


    public static String getTxUID(EntityDB entity) {
        return getTxUID(entity, entity.getSingleId().getAsString());
    }

    /**
     * overload to produce actions
     * Actions are displayed in a combox box at the top of the grid layout
     * Actions can be performed by selecting multiple rows and then applying the action to it
     *
     * @return
     */
    public String[] getActions() {
        return new String[]{};
    }

    /**
     * @param action
     * @param entities
     */

    public void action(String action, List<EntityDB> entities) {

    }

    /**
     * overload to produce filters
     * Filters are displayed in a combox box at the top of the grid layout
     * A filter is a sub select ( extra where clause )
     *
     * @return
     */
    public String[][] getFilters() {
        return new String[][]{};
    }


}
