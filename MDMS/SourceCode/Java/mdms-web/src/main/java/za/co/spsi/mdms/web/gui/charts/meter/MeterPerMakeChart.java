//package za.co.spsi.mdms.web.gui.charts.meter;
//
//import com.vaadin.addon.charts.Chart;
//import com.vaadin.addon.charts.PointClickEvent;
//import com.vaadin.addon.charts.model.DataSeries;
//import za.co.spsi.locale.annotation.MdmsLocaleId;
//import za.co.spsi.mdms.web.gui.charts.ChartHelper;
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
//public class MeterPerMakeChart extends AbstractChart {
//
//    @Inject
//    @TextFile("charts/meter_make.sql")
//    private String sql;
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
//    public MeterPerMakeChart() {
//        setTitle(MdmsLocaleId.METER_PER_MAKE);
//    }
//
//    @Override
//    protected DataSource getDataSource() {
//        return dataSource;
//    }
//
//    @Override
//    protected Chart createChart(boolean gradientColor) {
//        return super.buildPieChart(" ", gradientColor, 10, sql);
//    }
//
//    @Override
//    public AbstractChart[] getDrillDownChart(List<DrillDownEvent> drillDownEvents, PointClickEvent event) {
//
//        ChartHelper.MeterType selected =
//                ChartHelper.MeterType.getByName(((DataSeries) event.getSeries()).get(event.getPointIndex()).getName());
//
//        MeterStateChart meterDetailChart =
//                MeterStateChart.getNewInstance(beanManager, event, MdmsLocaleId.METER_PER_STATE, selected.tableName);
//
//        MeterModelChart meterModelChart =
//                MeterModelChart.getNewInstance(beanManager, event, MdmsLocaleId.METER_PER_MODEL, selected.tableName);
//
//        return new AbstractChart[] {meterDetailChart, meterModelChart};
//    }
//}
