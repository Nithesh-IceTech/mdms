package za.co.spsi.toolkit.crud.gui.gis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.google.maps.internal.StringJoin;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.shared.PopupState;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.db.gis.ImageGeoLocationEntity;
import za.co.spsi.toolkit.crud.gis.SnappedPoint;
import za.co.spsi.toolkit.crud.gis.db.DeviceLocationEntity;
import za.co.spsi.toolkit.crud.gui.render.AV;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.util.StringList;

import javax.enterprise.context.Dependent;
import javax.sql.DataSource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by jaspervdb on 2016/07/20.
 */
@Dependent
public class MapHelper {

    public enum MarkerColor {
        Blue("../toolkit/gwt-marker/marker-icon-blue-sh.png"), Red("../toolkit/gwt-marker/marker-icon-red-sh.png"),
        Green("../toolkit/gwt-marker/marker-icon-green-sh.png"), Yellow("../toolkit/gwt-marker/marker-icon-yellow-sh.png"),
        BlueSmall("../toolkit/gwt-marker/marker-icon-blue-sh-small.png");

        String image;

        MarkerColor(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }
    }

    public static PopupState POPUP_STATE = new PopupState();

    static {
        POPUP_STATE.autoPan = true;
        POPUP_STATE.closeButton = true;
        POPUP_STATE.minWidth = 200;
        POPUP_STATE.offset = new Point(0, -20);
        POPUP_STATE.zoomAnimation = true;
    }

    private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd MMM HH:mm");

    final Geocoder geocoder = new Geocoder();

    public MapHelper() {
    }

    private static final String degree = new String(new char[]{(char) 176});

    /**
     * convert the decimal heading into a text description (n, ne etc)
     *
     * @param x
     * @return
     */
    public String convertHeadingToText(double x) {
        x = (x < 360)
                ? x + 360
                : x;
        String directions[] = {
                "n", "ne", "e", "se", "s", "sw", "w", "nw", "n"
        };

        return directions[(int) Math.round((((double) x % 360) / 45))];
    }

    /**
     * text representation of the lon and lat
     *
     * @param value
     * @param ns
     * @return
     */
    private String convertDecToDegree(double value, char[] ns) {
        int dd = (int) value;
        double t1 = (value - dd) * 60;
        int mm = (int) t1;
        double ss = Math.round((t1 - mm) * 60 * 1000) / 1000.0;

        if (dd < 0) {
            return (dd * -1) + "" + degree + "" + (mm * -1) + "'" + ss * -1 + "\" " + ns[1];
        } else {
            return dd + "" + degree + "" + mm + "'" + ss + "\" " + ns[0];
        }
    }

    /**
     * convert the decimal to text formatted
     *
     * @param x
     * @param y
     * @return
     */
    public String convertDecToDegree(double y, double x) {
        return convertDecToDegree(y, new char[]{'N', 'S'}) + ", " + convertDecToDegree(x, new char[]{'E', 'W'});
    }

    /**
     * reverse geocode the location
     *
     * @param lat
     * @param lon
     * @return
     */
    public String reverseGeocode(double lat, double lon) {
        GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setLocation(new LatLng(new BigDecimal(lat), new BigDecimal(lon))).getGeocoderRequest();
        List<GeocoderResult> geocoderResults = null;
        try {
            geocoderResults = geocoder.geocode(geocoderRequest).getResults();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!geocoderResults.isEmpty()) {
            return geocoderResults.get(0).getFormattedAddress();
        } else {
            return "";
        }
    }

    /**
     * attach the address to the popup
     *
     * @param lMarker
     * @param popup
     * @param address
     */
    private void attachAddressToMarkerPopup(LMarker lMarker, String popup, String address) {
        lMarker.setPopup(popup + "<hr><font color=\"Silver\"><p><i>" + address + "</i></p>" +
                "<p>" + convertDecToDegree(lMarker.getPoint().getLat().doubleValue(), lMarker.getPoint().getLon().doubleValue()) + "</p></font>");
    }

    /**
     * reverse geocode the marker address and attach to the marker. also update and persist the location entity's address
     *
     * @param ui
     * @param lMarker
     * @param originalPopMessage
     * @param locationEntity
     */
    public void updateMarkerAndDeviceLocationWithReverseGeocodedAddress(
            final UI ui, final LMarker lMarker, final String originalPopMessage, DataSource dataSource, final DeviceLocationEntity locationEntity) {
        new Thread(() -> {
            final String address = reverseGeocode(lMarker.getPoint().getLat(), lMarker.getPoint().getLon());
            if (locationEntity != null) {
                locationEntity.address.set(address);
                DataSourceDB.set(dataSource, locationEntity);
            }
            attachAddressToMarkerPopup(lMarker, originalPopMessage, address);
        }).start();

    }

