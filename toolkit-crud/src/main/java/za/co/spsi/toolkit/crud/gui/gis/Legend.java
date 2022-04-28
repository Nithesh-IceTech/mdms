package za.co.spsi.toolkit.crud.gui.gis;

import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.locationtextfield.GeocodedLocation;
import org.vaadin.addons.locationtextfield.LocationTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;

import java.util.stream.IntStream;

/**
 * Created by jaspervdbijl on 2017/02/06.
 */
public class Legend extends MVerticalLayout{

    private GridLayout grid;

    public Legend() {
        init();
    }

    private Label getSpacer() {
        return new Label("&nbsp;", ContentMode.HTML);
    }

    public int addLegend(String name, String color) {
        Label faLabel = new Label(String.format("<font color=\"%s\" size=\"2px\">%s</font>",color, FontAwesome.CIRCLE.getHtml()), ContentMode.HTML);
        Label nameLabel = new Label(AbstractView.getLocaleValue(name));

        faLabel.addStyleName("legend");
        nameLabel.addStyleName("legend");

        faLabel.addStyleName(color);

        grid.addComponents(faLabel,getSpacer(),nameLabel);
        grid.setComponentAlignment(nameLabel, Alignment.MIDDLE_LEFT);
        return grid.getComponentCount()/3;
    }

    public int addLegend(Resource image, String name) {
        Image img = new Image(null, image);
        img.setWidth("15px");
        img.setHeight("15px");
        Label label = new Label(AbstractView.getLocaleValue(name));
        label.addStyleName("legend");
        grid.addComponents(img,getSpacer(),label);
        return grid.getComponentCount()/3;
    }

    public void setVisible(int row,boolean visible) {
        IntStream.range(0, 2).forEach(i -> grid.getComponent(i,row).setVisible(visible));
    }

    private void init() {
        setSpacing(false);
//        setMargin(new MarginInfo(false,true,false,true));
        setMargin(false);
        Label legend = new Label(AbstractView.getLocaleValue(ToolkitLocaleId.LEGEND));
        legend.addStyleName(ValoTheme.LABEL_H4);
//        add(legend);

//        add(new Label("<hr>", ContentMode.HTML));

        grid = new GridLayout(3,1);
        grid.setMargin(false);

        add(grid);

    }
}
