package za.co.spsi.toolkit.crud.gui.gis;

import com.vividsolutions.jts.geom.MultiPolygon;
import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.geometry.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.shared.Point;

import java.io.File;
import java.net.URI;
import java.util.*;

/**
 * Created by jaspervdbijl on 2016/12/09.
 */
public class ShapeTest {

    private static void process(SimpleFeature simpleFeature,MathTransform transform,MultiPolygon polygon) throws TransformException {
        System.out.println("Area " + polygon.getArea());
        simpleFeature.getAttributes().stream().forEach(a -> System.out.println(a));
        Arrays.stream(polygon.getCoordinates()).forEach(c -> {
            System.out.println(c);
        });

//        System.out.println("new Area " + JTS.transform((com.vividsolutions.jts.geom.Geometry) simpleFeature.getDefaultGeometry(), transform).getArea());
        System.out.println("\n\n");

    }

    public static void main(String args[]) throws Exception {

        File file = new File("/Users/jaspervdbijl/tmp/test.shp");


        try {
            Map connect = new HashMap();
            connect.put("url", file.toURL());

            CoordinateReferenceSystem crs = CRS.parseWKT(
                    new Scanner(new File("/Users/jaspervdbijl/tmp/test.prj")).useDelimiter("\\Z").next());

            ShapefileDataStore dataStore = (ShapefileDataStore) DataStoreFinder.getDataStore(connect);
//            dataStore.forceSchemaCRS(crs);
            String[] typeNames = dataStore.getTypeNames();
            String typeName = typeNames[0];

            System.out.println("Reading content " + typeName);

            FeatureSource featureSource = dataStore.getFeatureSource(typeName);
            FeatureCollection collection = featureSource.getFeatures();
            FeatureIterator iterator = collection.features();

            // get dynamically the CRS of your data:
            FeatureType schema = featureSource.getSchema();
            CoordinateReferenceSystem sourceCRS = schema.getCoordinateReferenceSystem();
            CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");

            MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
            MathTransform transform2 = CRS.findMathTransform(targetCRS,sourceCRS);

            try {
                while (iterator.hasNext()) {
//                    Feature feature = iterator.next();
                    SimpleFeatureImpl simpleFeature = (SimpleFeatureImpl) iterator.next();
                    if (simpleFeature.getDefaultGeometry() instanceof MultiPolygon) {

//                        CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:32736");
//                        JTS.transform((com.vividsolutions.jts.geom.Geometry) simpleFeature.getDefaultGeometry(), transform);

                        process(simpleFeature,transform2,(MultiPolygon) JTS.transform((com.vividsolutions.jts.geom.Geometry) simpleFeature.getDefaultGeometry(), transform));
//                        process(simpleFeature,(MultiPolygon) simpleFeature.getDefaultGeometry());
                    }
                }
            } finally {
                iterator.close();
            }

        } catch (Throwable e) {
        }

        System.out.println("Done");


        String poly = "-25.986325009429343, 32.0224127543555," +
                "-25.986283771285127, 32.02254495437254 ," +
                "-25.986221237079796, 32.02274541347173 ," +
                "-25.986458021026454, 32.022889460082546," +
                "-25.986539624568593, 32.022529088779216," +
                "-25.986325009429343, 32.0224127543555";

        String values[] = poly.split(",");//dao.gpsLocations.get().split(",");
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < values.length / 2; i++) {
            points.add(new Point(Double.valueOf(values[i * 2]), Double.valueOf(values[i * 2 + 1])));
        }
        LPolygon lPolygon = new LPolygon(points.toArray(new Point[]{}));

//        CoordinateReferenceSystem equalAreaCRS = CRS.parseWKT(
//                new Scanner(new File("/Users/jaspervdbijl/tmp/test.prj")).useDelimiter("\\Z").next());
        CoordinateReferenceSystem equalAreaCRS = CRS.decode("EPSG:32736", true);
        MathTransform transform =
                CRS.findMathTransform(DefaultGeographicCRS.WGS84, equalAreaCRS, true);

        // Reproject the polygon and return its area
        System.out.println("A1 " + JTS.transform(lPolygon.getGeometry(), transform).getArea());

        //
//
//
//        AreaFromLatLons areaFromLatLons;
//        CoordinateReferenceSystem c1 = CRS.decode("EPSG:4326");
//        CoordinateReferenceSystem c2 = CRS.decode("EPSG:3857");
//
//        MathTransform t1 = CRS.findMathTransform(c1, c2);

//        System.out.println("Area " + new AreaFromLatLons().getArea(
//                new double[]{
//                        32.0224127543555,32.02254495437254 ,32.02274541347173 ,32.022889460082546,32.022529088779216,32.0224127543555},
//                new double[]{-25.986325009429343,-25.986283771285127,-25.986221237079796,-25.986458021026454,-25.986539624568593,-25.986325009429343}
//                ));

    }
}
