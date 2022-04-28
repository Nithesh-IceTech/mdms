package za.co.spsi.toolkit.crud.gis.db;

import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.db.View;

/**
 * Created by jaspervdbijl on 2017/05/02.
 */
public class DeviceLocationView extends View<DeviceLocationView> {

    public DeviceEntity device = new DeviceEntity();
    public DeviceLocationEntity location = new DeviceLocationEntity();

    public DeviceLocationView() {
        super();
        setSql("select * from device, device_location where device.latest_device_location_id = device_location.device_location_id and " +
                "device.agency_id = ? order by device_alias asc", ToolkitCrudConstants.getChildAgencyId());
    }
}
