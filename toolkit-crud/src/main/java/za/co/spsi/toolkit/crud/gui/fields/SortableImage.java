package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.ui.Image;

/**
 * Created by ettienne on 2017/07/04.
 */
public class SortableImage extends Image implements Property, Comparable {
    private String itemId;

    public SortableImage(Resource resource, String itemId) {
        super(null, resource);
        this.itemId = itemId;
    }

    @Override
    public Object getValue() {
        return itemId;
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException {
    }

    @Override
    public Class getType() {
        return String.class;
    }

    @Override
    public int compareTo(Object o) {
        return itemId.compareTo(((SortableImage) o).getValue().toString());
    }
}