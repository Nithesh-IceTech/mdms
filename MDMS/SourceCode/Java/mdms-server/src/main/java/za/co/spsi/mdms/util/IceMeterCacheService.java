package za.co.spsi.mdms.util;

import za.co.spsi.mdms.common.db.utility.IceMeter;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.util.ExpiringCacheMap;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jaspervdbijl on 2017/06/28.
 */
@Dependent
public class IceMeterCacheService {

    private ExpiringCacheMap<String, List<IceMeter>> expiringCacheMap = new ExpiringCacheMap<>(TimeUnit.MINUTES.toMillis(15));

    @Resource(mappedName = "java:/jdbc/IceUtil")
    public javax.sql.DataSource iceDataSource;

    public IceMeterCacheService() {

    }

    public synchronized List<IceMeter> get(String meterNo) {
        if (!expiringCacheMap.containsKey(meterNo)) {
            List<IceMeter> meters = DataSourceDB.getAllFromSet(iceDataSource,(IceMeter)new IceMeter().iceMeterNumber.set(meterNo));
            expiringCacheMap.put(meterNo,meters);
        }
        return expiringCacheMap.get(meterNo);
    }

}
