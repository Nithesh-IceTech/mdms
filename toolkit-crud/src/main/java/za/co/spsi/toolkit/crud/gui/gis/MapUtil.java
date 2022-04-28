package za.co.spsi.toolkit.crud.gui.gis;

import com.vaadin.ui.UI;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;

/**
 * Created by jaspervdb on 2/3/16.
 */
public class MapUtil {

    public static void centerOnUserLocation(final GeoMap map, final Double defaultX, final Double defaultY,
                                            final boolean addMarker,final Location.LocationListener callback) {
        final Location location = new Location(UI.getCurrent());
        if (defaultX != null && defaultY != null) {
            map.getMap().setCenter(defaultY, defaultX);
        }

        location.addLocationListener(new Location.LocationListener() {
            @Override
            public void onLocationFound(double latitude, double longitude, double accuracy) {
                map.centerOn(latitude, longitude, AbstractView.getLocaleValue(ToolkitLocaleId.APPROX_LOCATION));
                UI.getCurrent().removeExtension(location);
                if (addMarker) {
                    map.addSearchMarker(longitude,latitude, AbstractView.getLocaleValue(ToolkitLocaleId.APPROX_LOCATION));
                }
                if (callback != null) {
                    callback.onLocationFound(latitude, longitude,accuracy);
                }
            }

            @Override
            public void onLocationError(LocationError error) {
                UI.getCurrent().removeExtension(location);
                if (callback != null) {
                    callback.onLocationError(error);
                }
            }

            @Override
            public void onLocationNotSupported() {
                UI.getCurrent().removeExtension(location);
                if (callback != null) {
                    callback.onLocationNotSupported();
                }

            }
        });
        location.requestLocation();
    }
}
