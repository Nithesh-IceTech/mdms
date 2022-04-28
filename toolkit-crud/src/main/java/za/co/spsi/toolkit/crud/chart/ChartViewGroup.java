package za.co.spsi.toolkit.crud.chart;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 */
public class ChartViewGroup extends HorizontalLayout {

    private TabSheet editors;

    private ChartGroup current = null;
    private List<ChartGroup> chartGroups = new ArrayList<>();

    public ChartViewGroup() {
        setSizeFull();
        editors = new TabSheet();
        editors.setSizeFull();
        addComponent(editors);
        // add all the views
        editors.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                if (current != null) {
                    current.exit();
                    current = ((ChartGroup) editors.getSelectedTab());
                    current.enter();
                }
            }
        });
    }

    public void addChartGroup(ChartGroup chartGroup) {
        chartGroups.add(chartGroup);
        editors.addTab(chartGroup, AbstractView.getLocaleValue(chartGroup.getName()));
        current = current == null ? chartGroup : current;
    }

    public List<ChartGroup> getChartGroups() {
        return chartGroups;
    }

    public Component getComponent() {
        return this;
    }

    /**
     * view has been displayed on screen, send the event to the current active child view
     */
    public void enter() {
        ((ChartGroup) editors.getSelectedTab()).enter();
    }

    /**
     * view has been removed from the screen, send the event to the current active child view
     */
    public void exit() {
        ((ChartGroup) editors.getSelectedTab()).exit();
    }
}
