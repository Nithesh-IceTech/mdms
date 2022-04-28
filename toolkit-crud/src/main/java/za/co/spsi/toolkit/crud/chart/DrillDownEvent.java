package za.co.spsi.toolkit.crud.chart;

import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;

/**
 * Created by jaspervdb on 2016/09/07.
 */
public class DrillDownEvent {

    private ListSeries listSeries;
    private DataSeriesItem dataSeriesItem;
    private AbstractChart chart;

    public DrillDownEvent(DataSeriesItem dataSeriesItem, AbstractChart chart) {
        this.dataSeriesItem = dataSeriesItem;
        this.chart = chart;
    }

    public DrillDownEvent(ListSeries listSeries, AbstractChart chart) {
        this.listSeries = listSeries;
        this.chart = chart;
    }

    public DataSeriesItem getDataSeriesItem() {
        return dataSeriesItem;
    }

    public void setDataSeriesItem(DataSeriesItem dataSeriesItem) {
        this.dataSeriesItem = dataSeriesItem;
    }

    public AbstractChart getChart() {
        return chart;
    }

    public void setChart(AbstractChart chart) {
        this.chart = chart;
    }

    public ListSeries getListSeries() {
        return listSeries;
    }

    public void setListSeries(ListSeries listSeries) {
        this.listSeries = listSeries;
    }
}
