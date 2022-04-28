package za.co.spsi.toolkit.crud.gis.gui;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gis.db.DeviceStatusEntity;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.ImageField;
import za.co.spsi.toolkit.crud.gui.gis.MapHelper;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/04/19.
 */
public abstract class DeviceLayout extends Layout implements ImageField.Callback {

    @EntityRef(main = true)
    private DeviceEntity device = new DeviceEntity();
    private DeviceStatusEntity deviceStatus = new DeviceStatusEntity();

    @UIGroup(column = -1)
    public Group detail = new Group(ToolkitLocaleId.DEVICE_DETAIL, this);

    @UIField(enabled = false)
    ImageField gridImage = new ImageField(ToolkitLocaleId.DEVICE_STATUS, this, "GRID_IMAGE", this);

    public LField<String> deviceAlias = new LField<>(device.deviceAlias, ToolkitLocaleId.DEVICE_ALIAS, this);

    @UIField(enabled = false)
    public LField<String> deviceCurrentUser = new LField<>(device.userId, ToolkitLocaleId.USERNAME, this);

    @UIField(enabled = false)
    public LField<String> imei = new LField<>(device.imei, ToolkitLocaleId.DEVICE_IMEI, this);

    @UIField(enabled = false)
    public LField<String> apkVersion = new LField<>(device.apkVersion, ToolkitLocaleId.DEVICE_APK_VERSION, this);

    @UIField(enabled = false)
    public LField<Timestamp> apkD = new LField<>(device.apkD, ToolkitLocaleId.DEVICE_APK_DATE, this);

    @UIField(enabled = false)
    public LField<String> androidVersion = new LField<>(deviceStatus.androidVersion, ToolkitLocaleId.ANDROID_VERSION, this);

    @UIField(enabled = false)
    public LField<String> modelNumber = new LField<>(device.deviceTracked.modelNumber, ToolkitLocaleId.DEVICE_MODEL_N, this);

    @UIField(enabled = false)
    public LField<String> serialNumber = new LField<>(device.deviceTracked.serialNumber, ToolkitLocaleId.DEVICE_SERIAL_N, this);

    @UIField(enabled = false)
    public LField<String> mobileNetwork = new LField<>(deviceStatus.mobileNetwork, ToolkitLocaleId.MOBILE_NETWORK, this);

    @UIField(enabled = false)
    public LField<Integer> screenWidth = new LField<>(device.deviceTracked.screenWidth, ToolkitLocaleId.DEVICE_SCREEN_WIDTH, this);

    @UIField(enabled = false)
    public LField<Integer> screenHeight = new LField<>(device.deviceTracked.screenHeight, ToolkitLocaleId.DEVICE_SCREEN_HEIGHT, this);

    @UIField(enabled = false)
    public LField<Integer> totalDiskSpace = new LField<>(deviceStatus.totalDiskSpace, ToolkitLocaleId.TOTAL_DISK_SPACE, this);

    @UIField(enabled = false)
    public LField<Integer> freeDiskSpace = new LField<>(deviceStatus.freeDiskSpace, ToolkitLocaleId.FREE_DISK_SPACE, this);

    @UIField(enabled = false)
    public Group nameGroup = new Group(ToolkitLocaleId.DEVICE_DETAIL, this, gridImage, deviceAlias, deviceCurrentUser, imei, apkVersion).setNameGroup();

    @UIField(enabled = false)
    public Pane detailPane = new Pane(ToolkitLocaleId.DEVICE_DETAIL, this, detail);

    public DeviceLayout() {
        super(ToolkitLocaleId.DEVICE_DETAIL);
        getPermission().setMayDelete(false);
        getPermission().setMayCreate(false);
    }

    @Override
    public String getMainSql() {
        Driver driver = DriverFactory.getDriver();

        String query = String.format("select * , " +
                "CASE WHEN LAST_COMMS_DATE < CURRENT_TIMESTAMP %s OR LAST_COMMS_DATE is NULL " +
                "THEN '../toolkit/img/red_dot_16.png' " +
                "ELSE '../toolkit/img/green_dot_16.png' " +
                "END as GRID_IMAGE " +
                "FROM Device " +
                "where agency_id = _AGENCY_ID_ ",
                driver.subtractTimezoneOffset());

        return query;
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();

        DeviceStatusEntity deviceStatusTmp =
                DataSourceDB.get(DeviceStatusEntity.class, getDataSource(),
                        DriverFactory.getDriver().limitSql(
                                "SELECT *\n" +
                                        " FROM\n" +
                                        "   DEVICE_STATUS\n" +
                                        " WHERE\n" +
                                        "   DEVICE_ID = ?\n" +
                                        " ORDER BY CAPTURE_TIME DESC", 1), device.deviceId.get());

        if (deviceStatusTmp != null) {
            deviceStatus.copyStrict(deviceStatusTmp);
        }
    }

    @Override
    public Resource getResource() {
        return new ThemeResource(MapHelper.getMarkerColorForNonTripPosition(device.lastCommsDate.get()).
                equals(MapHelper.MarkerColor.Red) ? "../toolkit/img/red_dot_16.png" : "../toolkit/img/green_dot_16.png");
    }

    @Override
    public Integer getItemId() {
        return MapHelper.getMarkerColorForNonTripPosition(device.lastCommsDate.get()).equals(MapHelper.MarkerColor.Red) ? 0 : 1;
    }


}
