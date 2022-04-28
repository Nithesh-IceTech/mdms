package za.co.spsi.mdms.common.services;

import za.co.spsi.mdms.common.db.MdmsSettingsEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arno Combrinck
 */
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
public class MdmsSettingsService {

    @Inject
    @ConfValue(value = "mdms.app.instance", folder = "server", defaultValue = "1")
    private Integer mdms_app_instance;

    @PostConstruct
    private void init() {

    }

    public MdmsSettingsEntity getProperty(String propKey) {
        MdmsSettingsEntity mdmsSettingsEntity = new MdmsSettingsEntity();
        mdmsSettingsEntity.appInstance.set(mdms_app_instance);
        mdmsSettingsEntity.propertyKey.set(propKey);
        return DataSourceDB.getFromSet(DriverFactory.getDataSource(), mdmsSettingsEntity);
    }

    public MdmsSettingsEntity updateProperty(String propKey, String propVal, String propType) throws SQLException {
        MdmsSettingsEntity updateSettingsEntity = getProperty(propKey);
        if(updateSettingsEntity == null) {
            updateSettingsEntity = new MdmsSettingsEntity();
            updateSettingsEntity.appInstance.set(mdms_app_instance);
            updateSettingsEntity.propertyKey.set(propKey);
            updateSettingsEntity.propertyValue.set(propVal);
            updateSettingsEntity.propertyType.set(propType);
            updateSettingsEntity.lastChangeTime.set( Timestamp.valueOf(LocalDateTime.now()) );
            DataSourceDB.set(DriverFactory.getDataSource(), updateSettingsEntity);
        } else {
            updateSettingsEntity.appInstance.set(mdms_app_instance);
            updateSettingsEntity.propertyValue.set(propVal);
            updateSettingsEntity.lastChangeTime.set( Timestamp.valueOf(LocalDateTime.now()) );
            DataSourceDB.setUpdate(DriverFactory.getDataSource(), updateSettingsEntity);
        }
        return updateSettingsEntity;
    }

    public Map<String, String> loadPropertiesFromDatabase() {
        List<MdmsSettingsEntity> mdmsSettingsList = DataSourceDB.getAllAsList(MdmsSettingsEntity.class, DriverFactory.getDataSource(),
                "select * from mdms_settings where app_instance = ?", mdms_app_instance);
        Map<String, String> propertiesMap = new HashMap<>();
        mdmsSettingsList.forEach( propObj -> {
            propertiesMap.put(propObj.propertyKey.get(), propObj.propertyValue.get());
        } );
        return propertiesMap;
    }

}
