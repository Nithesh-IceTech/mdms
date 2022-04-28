package za.co.spsi.toolkit.crud.chart;

import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.PointClickEvent;
import com.vaadin.addon.charts.model.*;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.style.GradientColor;
import com.vaadin.addon.charts.model.style.SolidColor;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StringUtils;

import javax.sql.DataSource;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.List;

/**
 * Created by jaspervdb
 */
public abstract class AbstractChart {

    static List<Color> COLORS = new ArrayList<>();

    static {
        for (int y1 : new int[]{0, 32, 96, 64, 192, 127, 255}) {
            for (int y2 : new int[]{255, 127, 192, 64, 96, 32, 0}) {
                if (y1 != y2) {
                    for (int i = 0; i < 3; i++) {
                        COLORS.add(new Color(i % 3 == 0 ? y2 : y1, i % 3 == 1 ? y2 : y1, i % 3 == 2 ? y2 : y1));
                    }
                }
            }
        }
    }

    public static PlotOptionsColumn plotFatColumn;
    public static PlotOptionsSpline plotOptionsSpline = new PlotOptionsSpline();

    static {
        plotFatColumn = new PlotOptionsColumn();
        plotFatColumn.setBorderWidth(5);
        plotFatColumn.setPointPadding(5);
    }

    protected abstract DataSource getDataSource();

    private String title = "", subtitle = "";

    private Button refresh = new Button(), back = new Button();