    /**
     * build a new marker
     *
     * @param title
     * @param popup
     * @param listener
     * @param x
     * @param y
     * @return
     */
    public LMarker getMarker(String title, String popup,
                             LeafletClickListener listener,
                             double y, double x, MarkerColor markerColor) {
        LMarker marker = new LMarker(y, x);
        marker.setTitle(title);
        marker.setPopup(popup);
        marker.setIcon(new ThemeResource(markerColor.getImage()));
        marker.setPopupState(POPUP_STATE);

        if (markerColor.equals(MarkerColor.BlueSmall)) {
            marker.setIconAnchor(new Point(6, 20));
        } else {
            marker.setIconAnchor(new Point(12, 40));
        }
        if (listener != null) {
            marker.addClickListener(listener);
        }
        return marker;
    }

    /**
     * @return the defined marker color based on speed and last update time
     */
    public static MarkerColor getMarkerColorForNonTripPosition(Timestamp timestamp) {
        return timestamp != null &&
                timestamp.toLocalDateTime().isAfter(LocalDateTime.now().minusHours(1)) ?
                MarkerColor.Green : MarkerColor.Red;
    }

    public LMarker getMarkerForTrips(UI ui, DataSource dataSource, DeviceEntity device,
                                     DeviceLocationEntity locationEntity, MarkerColor markerColor) {
        String popup = "<hr><b>IMEI: </b>" + device.imei.get() +
                String.format("<br><b>%s: </b>", AV.getLocaleValue(ToolkitLocaleId.ALIAS)) + device.deviceAlias.getNonNull() + "<hr>" +
                String.format("<br><b>%s: </b>", AV.getLocaleValue(ToolkitLocaleId.SPEED)) + locationEntity.speed.getNonNull() + " km/h" +
                String.format("<br><b>%s: </b>", AV.getLocaleValue(ToolkitLocaleId.BATTERY_LEVEL)) + locationEntity.batteryLevel.getNonNull() + "%" +
                String.format("<br><b>%s: </b>", AV.getLocaleValue(ToolkitLocaleId.DIRECTION)) + formatBearing(locationEntity.heading.getNonNull()) +
                "<br><i>" + dateTimeFormat.format(locationEntity.captureTime.get()) + "</i></p>";

        LMarker lMarker = getMarker(device.getDisplayName(), popup, null, locationEntity.lat.get(), locationEntity.lon.get(), markerColor);

        if (!StringUtils.isEmpty(locationEntity.address.get())) {
            attachAddressToMarkerPopup(lMarker, popup, locationEntity.address.get());
        } else {
            if (ui != null) {
                updateMarkerAndDeviceLocationWithReverseGeocodedAddress(ui, lMarker, popup, dataSource, locationEntity);
            }
        }
        return lMarker;
    }

    private String formatBearing(double bearing) {
        if (bearing < 0 && bearing > -180) {
            // Normalize to [0,360]
            bearing = 360.0 + bearing;
        }
        if (bearing > 360 || bearing < -180) {
            return "Unknown";
        }

        String directions[] = {
                "N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW",
                "N"};
        String cardinal = directions[(int) Math.floor(((bearing + 11.25) % 360) / 22.5)];
        return cardinal + " (" + Math.round(bearing) + " deg)";
    }

    public LMarker getMarkerForNonTripPosition(UI ui, DataSource dataSource, DeviceEntity device, DeviceLocationEntity latestLoc, MarkerColor markerColor) {

        // Build the sting for apps om popup en determine if location must be used instead of last MDM position

        String popup = "<hr><b>IMEI: </b>" + device.imei.getNonNull() +
                String.format("<br><b>%s: </b>", AV.getLocaleValue(ToolkitLocaleId.ALIAS)) + device.deviceAlias.getNonNull() +
                String.format("<br><b>%s: </b>", AV.getLocaleValue(ToolkitLocaleId.CURRENT_USER)) + device.userId.getNonNull() + "<hr>" +
                String.format("<br><b>%s: </b>", AV.getLocaleValue(ToolkitLocaleId.LAST_KNOWN_BATTERY_LEVEL)) + latestLoc.batteryLevel.getNonNull() + "%" +
                "<br>" +
                String.format("<br><b><u>%s</b></u>", AV.getLocaleValue(ToolkitLocaleId.LAST_SYNC_TIME)) +
                String.format("<br><b>%s: </b><i>", AV.getLocaleValue(ToolkitLocaleId.COMMUNICATION_TIME)) + (device.lastCommsDate.get() != null ? dateTimeFormat.format(device.lastCommsDate.get()) : "") + "</i>" +
                String.format("<br><b>%s: </b><i>", AV.getLocaleValue(ToolkitLocaleId.LOCATION_TIME)) + dateTimeFormat.format(latestLoc.captureTime.get()) + "</i>" +
                "</p>";

        // Determine best location via apps or DM data
        LMarker lMarker = getMarker(device.getDisplayName(), popup, null, latestLoc.lat.get(), latestLoc.lon.get(), markerColor);

        if (!StringUtils.isEmpty(latestLoc.address.get())) {
            attachAddressToMarkerPopup(lMarker, popup, latestLoc.address.get());
        } else {
            if (ui != null) {
                updateMarkerAndDeviceLocationWithReverseGeocodedAddress(ui, lMarker, popup, dataSource, latestLoc);
            }
        }
        return lMarker;
    }

