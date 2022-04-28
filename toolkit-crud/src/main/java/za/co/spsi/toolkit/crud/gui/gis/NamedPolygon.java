package za.co.spsi.toolkit.crud.gui.gis;

import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.shared.Point;

/**
 * Created by jaspervdbijl on 2017/05/24.
 */
public class NamedPolygon extends LPolygon {

    private String name;

    public NamedPolygon(String name,Point... points) {
        super(points);
        this.name = name;
    }

    public NamedPolygon(String name,LinearRing jtsLinearRing) {
        super(jtsLinearRing);
        this.name = name;
    }

    public NamedPolygon(String name,Polygon polygon) {
        super(polygon);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
