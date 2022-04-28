package za.co.spsi.mdms.web.gui.layout.settings;

import com.vaadin.ui.Notification;
import za.co.spsi.mdms.common.db.MdmsSettingsEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.fields.LocalTimestampField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by Arno Combrinck
 */
public class MdmsSettingsLayout extends Layout<MdmsSettingsEntity> {

    @Inject
    private PropertiesConfig propertiesConfig;

    @EntityRef(main = true)
    private MdmsSettingsEntity settings = new MdmsSettingsEntity();

    public Group detailGroup = new Group("Setting Details",this).setNameGroup();

    @UIField(enabled=false)
    public LField propertyKey = new LField(settings.propertyKey,"Name",this);

    @UIField(enabled=false)
    public TimestampField createTime = new TimestampField(settings.createTime,"Create Time",this);

    @UIField(enabled=false)
    public LocalTimestampField lastChangeTime = new LocalTimestampField(settings.lastChangeTime,"Last Change Time",this);

    @UIField(mandatory = false, enabled=true, rows = 10)
    public TextAreaField propertyValue = new TextAreaField(settings.propertyValue,"Value",this);

    public Pane settingsPane = new Pane("",this, detailGroup);

    public MdmsSettingsLayout() {
        super("MDMS Application Settings");
    }

    @PostConstruct
    private void init() {
        propertyKey.getProperties().setCaps(false);
        propertyKey.getProperties().setReadOnly(false);
        propertyValue.getProperties().setReadOnly(false);
        propertyValue.getProperties().setCaps(false);
        propertyValue.applyProperties();
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        // update MDMS_SETTINGS table properties when user makes a change to properties in the UI
    }

    @Override
    public DataSource getDataSource() {
        return DriverFactory.getDataSource();
    }

    @Override
    public String getMainSql() {
        return String.format("select * from mdms_settings where app_instance = %d order by property_key",
                propertiesConfig.getMdms_app_instance());
    }

    @Override
    public boolean save() {

        if (super.save()) {

            try {

                settings.appInstance.set( propertiesConfig.getMdms_app_instance() );
                settings.lastChangeTime.set( Timestamp.valueOf( LocalDateTime.now().minusMinutes( propertiesConfig.getMdms_global_timezone_offset() ) ) );

                DSDB.setUpdate(DriverFactory.getDataSource(), settings);

                VaadinNotification.show("Info",
                        "Settings saved.",
                        Notification.Type.HUMANIZED_MESSAGE);

                propertiesConfig.updatePropertyValues();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;
        } else {
            return false;
        }
    }

}
