package za.co.spsi.toolkit.crud.gis;

/**
 * Created by francoism on 2017/05/24.
 */
public class SnappedPoint{
    public SnappedLatLng location;
    public int originalIndex = -1;
    public String placeId;

    public SnappedLatLng getLocation() {
        return location;
    }

    public void setLocation(SnappedLatLng location) {
        this.location = location;
    }

    public int getOriginalIndex() {
        return originalIndex;
    }

    public void setOriginalIndex(int originalIndex) {
        this.originalIndex = originalIndex;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
