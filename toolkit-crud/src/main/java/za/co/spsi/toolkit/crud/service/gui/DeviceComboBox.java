package za.co.spsi.toolkit.crud.service.gui;

import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by jaspervdb on 2015/10/06.
 */
public class DeviceComboBox {

    private AbstractSelect comboBox;
    public DeviceComboBox() {

    }

    public void init(DataSource dataSource, String input,String imei,boolean allowMultiple) {

        if (allowMultiple) {
            comboBox = new ComboBoxMultiselect();
            ((ComboBoxMultiselect)comboBox).setInputPrompt(input);
        } else {
            comboBox = new ComboBox();
        }
        comboBox.setWidth("100%");
        DeviceEntity device = (DeviceEntity) new DeviceEntity().agencyId.set(ToolkitCrudConstants.getChildAgencyId());
        if (imei != null) {
            device.imei.set(imei);
        }
        List<DeviceEntity> devices = DataSourceDB.getAllFromSet(dataSource, device);
        devices.stream().forEach(d -> {
            comboBox.addItem(d);
            comboBox.setItemCaption(d, d.getDisplayName());
        });

        if (imei != null && !devices.isEmpty()) {
            comboBox.select(devices.get(0));
            comboBox.setReadOnly(true);
        }
    }

    public AbstractSelect getComboBox() {
        return comboBox;
    }

    public Collection<DeviceEntity> getValues() {
        return comboBox instanceof ComboBoxMultiselect?
                (Collection<DeviceEntity>)comboBox.getValue():
                comboBox.getValue() != null? Arrays.asList((DeviceEntity)comboBox.getValue()):new ArrayList<>();
    }
}