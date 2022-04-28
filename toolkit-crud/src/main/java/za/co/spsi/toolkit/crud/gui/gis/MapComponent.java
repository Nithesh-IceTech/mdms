package za.co.spsi.toolkit.crud.gui.gis;

import com.vaadin.ui.Component;
import com.vaadin.ui.OptionGroup;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.vaadin.addon.leaflet.AbstractLeafletVector;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.shared.Bounds;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.db.gis.ImageGeoEntity;
import za.co.spsi.toolkit.crud.db.gis.ImageGeoLocationEntity;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.locale.Translatable;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.sql.DataSource;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by jaspervdb on 2015/09/16.
 */
public class MapComponent extends GeoMap implements Translatable, LMarker.DragEndListener {

    public static final double DEFAULT_LON = 28.271691804, DEFAULT_LAT = -25.814581560;
    private boolean gpsPosWasNull;
    private String caption;

    private MapHelper mapHelper = new MapHelper();
    private ImageGeoEntity imageGeoEntity;
    private Map<LPolygon, ImageGeoLocationEntity> daoMap = new HashMap<>(), initDaoMap = new HashMap<>();
    private boolean modified = false;
    private Boolean hasValuesToUpdate;
    private Boolean multipleAllowed;

    private List<ToolkitConstants.GeoType> geoTypes = new ArrayList<>();
    private ValueUpdateCallback valueUpdateCallback;
    private LMarker marker = null;

    private boolean editable = true;

    public MapComponent(String caption, Boolean hasValuesToUpdate, Boolean multipleAllowed, ToolkitConstants.GeoType... geoTypes) {
        super((STATE_ALL & ~STATE_POLYLINE));
        this.caption = caption;
        this.hasValuesToUpdate = hasValuesToUpdate;

        if (geoTypes != null) {
            this.geoTypes.addAll(Arrays.asList(geoTypes));
        }
        init();
        setHeight("500px");
        setMapHeight("500px");

    }

    public LMarker getMarker() {
        return marker;
    }

    public void setValueUpdateCallback(ValueUpdateCallback valueUpdateCallback) {
        this.valueUpdateCallback = valueUpdateCallback;
    }

    private void init() {
        getMap().setCustomInitOption("scrollWheelZoom", false);
    }

    private void setPopup(LPolygon lPolyline, ToolkitConstants.GeoType geoType) {
        setPopup(lPolyline, getGeoLocationDesc(geoType) + " " + mapHelper.getGisHelper().calculateUserFriendlyArea(lPolyline.getGeometry()));
    }

    public static ToolkitConstants.GeoType getGeoType(ImageGeoLocationEntity dao, List<ToolkitConstants.GeoType> geoTypes) {

        for (ToolkitConstants.GeoType geoType : geoTypes) {
            if (dao.geoLocationTypeCd.get().equals(geoType.getCode())) {
                return geoType;
            }
        }
        return null;
    }

    private LPolygon getAsPolygon(ImageGeoLocationEntity dao) {
        ToolkitConstants.GeoType geoType = getGeoType(dao, geoTypes);
        LPolygon lPolygon = mapHelper.createPolygon(dao, geoTypes);
        setPopup(lPolygon, geoType);
        return lPolygon;
    }

    @Override
    public void setEditable(boolean editable) {
        super.setEditable(editable);
        this.editable = editable;
        if (editable && marker != null) {
            addDragListener();
        } else if (marker != null) {
            marker.removeDragEndListener(this);
        }
    }

    private List<LPolygon> getPolygonByMapType(int locationTypeCode) {
        List<LPolygon> polygons = new ArrayList<>();
        for (LPolygon polygon : daoMap.keySet()) {
            if (daoMap.get(polygon).geoLocationTypeCd.get() == locationTypeCode) {
                polygons.add(polygon);
            }
        }
        return polygons;
    }

    /**
     * always put the area on top
     */
    @Override
    public void reArrangePolys() {
        for (LPolygon polygon : getPolygonByMapType(ToolkitConstants.GeoType.BUILDING.getCode())) {
            polygon.bringToFront();
        }
    }

    private void addDragListener() {

        marker.removeDragEndListener(this);
        marker.addDragEndListener(this);

    }

    private void addCenterMarker(double y, double x) {

        if (marker == null) {
            marker = new LMarker(y, x);
        }

        marker.setPopup(AbstractView.getLocaleValue(ToolkitLocaleId.SURVEY_GPS_POSITION));
        if (editable) {
            addDragListener();
        }
        getMarkerLayer().addComponent(marker);
        centerOn(y, x, "");
        getMap().zoomToContent();
    }

