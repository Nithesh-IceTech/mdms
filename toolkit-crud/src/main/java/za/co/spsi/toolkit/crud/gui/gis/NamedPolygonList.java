package za.co.spsi.toolkit.crud.gui.gis;

import org.vaadin.addon.leaflet.LPolygon;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jaspervdbijl on 2017/05/24.
 */
public class NamedPolygonList extends ArrayList<NamedPolygon> {

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public int indexOf(Object o) {
        if (o instanceof NamedPolygon) {
            for (int i =0;i < size();i++) {
                if (get(i).getName().equals((((NamedPolygon) o).getName()))) {
                    return i;
                }
            }
            return -1;
        } else {
            return super.indexOf(o);
        }
    }

    public NamedPolygonList subtract(Set<LPolygon> set) {
        return set.stream().filter(p -> p instanceof NamedPolygon && !contains(p)).map(p -> (NamedPolygon)p).collect(Collectors.toCollection(NamedPolygonList::new));
    }
}
