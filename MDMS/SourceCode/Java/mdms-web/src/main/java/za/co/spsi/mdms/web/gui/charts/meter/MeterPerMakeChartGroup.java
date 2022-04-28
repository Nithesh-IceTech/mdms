//package za.co.spsi.mdms.web.gui.charts.meter;
//
//import za.co.spsi.locale.annotation.MdmsLocaleId;
//import za.co.spsi.toolkit.crud.chart.AbstractChart;
//import za.co.spsi.toolkit.crud.chart.ChartGroup;
//import za.co.spsi.toolkit.crud.gui.ano.UILayout;
//
//import javax.enterprise.inject.spi.BeanManager;
//import javax.inject.Inject;
//
//import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;
//
//@UILayout(column = 1,minWidth = 1024)
//public class MeterPerMakeChartGroup extends ChartGroup {
//
//    @Inject
//    private BeanManager beanManager;
//
//    public MeterPerMakeChartGroup() {
//        super(MdmsLocaleId.METER_PER_MAKE);
//    }
//
//    @Override
//    public AbstractChart[][] getCharts() {
//
//        return new AbstractChart[][] {
//                {getBean(beanManager,MeterPerMakeChart.class)},
//                {getBean(beanManager,MeterPerMakeReadingsAnnualChart.class)},
//        };
//    }
//}
