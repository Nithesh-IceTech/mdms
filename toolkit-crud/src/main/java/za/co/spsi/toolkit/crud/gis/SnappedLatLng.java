package za.co.spsi.toolkit.crud.gis;

import java.util.Locale;

/**
 * Created by francoism on 2017/05/24.
 */
public class SnappedLatLng {
    public double latitude;
    public double longitude;

    public SnappedLatLng(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public SnappedLatLng() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return this.toUrlValue();
    }

    public String toUrlValue() {
        return String.format(Locale.ENGLISH, "%f|%f|", new Object[]{Double.valueOf(this.latitude), Double.valueOf(this.longitude)});
    }
}
