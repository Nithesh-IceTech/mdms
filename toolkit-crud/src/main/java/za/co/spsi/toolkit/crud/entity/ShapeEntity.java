package za.co.spsi.toolkit.crud.entity;

import com.vividsolutions.jts.geom.Geometry;
import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.shared.Point;
import za.co.spsi.toolkit.crud.gui.fields.GPSCoordinatesLField;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.StringList;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jaspervdb on 2016/07/25.
 * Entity is utilised to define a data export for a specific view
 */
@Table(version = 0)
public class ShapeEntity extends EntityDB {

    public enum Type {
        Polygon;

        static Type get(String name) {
            return Arrays.asList(Type.values()).stream().filter(p -> p.name().equals(name)).findFirst().get();
        }
    }

    @Id(uuid = true)
    @Column(name = "SHAPE_ID", size = 50, notNull = true)
    public Field<String> shapeId = new Field<>(this);

    @ForeignKey(table = ShapeImportEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "SHAPE_IMPORT_ID", size = 50)
    public Field<String> shapeImportId = new Field<>(this);

    @Column(name = "label", size = 150)
    public Field<String> label = new Field<>(this);


    @Column(name = "area")
    public Field<Double> area = new Field<>(this);

    @Column(name = "type", size = 20)
    public Field<String> type = new Field<>(this);

    public Field<byte[]> geom = new Field<>(this);

    private Index idxShapeImportId = new Index("idx_SHAPE_IMPORT_ID",this,shapeImportId);

    public ShapeEntity() {
        super("SHAPE");
    }

    public void setGeo(Geometry geometry) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (DataOutputStream dos = new DataOutputStream(bos)) {
                Arrays.stream(geometry.getCoordinates()).forEach(c -> {
                    try {
                        dos.writeDouble(c.x);
                        dos.writeDouble(c.y);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                geom.set(bos.toByteArray());
            }
        }
    }

    private LPolygon getAsPoly(Double[] values) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < values.length / 2; i++) {
            points.add(new Point(values[i * 2], values[i * 2 + 1]));
        }
        LPolygon lPolygon = new LPolygon(points.toArray(new Point[]{}));
        lPolygon.setFill(true);
        lPolygon.setPopup(String.format("%s: %s m2",label.get(),area.get()));
        return lPolygon;
    }

    private List<Double> readPoints() {
        try {
            List<Double> values = new ArrayList<>();
            try (ByteArrayInputStream bis = new ByteArrayInputStream(geom.get())) {
                try (DataInputStream dis = new DataInputStream(bis)) {
                    while (dis.available() > 0) {
                        values.add(dis.readDouble());
                    }
                }
            }
            return values;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public AbstractLeafletVector getGeo() {
        if (geom.get() != null && type.get() != null) {
            Type geoType = Type.get(type.get());
            // set the geometry
            if (geoType == Type.Polygon) {
                return getAsPoly(readPoints().toArray(new Double[]{}));
            } else {
                throw new UnsupportedOperationException("Type not implemented " + type.get());
            }
        }
        return null;
    }

    public String getAsCoordinates() {
        if (geom.get() != null) {
            StringList values = new StringList();
            for (Double value : readPoints()) {
                values.add(GPSCoordinatesLField.LON_FORMAT.format(value));
            }
            return values.toString();
        }
        return null;
    }

}
