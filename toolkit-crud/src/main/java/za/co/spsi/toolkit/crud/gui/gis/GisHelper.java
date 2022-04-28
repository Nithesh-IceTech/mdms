package za.co.spsi.toolkit.crud.gui.gis;

import com.vividsolutions.jts.geom.Geometry;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.stereotype.Component;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Point;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.util.StringList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by jaspervdb on 2015/09/18.
 */
@Component
public class GisHelper {

    static MathTransform saTransform;

    static {
        try {
            saTransform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, CRS.decode("EPSG:32736", true), true);
        } catch (FactoryException e) {
            throw new RuntimeException(e);
        }
    }

    private static GisHelper gisHelper = new GisHelper();

    public static GisHelper getInstance() {
        return gisHelper;
    }

    public double calculateArea(Geometry geometry) {
        try {
            return JTS.transform(geometry, saTransform).getArea();
        } catch (TransformException te) {
            throw new RuntimeException(te);
        }
    }

    public long calculateRoundedArea(Geometry geometry) {
        return Math.round(calculateArea(geometry));
    }

    public String calculateUserFriendlyArea(Geometry geometry) {
        DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
        decSymbols.setGroupingSeparator(ToolkitCrudConstants.getGroupingSeparator());
        DecimalFormat dec = new DecimalFormat();
        dec.setMaximumFractionDigits(0);
        dec.setDecimalFormatSymbols(decSymbols);
        return dec.format(calculateArea(geometry)) + " m²";
    }

    public static int WORLD_MAP_HEIGHT = 256, WORLD_MAP_WIDTH = 256;
    public static int MAX_ZOOM_LEVEL = 20;

    public static String GM_URL_TEMPLATE =
            "http://maps.googleapis.com/maps/api/staticmap?center=%s&zoom=%s&size=%s&maptype=hybrid&sensor=false&%s";
    public static String GM_PATH_TEMPLATE = "path=color:%s|fillcolor:%s|%s";

    public String getPathString(Point... points) {
        StringList pointStrings = new StringList();
        for (Point point : points) {
            pointStrings.add(String.format("%.3f,%.3f", point.getLat(), point.getLon()));
        }
        return pointStrings.toString("|");
    }

    public String getGMImageUrl(LPolygon polygons[], int mapWidth, int mapHeight) {
        StringList polyStrings = new StringList();
        List<Point> points = new ArrayList<>();
        for (LPolygon polygon : polygons) {
            polyStrings.add(String.format(GM_PATH_TEMPLATE,
                    polygon.getStyle().getColor(), polygon.getStyle().getFillColor(),
                    getPathString(polygon.getPoints())).replace(":", "%3a"));
            Collections.addAll(points, polygon.getPoints());
        }
        Bounds bounds = new Bounds(points.toArray(new Point[]{}));
        String url = String.format(GM_URL_TEMPLATE,
                String.format("%s,%s", bounds.getCenter().getLat(), bounds.getCenter().getLon()),
                getGMZoomLevel(bounds, mapWidth, mapHeight),
                String.format("%sx%s", mapWidth, mapHeight),
                polyStrings.toString( "&")
        );
        return url;
    }

    public double latRad(double lat) {
        double sin = Math.sin(lat * Math.PI / 180);
        double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;
        return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;
    }

    public double zoom(double mapPx, int worldPx, double fraction) {
        return Math.floor(Math.log(mapPx / worldPx / fraction) / Math.log(2));
    }

    public int getGMZoomLevel(Bounds bounds, int mapWidth, int mapHeight) {

        double neLat = bounds.getNorthEastLat(), neLon = bounds.getNorthEastLon();
        double swLat = bounds.getSouthWestLat(), swLon = bounds.getSouthWestLon();
        double latFraction = (latRad(neLat) - latRad(swLat)) / Math.PI;

        double lngDiff = neLon - swLon;
        double lngFraction = ((lngDiff < 0) ? (lngDiff + 360) : lngDiff) / 360;

        double latZoom = zoom(mapHeight, WORLD_MAP_HEIGHT, latFraction);
        double lngZoom = zoom(mapWidth, WORLD_MAP_WIDTH, lngFraction);

        return (int) Math.min(latZoom, lngZoom);
    }

    private byte[] getImageData(String request) throws IOException {
        BufferedImage image = ImageIO.read(new URL(request));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bos);

        return bos.toByteArray();
    }

    public byte[] generateStaticMap(LPolygon... polygons) throws IOException {
        String url = getGMImageUrl(polygons, 640, 640);
        return getImageData(url);
    }

}
