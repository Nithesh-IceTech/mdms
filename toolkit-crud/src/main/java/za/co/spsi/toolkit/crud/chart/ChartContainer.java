package za.co.spsi.toolkit.crud.chart;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.PointClickListener;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.Series;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.VerticalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by jaspervdb on 2016/08/17.
 */
public class ChartContainer extends VerticalLayout {

    private Stack<AbstractChart> charts = new Stack<AbstractChart>();
    private List<DrillDownEvent> drillDownEvents = new ArrayList<>();

    private ChartGroup chartGroup;
    private ChartListener chartListener;
    private Chart chart = null;
    private ArrayList<Integer> popsNeeded = new ArrayList<>();


    public ChartContainer(ChartGroup chartGroup, AbstractChart abstractChart) {
        this.chartGroup = chartGroup;
        setWidth("100%");
        setHeight("-1px");
        setMargin(new MarginInfo(false, false, true, false));
        buildChart(abstractChart);
    }

    public ChartListener getChartListener() {
        return chartListener;
    }

    public void setChartListener(ChartListener chartListener) {
        this.chartListener = chartListener;
    }


    public void back() {
        removeAllComponents();
        for (int i = 0; i < popsNeeded.get(popsNeeded.size() - 1); i++) {
            charts.pop();
        }
        popsNeeded.remove(popsNeeded.size() - 1);
        buildChart(charts.pop());
        drillDownEvents.remove(drillDownEvents.size() - 1);
    }

    public void buildChart(final AbstractChart abstractChart) {
        chart = abstractChart.createChart(chartGroup.drawGradient());
        chart.addPointClickListener((PointClickListener) event -> {

            Series dataSeries = event.getSeries();
            AbstractChart[] drillDownCharts = abstractChart.getDrillDownChart(drillDownEvents, event);

            if (drillDownCharts != null) {
                removeAllComponents();
                if (chartListener != null) {
                    chartListener.drilledDown(ChartContainer.this);
                }
                popsNeeded.add(drillDownCharts.length);

                if (dataSeries instanceof DataSeries) {
                    drillDownEvents.add(new DrillDownEvent(((DataSeries)dataSeries).getData().get(event.getPointIndex()), abstractChart));
                } else if (dataSeries instanceof ListSeries) {
                    drillDownEvents.add(new DrillDownEvent(((ListSeries)dataSeries), abstractChart));
                }

                for (AbstractChart innerchart : drillDownCharts) {
                    buildChart(innerchart);
                }
            }
        });
        charts.add(abstractChart);
        addComponent(chart);
    }


    public static interface ChartListener {
        void drilledDown(ChartContainer chartContainer);
    }


}
