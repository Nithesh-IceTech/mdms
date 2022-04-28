package za.co.spsi.toolkit.crud.metrics.user;

import lombok.Synchronized;
import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.audit.AuditEntity;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.ExpiringCacheMap;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by jaspervdbijl on 2017/06/22.
 */
@Startup
@Singleton
public class MetricService {

    public static ExpiringCacheMap<String,List<MetricCategory>> MAP = new ExpiringCacheMap(TimeUnit.MINUTES.toMillis(15));

    static {
        AuditEntity.setCallbackHook((connection, auditEntity, entityDB, changed) -> process(connection,auditEntity,entityDB,changed));
    }

    public static List<MetricCategory> getMap(Connection connection) {
        if (!MAP.containsKey("MAP")) {
            MAP.put("MAP",new DataSourceDB(MetricCategory.class).getAllAsList(connection,"select * from metric_category",null));
        }
        return MAP.get("MAP");
    }

    @Synchronized("MAP")
    public static void process(Connection connection,AuditEntity auditEntity,EntityDB entityDB, FieldList changed) {
        Optional<MetricCategory> mc = getMap(connection).stream().filter(m -> m.className.get().equals(entityDB.getClass().getName())).findAny();
        if (mc.isPresent()) {
            UserMetricLog.createLog(connection,entityDB,auditEntity,mc.get(),
                    changed.stream().filter(p -> !(p instanceof UserIdField)).collect(Collectors.toCollection(FieldList::new)));
        }
    }

}
