package za.co.spsi.toolkit.crud.gui;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.ui.grid.ScrollDestination;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.crud.LayoutException;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitMaskMessage;
import za.co.spsi.toolkit.crud.gui.query.LayoutViewGrid;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.locale.TranslatableTabSheet;
import za.co.spsi.toolkit.crud.locale.TranslateProcessor;
import za.co.spsi.toolkit.crud.locale.VaadinLocaleHelper;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.EntityDB;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.*;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;
import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created by jaspervdb on 4/20/16.
 */
public abstract class CrudView extends MVerticalLayout implements CrudViewable {

    @Inject
    private ToolkitMaskMessage toolkitMaskMessage;

    @Inject
    private ToolkitLookupServiceHelper lookupServiceHelper;

    @Inject
    private BeanManager beanManager;

    private List<Class<? extends Layout>> layoutClasses = new ArrayList<>();
    private TranslatableTabSheet root;
    private Map<String, TabSheet.Tab> tabLayouts = new HashMap<>();
    private Map<TabSheet.Tab, Layout> tabSheetLayouts = new HashMap<>();
    private String locale;

    public CrudView() {
        withMargin(false).withSpacing(false);
    }

    protected abstract DataSource getDataSource();

    public void addLayout(Class<? extends Layout> layout) {
        layoutClasses.add(layout);
    }

    public List<Class<? extends Layout>> getLayoutClasses() {
        return layoutClasses;
    }

    public Collection<Layout> getLayouts() {
        return tabSheetLayouts.values();
    }

    public void clear() {
        removeAllComponents();
        tabLayouts.clear();
        tabSheetLayouts.clear();
    }

    public void releaseAllTx() {
        for (Layout layout : tabSheetLayouts.values()) {
            ToolkitUI.removeExclusiveTx(layout);
        }
    }

    @Override
    public Component getRoot() {
        return this;
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        // build the screen
        if (getComponentCount() == 0) {
            setSizeFull();
            root = new TranslatableTabSheet();
            TabSheet.CloseHandler closeHandler = (TabSheet.CloseHandler) (tabsheet, c) -> {
                Runnable execute = () -> {
                    ToolkitUI.removeExclusiveTx(tabSheetLayouts.get(tabsheet.getTab(c)));
                    tabLayouts.remove(tabSheetLayouts.get(tabsheet.getTab(c)).getTxUID());
                    tabSheetLayouts.remove(tabsheet.getTab(c));
                    tabsheet.removeComponent(c);
                };
                // check if its safe to close the tab
                if (
                        tabSheetLayouts.get(tabsheet.getTab(c)).getPermission().mayUpdate() &&
                                tabSheetLayouts.get(tabsheet.getTab(c)).getFields().getVisible().isChanged()) {
                    MessageBox.createQuestion().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.CLOSE_TAB)).
                            withMessage(AbstractView.getLocaleValue(ToolkitLocaleId.UNSAVED_CHANGES_WILL_BE_LOST)).
                            withYesButton(execute,
                                    ButtonOption.caption(AbstractView.getLocaleValue(ToolkitLocaleId.YES).toUpperCase()),
                                    ButtonOption.closeOnClick(true)).
                            withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()),
                                    ButtonOption.closeOnClick(true)).open();
                } else {
                    execute.run();
                }
            };
            root.setCloseHandler(closeHandler);
            add(root);
            root.setSizeFull();
            for (Class<? extends Layout> layoutClass : layoutClasses) {
                final Layout layout = getBean(beanManager, layoutClass);
                layout.getPermission().initFrom(layout.getClass().getAnnotation(Qualifier.class));
                TabSheet.Tab tab = root.addTab(layout.buildList(getDataSource(), new LayoutViewGrid.Callback() {
                    @Override
                    public void selected(Layout l, Component source, boolean newEvent, EntityDB entityDB) {
                        if (!tabLayouts.containsKey(Layout.getTxUID(entityDB))) {

                            Layout clone = Util.getLayout(beanManager, l.getClass());
                            clone.getPermission().updatePermission(l.getPermission());
                            clone.selected(clone, source, newEvent, entityDB);
                            boolean locked = !(ToolkitUI.isTxExclusive(clone) || ToolkitUI.getToolkitUI().getUsername().equals(ToolkitUI.getLockedUserName(clone)));
                            if (l.getParentLayout() != null) {
                                clone.setParentLayout(l.getParentLayout());
                                clone.initPermission(l.getParentLayout().getPermission());
                            }
                            clone.getPermission().initFrom(clone.getClass().getAnnotation(Qualifier.class));
                            clone.getPermission().setPermissionFlag(locked ? 0 : clone.getPermission().getPermissionFlag());
                            try {
                                if (newEvent) {
                                    clone.newEvent();
                                }
                                // children
                                TabSheet.Tab tab = root.addDetailTab(clone.buildComponent(getDataSource(), beanManager, this), clone.getCaption(), clone.getUniqueCaption());
                                tab.setClosable(true);
                                root.setSelectedTab(tab);
                                tabLayouts.put(Layout.getTxUID(entityDB), tab);
                                tabSheetLayouts.put(tab, clone);

                                if (locked) {
                                    MessageBox.createInfo().withMessage(String.format("%s. %s",
                                            getLocaleValue(ToolkitLocaleId.TX_ALREADY_LOCK_IN_ANOTHER_SESSION),
                                            ToolkitUI.getLockedTxMachineName(clone))).
                                            withOkButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase())).open();
                                }
                            } catch (LayoutException le) {
                                le.showMessage();
                            }
                        } else {
                            root.setSelectedTab(tabLayouts.get(Layout.getTxUID(entityDB)));
                        }
                        ToolkitUI.getToolkitUI().setLastTxUid(Layout.getTxUID(entityDB));
                    }
                }), layout.getCaption());
                tabLayouts.put(layout.getTxUID(), tab);
                tabSheetLayouts.put(tab, layout);
            }
            root.addSelectedTabChangeListener((TabSheet.SelectedTabChangeListener) event -> {
                Layout l = tabSheetLayouts.get(event.getTabSheet().getTab(event.getTabSheet().getSelectedTab()));
                if (l != null) {
                    if (l.getLayoutViewGrid() == null) {
                        l.beforeOnScreenEvent();
                    } else if (l.getLayoutViewGrid().getSelected() != null) {
                        try {
                            if (l.getLayoutViewGrid().getSelectionModel() instanceof Grid.MultiSelectionModel) {
                                l.getLayoutViewGrid().scrollTo(((List) l.getLayoutViewGrid().getSelectionModel().getSelectedRows()).get(0), ScrollDestination.START);
                            } else {
                                l.getLayoutViewGrid().scrollTo(l.getLayoutViewGrid().getSelectedRow(), ScrollDestination.START);
                            }
                        } catch (Exception ex) {
                            // could be that it has not been saved - in which case will throw an exception
                        }
                    }
                }
            });
        } else if (!locale.equals(ToolkitCrudConstants.getLocale())) {
            // translate the screen
            VaadinLocaleHelper.translateComponent(root, locale, ToolkitCrudConstants.getLocale());
        }
        locale = ToolkitCrudConstants.getLocale();
    }

    public void handleTranslationCompleteEvent(@Observes TranslateProcessor.TranslationCompleteEvent event) {
        if (UI.getCurrent().getNavigator() != null && UI.getCurrent().getNavigator().getCurrentView().getClass().equals(getClass())) {
            locale = event.getEvent().getNewLocale();
        }
    }
}