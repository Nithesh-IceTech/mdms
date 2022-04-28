package za.co.spsi.mdms.common.db.utility;

import org.springframework.util.CollectionUtils;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.util.ExpiringCacheMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by Arno Combrinck 2020-10-26.
 */
@Singleton
@DependsOn({"PropertiesConfig"})
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
public class IcePrepaidTimeOfUseViewSyncService {

    @Resource(mappedName = "java:/jdbc/IceUtil")
    public javax.sql.DataSource iceDataSource;

    public static String SYNC = UUID.randomUUID().toString();

    private ExpiringCacheMap<String,List<IcePrepaidTimeOfUseView>> touPPMeterMap = new ExpiringCacheMap<>(TimeUnit.MINUTES.toMillis(1));
    private ExpiringCacheMap<String,Boolean> isPrepaidTouMeterMap = new ExpiringCacheMap<>(TimeUnit.MINUTES.toMillis(1));
    private ExpiringCacheMap<String,String> meterRegisterTypeMap = new ExpiringCacheMap<>(TimeUnit.MINUTES.toMillis(1));

    public IcePrepaidTimeOfUseViewSyncService() {

    }

    @PostConstruct
    private void init() {

    }

    public List<IcePrepaidTimeOfUseView> filterByIceMeterNumber(String iceMeterNumber) {

        List<IcePrepaidTimeOfUseView> icePrepaidTimeOfUseList =
                DataSourceDB.getAllAsList(IcePrepaidTimeOfUseView.class,iceDataSource,
                        "select * from ICE_PREPAID_TOU_V where ICE_METER_NUMBER = ?",
                        iceMeterNumber);

        return icePrepaidTimeOfUseList.size() > 0 ? icePrepaidTimeOfUseList : new ArrayList<>();
    }

    public List<IcePrepaidTimeOfUseView> filterByIceMeterNumberAndRegisterId(String iceMeterNumber, String iceMeterRegisterId, String mdmsMeterRegisterId) {
        List<IcePrepaidTimeOfUseView> touViewList = filterByIceMeterNumber(iceMeterNumber);
        List<IcePrepaidTimeOfUseView> touViewFilteredList = touViewList != null ? touViewList.stream()
                .filter(v -> v.iceMeterRegisterId.get().equalsIgnoreCase(iceMeterRegisterId))
                .filter(v -> v.mdmsMeterRegisterId.get().equalsIgnoreCase(mdmsMeterRegisterId))
                .collect(Collectors.toList()) : new ArrayList<>();
        return touViewFilteredList;
    }

    public String getMeterRegisterType(String iceMeterNumber, String iceMeterRegisterId) {
        List<IcePrepaidTimeOfUseView> touViewList;
        String key = iceMeterNumber + ":" + iceMeterRegisterId;
        synchronized (SYNC) {
            if (!meterRegisterTypeMap.containsKey(key)) {
                touViewList = filterByIceMeterNumber(iceMeterNumber);
                Optional<IcePrepaidTimeOfUseView> touView = touViewList.stream()
                        .filter(v -> v.iceMeterRegisterId.get().equalsIgnoreCase(iceMeterRegisterId))
                        .findFirst();
                String meterRegisterType = touView.isPresent() ? touView.get().mtrRegType.get() : "none";
                meterRegisterTypeMap.put(key, meterRegisterType);
            }
            return meterRegisterTypeMap.get(key);
        }
    }

    public Boolean isPPTimeOfUseMeterAndRegister(String iceMeterNumber, String iceMeterRegisterId, String mdmsMeterRegisterId) {
        List<IcePrepaidTimeOfUseView> touViewList;
        String key = iceMeterNumber + ":" + iceMeterRegisterId + ":" + mdmsMeterRegisterId;
        synchronized (SYNC) {
            if (!isPrepaidTouMeterMap.containsKey(key)) {
                touViewList = filterByIceMeterNumberAndRegisterId(iceMeterNumber,iceMeterRegisterId,mdmsMeterRegisterId);
                if(CollectionUtils.isEmpty(touViewList)) {
                    isPrepaidTouMeterMap.put(key, false);
                    return false;
                } else {
                    isPrepaidTouMeterMap.put(key, true);
                    return true;
                }
            }
            return isPrepaidTouMeterMap.get(key);
        }
    }

}
