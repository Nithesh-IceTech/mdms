package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.gis.MapComponent;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class GeoField extends LField {

    private Field<Double> x, y;
    private MapComponent mapComponent;
    private MapComponent.ValueUpdateCallback valueUpdateCallback;

    public GeoField(String captionId, Field<Double> x, Field<Double> y, Layout model, MapComponent.ValueUpdateCallback valueUpdateCallback) {
        super(captionId, "", model);
        this.x = x;
        this.y = y;
        this.valueUpdateCallback = valueUpdateCallback;
    }

    public MapComponent getMapComponent() {
        return mapComponent;
    }

    @Override
    public Component buildComponent() {
        try {
            mapComponent = new MapComponent(getCaption(), false, false, ToolkitConstants.GeoType.LAND);
            mapComponent.setBeanManager(getLayout().getDataSource(), getLayout().getBeanManager());

            if (valueUpdateCallback == null) {
                valueUpdateCallback =
                        new MapComponent.ValueUpdateCallback() {
                            @Override
                            public void updateValue(ToolkitConstants.GeoType geoType, Double value) {

                            }

                            @Override
                            public void updateLocation(double lat, double lon) {
                                GeoField.this.x.set(lon);
                                GeoField.this.y.set(lat);
                            }

                            @Override
                            public double[] getGpsCoordinates() {
                                return GeoField.this.x.get() != null && GeoField.this.y.get() != null ?
                                        new double[]{GeoField.this.y.get(), GeoField.this.x.get()} : null;
                            }
                        };
            }

            mapComponent.init(getLayout().getDataSource(), valueUpdateCallback, null);
            mapComponent.setValueUpdateCallback(valueUpdateCallback);

            return mapComponent;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected com.vaadin.ui.Field intoBindingsWithNoValidation(boolean update) {
        return null;
    }

    @Override
    public void intoBindings() {

    }

    @Override
    public void applyProperties() {
        super.applyProperties();
        mapComponent.setEditable(!getProperties().isReadOnly());
    }

    @Override
    public void intoControl() {
        applyProperties();
        // add marker
    }
}
