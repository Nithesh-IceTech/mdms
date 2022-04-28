package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.db.gis.ImageGeoEntity;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.gis.MapComponent;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.Assert;

import java.sql.Connection;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class ImageGeoField extends LField {

    private MapComponent mapComponent;
    private ImageGeoEntity imageGeoEntity;
    private EntityRef<ImageGeoEntity> imageGeoRef;
    private MapComponent.ValueUpdateCallback valueUpdateCallback;
    private Boolean hasValuesToupdate;
    private Boolean multipleAllowed;
    private ToolkitConstants.GeoType[] geoTypes;

    public ImageGeoField(String captionId, EntityRef<ImageGeoEntity> imageGeoRef, MapComponent.ValueUpdateCallback valueUpdateCallback, Layout model,
                         Boolean hasValuesToUpdate, Boolean multipleAllowed, ToolkitConstants.GeoType... geoTypes) {
        super(captionId, "", model);
        this.imageGeoRef = imageGeoRef;
        this.valueUpdateCallback = valueUpdateCallback;
        this.hasValuesToupdate = hasValuesToUpdate;
        this.multipleAllowed = multipleAllowed;
        this.geoTypes = geoTypes;
    }

    public MapComponent getMapComponent() {
        return mapComponent;
    }

    @Override
    public Component buildComponent() {
        try {
            mapComponent = new MapComponent(getCaption(), hasValuesToupdate, multipleAllowed, geoTypes);
            mapComponent.setBeanManager(getLayout().getDataSource(),getLayout().getBeanManager());
            try (Connection connection = getLayout().getDataSource().getConnection()) {
                imageGeoEntity = getLayout().getMainEntity().getTxRefEntities(ImageGeoEntity.class).isEmpty() ?
                        imageGeoRef.getOne(connection, null) :
                        (ImageGeoEntity) getLayout().getMainEntity().getTxRefEntities(ImageGeoEntity.class).get(0);
                if (imageGeoEntity == null) {
                    imageGeoEntity = imageGeoRef.getNew();
                    DataSourceDB.set(getLayout().getDataSource(), imageGeoEntity);
                }
                FieldList fField = getLayout().getMainEntity().getForeignKeyFields(ImageGeoEntity.class);
                Assert.isTrue(fField.size() == 1, "None or more then one FKey to ImageGeoEntity found in %s", getLayout().getMainEntity().getClass().getName());
                fField.get(0).set(imageGeoEntity.getSingleId().get());

                mapComponent.init(getLayout().getDataSource(), valueUpdateCallback, imageGeoEntity);
            }
            mapComponent.setEditable(!getProperties().isReadOnly());
            mapComponent.setValueUpdateCallback(valueUpdateCallback);
            return mapComponent;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void saveEvent(Connection connection) {
        super.saveEvent(connection);
        if (mapComponent.isModified()) {
            imageGeoEntity.image.set(null);
            DataSourceDB.set(connection, imageGeoEntity);
            // persist the mapping changes
            mapComponent.saveEvent(connection);
            mapComponent.resetModified();
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
    }
}