    /**
     * build a polyline path from the supplied points
     *
     * @param pointList
     * @param listener
     * @return
     */
    public LPolyline buildPathFromTrip(List<Point> pointList, LeafletClickListener listener, String color) {
        LPolyline leafletPolyline = new LPolyline(pointList.toArray(new Point[]{}));
        leafletPolyline.setColor(color);
        leafletPolyline.setFill(false);
        leafletPolyline.setWeight(5);
        if (listener != null) {
            leafletPolyline.addClickListener(listener);
        }
        return leafletPolyline;
    }

    /**
     * build a polyline path from the supplied points
     *
     * @param pointList
     * @param listener
     * @return
     */
    public LPolyline buildSnappedToRoadPathFromTrip(List<Point> pointList, LeafletClickListener listener, String color) {
        List<Point> points = new ArrayList<>();
        SnappedPoint[] totalSnappedPoints = new SnappedPoint[0];
        List<com.google.maps.model.LatLng> unsnappedPoints = new ArrayList<>();
        int numberOfPoints = 1;
        for (Point point : pointList) {
            //Google API call limited to 100 points per service call
            if (unsnappedPoints.size() < 100 && numberOfPoints < pointList.size() - 1) {
                unsnappedPoints.add(new com.google.maps.model.LatLng(point.getLat(), point.getLon()));
                numberOfPoints++;
            } else if (unsnappedPoints.size() > 0) {
                com.google.maps.model.LatLng[] arr = unsnappedPoints.toArray(new com.google.maps.model.LatLng[unsnappedPoints.size()]);
                Client client = ClientBuilder.newClient();
                WebTarget target = client.target("https://roads.googleapis.com/v1/snapToRoads")
                        .queryParam("path", StringJoin.join('|', arr))
                        .queryParam("interpolate", "true")
                        .queryParam("key", "AIzaSyAGi3AezGbJ4BHt7TBxDXXECWJtsiwhr0E");
                Invocation.Builder request = target.request(MediaType.APPLICATION_JSON_TYPE);
                Response response = request.get();
                ObjectMapper mapper = new ObjectMapper();
                String snappedPointsString = response.readEntity(String.class);
                SnappedPoint[] snappedPoints = new SnappedPoint[0];
                try {
                    snappedPoints = mapper.readValue(snappedPointsString.substring(21, snappedPointsString.length() - 2),
                            SnappedPoint[].class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SnappedPoint[] both = Stream.concat(Arrays.stream(totalSnappedPoints), Arrays.stream(snappedPoints))
                        .toArray(SnappedPoint[]::new);
                totalSnappedPoints = both;
                unsnappedPoints.clear();
            }

        }

        Arrays.stream(totalSnappedPoints).forEach(snappedPoint -> {
            points.add(new Point(snappedPoint.location.getLatitude(), snappedPoint.location.getLongitude()));
        });
        return buildPathFromTrip(points, listener, color);
    }

    private GisHelper gisHelper = new GisHelper();

    public ImageGeoLocationEntity getAsImageGeoLocationsDao(LPolygon lPolygon, ToolkitConstants.GeoType geoType) {
        ImageGeoLocationEntity dao = new ImageGeoLocationEntity();
        dao.imageGeoLocationId.set(UUID.randomUUID().toString());
        dao.area.set(new Double(gisHelper.calculateRoundedArea(lPolygon.getGeometry())));
        List<String> strings = new ArrayList<>();
        for (Point point : lPolygon.getPoints()) {
            strings.add(point.getLat() + "," + point.getLon());
        }
        dao.gpsLocations.set(new StringList(strings).toString(","));
        dao.geoLocationTypeCd.set(geoType.getCode());
        return dao;
    }

    public LPolygon createPolygon(ImageGeoLocationEntity dao, List<ToolkitConstants.GeoType> geoTypes) {
        String values[] = dao.gpsLocations.get().split(",");
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < values.length / 2; i++) {
            points.add(new Point(Double.valueOf(values[i * 2]), Double.valueOf(values[i * 2 + 1])));
        }

        LPolygon lPolygon = new LPolygon(points.toArray(new Point[]{}));

        if (geoTypes != null) {
            ToolkitConstants.GeoType geoType = MapComponent.getGeoType(dao, geoTypes);
            lPolygon.setColor(geoType.getColor());
            lPolygon.setFillColor(geoType.getColor());
            lPolygon.setFill(true);

        }

        return lPolygon;
    }

    public GisHelper getGisHelper() {
        return gisHelper;
    }

    public String getExternalRequestUrl() {
        return UI.getCurrent().getPage().getLocation().getScheme() + ":" + UI.getCurrent().getPage().getLocation().getSchemeSpecificPart();
    }
}
