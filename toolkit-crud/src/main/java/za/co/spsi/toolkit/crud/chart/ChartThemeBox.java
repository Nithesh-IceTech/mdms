package za.co.spsi.toolkit.crud.chart;

import com.vaadin.addon.charts.ChartOptions;
import com.vaadin.addon.charts.model.style.Theme;
import com.vaadin.addon.charts.themes.*;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.themes.ValoTheme;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.codes.ToolkitLocale;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaspervdb on 2016/09/12.
 */
public class ChartThemeBox extends ComboBox {

    private static Map<String,Theme> themeMap = new HashMap<>();
    static {
        themeMap.put(ToolkitLocaleId.CHART_THEME_VALO,new VaadinTheme());
        themeMap.put(ToolkitLocaleId.CHART_THEME_VALO_DARK,new ValoDarkTheme());
        themeMap.put(ToolkitLocaleId.CHART_THEME_VALO_LIGHT,new ValoLightTheme());
        themeMap.put(ToolkitLocaleId.CHART_THEME_HIGH,new HighChartsDefaultTheme());
        themeMap.put(ToolkitLocaleId.CHART_THEME_GRAY,new GrayTheme());
        themeMap.put(ToolkitLocaleId.CHART_THEME_GRID,new GridTheme());
        themeMap.put(ToolkitLocaleId.CHART_THEME_SKY,new SkiesTheme());
    }

    public ChartThemeBox() {
        init();
    }

    private void init() {
        setNullSelectionAllowed(false);
        setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.CHART_THEME));
        for (String value : themeMap.keySet()) {
            addItem(themeMap.get(value));
            setItemCaption(themeMap.get(value), AbstractView.getLocaleValue(value));
        }
        setValue(themeMap.get(ToolkitLocaleId.CHART_THEME_VALO));
        addValueChangeListener((ValueChangeListener) event -> ChartOptions.get().setTheme((Theme) event.getProperty().getValue()));
        addStyleName(ValoTheme.COMBOBOX_TINY);
    }


}
