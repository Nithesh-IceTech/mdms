package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.util.ExpiringCacheMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import java.time.LocalTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdbijl on 2017/05/03.
 */
@Singleton
@DependsOn({"PropertiesConfig"})
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
public class IceTimeOfUseViewSyncService {

    @Resource(mappedName = "java:/jdbc/IceUtil")
    public javax.sql.DataSource iceDataSource;

    public static String SYNC = UUID.randomUUID().toString();

    private ExpiringCacheMap<String,IceTimeOfUseView.IceTimeOfUseViewList> meterTouMap = new ExpiringCacheMap<>(TimeUnit.MINUTES.toMillis(2));

    public IceTimeOfUseViewSyncService() {

    }

    @PostConstruct
    private void init() {
    }

    public String getTOU(String meterN,LocalTime time) {
        synchronized (SYNC) {
            if (!meterTouMap.containsKey(meterN)) {
                DataSourceDB.executeInTx(iceDataSource,connection -> {
                    meterTouMap.put(meterN,new IceTimeOfUseView.IceTimeOfUseViewList(
                            new IceTimeOfUseView().setMeterN(meterN).getDataSource(connection).getAllAsList()));
                });
            }
            return meterTouMap.get(meterN).getTOU(time);
        }
    }

    public String getTOUC(String timeOfUseC,LocalTime time) {
        synchronized (SYNC) {
            if (!meterTouMap.containsKey(timeOfUseC)) {
                DataSourceDB.executeInTx(iceDataSource,connection -> {
                    meterTouMap.put(timeOfUseC,new IceTimeOfUseView.IceTimeOfUseViewList(
                            new IceTimeOfUseView().setId(timeOfUseC).getDataSource(connection).getAllAsList()));
                });
            }
            return meterTouMap.get(timeOfUseC).getTOU(time);
        }
    }

}
