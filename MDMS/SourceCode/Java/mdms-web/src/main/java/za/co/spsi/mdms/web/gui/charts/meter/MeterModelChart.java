//package za.co.spsi.mdms.web.gui.charts.meter;
//
//import com.vaadin.addon.charts.Chart;
//import com.vaadin.addon.charts.PointClickEvent;
//import com.vaadin.addon.charts.model.DataSeries;
//import za.co.spsi.locale.annotation.MdmsLocaleId;
//import za.co.spsi.mdms.web.gui.charts.ChartHelper;
//import za.co.spsi.toolkit.crud.chart.AbstractChart;
//import za.co.spsi.toolkit.crud.chart.DrillDownEvent;
//import za.co.spsi.toolkit.crud.gui.CrudUI;
//import za.co.spsi.toolkit.crud.gui.CrudView;
//import za.co.spsi.toolkit.crud.gui.Layout;
//import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
//import za.co.spsi.toolkit.crud.gui.query.ViewQueryDelegate;
//import za.co.spsi.toolkit.crud.gui.render.AbstractView;
//import za.co.spsi.toolkit.db.FormattedSql;
//import za.co.spsi.toolkit.ee.properties.TextFile;
//
//import javax.annotation.Resource;
//import javax.enterprise.inject.spi.BeanManager;
//import javax.inject.Inject;
//import javax.sql.DataSource;
//import java.util.List;
//
//import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;
//
//public class MeterModelChart extends AbstractChart {
//
//    public static final String NOT_SET_NULL = "Not Set";
//    @Inject
//    @TextFile("charts/meter_model.sql")
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
//    public MeterModelChart() {
//        setTitle(MdmsLocaleId.METER_PER_MODEL);
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
//    public static MeterModelChart getNewInstance(BeanManager beanManager, PointClickEvent event, String title, String tableName) {
////        set type enum
//        String type = ((DataSeries) event.getSeries()).get(event.getPointIndex()).getName();
//        MeterModelChart surveyPerStateChart = getBean(beanManager, MeterModelChart.class);
//        surveyPerStateChart.setTitle(getCamelCase(type) + " " + AbstractView.getLocaleValue(title.toLowerCase()));
//        surveyPerStateChart.sql = surveyPerStateChart.sql.replace("_TABLE_NAME_", tableName);
//        return surveyPerStateChart;
//    }
//
//    @Override
//    public AbstractChart[] getDrillDownChart(List<DrillDownEvent> drillDownEvents, PointClickEvent event) {
//        String type = getSelectedValue(event).getName();
//        ChartHelper.MeterType entityType =
//                ChartHelper.MeterType.getByName(drillDownEvents.get(0).getDataSeriesItem().getName());
//        CrudUI.getCrudUI().displayViewsInWindow(
//                applyFilter(entityType.viewClass, entityType.tableName, type, entityType.modelLookup));
//        return null;    }
//
//    public String getSql() {
//        return sql;
//    }
//
//    private <T extends CrudView> T applyFilter(Class<T> viewClass, String tableName, String type, String entityType) {
//
//        T view = getBean(beanManager,viewClass);
//        view.clear();
//        view.enter(null);
//        Layout layout = view.getLayouts().toArray(new Layout[]{})[0];
//        ViewQueryDelegate delegate = layout.getLayoutViewGrid().getViewQueryDelegate();
//
//        delegate.setSqlSelect(new FormattedSql((layout.getOriginalQuery())).addWhere(
//                ("_TABLE_._TYPE_COLUMN_ = '" + type + "'").replace("_TABLE_",tableName).replace("_TYPE_COLUMN_", entityType)
//        ).toString().replace(String.format("= '%s'",NOT_SET_NULL)," is null"));
//        return view;
//    }
//
//}
