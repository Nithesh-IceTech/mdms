package za.co.spsi.toolkit.crud.gui.fields;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;

import java.text.DecimalFormat;


/**
 * Created by jaspervdb on 2015/09/14.
 */
public class GPSCoordinatesLField extends LField<String> {

    public static DecimalFormat LAT_FORMAT = new DecimalFormat("##.#######");
    public static DecimalFormat LON_FORMAT = new DecimalFormat("###.#######");
    private double lat = (double) 0;
    private double lon = (double) 0;

    public GPSCoordinatesLField(Layout model) {
        super(ToolkitLocaleId.GPS_COORDINATES, "", model);
        getProperties().setMax(40);
        getProperties().setMandatory(false);
        getProperties().setReadOnly(true);
    }

    public static String decimalToDMS(double coord) {
        String degrees, minutes, seconds;

        double mod = coord % 1;
        int intPart = (int) coord;
        degrees = String.valueOf(Math.abs(intPart));
        coord = mod * 60;
        mod = coord % 1;
        intPart = (int) coord;
        minutes = String.valueOf(Math.abs(intPart));
        coord = mod * 60;
        seconds = String.valueOf((new DecimalFormat("##.###").format(Math.abs(coord))));
        return degrees + "°" + minutes + "'" + seconds + "\"";
    }

    @Override
    public void intoControl() {
            getVaadinField().setValue(String.format("%s %s, %s %s", decimalToDMS(lat), lat > 0 ? "N" : "S", decimalToDMS(lon), lon > 0 ? "E" : "W"));
    }

    public void update(double lat, double lon) {
//        set(LAT_FORMAT.format(lat)+","+LON_FORMAT.format(lon));
        this.lat = lat;
        this.lon = lon;
        intoControl();
    }

    @Override
    protected com.vaadin.ui.Field intoBindingsWithNoValidation(boolean update) {
        return null;
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        return super.buildVaadinField();
    }

}
