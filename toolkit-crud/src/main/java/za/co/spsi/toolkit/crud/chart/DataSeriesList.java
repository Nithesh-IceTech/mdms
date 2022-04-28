package za.co.spsi.toolkit.crud.chart;

import com.vaadin.addon.charts.model.DataSeries;

import java.util.ArrayList;

/**
 * Created by jaspervdb on 2016/09/06.
 */
public class DataSeriesList extends ArrayList<DataSeries> {

    public DataSeries getByName(String name) {
        for (DataSeries dataSeries : this) {
            if (dataSeries.getName().equals(name)) {
                return dataSeries;
            }
        }
        return null;
    }

    public boolean contains(String name) {
        return getByName(name) != null;
    }

}
