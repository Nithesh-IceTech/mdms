package za.co.spsi.mdms.kamstrup.services.meter;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdb on 2016/10/13.
 * service is responsible for update the meter db with the meter info
 */
@Singleton
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
public class KamstrupMeterSyncService extends ProcessorService {

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    private KamstrupMeterService meterService;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Schedule(hour = "2", dayOfMonth = "*", persistent = false)
    @AccessTimeout(value = 24,unit = TimeUnit.HOURS)
    public void updateRegisters() {
        if (propertiesConfig.getKamstrup_processing_enabled()) {
            if (LocalTime.now().getHour() <= 3) {
                meterService.updateMeters(true, true);
            }
        }
    }

    @Schedule(hour = "*/1", minute = "45", persistent = false)
    @AccessTimeout(value = 24,unit = TimeUnit.HOURS)
    public void updateMeters() {
        if (propertiesConfig.getKamstrup_processing_enabled()) {
            meterService.updateMeters(true,false);
        }
    }

}
