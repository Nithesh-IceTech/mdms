package za.co.spsi.toolkit.crud.gui.gis;

import com.vaadin.data.Property;
import com.vaadin.event.UIEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.leaflet.*;
import org.vaadin.addon.leaflet.control.LAttribution;
import org.vaadin.addon.leaflet.control.LLayers;
import org.vaadin.addon.leaflet.control.LScale;
import org.vaadin.addon.leaflet.control.LZoom;
import org.vaadin.addon.leaflet.editable.*;
import org.vaadin.addon.leaflet.shared.ControlPosition;
import org.vaadin.addon.leaflet.shared.Point;
import org.vaadin.addon.leaflet.shramov.LGoogleLayer;
import org.vaadin.addon.leaflet.util.JTSUtil;
import org.vaadin.addons.locationtextfield.GeocodedLocation;
import org.vaadin.addons.locationtextfield.GoogleGeocoder;
import org.vaadin.addons.locationtextfield.LocationTextField;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.entity.ShapeEntity;
import za.co.spsi.toolkit.crud.entity.gui.ShapeLayout;
import za.co.spsi.toolkit.crud.gui.Permission;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.query.LayoutViewGrid;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.locale.LocaleCookieEntity;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.util.Assert;

import javax.enterprise.inject.spi.BeanManager;
import javax.sql.DataSource;
import java.util.*;
import java.util.function.Consumer;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created by jaspervdb on 2015/11/19.
 */
public class GeoMap extends VerticalLayout implements FeatureDrawnListener, FeatureModifiedListener {

    public static final int STATE_POLYGON = 0x02, STATE_POLYLINE = 0x04, STATE_EDIT = 0x10, STATE_DELETE = 0x20,
            STATE_ENLARGE = 0x40, STATE_LOCATE = 0x80, STATE_SEARCH = 0x100, STATE_DRAW = 0x200,
            STATE_ALL = STATE_POLYGON | STATE_POLYLINE | STATE_EDIT | STATE_DELETE | STATE_ENLARGE | STATE_LOCATE | STATE_SEARCH | STATE_DRAW;

    public enum CurrentState {
        DELETE, EDIT, NONE
    }

    private CurrentState state = CurrentState.NONE;
    private AbsoluteLayout root = new AbsoluteLayout();

    private ToggleGroup toggleGroup;
    private List<LEditable> editables = null;
    private AbstractLeafletVector toBeDeleted;
    private Integer toBeDeletedIndex;
    private LMap map = new LMap();
    private LEditableMap editMap = new LEditableMap(map);
    private LocationTextField<GeocodedLocation> ltf;
    private Legend legend = new Legend();
    private LLayerGroup geoLocationMarkerLayer = new LLayerGroup(), zoneLayer = new LLayerGroup(), markerLayer = new LLayerGroup();
    private Button
            importPolygonBtn = new Button(FontAwesome.MAP),
            polygonCreateBtn = new Button(FontAwesome.STAR),
            polylineCreateBtn = new Button(FontAwesome.STAR_O), editBtn = new Button(FontAwesome.EDIT),
            deleteBtn = new Button(FontAwesome.TRASH_O),
            acceptBtn = new Button(FontAwesome.CHECK),
            rejectBtn = new Button(FontAwesome.UNDO),
            enlargeBtn = new Button(FontAwesome.EXPAND),
            centerBtn = new Button(FontAwesome.CROSSHAIRS),
            backBtn = new Button(FontAwesome.STEP_BACKWARD),
            searchBtn = new Button(FontAwesome.SEARCH),
            showLegend = getToolbarButton(FontAwesome.KEY);
    private VerticalLayout topRightBtnGroup = new VerticalLayout(enlargeBtn, centerBtn, backBtn, searchBtn, showLegend);
    private VerticalLayout toolbar = new VerticalLayout(importPolygonBtn, polygonCreateBtn, polylineCreateBtn, editBtn, deleteBtn, acceptBtn, rejectBtn);

