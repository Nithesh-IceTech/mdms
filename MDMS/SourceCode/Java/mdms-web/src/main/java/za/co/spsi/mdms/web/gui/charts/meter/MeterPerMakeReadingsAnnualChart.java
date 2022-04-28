//package za.co.spsi.mdms.web.gui.charts.meter;
//
//import com.vaadin.addon.charts.Chart;
//import com.vaadin.addon.charts.PointClickEvent;
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
//import java.sql.Timestamp;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;
//
//public class MeterPerMakeReadingsAnnualChart extends AbstractChart {
//
//    @Inject
//    @TextFile("charts/meter_reading_annual_column_chart.sql")
//    private String annualSql;
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
//    private SimpleDateFormat monthFormat = new SimpleDateFormat("yy/MM"),
//            shortFormat = new SimpleDateFormat("yyMMdd");
//
//
//    public MeterPerMakeReadingsAnnualChart() {
//        setTitle("Meter Reading for year per month");
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
//        annualSql = annualSql.replace("_DATE_", "to_char(add_months(sysdate,-12), 'YYYYMMDD')");
//        return super.buildColumnChart("Meter Reading for year per month", annualSql, gradientColor, 10);
//    }
//
//    @Override
//    public AbstractChart[] getDrillDownChart(List<DrillDownEvent> drillDownEvents, PointClickEvent event) {
//
//        try {
//            LocalDateTime localDate = new Timestamp(monthFormat.parse(event.getCategory()).getTime()).toLocalDateTime();
//
//            ChartHelper.MeterType selected =
//                    ChartHelper.MeterType.getByName((event.getSeries()).getName());
//
//            MeterPerMakeReadingsDailyChart meterPerMakeReadingsDailyChart =
//                    getBean(beanManager, MeterPerMakeReadingsDailyChart.class);
//            meterPerMakeReadingsDailyChart.setTitle("Meter Reading daily for month " + monthFormat.format(Timestamp.valueOf(localDate)));
//
//            meterPerMakeReadingsDailyChart.setDailySql(meterPerMakeReadingsDailyChart.getDailySql().
//                    replace("_DATE_START_", shortFormat.format(Timestamp.valueOf(localDate))).
//                    replace("_DATE_END_",shortFormat.format(Timestamp.valueOf(localDate.plusMonths(1)))).
//                    replace("_LEGEND_", "'" + (event.getSeries()).getName() + "'").
//                    replace("_METER_FOREIGN_KEY_", selected.foreignKey));
//            return new AbstractChart[]{meterPerMakeReadingsDailyChart};
//
//        } catch (ParseException pe) {
//            throw new RuntimeException(pe);
//        }
//    }
//}
