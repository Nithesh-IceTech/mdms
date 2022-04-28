package za.co.spsi.toolkit.crud.gui;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalSplitPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.toolkit.ano.AgencyUIQualifier;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.crud.gui.ano.OneToOne;
import za.co.spsi.toolkit.crud.gui.query.LayoutViewGrid;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.util.Assert;

import javax.enterprise.inject.spi.BeanManager;
import javax.sql.DataSource;

import static za.co.spsi.toolkit.crud.gui.ToolkitUI.mayView;

/**
 * Created by jaspervdb on 4/20/16.
 */
public class PaneList extends ViewList<Pane> {

    /**
     * Panes filtered by roles
     * @return
     */
    public PaneList getQualifiedPanes() {
        PaneList qualified = new PaneList();
        for (Pane pane : apply()) {
            if (pane.getLayoutClass() == null || mayView(pane.getLayoutClass().getAnnotation(Qualifier.class)) &&
                    mayView(pane.getAnnotation(AgencyUIQualifier.class))) {
                qualified.add(pane);
            }
        }
        return qualified;
    }

    private PaneList getByType(boolean list) {
        PaneList panes = new PaneList();
        for (Pane pane : getQualifiedPanes()) {
            if (pane.getLayoutClass() == null && !list || list && pane.getLayoutClass() != null) {
                panes.add(pane);
            }
        }
        return panes;
    }

    public PaneList getNonLists() {
        return getByType(false);
    }

    public PaneList getLists() {
        return getByType(true);
    }

    public boolean anyLists() {
        return !getLists().isEmpty();
    }

    public LFieldList getFields() {
        LFieldList fields = new LFieldList();
        for (Pane pane : this) {
            fields.addAll(pane.getFields());
        }
        return fields;
    }

    public MVerticalLayout buildNonList() {
        MVerticalLayout north = new MVerticalLayout().withMargin(false).withFullHeight().withFullWidth();
        Assert.isTrue(!getNonLists().isEmpty(),"You need to define at least one non list Pane component in layout %s",get(0).getLayout().getClass());
        for (Pane pane : getNonLists()) {
            north.add(pane.getComponent());
        }
        north.setHeightUndefined();
        return north.withMargin(false).withFullWidth();
    }

    private TabSheet buildLists(DataSource dataSource,BeanManager beanManager, LayoutViewGrid.Callback callback) {
        final TabSheet tabSheet = new TabSheet();

        tabSheet.setSizeFull();
        for (Pane pane : getLists()) {
            tabSheet.addTab(new LayoutPaneHolder(dataSource,beanManager,pane,callback),pane.getCaption().toUpperCase());
        }
        // build the very first component
        ((LayoutPaneHolder)tabSheet.getTab(0).getComponent()).getComponent();
        tabSheet.addSelectedTabChangeListener((TabSheet.SelectedTabChangeListener) event -> {
                ((LayoutPaneHolder)tabSheet.getSelectedTab()).getComponent();

        });
        return tabSheet;
    }

    public Component buildComponent(DataSource dataSource,BeanManager beanManager, LayoutViewGrid.Callback callback) {
        // if there are any lists defined then we need to build a split panel layout
        if (anyLists()) {
            VerticalSplitPanel splitPanel = new VerticalSplitPanel(
                    buildNonList(),buildLists(dataSource,beanManager,callback));
            splitPanel.setSplitPosition(60, Sizeable.Unit.PERCENTAGE);
            splitPanel.setSizeFull();
            return splitPanel;
        } else {
            return new Panel(buildNonList());
        }
    }

    public static class LayoutPaneHolder extends MVerticalLayout {

        private Component component;
        private DataSource dataSource;
        private BeanManager beanManager;
        private LayoutViewGrid.Callback callback;
        private Pane pane;

        public LayoutPaneHolder(DataSource dataSource, BeanManager beanManager, Pane pane, LayoutViewGrid.Callback callback) {
            this.pane = pane;
            this.dataSource = dataSource;
            this.beanManager = beanManager;
            this.callback = callback;
            setSizeFull();
        }

        public Component getComponent() {
            if (component == null) {
                Layout layout = Util.getLayout(beanManager,pane.getLayoutClass());
                // init
                layout.getPermission().updatePermission(pane.getPermission());
                layout.setParentLayout(pane.getLayout());
                if (pane.getAnnotation(OneToOne.class)!=null) {
                    layout.getPermission().setMayCreate(false);
                }
                component = layout.buildList(
                        dataSource,pane.getFormattedSql(layout),
                        callback,layout.shouldAddFilterOnPaneList());
                component.setSizeFull();
                add(component);
            }
            return this;
        }
    }
}