    private Map<AbstractLeafletVector, String> popupMap = new HashMap<>();
    private Window expandedWindow = null;
    private List<Point> prevLocations = new ArrayList<>();
    private List<String> prevLocationDesc = new ArrayList<>();
    private LMarker searchMarker;

    private DataSource dataSource;
    private BeanManager beanManager;

    private String mapHeight = "100%";
    private int mapState;
    private Label busyLabel;


    public GeoMap(int state) {
        init();
        updateState(state);
    }

    public GeoMap() {
        this(STATE_ALL);
    }

    public Legend getLegend() {
        return legend;
    }

    public void setBeanManager(DataSource dataSource,BeanManager beanManager) {
        this.dataSource = dataSource;
        this.beanManager = beanManager;
        updateState(mapState);
    }

    public void updateState(int state) {
        this.mapState = state;
        deleteBtn.setVisible((state & STATE_DELETE) == STATE_DELETE);
        importPolygonBtn.setVisible(beanManager != null && (state & STATE_POLYGON) == STATE_POLYGON && ((ToolkitUI)UI.getCurrent()).hasRole(ToolkitCrudConstants.SYS_ADMIN));
        polygonCreateBtn.setVisible((state & STATE_POLYGON) == STATE_POLYGON);
        polylineCreateBtn.setVisible((state & STATE_POLYLINE) == STATE_POLYLINE);
        editBtn.setVisible((state & STATE_EDIT) == STATE_EDIT);
        enlargeBtn.setVisible((state & STATE_ENLARGE) == STATE_ENLARGE);
        centerBtn.setVisible((state & STATE_LOCATE) == STATE_LOCATE);
        searchBtn.setVisible((state & STATE_SEARCH) == STATE_SEARCH);
        backBtn.setVisible(searchBtn.isVisible() && prevLocations.size() > 1);

        searchBtn.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.SEARCH_RECORD));
    }

    public static Button getToolbarButton(Resource icon) {
        Button toolbarButton = new Button(icon);
        toolbarButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        toolbarButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        toolbarButton.addStyleName(ValoTheme.BUTTON_SMALL);

        return toolbarButton;
    }

    public VerticalLayout getTopRightBtnGroup() {
        return topRightBtnGroup;
    }

    private void init() {
        setSizeFull();
        addComponent(root);
        toolbar.addStyleName("map");
        topRightBtnGroup.addStyleName("map");

        setHeightUndefined();

        initLocationTextField();
        initMap();
        initEdit();
        initButtons();
        root.setSizeFull();

        map.setSizeFull();
        map.setHeight(mapHeight);
        map.setWidth("100%");

        root.setSizeFull();

        busyLabel = com.kbdunn.vaadin.addons.fontawesome.FontAwesome.COG.getLabel().setSize2x().spin();
        busyLabel.setSizeUndefined();
        busyLabel.setVisible(false);
        root.addComponent(map, "z-index:0");
        root.addComponent(initToolbar(toolbar), "top:30%;left:10px;z-index:1");
        root.addComponent(initToolbar(topRightBtnGroup), "top:10px;right:10px;z-index:2");
        root.addComponent(initToolbar(legend), "bottom:50px;right:10px;z-index:3");
        root.addComponent(ltf, "top:10px;left:50px;z-index:4");
        root.addComponent(busyLabel,"top:10px;right:50px;z-index:5");

        setExpandRatio(root, 2f);
    }

    public void setBusy(boolean busy) {
        this.busyLabel.setVisible(busy);
    }

    public AbsoluteLayout getRootLayout() {
        return root;
    }

    public void setMapHeight(String height) {
        root.setHeight(height);
    }

    public void setEditable(boolean editable) {
        toolbar.setVisible(editable);
    }

    public void addSearchMarker(double lon, double lat, String desc) {
        if (searchMarker != null) {
            geoLocationMarkerLayer.removeComponent(searchMarker);
        }
        searchMarker = new LMarker(lat, lon);
        searchMarker.setIcon(new ThemeResource("img/marker-icon-green-sh.png"));
        searchMarker.setPopup(desc);
        geoLocationMarkerLayer.addComponent(searchMarker);
    }

    private void initLocationTextField() {
        GoogleGeocoder googleGeocoder = GoogleGeocoder.getInstance();
        googleGeocoder.setUseSecureConnection(true);
        googleGeocoder.setLimit(0);

        ltf = new LocationTextField<GeocodedLocation>(GoogleGeocoder.getInstance(), GeocodedLocation.class);
        ltf.setWidth("300px");
        ltf.setCaption(null);
        ltf.addLocationValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                // add a marker to the position and zoom to it
                geoLocationMarkerLayer.removeAllComponents();
                GeocodedLocation location = (GeocodedLocation) event.getProperty().getValue();
                addSearchMarker(location.getLon(), location.getLat(), location.getGeocodedAddress());
                centerOn(location.getLat(), location.getLon(), location.getGeocodedAddress());
            }
        });
        ltf.setVisible(false);
    }

    public List<AbstractLeafletVector> getZones(boolean sorted) {
        List<AbstractLeafletVector> zones = new ArrayList<>();
        for (Iterator<Component> iterator = zoneLayer.iterator(); iterator.hasNext(); zones.add((AbstractLeafletVector) iterator.next())) {
        }
        if (sorted) {
            for (int i = 0; i < zones.size() - 1; i++) {
                for (int e = i + 1; e < zones.size(); e++) {
                    if (zones.get(i).getGeometry().contains(zones.get(e).getGeometry())) {
                        AbstractLeafletVector feature = zones.get(i);
                        zones.set(i, zones.get(e));
                        zones.set(e, feature);
                    }
                }
            }
        }
        return zones;
    }

    public void reArrangePolys() {
        // rearrange components such that contained poly's are added last
        final List<AbstractLeafletVector> zones = getZones(true);
        UI.getCurrent().addPollListener(new UIEvents.PollListener() {
            @Override
            public void poll(UIEvents.PollEvent event) {
                UI.getCurrent().removePollListener(this);
                zones.get(zones.size() - 1).bringToBack();
                zones.get(0).bringToFront();
                UI.getCurrent().setPollInterval(-1);
            }
        });
        UI.getCurrent().setPollInterval(100);
    }

    public static Button initToolbarBtn(Button btn) {
        btn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btn.addStyleName(ValoTheme.BUTTON_SMALL);
        return btn;
    }

    private void initButtons() {
        Consumer<Button> init = btn -> initToolbarBtn(btn);
        Arrays.asList(importPolygonBtn, polygonCreateBtn, polylineCreateBtn, editBtn, deleteBtn,
                acceptBtn, rejectBtn, enlargeBtn, centerBtn, searchBtn, backBtn).stream().forEach(b -> init.accept(b));
        toggleGroup = new ToggleGroup(polygonCreateBtn, polylineCreateBtn, editBtn, deleteBtn);
        acceptBtn.setVisible(false);
        rejectBtn.setVisible(false);
        importPolygonBtn.addClickListener((Button.ClickListener) event -> importPolygon());
        polygonCreateBtn.addClickListener((Button.ClickListener) event -> {
            disablePopups();
            editMap.startPolygon();
            toggleGroup.setEnabled(false);
        });
        polylineCreateBtn.addClickListener((Button.ClickListener) event -> {
            disablePopups();
            editMap.startPolyline();
            toggleGroup.setEnabled(false);
        });
        editBtn.addClickListener((Button.ClickListener) event -> {
            state = CurrentState.EDIT;
            disablePopups();
            editables = new ArrayList<>();
            for (AbstractLeafletVector feature : getZones(false)) {
                editables.add(new LEditable(feature));
                editables.get(editables.size() - 1).addFeatureModifiedListener(GeoMap.this);
            }
            toggleGroup.setEnabled(false);
            acceptBtn.setVisible(true);
        });
        final LeafletClickListener deleteClickListener = new LeafletClickListener() {
            @Override
            public void onClick(LeafletClickEvent event) {
                List<AbstractLeafletVector> zones = getZones(true);
                for (AbstractLeafletVector zone : zones) {
                    if (zone.getGeometry().contains(JTSUtil.toPoint(event.getPoint()))) {
                        map.removeClickListener(this);
                        toBeDeleted = zone;
                        toBeDeletedIndex = zones.indexOf(zone);
                        zoneLayer.removeComponent(toBeDeleted);
                        acceptBtn.setVisible(true);
                        toggleGroup.setEnabled(false);
                        break;
                    }
                }
            }
        };
        deleteBtn.addClickListener((Button.ClickListener) event -> {
            disablePopups();
            state = CurrentState.DELETE;
            rejectBtn.setVisible(true);
            toBeDeleted = null;
            toBeDeletedIndex = 0;
            toggleGroup.setEnabled(false);
            map.addClickListener(deleteClickListener);
        });
        acceptBtn.addClickListener((Button.ClickListener) event -> {
            toggleGroup.setEnabled(true);
            if (state == CurrentState.EDIT) {
                for (LEditable editable : editables) {
                    editable.remove();
                }
                enablePopups();
            }
            if (state == CurrentState.DELETE) {
                acceptBtn.setVisible(false);
                rejectBtn.setVisible(false);
                featureDeleted(toBeDeleted);
                editBtn.setEnabled(getZoneCount() > 0);
                deleteBtn.setEnabled(getZoneCount() > 0);
                enablePopups();
            }
            acceptBtn.setVisible(false);
            rejectBtn.setVisible(false);
        });
        rejectBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (state == CurrentState.DELETE) {
                    if (toBeDeleted != null) {
                        addLayerAtIndex(toBeDeleted, toBeDeletedIndex);
                        for (AbstractLeafletVector zone : getZones(false)) {
                            zone.removeListener(LeafletClickEvent.class, this);
                        }
                    }
                }
                enablePopups();
                state = CurrentState.NONE;
                acceptBtn.setVisible(false);
                rejectBtn.setVisible(false);
                toggleGroup.setEnabled(true);
            }
        });
        enlargeBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (expandedWindow == null) {
                    // remove map from this vertical layout and add in a new window
                    removeComponent(root);
                    expandedWindow = new Window("", root);
                    expandedWindow.setResizable(false);
                    expandedWindow.setClosable(false);
                    expandedWindow.setWidth("90%");
                    expandedWindow.setHeight("90%");
                    expandedWindow.setModal(true);
                    root.setHeight(Math.round(Page.getCurrent().getBrowserWindowHeight() * 0.9) + "px");
                    expandedWindow.addCloseListener(new Window.CloseListener() {
                        @Override
                        public void windowClose(Window.CloseEvent e) {
                            // put the map component back
                            root.setHeight(mapHeight);
                            addComponent(root);
                            GeoMap.this.expandedWindow = null;
                        }
                    });
                    UI.getCurrent().addWindow(expandedWindow);
                } else {
                    expandedWindow.close();
                }
            }
        });
        centerBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                MapUtil.centerOnUserLocation(GeoMap.this, null, null, true, null);
            }
        });
        searchBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ltf.setVisible(!ltf.isVisible());
                ltf.focus();
            }
        });
        backBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Point p = prevLocations.get(prevLocations.size() - 2);
                map.setCenter(p.getLat(), p.getLon());
                if (!prevLocationDesc.get(prevLocationDesc.size() - 2).isEmpty() && prevLocations.size() > 2) {
                    addSearchMarker(p.getLon(), p.getLat(), prevLocationDesc.get(prevLocationDesc.size() - 2));
                } else {
                    if (searchMarker != null) {
                        geoLocationMarkerLayer.removeComponent(searchMarker);
                    }
                    searchMarker = null;
                }
                prevLocations.remove(prevLocations.size() - 1);
                prevLocationDesc.remove(prevLocationDesc.size() - 1);

                backBtn.setVisible(searchBtn.isVisible() && prevLocations.size() > 1);
            }
        });
        backBtn.setVisible(false);
    }

    private void importPolygon() {
        // show the polygon's in a window
        ShapeLayout shapeLayout = getBean(beanManager, ShapeLayout.class);
        Assert.notNull(shapeLayout, "No implementation of ShapeLayout provider");
        LayoutViewGrid.Container component = shapeLayout.buildList(dataSource, (layout, source, newEvent, entityDB) -> {
            AbstractLeafletVector feature = ((ShapeEntity) entityDB).getGeo();
            featureDrawn(feature);
            map.zoomToExtent(feature.getGeometry());
            Util.getComponentParent(Window.class, source).close();
        });
        shapeLayout.getToolbar().init(new Permission().setMayCreate(false).setMayDelete(false));
        Util.showInWindow(AbstractView.getLocaleValue(ToolkitLocaleId.SHAPE_IMPORT), component, "95%", "95%");
    }

    private void disablePopups() {
        for (AbstractLeafletVector feature : popupMap.keySet()) {
            feature.setPopup(null);
        }
    }

    private void enablePopups() {
        for (AbstractLeafletVector feature : popupMap.keySet()) {
            feature.setPopup(popupMap.get(feature));
        }
    }

    private void addLayerAtIndex(AbstractLeafletVector layer, int index) {
        List<AbstractLeafletVector> zones = getZones(false);
        zones.add(index, layer);
        zoneLayer.removeAllComponents();
        for (AbstractLeafletVector zone : zones) {
            zoneLayer.addComponent(zone);
        }
    }

    /**
     * overload to intercept
     *
     * @param feature
     */
    public void featureDeleted(AbstractLeafletVector feature) {

    }

    public void centerOn(double lat, double lon, String locDesc) {
        prevLocations.add(new Point(lat, lon));
        prevLocationDesc.add(locDesc);

        backBtn.setVisible(searchBtn.isVisible() && prevLocations.size() > 1);
        map.setCenter(lat, lon);
    }

    public LMap getMap() {
        return map;
    }

    public void swopZoneLayer(LLayerGroup zoneLayer) {
        Assert.isTrue(state == CurrentState.NONE, "You can only swop a layer if the current state is not editing");
        // remove current zone layer
        map.removeLayer(this.zoneLayer);
        this.zoneLayer = zoneLayer;
        map.addLayer(this.zoneLayer);
    }

    public void addZone(AbstractLeafletVector vector) {
        zoneLayer.addComponent(vector);
        editBtn.setEnabled(toolbar.isEnabled());
        deleteBtn.setEnabled(toolbar.isEnabled());
    }

    public void removeZone(AbstractLeafletVector vector) {
        zoneLayer.removeComponent(vector);
        editBtn.setEnabled(zoneLayer.getComponentCount() > 0 && toolbar.isEnabled());
        deleteBtn.setEnabled(zoneLayer.getComponentCount() > 0 && toolbar.isEnabled());
    }

    public void clearZones() {
        zoneLayer.removeAllComponents();
        editBtn.setEnabled(false);
        deleteBtn.setEnabled(false);
    }

    public LLayerGroup getZoneLayer() {
        return zoneLayer;
    }

    public int getZoneCount() {
        return zoneLayer.getComponentCount();
    }

    public Iterator<Component> getZoneIterator() {
        return zoneLayer.iterator();
    }

    public LLayerGroup getMarkerLayer() {
        return markerLayer;
    }

    public static LMap initMap(LMap map) {
        map.setCustomInitOption("editable", true);
        map.addControl(new LAttribution());
        map.addControl(new LScale());
        map.addControl(new LLayers());
        map.addControl(new LZoom());
        map.setMaxZoom(25);

        map.getLayersControl().setPosition(ControlPosition.bottomleft);

        map.addBaseLayer(new LGoogleLayer(LGoogleLayer.Type.ROADMAP), "Road");
        map.addBaseLayer(new LGoogleLayer(LGoogleLayer.Type.SATELLITE), "Satelite");
        map.addBaseLayer(new LGoogleLayer(LGoogleLayer.Type.TERRAIN), "Terrain");
        map.addBaseLayer(new LGoogleLayer(LGoogleLayer.Type.HYBRID), "Hybrid");




        return map;
    }

    private void initMap() {

        initMap(map);
        map.addLayer(geoLocationMarkerLayer);
        map.addLayer(zoneLayer);
        map.addLayer(markerLayer);

    }

    public void addLayer(LLayerGroup layer) {
        map.addLayer(layer);
    }

    private VerticalLayout getSideBarLayout() {
        Label header = new Label(AbstractView.getLocaleValue(ToolkitLocaleId.SEARCH_MAPS));
        header.addStyleName(ValoTheme.LABEL_H2);
        VerticalLayout main = new VerticalLayout(header, ltf);
        main.setMargin(true);
        main.setSpacing(true);
        return main;
    }

    private void initEdit() {
        editMap.addFeatureDrawnListener(this);
        showLegend.addClickListener((Button.ClickListener) clickEvent -> {
            legend.setVisible(!legend.isVisible());
            LocaleCookieEntity.set(entity -> entity.mapLegendVisible.set(legend.isVisible()));
        });
        legend.setVisible(LocaleCookieEntity.get().mapLegendVisible.getNonNull());
    }

    public void setState(int state) {
        polygonCreateBtn.setVisible((state & STATE_POLYGON) == STATE_POLYGON);
        polylineCreateBtn.setVisible((state & STATE_POLYGON) == STATE_POLYLINE);
        editBtn.setVisible((state & STATE_POLYGON) == STATE_EDIT);
        deleteBtn.setVisible((state & STATE_POLYGON) == STATE_DELETE);
    }

    public CurrentState getCurrentState() {
        return state;
    }

    private VerticalLayout initToolbar(VerticalLayout toolbar) {
        toolbar.setWidthUndefined();
        toolbar.setHeightUndefined();
        toolbar.addStyleName("toolbar");
        return toolbar;
    }

    @Override
    public final void featureDrawn(FeatureDrawnEvent event) {
        final AbstractLeafletVector drawnFeature = (AbstractLeafletVector) event.getDrawnFeature();
        featureDrawn(drawnFeature);
    }

    public final void featureDrawn(AbstractLeafletVector drawnFeature) {
        initFeatureDrawn(drawnFeature);
        // Save the layer
        zoneLayer.addComponent(drawnFeature);
        enablePopups();
        toggleGroup.setEnabled(true);
        editBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
    }

    /**
     * overlaod method to process feature
     *
     * @param drawnFeature
     */
    public void initFeatureDrawn(AbstractLeafletVector drawnFeature) {
    }

    @Override
    public void featureModified(FeatureModifiedEvent event) {
        final AbstractLeafletVector modFeature = (AbstractLeafletVector) event.getModifiedFeature();
        initFeatureModified(modFeature);
    }

    /**
     * important to use this method instead of just setting it yourself
     * edit and create will deactivate all the popups on the layers and reset them after its done
     *
     * @param popup
     */
    public void setPopup(AbstractLeafletVector feature, String popup) {
        popupMap.put(feature, popup);
        feature.setPopup(popup);
    }

    /**
     * overload method to process feature
     *
     * @param drawnFeature
     */
    public void initFeatureModified(AbstractLeafletVector drawnFeature) {
    }

    public static class ToggleGroup {
        private List<Button> buttons = new ArrayList<>();

        public ToggleGroup(Button... buttons) {
            for (Button b : buttons) {
                add(b);
            }
        }

        public void setEnabled(boolean enable) {
            for (Button btn : buttons) {
                btn.setEnabled(enable);
            }
        }

        public void add(final Button btn) {
            buttons.add(btn);
            btn.addClickListener((Button.ClickListener) event -> {
                for (Button b : buttons) {
                    btn.setEnabled(!btn.isEnabled());
                    if (btn != b) {
                        b.setEnabled(!btn.isEnabled());
                    }
                }
            });
        }
    }

}
