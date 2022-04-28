//package za.co.spsi.mdms.web.gui.charts.meter;
//
//import com.vaadin.addon.charts.Chart;
//import com.vaadin.addon.charts.PointClickEvent;
//import za.co.spsi.toolkit.crud.chart.AbstractChart;
//import za.co.spsi.toolkit.crud.chart.DrillDownEvent;
//import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
//import za.co.spsi.toolkit.ee.properties.TextFile;
//
//import javax.annotation.Resource;
//import javax.enterprise.inject.spi.BeanManager;
//import javax.inject.Inject;
//import javax.sql.DataSource;
//import java.util.List;
//
//public class MeterPerMakeReadingsDailyChart extends AbstractChart {
//
//    @Inject
//    @TextFile("charts/meter_reading_daily_column_chart.sql")
//    private String dailySql;
//
//    @Resource(mappedName = "java:/jdbc/mdms")
//    private DataSource dataSource;
//
//    @Inject
//    private BeanManager beanManager;
//
//    @Inject
//    private ToolkitLookupServiceHelper lookupServiceHelper;
//
//
//    public MeterPerMakeReadingsDailyChart() {
//        setTitle("Meter Reading daily for month");
//    }
//
//    @Override
//    protected DataSource getDataSource() {
//        return dataSource;
//    }
//
//
//    @Override
//    protected Chart createChart(boolean gradientColor) {
//        return super.buildColumnChart(getTitle(), dailySql, gradientColor, 10);
//    }
//
//    @Override
//    public AbstractChart[] getDrillDownChart(List<DrillDownEvent> drillDownEvents, PointClickEvent event) {
//        return null;
//    }
//
//    public String getDailySql() {
//        return dailySql;
//    }
//
//    public void setDailySql(String dailySql) {
//        this.dailySql = dailySql;
//    }
//}
