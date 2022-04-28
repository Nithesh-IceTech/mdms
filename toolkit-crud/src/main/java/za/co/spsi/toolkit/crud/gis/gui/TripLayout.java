package za.co.spsi.toolkit.crud.gis.gui;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gis.db.TripEntity;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/04/19.
 */
public abstract class TripLayout extends Layout {

    @EntityRef(main = true)
    private TripEntity trip = new TripEntity();

    @UIGroup(column = 0)
    public Group detail = new Group(ToolkitLocaleId.DEVICE_DETAIL, this);

    public LField<Timestamp> startTime = new LField<>(trip.startTime, ToolkitLocaleId.TRIP_START_TIME,this);

    public LField<Timestamp> endTime = new LField<>(trip.endTime, ToolkitLocaleId.TRIP_END_TIME,this);

    public LField<Double> fromLon = new LField<>(trip.fromLon, ToolkitLocaleId.TRIP_FROM_LON,this);

    public LField<Double> fromLat = new LField<>(trip.fromLat, ToolkitLocaleId.TRIP_FROM_LAT,this);

    public LField<Double> toLon = new LField<>(trip.toLon, ToolkitLocaleId.TRIP_TO_LON,this);

    public LField<Double> toLat = new LField<>(trip.toLat, ToolkitLocaleId.TRIP_TO_LAT,this);

    public LField<Float> maxSpeed = new LField<>(trip.maxSpeed, ToolkitLocaleId.TRIP_MAX_SPEED,this);

    public LField<Double> distance = new LField<>(trip.distance, ToolkitLocaleId.TRIP_DISTANCE,this);

    public LField<String> fromAddress = new LField<>(trip.fromAddress, ToolkitLocaleId.TRIP_FROM_ADDRESS,this);

    public LField<String> toAddress = new LField<>(trip.toAddress, ToolkitLocaleId.TRIP_TO_ADDRESS,this);

    public LField<String> user = new LField<>(trip.userId, ToolkitLocaleId.USERNAME,this);

    public Group nameGroup = new Group(ToolkitLocaleId.DEVICE_DETAIL, this, startTime,endTime,distance,maxSpeed,fromAddress, toAddress,user).setNameGroup();

    public Pane detailPane = new Pane(ToolkitLocaleId.DEVICE_DETAIL,this,detail);

    public TripLayout() {
        super(ToolkitLocaleId.DEVICE_DETAIL);
    }

    @Override
    public String getMainSql() {
        return "select * from trip";
    }
}
