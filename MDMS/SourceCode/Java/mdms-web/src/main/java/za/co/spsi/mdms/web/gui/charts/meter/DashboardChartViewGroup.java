//package za.co.spsi.mdms.web.gui.charts.meter;
//
//import com.vaadin.cdi.CDIView;
//import com.vaadin.cdi.UIScoped;
//import com.vaadin.navigator.View;
//import com.vaadin.navigator.ViewChangeListener;
//import com.vaadin.server.FontAwesome;
//import za.co.spsi.locale.annotation.MdmsLocaleId;
//import za.co.spsi.toolkit.crud.chart.ChartGroup;
//import za.co.spsi.toolkit.crud.chart.ChartViewGroup;
//import za.co.spsi.toolkit.crud.util.Util;
//import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;
//
//import javax.annotation.PostConstruct;
//import javax.enterprise.inject.spi.BeanManager;
//import javax.inject.Inject;
//
//
//@CDIView("dashboard-chart")
//@ViewMenuItem(icon = FontAwesome.PIE_CHART, order = 1, value = MdmsLocaleId.MENU_DASHBOARD_CHARTS,
//        groupName = MdmsLocaleId.MENU_DASHBOARD)
//@UIScoped
//public class DashboardChartViewGroup extends ChartViewGroup implements View {
//
//    @Inject
//    BeanManager beanManager;
//
//    @PostConstruct
//    public void init() {
//        addChartGroup(getBean(beanManager, MeterPerMakeChartGroup.class));
//
//    }
//
//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent event) {
//        for (ChartGroup chartGroup : getChartGroups()) {
//            chartGroup.enter();
//            break;
//        }
//    }
//}
