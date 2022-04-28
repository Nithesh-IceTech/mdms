package za.co.spsi.toolkit.crud.chart;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.ano.UILayout;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.util.ResponsiveHelper;

import java.util.*;

/**
 * * Created by jaspervdb on 2016/08/17.
 * Add button to move backwards
 */
public abstract class ChartGroup extends VerticalLayout implements ChartContainer.ChartListener,ResponsiveHelper.ResponseCallback<ChartGroup> {

    private Button refresh = formatButton(new Button(FontAwesome.REFRESH)),back = formatButton(new Button(FontAwesome.BACKWARD));
    private CheckBox gradient = new CheckBox(AbstractView.getLocaleValue(ToolkitLocaleId.CHART_COLOR_GRADIANT),false);
    private MVerticalLayout mainLayout = new MVerticalLayout().withHeight("-1px").withFullWidth().withSpacing(true).
            withMargin(new MarginInfo(false,false,true,false));
    private boolean built = false;
    private ResponsiveHelper responsiveHelper;

    private String name;

    public ChartGroup(String name) {
        this.name = name;
        init();
    }

    public String getName() {
        return name;
    }

    public abstract AbstractChart[][] getCharts();

    private Button formatButton(Button btn) {
        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btn.addStyleName(ValoTheme.BUTTON_TINY);
        return btn;
    }

    public boolean drawGradient() {
        return gradient.getValue();
    }

    public void init() {
        MHorizontalLayout buttons = new MHorizontalLayout(new ChartThemeBox(),
                new MHorizontalLayout(gradient).withAlign(gradient,Alignment.MIDDLE_CENTER).withMargin(false).withWidth("-1px"),back,refresh).withWidth("-1px");
        addComponent(new MHorizontalLayout(buttons).withAlign(buttons,Alignment.MIDDLE_RIGHT).withFullWidth());

        back.setEnabled(false);
        refresh.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.REFRESH_RECORD));

        refresh.addClickListener((Button.ClickListener) event -> build());
        back.addClickListener((Button.ClickListener) event -> {
            drillDownStack.pop().back();
            back.setEnabled(!drillDownStack.isEmpty());
        });
        gradient.addValueChangeListener((Property.ValueChangeListener) event -> refresh.click());

        setSizeFull();
        mainLayout.setHeightUndefined();
        Panel panel = new Panel(mainLayout);
        panel.setSizeFull();
        addComponent(panel);
        setExpandRatio(panel,2f);

        responsiveHelper = new ResponsiveHelper(this,getClass().getAnnotation(UILayout.class),this);

    }

    public ChartGroup build() {
        return build(responsiveHelper.getCols());
    }

    public ChartGroup build(int cols) {
        mainLayout.removeAllComponents();
        for (AbstractChart[] charts : getCharts()) {
            List<Component> chartList = new ArrayList<Component>();
            for (AbstractChart chart : charts) {
                ChartContainer container = new ChartContainer(this,chart);
                container.setChartListener(this);
                container.addStyleName("");
                Panel panel = new Panel(container);
                chartList.add(panel);
            }
            if (cols < 2) {
                mainLayout.addComponent(new MVerticalLayout(chartList.toArray(new Component[]{})).withMargin(false).
                        withFullWidth().withSpacing(true));
            } else {
                mainLayout.addComponent(new MHorizontalLayout(chartList.toArray(new Component[]{})).withMargin(false).
                        withFullWidth().withSpacing(true));
            }
        }
        back.setEnabled(false);
        mainLayout.setSpacing(true);

        return this;
    }

    public Component getComponent() {
        return this;
    }

    final Page.BrowserWindowResizeListener browserWindowResizeListener = (Page.BrowserWindowResizeListener) event -> {
        build();
    };

    public void enter() {
        if (!built) {
            built = true;
            build();
        }
        responsiveHelper.attachListener();
    }

    public void exit() {
        responsiveHelper.detachListener();
    }

    private Stack<ChartContainer> drillDownStack = new Stack<ChartContainer>();

    public void drilledDown(ChartContainer chartContainer) {
        drillDownStack.add(chartContainer);
        back.setEnabled(true);
    }
}