    private CssLayout reportLayout = new CssLayout();

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getCamelCase(String value) {
        StringList newName = new StringList();
        for (String name : value.split(" ")) {
            if (name.length() > 0) {
                newName.add(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
            }
        }
        return newName.toString(" ");
    }

    public String formatSql(String sql) {
        return sql.replace("_AGENCY_ID_", ToolkitCrudConstants.getChildAgencyId().toString()).
                replace("_PARENT_ID_", ToolkitCrudConstants.getParentAgencyId().toString()).
                replace("_LANG_", ToolkitCrudConstants.getLocale());
    }

    public DataSeries getPieDataSeries(DataSource dataSource, int limit, boolean gradientColor, String sql, Object... ref) {
        try {
            try (Connection connection = dataSource.getConnection();) {
                PreparedStatement ps = DataSourceDB.prepareStatement(connection, formatSql(sql), ref);
                ResultSet rs = ps.executeQuery();
                // pie chart may only have 2 columns, first is a string second is a integrate
                DataSeries series = new DataSeries();

                int cnt = 0, otherTotal = 0;
                while (rs.next()) {
                    if (cnt++ < limit) {
                        String name = rs.getString(1) != null ? AbstractView.getLocaleValue(rs.getString(1)) : "Null";
                        DataSeriesItem dataSeriesItem = new DataSeriesItem(name, rs.getInt(2));
                        if (gradientColor) {
                            setGradientColor(cnt - 1, dataSeriesItem);
                        }
                        series.add(dataSeriesItem);
                    } else {
                        otherTotal += rs.getInt(2);
                    }
                }
                if (otherTotal > 0) {
                    DataSeriesItem dataSeriesItem = new DataSeriesItem("Other", otherTotal);
                    series.add(dataSeriesItem);
                }
                return series;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public ListSeriesResult getListDataSeries(DataSource dataSource, int limit, boolean gradientColor, String sql, Object... ref) {
        try {
            try (Connection connection = dataSource.getConnection();) {

                PreparedStatement ps = DataSourceDB.prepareStatement(connection, formatSql(sql), ref);
                ResultSet rs = ps.executeQuery();
                // pie chart may only have 2 columns, first is a string second is a integrate
                TreeMap<String, ArrayList<Number>> legendMap = new TreeMap<>();
                List<String> xList = new ArrayList<>();
                List<String> resultList = new ArrayList<>();

                while (rs.next()) {
                    String legend = rs.getString("LEGEND");
                    Assert.notNull(legend, "Legend can't be null");
                    String x = rs.getString("X");
                    Assert.notNull(x, "X can't be null");
                    String y = rs.getString("Y");
                    Assert.notNull(y, "Y can't be null");
                    resultList.add(legend+","+x+","+y);

                    if (!legendMap.containsKey(legend)) {
//                        ArrayList<Number> list = legendMap.get(legend);
//                        list.add(new BigDecimal(y));
//                    } else {
                        ArrayList<Number> list = new ArrayList<>();
//                        list.add(new BigDecimal(y));
                        legendMap.put(legend, list);
                    }
                    if(!xList.contains(x)) {
                        xList.add(x);
                    }

                }
                //Make sure that if some of the X values only has a value for one of the legened values, that a 0 is inserted for the other
                int numberOfValuesAdded = 0;
                int numberOfNeededValues = 0;
                for(String x:xList){
                    numberOfNeededValues++;
                    for(String result:resultList){
                        String[] resulSet = result.split(",");
                        if(x.equals(resulSet[1])){
                            ArrayList<Number> list = legendMap.get(resulSet[0]);
                            list.add(new BigDecimal(resulSet[2]));
                            numberOfValuesAdded++;
                        }
                    }
                    if(numberOfValuesAdded<legendMap.size()){
                        for(Map.Entry<String,ArrayList<Number>> entry : legendMap.entrySet()) {
                            if(legendMap.get(entry.getKey()).size() < numberOfNeededValues){
                                legendMap.get(entry.getKey()).add(new BigDecimal(0));
                            }
                        }
                        numberOfValuesAdded = 0;
                    }
                }

                ArrayList<ListSeries> listSeriesArrayList = new ArrayList<>();

                for (String item : legendMap.keySet()) {
                    legendMap.get(item);
                    ListSeries listSeries = new ListSeries(item, legendMap.get(item).toArray(new BigDecimal[0]));
                    listSeriesArrayList.add(listSeries);
                }

                return new ListSeriesResult(xList, listSeriesArrayList);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    private void setGradientColor(int index, DataSeriesItem dataSeriesItem) {
        Color color = COLORS.get(index % COLORS.size());
        GradientColor gradient = GradientColor.createRadial(0.5, 0.3, 0.7);
        gradient.addColorStop(0, new SolidColor(color.getRed(), color.getGreen(), color.getBlue()));
        gradient.addColorStop(1, new SolidColor(color.getRed() / 2, color.getGreen() / 2, color.getBlue() / 2));

        dataSeriesItem.setColor(gradient);
    }

    /**
     * get the selected value from the click event
     *
     * @param event
     * @return
     */
    public DataSeriesItem getSelectedValue(PointClickEvent event) {
        return ((DataSeries) event.getSeries()).get(event.getPointIndex());
    }

    public Chart buildPieChart(String pointType, boolean gradient, int limit, String sql, Object... ref) {
        Chart chart = new Chart(ChartType.PIE);
        chart.setWidth("100%");
        Configuration conf = chart.getConfiguration();
        conf.setTitle(AbstractView.getLocaleValue(title));
        if (!StringUtils.isEmpty(subtitle)) {
            conf.setSubTitle(AbstractView.getLocaleValue(subtitle));
        }

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        tooltip.setPointFormat("{point.y:0f}" + pointType);
        conf.setTooltip(tooltip);

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setDepth(10);
        plotOptions.setShowInLegend(true);
        plotOptions.setAnimation(true);
        plotOptions.setBorderColor(SolidColor.BLACK);

        conf.setPlotOptions(plotOptions);
        DataSeries series = getPieDataSeries(getDataSource(), limit, gradient, sql, ref);

        conf.setSeries(series);

        chart.drawChart();

        return chart;
    }

    protected Chart buildColumnChart(String title, String sql, boolean gradient, int limit) {

        Chart chart = new Chart(ChartType.COLUMN);
        chart.setWidth("100%");
        Configuration conf = chart.getConfiguration();
        conf.setTitle(AbstractView.getLocaleValue(title));
        if (!StringUtils.isEmpty(subtitle)) {
            conf.setSubTitle(AbstractView.getLocaleValue(subtitle));
        }

        XAxis x = new XAxis();
        conf.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle(title);
        conf.addyAxis(y);

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setBackgroundColor(new SolidColor("#FFFFFF"));
        legend.setAlign(HorizontalAlign.LEFT);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setX(100);
        legend.setY(70);
        legend.setFloating(true);
        legend.setShadow(true);
        conf.setLegend(legend);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter("this.x +':  '+ this.y");
        conf.setTooltip(tooltip);

        PlotOptionsColumn plot = new PlotOptionsColumn();
        plot.setPointPadding(0.2);
        plot.setBorderWidth(0);

        ListSeriesResult listSeriesResult = getListDataSeries(getDataSource(), limit, gradient, sql, null);
        conf.setSeries(listSeriesResult.getListSeries().toArray(new ListSeries[0]));
        x.setCategories(listSeriesResult.xAxisList.toArray(new String[0]));

        chart.drawChart(conf);
        return chart;
    }

    protected List<List> mapResultSetValues(String sql, Object[] params) {
        try {
            List<List> listValues = new ArrayList<List>();
            Connection connection = getDataSource().getConnection();
            try {
                PreparedStatement ps = DataSourceDB.prepareStatement(connection, sql, params);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    List values = new ArrayList();
                    for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                        values.add(rs.getObject(i + 1));
                    }
                    listValues.add(values);
                }
                return listValues;
            } finally {
                connection.close();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String formatToolTip(List<String> columns, List<String> pointTypes) {
        if (columns.size() > 2) {
            return "(this.series.name == '" + columns.remove(0) + "' ? ' " + pointTypes.remove(0) + "' : " + formatToolTip(columns, pointTypes) + ")";
        } else {
            return "(this.series.name == '" + columns.remove(0) + "' ? ' " + pointTypes.remove(0) + "' : '" + pointTypes.remove(0) + "')";
        }
    }

    private String formatToolTip(String[] columns, String[] pointTypes) {
        List<String> colList = new ArrayList<String>(), pointList = new ArrayList<String>();
        List<Boolean> roundList = new ArrayList<Boolean>();
        Collections.addAll(colList, columns);
        Collections.addAll(pointList, pointTypes);
        return "new Date(this.x).toLocaleString() +': '+ this.y + " + formatToolTip(colList, pointList);
    }


    /**
     * select timestamp, seriesName, value
     *
     * @param title
     * @param sql
     * @param ref
     * @return
     */
    public Chart buildTimeLine(String title, String sql, Object... ref) {
        final Chart chart = new Chart();
        chart.setSizeFull();
        chart.setTimeline(true);

        Configuration configuration = chart.getConfiguration();
        configuration.getTitle().setText(AbstractView.getLocaleValue(title));

        YAxis yAxis = new YAxis();
        Labels label = new Labels();
        label.setFormatter("(this.value > 0 ? ' + ' : '') + this.value");
        yAxis.setLabels(label);

        PlotLine plotLine = new PlotLine();
        plotLine.setValue(2);
        plotLine.setWidth(2);
        plotLine.setColor(SolidColor.SILVER);
        yAxis.setPlotLines(plotLine);
        configuration.addyAxis(yAxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setPointFormat("<span style=\"color:{series.color}\">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>");
        tooltip.setValueDecimals(2);
        configuration.setTooltip(tooltip);


        List<List> valuesList = mapResultSetValues(formatSql(sql), ref);
        DataSeriesList series = new DataSeriesList();

        for (List values : valuesList) {
            if (!series.contains(values.get(1))) {
                series.add(new DataSeries((String) values.get(1)));
            }
            DataSeriesItem item = new DataSeriesItem();
            item.setX((Date) values.get(0));
            item.setY((Number) values.get(2));
            series.getByName((String) values.get(1)).add(item);
        }


        configuration.setSeries(series.toArray(new DataSeries[]{}));

        Legend legend = new Legend();
        legend.setLayout(LayoutDirection.VERTICAL);
        legend.setVerticalAlign(VerticalAlign.TOP);
        legend.setFloating(true);
        configuration.setLegend(legend);

        RangeSelector rangeSelector = new RangeSelector();
        rangeSelector.setSelected(1);
        configuration.setRangeSelector(rangeSelector);

        chart.drawChart(configuration);

        return chart;
    }

    protected abstract Chart createChart(boolean gradientColor);

//    public abstract String getName();

    public abstract AbstractChart[] getDrillDownChart(List<DrillDownEvent> drillDownEvents, PointClickEvent event);

    public class ListSeriesResult {
        private List<String>  xAxisList;
        private List<ListSeries> listSeries;

        public ListSeriesResult(List<String> xAxisList, ArrayList<ListSeries> listSeries) {
            this.xAxisList = xAxisList;
            this.listSeries = listSeries;
        }

        public List<String> getxAxisList() {
            return xAxisList;
        }

        public void setxAxisList(List<String> xAxisList) {
            this.xAxisList = xAxisList;
        }

        public List<ListSeries> getListSeries() {
            return listSeries;
        }

        public void setListSeries(List<ListSeries> listSeries) {
            this.listSeries = listSeries;
        }
    }

}
