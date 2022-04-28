package za.co.spsi.mdms.nes.services.meter;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.nes.db.NESDeviceEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * Created by jaspervdb on 2016/10/12.
 */
@Dependent
public class NESMeterService {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource mdmsDs;

    @Resource(mappedName = "java:/jdbc/nesdb")
    private javax.sql.DataSource nesDs;

    @Inject
    private PropertiesConfig propertiesConfig;

    public void updateMeters() {

        try {
            if(propertiesConfig.getNes_processing_enabled()) {
                try (Connection nesConnection = nesDs.getConnection()) {
                    for (NESDeviceEntity device : new DataSourceDB<>(NESDeviceEntity.class).getAllWhere(nesConnection,
                            "serialNumber is not null and len(serialNumber) > 0", null)) {
                        NESMeterEntity meter = DataSourceDB.getFromSet(mdmsDs,
                                (NESMeterEntity) new NESMeterEntity().serialN.set(device.serialNumber.get()));
                        DataSourceDB.set(mdmsDs, meter == null ? new NESMeterEntity(device) : meter.init(device));
                    }
                }
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }

    }

    public NESMeterEntity update(DataSource dataSource, NESDeviceEntity deviceEntity) {
        // the nes meters changes their id from time to time, will use serial_n as unique id
        NESMeterEntity meter = DataSourceDB.getFromSet(dataSource,new NESMeterEntity().serialN.set(deviceEntity.serialNumber.get()));
        return DataSourceDB.setIfChanged(dataSource,(meter == null?new NESMeterEntity():meter).init(deviceEntity));
    }

}
