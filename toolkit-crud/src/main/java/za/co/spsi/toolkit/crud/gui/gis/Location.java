package za.co.spsi.toolkit.crud.gui.gis;

import com.vaadin.annotations.JavaScript;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;
import elemental.json.JsonArray;
import org.vaadin.addon.leaflet.LMap;

import java.util.HashSet;

/**
 * Created by andyphillips404 on 6/9/15.
 */
@JavaScript("location.js")
public class Location extends AbstractJavaScriptExtension {

    private Boolean isLocationSupported = null;


    public interface LocationListener {
        void onLocationFound(double latitude, double longitude, double accuracy);
        void onLocationError(LocationError error);
        void onLocationNotSupported();
    }

    private HashSet<LocationListener> locationListeners = new HashSet<>();

    public void addLocationListener(LocationListener locationListener) {
        locationListeners.add(locationListener);
    }

    public void removeLocationListener(LocationListener locationListener) {
        locationListeners.remove(locationListener);
    }

    public Location(UI ui) {
        extend(ui);

        addFunction("onLocationFound", new JavaScriptFunction() {
            @Override
            public void call(JsonArray arguments) {
                for (LocationListener locationListener : locationListeners) {
                    locationListener.onLocationFound(arguments.getNumber(0), arguments.getNumber(1), arguments.getNumber(2));
                }
            }
        });

        addFunction("onLocationError", new JavaScriptFunction() {
            @Override
            public void call(JsonArray arguments) {
                for (LocationListener locationListener : locationListeners) {
                    locationListener.onLocationError(LocationError.getErrorForCode((int)arguments.getNumber(0)));
                }

            }
        });

        addFunction("onLocationNotSupported", new JavaScriptFunction() {
            @Override
            public void call(JsonArray arguments) {
                for (LocationListener locationListener : locationListeners) {
                    locationListener.onLocationNotSupported();
                }

            }
        });
    }

    public static void zoomToCurrentLocation(final LMap map) {
        final Location location = new Location(UI.getCurrent());
        location.addLocationListener(new Location.LocationListener() {
            @Override
            public void onLocationFound(double latitude, double longitude, double accuracy) {
                map.setCenter(latitude,longitude);
                UI.getCurrent().removeExtension(location);
            }

            @Override
            public void onLocationError(LocationError error) {
                UI.getCurrent().removeExtension(location);
            }

            @Override
            public void onLocationNotSupported() {
                UI.getCurrent().removeExtension(location);
            }
        });
        location.requestLocation();
    }

    public void requestLocation() {
        callFunction("location");
    }


}
