package za.co.spsi.toolkit.crud.gis.gui;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addon.leaflet.LLayerGroup;
import org.vaadin.addon.leaflet.shared.Point;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gis.db.DeviceLocationEntity;
import za.co.spsi.toolkit.crud.gis.db.DeviceLocationView;
import za.co.spsi.toolkit.crud.gis.db.TripEntity;
import za.co.spsi.toolkit.crud.gui.gis.GeoMap;
import za.co.spsi.toolkit.crud.gui.gis.MapHelper;
import za.co.spsi.toolkit.crud.gui.gis.MapUtil;
import za.co.spsi.toolkit.crud.gui.query.LayoutViewGrid;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.crud.util.AccessHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.FormattedSql;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static za.co.spsi.toolkit.crud.gui.gis.GeoMap.*;

/**
 * Created by jaspervdbijl on 2017/04/19.
 * Custom component that contains a map, devices and trips
 */
@Dependent
public class DeviceTrackMap extends VerticalSplitPanel {

    @Inject
    public DeviceLayout deviceLayout;

    @Inject
    public TripLayout tripLayout;

    private LayoutViewGrid tripList;
    private LayoutViewGrid deviceList;

    Thread googleSnapThread = null;

    @Inject
    public MapHelper mapHelper;

    private FormattedSql tripSql;

    private GeoMap map = new GeoMap(STATE_LOCATE | STATE_SEARCH | STATE_ENLARGE);
    private LLayerGroup posLayer = new LLayerGroup();

    @PostConstruct
    private void initMap() {
        map.addLayer(posLayer);
        map.setSizeFull();
        Button btnRefresh = GeoMap.initToolbarBtn(new Button(FontAwesome.REFRESH));
        map.getTopRightBtnGroup().addComponent(btnRefresh);
        btnRefresh.addClickListener(event -> drawDevices());
    }


    public void init() {
        addStyleName("webframe");
        deviceList = new LayoutViewGrid(deviceLayout.getDataSource(), deviceLayout, false, 1000,
                deviceLayout.getMainSql(), (layout, source, newEvent, entityDB) -> {
            drawDevice((DeviceEntity) entityDB);
        }).setAllowSingleClickSelection(true);
        ;
        deviceList.build(true);

        deviceList.setSizeFull();
        deviceList.setEnabled(true);
        deviceList.setEditorEnabled(true);
        deviceList.getEditorFieldGroup().addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                String value = (String) commitEvent.getFieldBinder().getField("DEVICE_ALIAS").getValue();
                commitEvent.getFieldBinder().discard();
                ((Field) commitEvent.getFieldBinder().getField("DEVICE_ALIAS")).setValue(value);
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                String value = (String) commitEvent.getFieldBinder().getField("DEVICE_ALIAS").getValue();
                DataSourceDB.set(deviceList.getDataSource(), (EntityDB) ((DeviceEntity) deviceList.getSelectedEntity()).deviceAlias.set(value));
            }
        });

        tripSql = new FormattedSql(tripLayout.getMainSql());
        tripSql.setWhere(String.format("(%s) and (%s)", tripSql.getWhere() == null ? "1 = 1 " : tripSql.getWhere(), "1 = 0"));
        tripList = new LayoutViewGrid(tripLayout.getDataSource(), tripLayout, false, 1000,
                tripSql.toString(), (layout, source, newEvent, entityDB) -> {
            if (entityDB != null) {
                drawTrip((TripEntity) entityDB);
            }

        }).setAllowSingleClickSelection(true);
        tripList.build(true);
        tripList.setWidth("100%");
        tripList.addStyleName(ValoTheme.TABLE_COMPACT);
        tripList.addStyleName(ValoTheme.TABLE_SMALL);

        HorizontalSplitPanel split = new HorizontalSplitPanel(map, deviceList);
        split.setSplitPosition(70, Unit.PERCENTAGE);
        split.setSizeFull();
        setFirstComponent(split);
        setSecondComponent(tripList);
        setSplitPosition(80, Unit.PERCENTAGE);
        drawDevices();
    }

    public void drawTrip(TripEntity trip) {
        if(googleSnapThread != null && googleSnapThread.isAlive()){
            googleSnapThread.interrupt();
        }
        posLayer.removeAllComponents();
        try (Connection connection = tripLayout.getDataSource().getConnection()) {
            DeviceEntity device = trip.device.getOne(connection);
            List<Point> points = new ArrayList<>();
            List<DeviceLocationEntity> locations = trip.locations.get(connection).getAllAsList();
            locations.stream().forEach(location -> {
                MapHelper.MarkerColor markerColor = posLayer.getComponentCount() == 0 ? MapHelper.MarkerColor.Green :
                        location == locations.get(locations.size() - 1) ? MapHelper.MarkerColor.Red :
                                MapHelper.MarkerColor.BlueSmall;
                posLayer.addComponent(mapHelper.getMarkerForTrips(UI.getCurrent(), tripLayout.getDataSource(), device, location, markerColor));
                points.add(new Point(location.lat.get(), location.lon.get()));
            });

            posLayer.addComponent(mapHelper.buildPathFromTrip(points, null, "#ff0000"));

            googleSnapThread = AccessHelper.accessThread(() -> {
                posLayer.addComponent(mapHelper.buildSnappedToRoadPathFromTrip(points, null, "#0066ff"));
            }, () -> {
            });
            googleSnapThread.start();
            map.getMap().zoomToContent();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void drawDevice(DeviceEntity device) {
        posLayer.removeAllComponents();
        if (device != null) {
            if (device.latestDeviceLocationId.get() != null) {
                // zoom to location
                DeviceLocationEntity latest = device.latestLocation.getOne(deviceLayout.getDataSource());
                posLayer.addComponent(mapHelper.getMarkerForNonTripPosition(UI.getCurrent(),
                        tripLayout.getDataSource(), device, latest, mapHelper.getMarkerColorForNonTripPosition(device.lastCommsDate.get())));

                map.getMap().zoomToContent();
            } else {
                Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.NO_DEVICE_LOCATION), Notification.Type.TRAY_NOTIFICATION);
            }
            tripList.getViewQueryDelegate().setSql(String.format("select * from trip where device_id = '%s' order by start_time desc ", device.deviceId.get()));
            tripList.getSqlContainer().refresh();
            tripList.select(null);
        }
    }

    boolean firstDraw = false;

    public void drawDevices() {
        tripList.getViewQueryDelegate().setSql("select * from trip where 0 = 1 ");
        tripList.getSqlContainer().refresh();
        posLayer.removeAllComponents();
        deviceList.select(null);
        try {
            try (Connection connection = tripLayout.getDataSource().getConnection()) {
                for (DeviceLocationView view : new DeviceLocationView().getDataSource(connection)) {
                    posLayer.addComponent(mapHelper.getMarkerForNonTripPosition(UI.getCurrent(),
                            tripLayout.getDataSource(), view.device, view.location, mapHelper.getMarkerColorForNonTripPosition(view.device.lastCommsDate.get())));
                }
            }
            AccessHelper.accessDelayed(new AccessHelper.AccessCallback(() -> {
                if (posLayer.getComponentCount() == 0) {
                    MapUtil.centerOnUserLocation(map, null, null, false, null);
                } else {
                    map.getMap().zoomToContent();
                }
            },100));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
