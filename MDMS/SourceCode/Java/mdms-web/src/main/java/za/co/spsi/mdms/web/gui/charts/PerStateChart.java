//package za.co.spsi.mdms.web.gui.charts;
//
//import com.vaadin.addon.charts.Chart;
//import com.vaadin.addon.charts.PointClickEvent;
//import za.co.spsi.toolkit.crud.chart.AbstractChart;
//import za.co.spsi.toolkit.crud.chart.DrillDownEvent;
//import za.co.spsi.toolkit.crud.gui.CrudView;
//import za.co.spsi.toolkit.crud.gui.Layout;
//import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
//import za.co.spsi.toolkit.crud.gui.query.ViewQueryDelegate;
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
///**
// * Created by jaspervdb
// */
//public class PerStateChart extends AbstractChart {
//
//    @Resource(mappedName = "java:/jdbc/mdms")
//    public DataSource dataSource;
//
//    @Inject
//    @TextFile("charts/survey_by_type_per_state_tx_filter.sql")
//    public String typeTxFilter;
//
//    @Inject
//    public ToolkitLookupServiceHelper lookupServiceHelper;
//
//    @Inject
//    public BeanManager beanManager;
//    public String period;
//
//    public void setPeriod(String period) {
//        this.period = period;
//    }
//
//    @Override
//    protected DataSource getDataSource() {
//        return dataSource;
//    }
//
//    @Override
//    protected Chart createChart(boolean gradientColor) {
//        return null;
//    }
//
//    public <T extends CrudView> T applyFilter(Class<T> viewClass, String filter, String tableName, String entityStatusCd, String employeeName) {
//
//        T view = getBean(beanManager, viewClass);
//        view.clear();
//        view.enter(null);
//        Layout layout = view.getLayouts().toArray(new Layout[]{})[0];
//        ViewQueryDelegate delegate = layout.getLayoutViewGrid().getViewQueryDelegate();
//
//        delegate.setSqlSelect(
//                formatSql(new FormattedSql((layout.getOriginalQuery())).addWhere(
//                        filter.replace("_TABLE_NAME_", tableName).replace("_EMPLOYEE_NAME_", employeeName)
//                                .replace("_ENTITY_STATUS_CD_", entityStatusCd).replace("_PERIOD_", period)
//                ).toString()));
//        return view;
//    }
//
//    @Override
//    public AbstractChart[] getDrillDownChart(List<DrillDownEvent> drillDownEvents, PointClickEvent event) {
//        return null;
//    }
//
//
//}