    public void init(DataSource dataSource, final MapComponent.ValueUpdateCallback valueUpdateCallback, ImageGeoEntity imageGeoEntity) {
        try {
            this.imageGeoEntity = imageGeoEntity;
            gpsPosWasNull = valueUpdateCallback.getGpsCoordinates() == null || valueUpdateCallback.getGpsCoordinates()[0] == 0;
            if (!gpsPosWasNull) {
                getMarkerLayer().removeAllComponents();
                addCenterMarker(valueUpdateCallback.getGpsCoordinates()[0], valueUpdateCallback.getGpsCoordinates()[1]);
            }

            clearZones();
            if (imageGeoEntity != null) {
                // load the details
                try (Connection connection = dataSource.getConnection()) {
                    List<ImageGeoLocationEntity> imageGeoLocationEntities = imageGeoEntity.getTxRefEntities(ImageGeoLocationEntity.class).isEmpty() ?
                            imageGeoEntity.imageGeoLocations.getAllAsList(connection, null) :
                            imageGeoEntity.getTxRefEntities(ImageGeoLocationEntity.class);
                    for (ImageGeoLocationEntity dao : imageGeoLocationEntities) {
                        LPolygon lPolygon = getAsPolygon(dao);
                        addZone(lPolygon);
                        daoMap.put(lPolygon, dao);
                        initDaoMap.put(lPolygon, dao);
                    }
                    if (!daoMap.isEmpty()) {
                        super.reArrangePolys();
                    }
                }
            }

            addAttachListener((AttachListener) event -> {
                //
                if (valueUpdateCallback.getGpsCoordinates() == null || (valueUpdateCallback.getGpsCoordinates()[0] == 0)) {
                    if (daoMap.isEmpty()) {
                        // zoom to any locations added
                        MapUtil.centerOnUserLocation(MapComponent.this, 28.271691804, -25.814581560, false,
                                new Location.LocationListener() {
                                    @Override
                                    public void onLocationFound(double latitude, double longitude, double accuracy) {
                                        addCenterMarker(latitude, longitude);
                                    }

                                    @Override
                                    public void onLocationError(LocationError error) {
                                        addCenterMarker(DEFAULT_LAT, DEFAULT_LON);
                                    }

                                    @Override
                                    public void onLocationNotSupported() {
                                        addCenterMarker(DEFAULT_LAT, DEFAULT_LON);
                                    }
                                });
                    } else {
                        Bounds bounds = new Bounds();
                        daoMap.keySet().stream().forEach(p -> bounds.extend(p.getPoints()));
                        addCenterMarker(bounds.getCenter().getLat(), bounds.getCenter().getLon());
                    }
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveEvent(Connection connection) {
        // execute all the old references
        if (editable) {
            for (ImageGeoLocationEntity geo : imageGeoEntity.imageGeoLocations.get(connection, null)) {
                DataSourceDB.deleteHandleException(connection, geo);
            }
            for (ImageGeoLocationEntity image : daoMap.values()) {
                image.setInDatabase(false);
                image.imageId.set(imageGeoEntity.imageId.get());
                DataSourceDB.set(connection, image);
            }
            // reset the image to be re drawn
            imageGeoEntity.image.set(null);
            DataSourceDB.set(connection, imageGeoEntity);
        }
    }

    private Double getAreaValue(ToolkitConstants.GeoType geoType) {
        double area = 0d;
        // update all the dao values
        for (ImageGeoLocationEntity dao : daoMap.values()) {
            if (dao.geoLocationTypeCd.get().equals(geoType.getCode())) {
                area += dao.area.get().doubleValue();
            }
        }
        return area > 0.0 ? area : null;
    }


    private void executeValueCallback(final ToolkitConstants.GeoType geoType) {
        if (valueUpdateCallback != null && editable && hasValuesToUpdate) {
            String question = geoType == ToolkitConstants.GeoType.BUILDING ?
                    AbstractView.getLocaleValue(ToolkitLocaleId.UPDATE_SURVEY_AREA) :
                    geoType == ToolkitConstants.GeoType.LAND ?
                            AbstractView.getLocaleValue(ToolkitLocaleId.UPDATE_SURVEY_LAND_AREA) :

                            geoType == ToolkitConstants.GeoType.LOCATION ?
                                    AbstractView.getLocaleValue(ToolkitLocaleId.UPDATE_SURVEY_LAND_AREA) :

                                    geoType == ToolkitConstants.GeoType.CROP ?
                                            AbstractView.getLocaleValue(ToolkitLocaleId.UPDATE_CROP_AREA) :
                                            AbstractView.getLocaleValue(ToolkitLocaleId.UPDATE_FARM_SIZE);
            MessageBox.createQuestion().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.UPDATE_SURVEY_AREA))
                    .withMessage(question)
                    .withNoButton(ButtonOption.closeOnClick(true))
                    .withOkButton(new Runnable() {
                        @Override
                        public void run() {
                            valueUpdateCallback.updateValue(geoType, getAreaValue(geoType));
                            reArrangePolys();
                        }
                    }, ButtonOption.closeOnClick(true))
                    .open();
        }
    }

    private static DecimalFormat gpsFormat = new DecimalFormat("#.000000");

    private String formatGpsCoordinates(com.vividsolutions.jts.geom.Point point) {
        return String.format("%s,%s", gpsFormat.format(point.getY()), gpsFormat.format(point.getX()));
    }

    public void setGeoTypes(List<ToolkitConstants.GeoType> geoTypes) {
        this.geoTypes = geoTypes;
    }


    private static synchronized String getGeoLocationDesc(ToolkitConstants.GeoType geoType) {
        return AbstractView.getLocaleValue(geoType.getBackofficeCaption());
    }

    @Override
    public void initFeatureDrawn(final AbstractLeafletVector drawnFeature) {
        modified = true;
        final OptionGroup typeBox = new OptionGroup();
        for (ToolkitConstants.GeoType geoType : geoTypes) {
            typeBox.addItem(geoType.getCode());
            typeBox.setItemCaption(geoType.getCode(), getGeoLocationDesc(geoType));
        }

        typeBox.setValue(geoTypes.get(0).getCode());
        typeBox.setNullSelectionAllowed(false);

        MessageBox.createQuestion().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.SELECT_AREA_TYPE))
                .withMessage(typeBox).withWidth("25em")
                .withOkButton(() -> {
                    LPolygon lPolygon = (LPolygon) drawnFeature;
                    Integer selectedValue = (Integer) typeBox.getValue();
                    //final ToolkitCrudConstants.GeoType geoType = (ToolkitCrudConstants.GeoType) typeBox.getValue();
                    final ToolkitConstants.GeoType geoType = ToolkitConstants.GeoType.getByCode(selectedValue);
                    lPolygon.setFillColor(geoType.getColor());
                    lPolygon.setColor(geoType.getColor());
                    lPolygon.setFill(true);
                    setPopup(lPolygon, geoType);
                    addZone(lPolygon);
                    daoMap.put(lPolygon, mapHelper.getAsImageGeoLocationsDao(lPolygon, geoType));
                    executeValueCallback(geoType);
                    if (!daoMap.isEmpty()) {
                        //Update marker to center of all drawings
                        updateMarkerToCenter();
                    }
                    reArrangePolys();
                }, ButtonOption.closeOnClick(true))
                .open();

    }

    @Override
    public void initFeatureModified(AbstractLeafletVector drawnFeature) {
        // point must have at least two points
        modified = true;

        ToolkitConstants.GeoType geoType = getGeoType(daoMap.get(drawnFeature), geoTypes);
        setPopup((LPolygon) drawnFeature, geoType);
        // update the dao area
        daoMap.get(drawnFeature).area.set(
                new Double(mapHelper.getGisHelper().calculateRoundedArea(drawnFeature.getGeometry())));
        executeValueCallback(geoType);
        daoMap.put((LPolygon) drawnFeature, mapHelper.getAsImageGeoLocationsDao((LPolygon) drawnFeature, geoType));
        if (!daoMap.isEmpty()) {
            //Update marker to center of all drawings
            updateMarkerToCenter();
        }
        reArrangePolys();
    }

    @Override
    public void featureDeleted(AbstractLeafletVector feature) {
        modified = true;
        ToolkitConstants.GeoType geoType = getGeoType(daoMap.get(feature), geoTypes);
        daoMap.remove(feature);
        executeValueCallback(geoType);
        if (!daoMap.isEmpty()) {
            //Update marker to center of all drawings
            updateMarkerToCenter();
        }
    }

    private void updateMarkerToCenter() {
        Bounds bounds = new Bounds();
        daoMap.keySet().stream().forEach(p -> bounds.extend(p.getPoints()));
        valueUpdateCallback.updateLocation(bounds.getCenter().getLat(), bounds.getCenter().getLon());

        if (marker == null || marker.getPoint() == null) {
            marker = new LMarker(bounds.getCenter().getLat(), bounds.getCenter().getLon());
        } else {
            marker.getPoint().setLat(bounds.getCenter().getLat());
            marker.getPoint().setLon(bounds.getCenter().getLon());
        }
    }

    public boolean isModified() {
        return modified;
    }

    public void resetModified() {
        modified = false;
    }

    @Override
    public void translate(String oldLocale, String newLocale) {
        // translate the zone captions

        Iterator<Component> iterator = getZoneIterator();

        while (iterator.hasNext()) {
            LPolygon lPolygon = (LPolygon) iterator.next();
            ToolkitConstants.GeoType geoType = getGeoType(daoMap.get(lPolygon), geoTypes);
            setPopup(lPolygon, geoType);
        }
        if (marker != null) {
            marker.setPopup(AbstractView.getLocaleValue(ToolkitLocaleId.SURVEY_GPS_POSITION));
        }
    }

    @Override
    public void dragEnd(LMarker.DragEndEvent dragEndEvent) {
        valueUpdateCallback.updateLocation(((LMarker) dragEndEvent.getSource()).getPoint().getLat(),
                ((LMarker) dragEndEvent.getSource()).getPoint().getLon());
    }


    public static interface ValueUpdateCallback {

        void updateValue(ToolkitConstants.GeoType geoType, Double value);

        void updateLocation(double lat, double lon);

        double[] getGpsCoordinates();

    }

}