package za.co.spsi.mdms.util;

import lombok.Data;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.services.AbstractPrepaidService;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jaspervdbijl on 2017/06/28.
 */
@Singleton()
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"PropertiesConfig"})
@Data
public class PrepaidMeterFilterService extends AbstractPrepaidService {

    private String syncObject = UUID.randomUUID().toString();

    private List<String> prepaidMeters = new ArrayList<>();

    @Resource(mappedName = "java:/jdbc/IceUtil")
    private javax.sql.DataSource iceDataSource;

    @Inject
    private PropertiesConfig propertiesConfig;

    @PostConstruct
    private void init() {
        p.delay(5).minutes(5).repeat(() -> refresh());
    }

    public void refresh() {
        if(!propertiesConfig.getUtility_prepaid_push_enabled()) return;
        synchronized (syncObject)  {
            prepaidMeters.clear();
            DataSourceDB.executeQuery(iceDataSource,new Class[]{String.class},
                    "select distinct(ICE_METER_NUMBER) from ICE_Prepaid_Meter where ice_meter_prepaid = 'Y'").
                    stream().forEach(l -> prepaidMeters.add((String) l.get(0)));
        }
    }

    public synchronized boolean isPrepaid(String meterNo) {
        synchronized (syncObject)  {
            return prepaidMeters.contains(meterNo);
        }
    }

}
