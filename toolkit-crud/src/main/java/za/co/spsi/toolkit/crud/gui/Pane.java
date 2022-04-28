package za.co.spsi.toolkit.crud.gui;


import com.vaadin.server.ClientConnector;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.UI;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.ano.UILayout;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.Viewable;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2/11/16.
 */
public class Pane extends AbstractView {

    private String sql;
    private Class<? extends Layout> layoutClass = null;
    private Layout layoutInstance;
    private String captionId;
    private EntityDB workingEntity;

    private Permission permission;

    private CssLayout root = new CssLayout();

    public Pane(String captionId, Group groups[], Layout layout) {
        super(captionId, layout);
        layout.add(this);
        addViews(groups);
    }

    public Pane(String captionId, String sql, Class<? extends Layout> layoutClass, Permission permission, Layout layout) {
        super(captionId, layout);
        layout.add(this);
        this.layoutClass = layoutClass;
        this.sql = sql;
        this.permission = permission;
    }

    public Pane(String captionId, String sql, Class<? extends Layout> layoutClass, Permission permission, Layout layout, EntityDB workingEntity) {
        this(captionId, sql, layoutClass, permission, layout);
        this.workingEntity = workingEntity;
    }

    public Pane(String captionId, String sql, Class<? extends Layout> layoutClass, Layout layout) {
        this(captionId, sql, layoutClass, new Permission(Permission.PERMISSION_ALL), layout);
    }

    public Permission getPermission() {
        return permission;
    }

    public LFieldList getFields() {
        LFieldList fields = new LFieldList();
        for (Viewable viewable : getViews()) {
            if (viewable instanceof Group) {
                fields.addAll(((Group) viewable).getFields());
            }
        }
        return fields.apply();
    }


    @Override
    public void intoBindings() {
        getViews().stream().filter(view -> view.isBuilt() && view.getComponent().isVisible()).forEach(Viewable::intoBindings);
    }

    /**
     * format the sql to complete the parent relationship
     *
     * @return
     */
    public String getFormattedSql(Layout layout) {

        EntityDB mainEntity = null;
        if (workingEntity != null) {
            mainEntity = workingEntity;
        } else {
            mainEntity = layout.getParentLayout().getMainEntity();
        }

        String formatted = sql;
        if (formatted.indexOf("?") != -1) {
            Field field = mainEntity.getSingleId();
            formatted = formatted.replace("?", String.format("'%s'", field.get() != null ? field.get().toString() : "-1"));
        } else {
            for (Field field : mainEntity.getFields()) {
                while (formatted.toLowerCase().indexOf(mainEntity.getFullColumnName(field).toLowerCase()) != -1) {
                    formatted = formatted.substring(0, formatted.toLowerCase().indexOf(mainEntity.getFullColumnName(field).toLowerCase())) +
                            String.format("'%s'", field.get() != null ? field.get().toString() : "-1") + formatted.substring(formatted.toLowerCase().indexOf(mainEntity.getFullColumnName(field).toLowerCase()) + mainEntity.getFullColumnName(field).length());
                }
            }
        }
        return formatted;
    }

    public String getSql() {
        return sql;
    }

    public Layout getLayoutInstance() {
        try {
            if (layoutInstance == null) {
                layoutInstance = layoutClass.newInstance();
            }
            return layoutInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Pane(String captionId, Layout layout, Group... groups) {
        this(captionId, groups, layout);
    }

    public Class<? extends Layout> getLayoutClass() {
        return layoutClass;
    }

    private int getUIColumn(UIGroup uiGroup) {
        if (uiGroup == null) {
            return 0;
        }
        for (UILayout uiLayout : uiGroup.layout()) {
            if (uiLayout.minWidth() >= Page.getCurrent().getBrowserWindowWidth()) {
                return uiLayout.column();
            }
        }
        return uiGroup.column();
    }

    private MVerticalLayout rebuildComponent() {
        MHorizontalLayout root = new MHorizontalLayout().withFullHeight().withFullWidth().withStyleName("group-panel");
        MVerticalLayout vRoot = new MVerticalLayout(root).withMargin(false).withSpacing(false);

        root.add(new MVerticalLayout().withHeight("-1px").withMargin(new MarginInfo(false, false, true, false)));
        getViews().forEach((k) -> {
            if (k instanceof Group) {
                int column = getUIColumn(k.getAnnotation(UIGroup.class));
                for (int i = root.getComponentCount(); i < column + 1; i++) {
                    root.add(new MVerticalLayout().withHeight("-1px").withMargin(new MarginInfo(false, false, true, false)));
                }
            }
        });
        getViews().forEach((k) -> {
                    AbstractLayout container = (AbstractLayout) root.getComponent(0);
                    if (k instanceof Group) {
                        int column = getUIColumn(((Group) k).getAnnotation(UIGroup.class));
                        container = column == -1 ? vRoot :
                                (AbstractLayout) root.getComponent(column);
                    }
                    Component component = k.getComponent();
                    component.setWidth("100%");
                    container.addComponent(component);
                }
        );
        return vRoot;
    }

    @Override
    public Component buildComponent() {
        root.setSizeFull();
        root.addComponent(rebuildComponent());
        getLayout().beforeOnScreenEvent();
        final Page.BrowserWindowResizeListener browserWindowResizeListener = (Page.BrowserWindowResizeListener) event -> {
            root.removeAllComponents();
            root.addComponent(rebuildComponent());
        };

        UI.getCurrent().getPage().addBrowserWindowResizeListener(browserWindowResizeListener);
        root.addDetachListener((ClientConnector.DetachListener) event -> UI.getCurrent().getPage().removeBrowserWindowResizeListener(browserWindowResizeListener));
        return root;
    }

    public void setGroupsVisible(boolean visible) {
        getViews().forEach((k) -> {
            k.getComponent().setVisible(visible);
        });

    }

}
